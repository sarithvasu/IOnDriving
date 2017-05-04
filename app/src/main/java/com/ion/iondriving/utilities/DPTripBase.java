package com.ion.iondriving.utilities;

import java.lang.reflect.Array;

public class DPTripBase {
	private  String m_tripDistance ;
	private  String m_tripDuration;
	private  String m_lastRecordedTimeStamp;
	private  Array m_latitudeCollection;
	private  Array m_longitudeCollection;
	private  Array m_speedCollection;
	private  int m_accelerationRating;
	private  int m_breakingRating;
	private  int m_corneringRating;
	private  int m_score;
	private  int m_distractionRating;
	private  String m_tripDate;
	private  String m_firstRecordedTimeStamp;
	private  Array m_mapPoints;
	private  int m_isToBeMarkedWithRed;
	private String m_updatedFileName;
	private String m_originalFileName;


	private String m_tripFileName;


	public String getTripDistance() {
		return m_tripDistance;
	}
	public void setTripDistance(String tripDistance) {
		this.m_tripDistance = tripDistance;
	}
	public String getTripDuration() {
		return m_tripDuration;
	}
	public void setTripDuration(String tripDuration) {
		this.m_tripDuration = tripDuration;
	}
	public String getLastRecordedTimeStamp() {
		return m_lastRecordedTimeStamp;
	}
	public void setLastRecordedTimeStamp(String lastRecordedTimeStamp) {
		this.m_lastRecordedTimeStamp = lastRecordedTimeStamp;
	}
	public Array getLatitudeCollection() {
		return m_latitudeCollection;
	}
	public void setLatitudeCollection(Array latitudeCollection) {
		this.m_latitudeCollection = latitudeCollection;
	}
	public Array getLongitudeCollection() {
		return m_longitudeCollection;
	}
	public void setLongitudeCollection(Array longitudeCollection) {
		this.m_longitudeCollection = longitudeCollection;
	}
	public Array getSpeedCollection() {
		return m_speedCollection;
	}
	public void setSpeedCollection(Array speedCollection) {
		this.m_speedCollection = speedCollection;
	}
	public int getAccelerationRating() {
		return m_accelerationRating;
	}
	public void setAccelerationRating(int accelerationRating) {
		this.m_accelerationRating = accelerationRating;
	}
	public int getBreakingRating() {
		return m_breakingRating;
	}
	public void setBreakingRating(int breakingRating) {
		this.m_breakingRating = breakingRating;
	}
	public int getCorneringRating() {
		return m_corneringRating;
	}
	public void setCorneringRating(int corneringRating) {
		this.m_corneringRating = corneringRating;
	}
	public int getScore() {
		return m_score;
	}
	public void setScore(int score) {
		this.m_score = score;
	}
	public int getDistractionRating() {
		return m_distractionRating;
	}
	public void setDistractionRating(int distractionRating) {
		this.m_distractionRating = distractionRating;
	}
	public String getTripDate() {
		return m_tripDate;
	}
	public void setTripDate(String tripDate) {
		this.m_tripDate = tripDate;
	}
	public String getFirstRecordedTimeStamp() {
		return m_firstRecordedTimeStamp;
	}
	public void setFirstRecordedTimeStamp(String firstRecordedTimeStamp) {
		this.m_firstRecordedTimeStamp = firstRecordedTimeStamp;
	}
	public Array getMapPoints() {
		return m_mapPoints;
	}
	public void setMapPoints(Array mapPoints) {
		this.m_mapPoints = mapPoints;
	}
	public int isToBeMarkedWithRed() {
		return m_isToBeMarkedWithRed;
	}
	public void setToBeMarkedWithRed(int isToBeMarkedWithRed) {
		this.m_isToBeMarkedWithRed = isToBeMarkedWithRed;
	}
	public String getM_updatedFileName() {
		return m_updatedFileName;
	}
	public void setM_updatedFileName(String m_updatedFileName) {
		this.m_updatedFileName = m_updatedFileName;
	}
	public String getM_originalFileName() {
		return m_originalFileName;
	}
	public void setM_originalFileName(String m_originalFileName) {
		this.m_originalFileName = m_originalFileName;
	}
	public String gettripFileName() {
		return m_tripFileName;
	}
	public void settripFileName(String m_tripFileName) {
		this.m_tripFileName = m_tripFileName;
	}

}
