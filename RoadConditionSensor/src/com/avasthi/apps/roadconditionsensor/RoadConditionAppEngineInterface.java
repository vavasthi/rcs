package com.avasthi.apps.roadconditionsensor;

import com.avasthi.apps.roadconditionsensor.sensorrecordendpoint.Sensorrecordendpoint;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson.JacksonFactory;

public class RoadConditionAppEngineInterface {

	private static RoadConditionAppEngineInterface self_ = null;

	private Sensorrecordendpoint endpoint_;

	public static synchronized RoadConditionAppEngineInterface getInstance() {
		if (self_ == null) {
			self_ = new RoadConditionAppEngineInterface();
		}
		return self_;
	}
	protected void createCloudEndpoint() {
		Sensorrecordendpoint.Builder builder = new Sensorrecordendpoint.Builder(
		AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
		endpoint_ = builder.build();
	}
}
