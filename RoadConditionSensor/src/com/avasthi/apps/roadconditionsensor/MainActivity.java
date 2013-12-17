package com.avasthi.apps.roadconditionsensor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;











import java.util.Timer;
import java.util.TimerTask;

import com.avasthi.apps.roadconditionsensor.sensorrecordendpoint.Sensorrecordendpoint;
import com.avasthi.apps.roadconditionsensor.sensorrecordendpoint.model.SensorRecord;
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
import android.database.Cursor;
import android.text.TextUtils;
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
	Sensor accelerometerSensor_;
	Sensor magneticFieldSensor_;
	Timer timer_;
	Timer uploadToCloudThread_;
	Runnable updateUiRunnable_;
	final long samplingTime = 5000; // Milliseconds
	
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
		updateUiRunnable_ = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
			    updateUI();
			}
			
		};
		timer_ = new Timer();
		timer_.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// UP Updates need to be done 
				if (db_.valuesAvailable()) {
					
					db_.sampleValues();
					runOnUiThread(updateUiRunnable_);
				}				
			}
			
		}, 0, samplingTime);
		
		uploadToCloudThread_ = new Timer();
		uploadToCloudThread_.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				uploadToCloud();
			}
			
		}, 0, samplingTime * 100);
		
		//createCloudEndpoint();
	}
	public void uploadToCloud() {

		Sensorrecordendpoint.Builder builder;
		List<Integer> idsToBeDeleted = new ArrayList<Integer>(); 
		try {
			builder = new Sensorrecordendpoint.Builder(AndroidHttp.newCompatibleTransport(), 
					new JacksonFactory(), 
					null);
			Sensorrecordendpoint endpoint = builder.build();
			Cursor cursor = db_.getCursor();
			if (cursor.moveToFirst()) {
				do {
					
					SensorRecord sr = new SensorRecord();
					for (int i = 0; i < db_.columnNames_.length; ++i) {
						if (db_.columnTypes_[i].equals(RoadConditionSensorDatabaseSchema.Column.INTEGER)) {
							sr.set(db_.columnNames_[i], 
									cursor.getInt(cursor.getColumnIndex(db_.columnNames_[i])));
						}
						else if (db_.columnTypes_[i].equals(RoadConditionSensorDatabaseSchema.Column.REAL)) {
							sr.set(db_.columnNames_[i], 
									cursor.getFloat(cursor.getColumnIndex(db_.columnNames_[i])));
						}
					}
					endpoint.insertSensorRecord(sr).execute();
					idsToBeDeleted.add(cursor.getInt(cursor.getColumnIndex(RoadConditionSensorDatabaseHelper.ID_FIELD)));
				} while(cursor.moveToNext());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		db_.deleteRows(idsToBeDeleted);
	}
	protected void onResume() {

        super.onResume();
	}

	void registerSensorListeners(SensorManager sm) {
		accelerometerSensor_ = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magneticFieldSensor_ = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sm.registerListener(this, accelerometerSensor_, 1 * 1024 * 1024);
		sm.registerListener(this, magneticFieldSensor_, 1 * 1024 * 1024);
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
		if (event.sensor.getType() == accelerometerSensor_.getType()) {
			
			db_.setAccelerometerValues(event.timestamp, event.values[0], event.values[1], event.values[2]);
		}
		else if(event.sensor.getType() == magneticFieldSensor_.getType()) {
			db_.setMagneticFieldValues(event.timestamp, event.values[0], event.values[1], event.values[2]);
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
	private void updateUI() {

		// before this is called, we need to make sure that all the variables have some value.
		TextView tv1 = (TextView)findViewById(R.id.timestamp);
		tv1.setText(DateFormat.getTimeInstance().toString());
		((TextView)findViewById(R.id.accuracy)).setText(getThreeDecimalPlaces(db_.accuracy_));
		((TextView)findViewById(R.id.bearing)).setText(getThreeDecimalPlaces(db_.bearing_));
		((TextView)findViewById(R.id.altitude)).setText(getThreeDecimalPlaces(db_.altitude_));
		((TextView)findViewById(R.id.latitude)).setText(getThreeDecimalPlaces(db_.latitude_));
		((TextView)findViewById(R.id.longitude)).setText(getThreeDecimalPlaces(db_.longitude_));
		((TextView)findViewById(R.id.speed)).setText(getThreeDecimalPlaces(db_.speed_));
		
		((TextView)findViewById(R.id.azimuth)).setText(getThreeDecimalPlaces(db_.azimuth_));
		((TextView)findViewById(R.id.pitch)).setText(getThreeDecimalPlaces(db_.pitch_));
		((TextView)findViewById(R.id.roll)).setText(getThreeDecimalPlaces(db_.roll_));
		((TextView)findViewById(R.id.accelx)).setText(getThreeDecimalPlaces(db_.accelx_));
		((TextView)findViewById(R.id.accely)).setText(getThreeDecimalPlaces(db_.accely_));
		((TextView)findViewById(R.id.accelz)).setText(getThreeDecimalPlaces(db_.accelx_));
		((TextView)findViewById(R.id.deltaaccelx)).setText(getThreeDecimalPlaces(db_.deltaAccelx_));
		((TextView)findViewById(R.id.deltaaccely)).setText(getThreeDecimalPlaces(db_.deltaAccely_));
		((TextView)findViewById(R.id.deltaaccelz)).setText(getThreeDecimalPlaces(db_.deltaAccelz_));
	}
}
