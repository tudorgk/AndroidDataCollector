package com.tudordev.powerusageservice;

public class PowerEvent {
	
	public enum DrainType {
		IDLE,
		CELL,
		PHONE,
		WIFI,
		BLUETOOTH,
		SCREEN,
		APP
	}

	private String name;
	private Object uidObj;
	private double value;
	private double[] values;
	private DrainType drainType;
	private long usageTime;
	private long cpuTime;
	private long gpsTime;
	private long cpuFgTime;
	
	private long tcpBytesReceived;
	private long tcpBytesSent;
	
	public long getTcpBytesReceived() {
		return tcpBytesReceived;
	}

	public void setTcpBytesReceived(long tcpBytesReceived) {
		this.tcpBytesReceived = tcpBytesReceived;
	}

	public long getTcpBytesSent() {
		return tcpBytesSent;
	}

	public void setTcpBytesSent(long tcpBytesSent) {
		this.tcpBytesSent = tcpBytesSent;
	}

	public double getWifiPowerDrain() {
		return wifiPowerDrain;
	}

	public void setWifiPowerDrain(double wifiPowerDrain) {
		this.wifiPowerDrain = wifiPowerDrain;
	}

	private double powerDrain;
	private double wifiPowerDrain;
	private double percent;
	private double noCoveragePercent;
	private String defaultPackageName;

	public PowerEvent(String label, DrainType drainType, int iconId,
			Object uid, double[] values) {
		this.values = values;
		name = label;
		this.drainType = drainType;
		if (values != null)
			value = values[0];

		uidObj = uid;
	}

	public long getCpuTime(){
		return cpuTime;
	}

	public void setCpuTime(long time){
		cpuTime = time;
	}

	public long getUsageTime(){
		return usageTime;
	}

	public void setUsageTime(long time){
		usageTime = time;
	}

	public long getGpsTime(){
		return gpsTime;
	}

	public void setGpsTime(long time){
		gpsTime = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getUidObj() {
		return uidObj;
	}

	public void setUidObj(Object uidObj) {
		this.uidObj = uidObj;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public DrainType getDrainType() {
		return drainType;
	}

	public void setDrainType(DrainType drainType) {
		this.drainType = drainType;
	}

	public long getCpuFgTime() {
		return cpuFgTime;
	}

	public void setCpuFgTime(long cpuFgTime) {
		this.cpuFgTime = cpuFgTime;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public double getNoCoveragePercent() {
		return noCoveragePercent;
	}

	public void setNoCoveragePercent(double noCoveragePercent) {
		this.noCoveragePercent = noCoveragePercent;
	}

	public String getDefaultPackageName() {
		return defaultPackageName;
	}

	public void setDefaultPackageName(String defaultPackageName) {
		this.defaultPackageName = defaultPackageName;
	}

	public double getSortValue() {
		return value;
	}

	public double[] getValues() {
		return values;
	}

	public int compareTo(PowerEvent other) {
		// Return the flipped value because we want the items in descending
		// order
		return (int) (other.getSortValue() - getSortValue());
	}

	public double getPowerDrain() {
		return powerDrain;
	}

	public void setPowerDrain(double powerDrain) {
		this.powerDrain = powerDrain;
	}
}
