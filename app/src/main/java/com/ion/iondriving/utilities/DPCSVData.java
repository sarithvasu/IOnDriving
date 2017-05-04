package com.ion.iondriving.utilities;



import android.location.Location;


public class DPCSVData {
	public DPCSVData(){

	}
	private double score,latitude,longitude,speed,acceleration,breaking,cornerning,accX,accY,accZ,gyroX,gyroY,gyroZ;
	private long seconds;
	private int signalStrength;
	private String formater;
	private float gps_accuracy;
	private String timestamp;
	/*private DateFormat date,time;*/
	private String strdate,strtime;
	private Location currentLocation,location;
	private int breaking_value;

	public int getBreaking_value() {
		return breaking_value;
	}

	public void setBreaking_value(int breaking_value) {
		this.breaking_value = breaking_value;
	}

	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}
	public double getBreaking() {
		return breaking;
	}
	public void setBreaking(double breaking) {
		this.breaking = breaking;
	}
	public double getCornerning() {
		return cornerning;
	}
	public void setCornerning(double cornerning) {
		this.cornerning = cornerning;
	}
	public long getSeconds() {
		return seconds;
	}
	public void setSeconds(long seconds) {
		this.seconds = seconds;
	}
	public String getFormater() {
		return formater;
	}
	public void setFormater(String formater) {
		this.formater = formater;
	}

	public String getDate() {
		return strdate;
	}
	public void setDate(String date) {
		this.strdate = date;
	}

	public String getTime() {
		return strtime;
	}
	public void setTime(String time) {
		this.strtime = time;
	}
	public int getSignalStrength() {
		return signalStrength;
	}
	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}

	public double getAccX() {
		return accX;
	}
	public void setAccX(double accX) {
		this.accX = accX;
	}
	public double getAccY() {
		return accY;
	}
	public void setAccY(double accY) {
		this.accY = accY;
	}
	public double getAccZ() {
		return accZ;
	}
	public void setAccZ(double accZ) {
		this.accZ = accZ;
	}
	public double getGyroX() {
		return gyroX;
	}
	public void setGyroX(double gyroX) {
		this.gyroX = gyroX;
	}
	public double getGyroY() {
		return gyroY;
	}
	public void setGyroY(double gyroY) {
		this.gyroY = gyroY;
	}
	public double getGyroZ() {
		return gyroZ;
	}
	public void setGyroZ(double gyroZ) {
		this.gyroZ = gyroZ;
	}
	public float getGPSAccuracy() {
		return gps_accuracy;
	}
	public void setGPSAccuracy(float accuracy) {
		this.gps_accuracy = accuracy;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public Location getCurrentLocation() {
		return currentLocation;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}








}
