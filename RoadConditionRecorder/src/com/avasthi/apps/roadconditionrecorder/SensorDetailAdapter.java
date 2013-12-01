package com.avasthi.apps.roadconditionrecorder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avasthi.roadconditionrecorder.R;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SensorDetailAdapter extends ArrayAdapter<String> {
	private final Context context_;
	Map<Sensor, SensorData> sensorDetail_;
	Map<Integer, Sensor> sensorPosition_;
	SensorEvent se;
	Location lastKnownLocation_;

	public SensorDetailAdapter(Context context, List<Sensor> sensorList,
			Location lastKnownLocation) {
		super(context, R.layout.sensor_view, new String[sensorList.size()]);
		lastKnownLocation_ = lastKnownLocation;
		context_ = context;
		sensorDetail_ = new HashMap<Sensor, SensorData>();
		sensorPosition_ = new HashMap<Integer, Sensor>();
		int i = 0;
		for (Sensor sensor : sensorList) {
			sensorDetail_.put(sensor, new SensorData(sensor));
			sensorPosition_.put(i, sensor);
			++i;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context_
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.sensor_view, parent, false);
		TextView nameView = (TextView) rowView.findViewById(R.id.sensorName);
		TextView vendorView = (TextView) rowView
				.findViewById(R.id.sensorVendor);
		TextView valuesView = (TextView) rowView.findViewById(R.id.values);
		TextView locationView = (TextView) rowView.findViewById(R.id.location);
		Sensor s = sensorPosition_.get(position);
		SensorData sd = sensorDetail_.get(s);
		nameView.setText(sd.getSensor().getName());
		vendorView.setText(sd.getSensor().getVendor());
		String str = "(";
		if (sd.getValues() != null) {
			if (sd.getValues().length > 0) {
				str += sd.getValues()[0].toString();
				for (int i = 1; i < sd.getValues().length; ++i) {
					str += "," + sd.getValues()[i].toString();
				}
			}
		}
		valuesView.setText(str);

		str += ")";
		str = "(";
		if (lastKnownLocation_ != null) {

			str += lastKnownLocation_.getAccuracy() + ",";
			str += lastKnownLocation_.getAltitude() + ",";
			str += lastKnownLocation_.getLatitude() + ",";
			str += lastKnownLocation_.getLongitude() + ",";
			str += lastKnownLocation_.getSpeed() + ",";
			str += lastKnownLocation_.getTime();
		}
		str += ")";
		locationView.setText(str);

		// Change the icon for Windows and iPhone
		return rowView;
	}

	public void setValues(Sensor sensor, float[] values) {
		SensorData sd = sensorDetail_.get(sensor);
		sd.setValues(values);
	}

	public void setLocation(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {

			lastKnownLocation_ = location;
			Log.v("location", "Location updated in adapter"
					+ lastKnownLocation_.toString());
			this.notifyDataSetChanged();
		}

	}
}
