package com.tudordev.powerusageservice;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Parcel;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.tudordev.powerusageservice.PowerEvent;
import com.tudordev.powerusageservice.RecordingStrategy;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;


public class PowerUsageScanner extends Thread {

	ActivityManager am;
	Timer myTimer;

	private static final String TAG = "PowerInterfaceAdapter";
	private static final String BATTERY_STATS_IMPL_CLASS = "com.android.internal.os.BatteryStatsImpl";
	private static final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
	private static final String M_BATTERY_INFO_CLASS = "com.android.internal.app.IBatteryStats";
	private static final String BATTERY_STATS_CLASS = "android.os.BatteryStats";
	private static final String TRAFFIC_CLASS = "android.net.TrafficStats";
	private static final String UID_CLASS = BATTERY_STATS_CLASS + "$Uid";
	private static final String UID_TRAFFIC_CLASS = TRAFFIC_CLASS + "$Uid";
	private static final String PROC_CLASS = UID_CLASS + "$Proc";
	private static final String SENSOR_CLASS = UID_CLASS + "$Sensor";
	private static final String BATTER_STATS_TIMER_CLASS = BATTERY_STATS_CLASS + "$Timer";

	private int pollingRate = 10;

	private Object mBatteryInfo_;
	private Object mStats_;
	private Object mPowerProfile_;
	private int mStatsType_ = BatteryStatsConstants.STATS_TOTAL;
	private Activity parent_;
	private ArrayList<RecordingStrategy> recorder_ = new ArrayList<RecordingStrategy> ();

	//the data from the scanner
	private HashMap<String, ArrayList<PowerEvent>> powerData = new HashMap<String, ArrayList<PowerEvent>>();

	private static PowerUsageScanner sharedScanner;
	// Note that the constructor is private
	private PowerUsageScanner() {
		// Optional Code
	}
	public static PowerUsageScanner getSingletonObject() {
		if (sharedScanner == null) {
			sharedScanner = new PowerUsageScanner();
		}
		return sharedScanner;
	}
	public void initSingletonObject(Context context) {
		parent_=(Activity) context;
	}


	@Override
	public void run() {

		myTimer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				
				if(pollingRate!=0){
					processApplicationUsage("test");
					pollingRate--;
				}
				else{
					myTimer.cancel();
				}

			}

		};

		//adding timer fire for every minute	
		myTimer.schedule(task, 0, 60 * 1000);
	}

	private void load(){
		try{

			byte[] data = (byte[])Class.forName(M_BATTERY_INFO_CLASS).getMethod("getStatistics", null)
					.invoke(mBatteryInfo_, null);
			Parcel parcel = Parcel.obtain();
			parcel.unmarshall(data, 0, data.length);
			parcel.setDataPosition(0);
			/*
			 * Non-reflection call looks like
			 * mStats = com.android.internal.os.BatteryStatsImpl.CREATOR.createFromParcel(parcel);
			 */
			mStats_ = Class.forName(BATTERY_STATS_IMPL_CLASS)
					.getField("CREATOR").getType().getMethod("createFromParcel", Parcel.class)
					.invoke(
							Class.forName(BATTERY_STATS_IMPL_CLASS).getField("CREATOR").get(null), parcel);
			
			Log.d(TAG, mStats_.toString());

		}catch(InvocationTargetException e){
			Log.e("BatteryTester", "Exception: " + e);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.spot.android.PowerCollector#processApplicationUsage()
	 */
	public void processApplicationUsage(String label){

		//TODO: get the activity instance 

		try {

			mBatteryInfo_ = Class.forName("com.android.internal.app.IBatteryStats$Stub").getDeclaredMethod("asInterface", 
					IBinder.class).invoke(null, Class.forName("android.os.ServiceManager")
							.getMethod("getService", String.class).invoke(null, "batteryinfo"));
			mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS).getConstructor(Context.class).newInstance(parent_);
			load();	


		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		try {
			SensorManager sensorManager = (SensorManager)parent_.getSystemService(Context.SENSOR_SERVICE);
			Method getAvgPowerMeth = Class.forName(POWER_PROFILE_CLASS).getMethod("getAveragePower", String.class);
			final double powerCpuNormal = .2;//(Double) getAvgPowerMeth.invoke(mPowerProfile_, PowerProfileConstants.POWER_CPU_NORMAL);
			final double averageCostPerByte = getAverageDataCost();
			final int which = mStatsType_;
			long uSecTime = (Long)Class.forName(BATTERY_STATS_IMPL_CLASS).getMethod("computeBatteryRealtime", java.lang.Long.TYPE, java.lang.Integer.TYPE)
					.invoke(mStats_, SystemClock.elapsedRealtime() * 1000, which);
			updateStatsPeriod(uSecTime);

			SparseArray uidStats = (SparseArray)Class.forName(BATTERY_STATS_IMPL_CLASS).getMethod("getUidStats", null).invoke(mStats_, null);
			final int NU = uidStats.size();
			for (int iu = 0; iu < NU; iu++){
				Object u = uidStats.valueAt(iu);
				
				double power = 0;
				long tcpBytesReceived = 0;
				long tcpBytesSent = 0;
				double tcpPowerUsed = 0;
				String packageWithHighestDrain = "";
				Map<String, Object> processStats = (Map)Class.forName(UID_CLASS).getMethod("getProcessStats", (Class[])null).invoke(u, null);
				long cpuTime = 0;
				long cpuFgTime = 0;
				long gpsTime = 0;

				//				Log.d("uidStats", processStats.toString());

				//Process cpu usage
				if(processStats.size() > 0){
					for (Map.Entry<String, Object> ent: processStats.entrySet()){
						packageWithHighestDrain = ent.getKey();
										
						//initializing the array
						if(packageWithHighestDrain!=null && powerData.get(packageWithHighestDrain)==null)
							powerData.put(packageWithHighestDrain, new ArrayList<PowerEvent>());

						Object ps = ent.getValue();
						
						final long userTime = (Long)Class.forName(PROC_CLASS).getMethod("getUserTime", java.lang.Integer.TYPE).invoke(ps, which);
						final long systemTime = (Long)Class.forName(PROC_CLASS).getMethod("getSystemTime", java.lang.Integer.TYPE).invoke(ps, which);
						final long foregroundTime = (Long)Class.forName(PROC_CLASS).getMethod("getForegroundTime", java.lang.Integer.TYPE).invoke(ps, which);
						cpuFgTime += foregroundTime *10; //convert to millis
						final long tmpCpuTime = (userTime + systemTime) * 10; //convert to millis;
						final double processPower = tmpCpuTime * powerCpuNormal;

						cpuTime += tmpCpuTime;
//						Log.d(TAG, "CPU Time " + cpuTime);
						power += processPower;
						power /= 1000;
						
						
					}
				}
				//if (!packageWithHighestDrain.contains("org.spot.android"))
				//	continue;	

				//Calculate network usage
				//This doesn't currently work, adds zero
				//tcpBytesReceived = (Long)Class.forName(UID_CLASS).getMethod("getTcpBytesReceived", java.lang.Integer.TYPE).invoke(u, mStatsType_);
				//tcpBytesSent = (Long)Class.forName(UID_CLASS).getMethod("getTcpBytesSent",java.lang.Integer.TYPE).invoke(u, mStatsType_);
				
				//New method of getting the number of received/sent bytes
				int uid = (Integer)Class.forName(UID_CLASS).getMethod("getUid",  (Class[])null).invoke(u);
				Log.d("UID", Integer.toString(uid));				
				tcpBytesReceived = TrafficStats.getUidTcpRxBytes(uid);
				tcpBytesSent = TrafficStats.getUidTcpTxBytes(uid);
				
				//tcpBytesReceived = (Long)Class.forName(TRAFFIC_CLASS).getMethod("getTotalRxBytes", (Class[])null).invoke(u);
				//tcpBytesSent = (Long)Class.forName(TRAFFIC_CLASS).getMethod("getTotalTxBytes",(Class[])null).invoke(u);
				Log.d("SENT/RECEIVED", "Received: " + Long.toString(tcpBytesReceived) + "\nSent: " + Long.toString(tcpBytesSent));
				
				
				tcpPowerUsed += (tcpBytesReceived+ tcpBytesSent) * averageCostPerByte;

				//Process sensor usage
				Map<Integer, Object> sensorStats = (Map)Class.forName(UID_CLASS).getMethod("getSensorStats", (Class[])null).invoke(u);

				for(Map.Entry<Integer, Object> sensorEntry: sensorStats.entrySet()){
					Object sensor = sensorEntry.getValue();
					int sensorType = (Integer)Class.forName(SENSOR_CLASS).getMethod("getHandle", (Class[])null).invoke(sensor);
					Object timer = Class.forName(SENSOR_CLASS).getMethod("getSensorTime", (Class[])null).invoke(sensor);
					long sensorTime = (Long)Class.forName(BATTER_STATS_TIMER_CLASS)
							.getMethod("getTotalTimeLocked", java.lang.Long.TYPE, java.lang.Integer.TYPE)
							.invoke(timer, uSecTime, which);
					double multiplier = 0;

					switch(sensorType){

					//GPS
					case -10000:
						multiplier = (Double)Class.forName(POWER_PROFILE_CLASS).getMethod("getAveragePower", String.class)
						.invoke(mPowerProfile_, PowerProfileConstants.POWER_GPS_ON);
						gpsTime = sensorTime;
						Log.d(TAG, "GPS sensor time: " + sensorTime);
						break;
					default:
						android.hardware.Sensor sensorData = sensorManager.getDefaultSensor(1);//sensorType);
						if(sensorData != null) {
							multiplier = sensorData.getPower();
							Log.d(TAG, "Other sensor time: " + sensorTime);

						}
					}
					power += (multiplier * sensorTime) /1000;
				}
				Log.d(TAG, "Power for package " + packageWithHighestDrain + " equal to " + power);
				Log.d(TAG, "CPU Time " + cpuTime);
				PowerEvent app = new PowerEvent(label, PowerEvent.DrainType.APP, 0, u, new double[] {power});
				app.setCpuTime(cpuTime);
				app.setGpsTime(gpsTime);
				app.setCpuFgTime(cpuFgTime);
				app.setPowerDrain(power);
				app.setTcpBytesReceived(tcpBytesReceived);
				app.setTcpBytesSent(tcpBytesSent);
				app.setWifiPowerDrain(tcpPowerUsed);
				if(packageWithHighestDrain!=null){
					
					Log.d("PowerEvent", "PackageName: " + packageWithHighestDrain + "\nPowerEvent: " + app.toString() + "\n");
					if(powerData.get(packageWithHighestDrain)!=null)
						powerData.get(packageWithHighestDrain).add(app);		
				}




			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}

	private double getAverageDataCost(){
		return 0.0;
	}

	public HashMap<String, ArrayList<PowerEvent>> getPowerData() {
		return powerData;
	}

	public void setPowerData(HashMap<String, ArrayList<PowerEvent>> powerData) {
		this.powerData = powerData;
	}



	private void updateStatsPeriod(long uSecTime){

	}
	private class PowerProfileConstants{

		/**
		 * No power consumption, or accounted for elsewhere.
		 */
		public static final String POWER_NONE = "none";

		/**
		 * Power consumption when CPU is in power collapse mode.
		 */
		public static final String POWER_CPU_IDLE = "cpu.idle";

		/**
		 * Power consumption when CPU is running at normal speed.
		 */
		public static final String POWER_CPU_NORMAL = "cpu.normal";

		/**
		 * Power consumption when CPU is running at full speed.
		 */
		public static final String POWER_CPU_FULL = "cpu.full";

		/**
		 * Power consumption when WiFi driver is scanning for networks.
		 */
		public static final String POWER_WIFI_SCAN = "wifi.scan";

		/**
		 * Power consumption when WiFi driver is on.
		 */
		public static final String POWER_WIFI_ON = "wifi.on";

		/**
		 * Power consumption when WiFi driver is transmitting/receiving.
		 */
		public static final String POWER_WIFI_ACTIVE = "wifi.active";

		/**
		 * Power consumption when GPS is on.
		 */
		public static final String POWER_GPS_ON = "gps.on";

		/**
		 * Power consumption when Bluetooth driver is on.
		 */
		public static final String POWER_BLUETOOTH_ON = "bluetooth.on";

		/**
		 * Power consumption when Bluetooth driver is transmitting/receiving.
		 */
		public static final String POWER_BLUETOOTH_ACTIVE = "bluetooth.active";

		/**
		 * Power consumption when Bluetooth driver gets an AT command.
		 */
		public static final String POWER_BLUETOOTH_AT_CMD = "bluetooth.at";

		/**
		 * Power consumption when screen is on, not including the backlight power.
		 */
		public static final String POWER_SCREEN_ON = "screen.on";

		/**
		 * Power consumption when cell radio is on but not on a call.
		 */
		public static final String POWER_RADIO_ON = "radio.on";

		/**
		 * Power consumption when talking on the phone.
		 */
		public static final String POWER_RADIO_ACTIVE = "radio.active";

		/**
		 * Power consumption at full backlight brightness. If the backlight is at
		 * 50% brightness, then this should be multiplied by 0.5
		 */
		public static final String POWER_SCREEN_FULL = "screen.full";

		/**
		 * Power consumed by the audio hardware when playing back audio content. This is in addition
		 * to the CPU power, probably due to a DSP and / or amplifier.
		 */
		public static final String POWER_AUDIO = "dsp.audio";

		/**
		 * Power consumed by any media hardware when playing back video content. This is in addition
		 * to the CPU power, probably due to a DSP.
		 */
		public static final String POWER_VIDEO = "dsp.video";


	}

	private class BatteryStatsConstants{
		private static final boolean LOCAL_LOGV = false;

		/**
		 * A constant indicating a partial wake lock timer.
		 */
		public static final int WAKE_TYPE_PARTIAL = 0;

		/**
		 * A constant indicating a full wake lock timer.
		 */
		public static final int WAKE_TYPE_FULL = 1;

		/**
		 * A constant indicating a window wake lock timer.
		 */
		public static final int WAKE_TYPE_WINDOW = 2;

		/**
		 * A constant indicating a sensor timer.
		 * 
		 * {@hide}
		 */
		public static final int SENSOR = 3;

		/**
		 * A constant indicating a a wifi turn on timer
		 *
		 * {@hide}
		 */
		public static final int WIFI_TURNED_ON = 4;

		/**
		 * A constant indicating a full wifi lock timer
		 *
		 * {@hide}
		 */
		public static final int FULL_WIFI_LOCK = 5;

		/**
		 * A constant indicating a scan wifi lock timer
		 *
		 * {@hide}
		 */
		public static final int SCAN_WIFI_LOCK = 6;

		/**
		 * A constant indicating a wifi multicast timer
		 *
		 * {@hide}
		 */
		public static final int WIFI_MULTICAST_ENABLED = 7;

		/**
		 * A constant indicating an audio turn on timer
		 *
		 * {@hide}
		 */
		public static final int AUDIO_TURNED_ON = 7;

		/**
		 * A constant indicating a video turn on timer
		 *
		 * {@hide}
		 */
		public static final int VIDEO_TURNED_ON = 8;

		/**
		 * Include all of the data in the stats, including previously saved data.
		 */
		public static final int STATS_TOTAL = 0;

		/**
		 * Include only the last run in the stats.
		 */
		public static final int STATS_LAST = 1;

		/**
		 * Include only the current run in the stats.
		 */
		public static final int STATS_CURRENT = 2;

		/**
		 * Include only the run since the last time the device was unplugged in the stats.
		 */
		public static final int STATS_UNPLUGGED = 3;

	}




}
