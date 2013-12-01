package com.avasthi.apps.roadconditionsensor;

public class RoadConditionSensorDataStorage {
	
	private static RoadConditionSensorDataStorage self_ = null;

	public static synchronized RoadConditionSensorDataStorage getInstance() {
		if (self_ == null) {
			self_ = new RoadConditionSensorDataStorage();
		}
		return self_;
	}
	private void createFile() {
		
	}
}
