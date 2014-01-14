package com.tudordev.powerusageservice;


import java.io.IOException;
import java.io.RandomAccessFile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class StaticAndroidCollector {

	

	public String OSVersion;
	public String VersionNr;
	public String DeviceName;
	public String DeviceModel;
	public String Product;
	public String Brand;
	public String Display;
	public String CPU_ABI;
	public String CPU_ABI2;
	public String UNKNOWN;
	public String Hardware;
	public String ID;
	public String Manufacturer;
	public String Serial;
	public String User;
	public String Host;
	public String BatteryInfo;
	public String DeviceInfo;
	public String RAMTotal;
	public String RAMFree;
	
	public String getDeviceInfo() {
		getRAMInfo();
		
		DeviceInfo = "Device Model: "+DeviceModel+"\n"+
				"Android OS Version: "+VersionNr+"\n"+
				RAMTotal +"\n"+
				RAMFree  +"\n"+
				"Total Disk Space: "+Double.toString(getTotalMemory())+"\n"+
				"Free Disk Space: "+Double.toString(getFreeMemory())+"\n"+
				"CPU: "+CPU_ABI+"\n"+
				"Brand: "+Brand+"\n"+
				"Product: "+Product+"\n"+
				"Version Number: "+OSVersion+"\n";
		return DeviceInfo;
	}

	public BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
			int  icon_small= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
			int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
			int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
			boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT); 
			int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
			int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
			String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
			int  temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
			int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
			
 
			BatteryInfo =
					"Health: "+health+"\n"+
					"Icon Small:"+icon_small+"\n"+
					"Level: "+level+"\n"+
					"Plugged: "+plugged+"\n"+
					"Present: "+present+"\n"+
					"Scale: "+scale+"\n"+
					"Status: "+status+"\n"+
					"Technology: "+technology+"\n"+
					"Temperature: "+temperature+"\n"+
					"Voltage: "+voltage+"\n";
		}
	};
	
	public StaticAndroidCollector() {
		// TODO Auto-generated constructor stub
		OSVersion = System.getProperty("os.version");
		VersionNr = android.os.Build.VERSION.RELEASE;
		DeviceName = android.os.Build.DEVICE; 
		DeviceModel = android.os.Build.MODEL; 
		Product = android.os.Build.PRODUCT; 
		Brand = android.os.Build.BRAND; 
		Display = android.os.Build.DISPLAY; 
		CPU_ABI = android.os.Build.CPU_ABI; 
		CPU_ABI2 = android.os.Build.CPU_ABI2; 
		UNKNOWN = android.os.Build.UNKNOWN; 
		Hardware = android.os.Build.HARDWARE;
		ID = android.os.Build.ID; 
		Manufacturer = android.os.Build.MANUFACTURER; 
		Serial = android.os.Build.SERIAL; 
		User = android.os.Build.USER; 
		Host = android.os.Build.HOST; 
	}
	
	public String getRAMInfo() {
	    RandomAccessFile reader = null;
	    RAMTotal = null;
	    RAMFree = null;
	    
	    try {
	        reader = new RandomAccessFile("/proc/meminfo", "r");
	        RAMFree = reader.readLine();
	        RAMTotal = reader.readLine();
	        reader.close();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	       
	    }
	    return RAMTotal;
	}
	
	 public double  getTotalMemory()
	    {
	        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());   
	        double  Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
	        Log.d("memory", "Total Memory: " + Double.toString(Total));
	        return Total;
	    }

	    public double  getFreeMemory()
	    {
	        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
	        double  Free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
	        Log.d("memory", "Free Memory: " + Double.toString(Free));
	        return Free;
	    }

	    public double  getBusyMemory()
	    {
	        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());   
	        double  Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
	        double  Free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
	        double  Busy  = Total - Free;
	        
	        Log.d("memory", "Used Memory: " + Double.toString(Busy));
	        return Busy;
	    }
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.OSVersion + "\n" + this.VersionNr + "\n" + this.CPU_ABI;
	}
	
	public String getOSVersion() {
		return OSVersion;
	}

	public String getVersionNr() {
		return VersionNr;
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public String getDeviceModel() {
		return DeviceModel;
	}

	public String getProduct() {
		return Product;
	}

	public String getBrand() {
		return Brand;
	}

	public String getDisplay() {
		return Display;
	}

	public String getCPU_ABI() {
		return CPU_ABI;
	}

	public String getCPU_ABI2() {
		return CPU_ABI2;
	}

	public String getUNKNOWN() {
		return UNKNOWN;
	}

	public String getHardware() {
		return Hardware;
	}

	public String getID() {
		return ID;
	}

	public String getManufacturer() {
		return Manufacturer;
	}

	public String getSerial() {
		return Serial;
	}

	public String getUser() {
		return User;
	}

	public String getHost() {
		return Host;
	}
	


}
