package com.example.androiddatacollector;


import java.io.IOException;
import java.io.RandomAccessFile;

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
	    String memInfo = null;
	    String freeMemory = null;
	    
	    try {
	        reader = new RandomAccessFile("/proc/meminfo", "r");
	        freeMemory = reader.readLine();
	        memInfo = reader.readLine();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	       
	    }
	    
	    try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    Log.d("memory", freeMemory);
	    Log.d("memory", memInfo);
	    return freeMemory;
	}
	
	 public long getTotalMemory()
	    {
	        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());   
	        long Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
	        Log.d("memory", "Total Memory: " + Long.toString(Total));
	        return Total;
	    }

	    public long getFreeMemory()
	    {
	        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
	        long Free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
	        Log.d("memory", "Free Memory: " + Long.toString(Free));
	        return Free;
	    }

	    public long getBusyMemory()
	    {
	        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());   
	        long Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
	        long Free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
	        long Busy  = Total - Free;
	        
	        Log.d("memory", "Used Memory: " + Long.toString(Busy));
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
