package com.avasthi.apps.roadconditionsensor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;

public class RoadConditionSensorDatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "roadconditionsensorDB.db";
	private static final String TABLE = "sensorrecord";
	RoadConditionSensorDatabaseSchema schema_;
	String[] columnNames_ = { "timestamp", // 0
			"accelx", // 1
			"accely", // 2
			"accelz", // 3
			"deltaaccelx", // 4
			"deltaaccely", // 5
			"deltaaccelz", // 6
			"accuracy", // 7
			"bearing", // 8
			"latitude", // 9
			"longitude", // 10
			"altitude", // 11
			"speed" // 12
	};
	String[] columnTypes_ = { RoadConditionSensorDatabaseSchema.Column.INTEGER, // 0
			RoadConditionSensorDatabaseSchema.Column.REAL, // 1
			RoadConditionSensorDatabaseSchema.Column.REAL, // 2
			RoadConditionSensorDatabaseSchema.Column.REAL, // 3
			RoadConditionSensorDatabaseSchema.Column.REAL, // 4
			RoadConditionSensorDatabaseSchema.Column.REAL, // 5
			RoadConditionSensorDatabaseSchema.Column.REAL, // 6
			RoadConditionSensorDatabaseSchema.Column.REAL, // 7
			RoadConditionSensorDatabaseSchema.Column.REAL, // 8
			RoadConditionSensorDatabaseSchema.Column.REAL, // 9
			RoadConditionSensorDatabaseSchema.Column.REAL, // 10
			RoadConditionSensorDatabaseSchema.Column.REAL, // 11
			RoadConditionSensorDatabaseSchema.Column.REAL // 12
	};
	Long timestamp_ = null; // 0
	Float accelx_ = null; // 1
	Float accely_ = null; // 2
	Float accelz_ = null; // 3
	Float deltaAccelx_ = null; // 4
	Float deltaAccely_ = null; // 5
	Float deltaAccelz_ = null; // 6
	Float accuracy_ = null; // 7
	Float bearing_ = null; // 8
	Double latitude_ = null; // 9
	Double longitude_ = null; // 10
	Double altitude_ = null; // 11
	Float speed_ = null; // 12

	public RoadConditionSensorDatabaseHelper(Context context) {
		super(context, getDatabaseFile(context), null, DATABASE_VERSION);
		schema_ = new RoadConditionSensorDatabaseSchema(TABLE);
		schema_.addAutomaticId();
		for (int i = 0; i < columnNames_.length; ++i) {

			schema_.addColumn(columnNames_[i], columnTypes_[i]);
		}
	}

	protected static String getMainMemoryDatabaseDirectory() {
		String path = Environment.getDataDirectory() + "/"
				+ R.string.app_name + "/" + "database";
		File fp = new File(path);
		if (!fp.canWrite()) {
			fp.mkdirs();
		}
		return path;
	}

	protected static String getExternalMemoryDatabaseDirectory() {
		String path = Environment.getExternalStorageDirectory() + "/"
				+ R.string.app_name + "/" + "database";
		File fp = new File(path);
		if (!fp.canWrite()) {
			fp.mkdirs();
		}
		return path;
		
	}

	protected static String getMainMemoryDatabaseFile() {
		return getMainMemoryDatabaseDirectory() + "/" + DATABASE_NAME;
	}

	protected static String getExternalMemoryDatabaseFile() {
		return getExternalMemoryDatabaseDirectory() + "/" + DATABASE_NAME;
	}

	protected static void copyFromInternalToExternalDB() {

	}

	protected static String getDatabaseFile(Context context) {
		String path = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			// We have a SD card available. We need to find out if some data
			// exists on main storage.
//			try {
//				FileInputStream fis = context.
//						.openFileInput(getMainMemoryDatabaseFile());
//				fis.close();
//			} catch (FileNotFoundException e) {
//				Log.v("DatabasePath", e.getMessage());
//				// TODO Auto-generated catch block
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				Log.v("DatabasePath", e.getMessage());
//			}
			copyFromInternalToExternalDB();
			path = getExternalMemoryDatabaseFile();
		} else {
			path = getMainMemoryDatabaseFile();
		}
		Log.v("DatabasePath", path);
		return path;
	}

	public void insert(SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(columnNames_[0], timestamp_);
		cv.put(columnNames_[1], accelx_);
		cv.put(columnNames_[2], accely_);
		cv.put(columnNames_[3], accelz_);
		cv.put(columnNames_[4], deltaAccelx_);
		cv.put(columnNames_[5], deltaAccely_);
		cv.put(columnNames_[6], deltaAccelz_);
		cv.put(columnNames_[7], accuracy_);
		cv.put(columnNames_[8], latitude_);
		cv.put(columnNames_[9], longitude_);
		cv.put(columnNames_[10], altitude_);
		cv.put(columnNames_[11], speed_);
		db.insert(schema_.tableName_, null, cv);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = schema_.getCreateSchema();
		db.execSQL(sql);

		Log.v("DB", "Calling On create.. " + sql);
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void setAccelerometerValues(long timestamp, float accelx,
			float accely, float accelz) {
		// TODO Auto-generated method stub
		if (accelx_ == null) {
			accelx_ = 0F; // 1
			accely_ = 0F; // 2
			accelz_ = 0F; // 3
		}
		timestamp_ = timestamp;
		deltaAccelx_ = accelx - accelx_;
		deltaAccely_ = accely - accely_;
		deltaAccelz_ = accely - accely_;
		accelx_ = accelx;
		accely_ = accely;
		accelz_ = accelz;
	}

	public void setLocationValues(long timestamp, 
								  float accuracy,
								  float bearing, 
								  double altitude, 
								  double latitude, 
								  double longitude,
								  float speed) {
		// TODO Auto-generated method stub
		if (accelx_ == null) {
			timestamp_ = timestamp;
			accelx_ = 0F; // 1
			accely_ = 0F; // 2
			accelz_ = 0F; // 3
			deltaAccelx_ = 0F; // 4
			deltaAccely_ = 0F; // 5
			deltaAccelz_ = 0F; // 6
		}
		timestamp_ = timestamp;
		accuracy_ = accuracy;
		latitude_ = latitude;
		longitude_ = longitude;
		altitude_ = altitude;
		speed_ = speed;
		bearing_ = bearing;
	}

}
