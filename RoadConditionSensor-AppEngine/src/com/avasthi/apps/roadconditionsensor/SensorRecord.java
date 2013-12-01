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
	private Key key;
	
	private Date timestamp_;
	float	 accuracy_;
	double	 altitude_;
	float	 bearing_;
	double	 latitude_;
	double	 longitude_;
	float	 speed_;

	public Key getKey() {
		return key;
	}
	public Date getTimestamp() {
		return timestamp_;
	}
	public float	 getAccuracy() {
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
}
