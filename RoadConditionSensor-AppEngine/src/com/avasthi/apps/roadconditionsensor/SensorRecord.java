package com.avasthi.apps.roadconditionsensor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class SensorRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Key key;

	Date timestamp_;
	float accuracy_;
	double altitude_;
	float bearing_;
	double latitude_;
	double longitude_;
	float speed_;
	float accelx_;
	float accely_;
	float accelz_;
	float deltaAccelx_;
	float deltaAccely_;
	float deltaAccelz_;
	float azimuth_;
	float pitch_;
	float roll_;
	float mfx_;
	float mfy_;
	float mfz_;
	
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public Date getTimestamp() {
		return timestamp_;
	}
	public float getAccuracy() {
		return accuracy_;
	}
	public double getAltitude() {
		return altitude_;
	}
	public float getBearing() {
		return bearing_;
	}
	public double getLatitude() {
		return latitude_;
	}
	public double getLongitude() {
		return longitude_;
	}
	public float getSpeed() {
		return speed_;
	}
	public float getAccelx() {
		return accelx_;
	}
	public float getAccely() {
		return accely_;
	}
	public float getAccelz() {
		return accelz_;
	}
	public float getDeltaAccelx() {
		return deltaAccelx_;
	}
	public float getDeltaAccely() {
		return deltaAccely_;
	}
	public float getDeltaAccelz() {
		return deltaAccelz_;
	}
	public float getAzimuth() {
		return azimuth_;
	}
	public float getPitch() {
		return pitch_;
	}
	public float getRoll() {
		return roll_;
	}
	public float getMfx() {
		return mfx_;
	}
	public float getMfy() {
		return mfy_;
	}
	public float getMfz() {
		return mfz_;
	}

}
