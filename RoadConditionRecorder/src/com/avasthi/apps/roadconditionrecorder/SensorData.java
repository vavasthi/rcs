package com.avasthi.apps.roadconditionrecorder;

import android.hardware.Sensor;

public class SensorData {

	private Sensor sensor_;
	private Float[] values_;
	public SensorData(Sensor sensor) {
		sensor_ = sensor;
		values_ = null;
	}
	public Sensor getSensor() {
		return sensor_;
	}
	public void setValues(Float[] values) {
		values_ = values;
	}
	public void setValues(float[] values) {
		values_ = new Float[values.length];
		for (int i = 0; i < values.length; ++i) {
			values_[i] = values[i];
		}
	}
	public Float[] getValues() {
		return values_;
	}
}
