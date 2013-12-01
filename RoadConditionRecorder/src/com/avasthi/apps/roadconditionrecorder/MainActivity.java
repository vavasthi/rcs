package com.avasthi.apps.roadconditionrecorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.avasthi.roadconditionrecorder.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.Service;
import android.util.Log;
import android.view.Menu;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity  implements SensorEventListener, LocationListener {

	LocationManager locationManager_;
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
		
	}
	protected void onResume() {

        super.onResume();
	}

	void registerSensorListeners(SensorManager sm) {
		List<Sensor> sensorList = sm.getSensorList(Sensor.TYPE_ALL);
		populateList(sensorList);
		for (Sensor s : sensorList) {

		    sm.registerListener(this, s, 50000000);
		}
	}
	@SuppressWarnings("rawtypes")
	void populateList(List<Sensor> sensorList) {
		ListView lv 
		= (ListView) findViewById(R.id.listView1);
		lv.setAdapter(new SensorDetailAdapter(this, sensorList, locationManager_.getLastKnownLocation(LOCATION_SERVICE)));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		ListView lv = (ListView) findViewById(R.id.listView1);
		SensorDetailAdapter sda = (SensorDetailAdapter)lv.getAdapter();
		sda.setValues(event.sensor, event.values);
		sda.setLocation(locationManager_.getLastKnownLocation(LOCATION_SERVICE));
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.v(LOCATION_SERVICE, "Location change receieved " + location.getLatitude() + "," + location.getLongitude());
		ListView lv = (ListView) findViewById(R.id.listView1);
		SensorDetailAdapter sda = (SensorDetailAdapter)lv.getAdapter();
		sda.setLocation(location);
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
