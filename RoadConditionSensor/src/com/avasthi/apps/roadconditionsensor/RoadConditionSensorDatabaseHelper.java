package com.avasthi.apps.roadconditionsensor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.SensorManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

public class RoadConditionSensorDatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "roadconditionsensorDB.db";
	private static final String TABLE = "sensorrecord";
	static final String ID_FIELD = "_id";
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
			"speed", // 12
			"azimuth", // 13
			"pitch", // 14
			"roll", // 15
			"mfx", // 16
			"mfy", // 17
			"mfz", // 18
			"uploaded" // 19
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
			RoadConditionSensorDatabaseSchema.Column.REAL, // 12
			RoadConditionSensorDatabaseSchema.Column.REAL, // 13
			RoadConditionSensorDatabaseSchema.Column.REAL, // 14
			RoadConditionSensorDatabaseSchema.Column.REAL, // 15
			RoadConditionSensorDatabaseSchema.Column.REAL, // 16
			RoadConditionSensorDatabaseSchema.Column.REAL, // 17
			RoadConditionSensorDatabaseSchema.Column.REAL, // 18
			RoadConditionSensorDatabaseSchema.Column.INTEGER // 19
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
	Float azimuth_ = null; // 13
	Float pitch_ = null; // 14
	Float roll_ = null; // 15
	Float mfx_ = null; // 16
	Float mfy_ = null; // 17
	Float mfz_ = null; // 18
	Integer uploaded_ = 0; // 19

	public RoadConditionSensorDatabaseHelper(Context context) {
		super(context, getDatabaseFile(context), null, DATABASE_VERSION);
		schema_ = new RoadConditionSensorDatabaseSchema(TABLE);
		schema_.addAutomaticId();
		for (int i = 0; i < columnNames_.length; ++i) {

			schema_.addColumn(columnNames_[i], columnTypes_[i]);
		}
	}

	protected static String getMainMemoryDatabaseDirectory() {
		String path = Environment.getDataDirectory() + "/" + R.string.app_name
				+ "/" + "database";
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
			// try {
			// FileInputStream fis = context.
			// .openFileInput(getMainMemoryDatabaseFile());
			// fis.close();
			// } catch (FileNotFoundException e) {
			// Log.v("DatabasePath", e.getMessage());
			// // TODO Auto-generated catch block
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// Log.v("DatabasePath", e.getMessage());
			// }
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
		cv.put(columnNames_[12], azimuth_);
		cv.put(columnNames_[13], pitch_);
		cv.put(columnNames_[14], roll_);
		cv.put(columnNames_[15], mfx_);
		cv.put(columnNames_[16], mfy_);
		cv.put(columnNames_[17], mfz_);
		cv.put(columnNames_[18], uploaded_);
		db.insert(schema_.tableName_, null, cv);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = schema_.getCreateSchema();
		db.execSQL(sql);

		Log.v("DB", "Calling On create.. " + sql);
		// TODO Auto-generated method stub

	}
	public Cursor getCursor() {
		Cursor cursor 
		= getWritableDatabase().query(TABLE, null, "uploaded = 0", null, null, null, null);
		return cursor;
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL(schema_.getDropSchema());
		db.execSQL(schema_.getCreateSchema());
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

	public void setOrientationValues(long timestamp, float azimuth,
			float pitch, float roll) {
		// TODO Auto-generated method stub
		if (accelx_ == null) {
			accelx_ = 0F; // 1
			accely_ = 0F; // 2
			accelz_ = 0F; // 3
		}
		timestamp_ = timestamp;
		azimuth_ = azimuth;
		pitch_ = pitch;
		roll_ = roll;
	}

	public void setMagneticFieldValues(long timestamp, float mfx, float mfy,
			float mfz) {
		// TODO Auto-generated method stub
		if (accelx_ == null) {
			accelx_ = 0F; // 1
			accely_ = 0F; // 2
			accelz_ = 0F; // 3
		}
		timestamp_ = timestamp;
		mfx_ = mfx;
		mfy_ = mfy;
		mfz_ = mfz;
	}

	public void setLocationValues(long timestamp, float accuracy,
			float bearing, double altitude, double latitude, double longitude,
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

	public Boolean valuesAvailable() {

		return (timestamp_ != null && // 0
				accelx_ != null && // 1
				accely_ != null && // 2
				accelz_ != null && // 3
				deltaAccelx_ != null && // 4
				deltaAccely_ != null && // 5
				deltaAccelz_ != null && // 6
				accuracy_ != null && // 7
				bearing_ != null && // 8
				latitude_ != null && // 9
				longitude_ != null && // 10
				altitude_ != null && // 11
				speed_ != null && // 12
				mfx_ != null && // 16
				mfy_ != null && // 17
		mfz_ != null); // 18

	}

	public void sampleValues() {

		float[] mags = new float[3];
		mags[0] = mfx_;
		mags[1] = mfy_;
		mags[2] = mfz_;
		float[] accels = new float[3];
		accels[0] = accelx_;
		accels[1] = accely_;
		accels[2] = accelz_;
		float[] I = new float[16];
		float[] R = new float[16];
		float[] outR = new float[16];
		float[] orientationValues = new float[3];

		SensorManager.getRotationMatrix(R, I, accels, mags);
		SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X,
				SensorManager.AXIS_Z, outR);
		SensorManager.getOrientation(outR, orientationValues);
		azimuth_ = orientationValues[0];
		pitch_ = orientationValues[1];
		roll_ = orientationValues[2];
		insert(getWritableDatabase());
	}
	void deleteRows(List<Integer> idsToBeDeleted) {
		String whereClause = RoadConditionSensorDatabaseHelper.ID_FIELD + " IN (" + TextUtils.join(",", idsToBeDeleted) + ")";
		getWritableDatabase().delete(TABLE, whereClause, null);
	}
}
