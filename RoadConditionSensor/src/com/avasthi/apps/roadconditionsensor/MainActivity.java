package com.avasthi.apps.roadconditionsensor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;











import com.avasthi.apps.roadconditionsensor.sensorrecordendpoint.Sensorrecordendpoint;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson.JacksonFactory;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.Service;
import android.util.Log;
import android.view.Menu;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity  implements SensorEventListener, LocationListener {

	LocationManager locationManager_;
	RoadConditionSensorDatabaseHelper db_;
	Sensor s_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Setup location stuff
		locationManager_ 
		= (LocationManager)this.getSystemService(LOCATION_SERVICE);
		locationManager_.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
		// Get the reference to the sensor manager
		SensorManager sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
		// Get the list of sensor
		registerSensorListeners(sensorManager);

		db_ = new RoadConditionSensorDatabaseHelper(this.getApplicationContext());
		db_.getReadableDatabase();
		
		//createCloudEndpoint();
	}
	protected void onResume() {

        super.onResume();
	}

	void registerSensorListeners(SensorManager sm) {
		s_ = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(this, s_, 1 * 1024 * 1024);
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	private String getThreeDecimalPlaces(float v) {
		final DecimalFormat threeDecimal = new DecimalFormat("#,##0.000");
		return threeDecimal.format(v);
	}
	private String getThreeDecimalPlaces(double v) {
		final DecimalFormat threeDecimal = new DecimalFormat("#,##0.000");
		return threeDecimal.format(v);
	}
	private void setWidget(int widgetId, String value) {
		
		TextView tv1 = (TextView)findViewById(widgetId);
		tv1.setText(value);
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == s_.getType()) {
			// Update the UI
			setWidget(R.id.timestamp, new Date().toString());
			setWidget(R.id.accelx, getThreeDecimalPlaces(event.values[0]));
			setWidget(R.id.accely, getThreeDecimalPlaces(event.values[1]));
			setWidget(R.id.accelz, getThreeDecimalPlaces(event.values[2]));

			
			db_.setAccelerometerValues(event.timestamp, event.values[0], event.values[1], event.values[2]);
		}
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		db_.setLocationValues(location.getTime(), 
					          location.getAccuracy(), 
					          location.getBearing(), 
					          location.getAltitude(), 
					          location.getLatitude(), 
					          location.getLongitude(), 
					          location.getSpeed());
		db_.insert(db_.getWritableDatabase());
		// Update the UI
		TextView tv1 = (TextView)findViewById(R.id.timestamp);
		tv1.setText(new Date(location.getTime()).toString());
		((TextView)findViewById(R.id.accuracy)).setText(getThreeDecimalPlaces(location.getAccuracy()));
		((TextView)findViewById(R.id.bearing)).setText(getThreeDecimalPlaces(location.getBearing()));
		((TextView)findViewById(R.id.altitude)).setText(getThreeDecimalPlaces(location.getAltitude()));
		((TextView)findViewById(R.id.latitude)).setText(getThreeDecimalPlaces(location.getLatitude()));
		((TextView)findViewById(R.id.longitude)).setText(getThreeDecimalPlaces(location.getLongitude()));
		((TextView)findViewById(R.id.speed)).setText(getThreeDecimalPlaces(location.getSpeed()));
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
