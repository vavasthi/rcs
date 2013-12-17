package com.avasthi.apps.roadconditionsensor;

import java.util.ArrayList;
import java.util.List;

public class RoadConditionSensorDatabaseSchema {
	public class Column {
		public static final String INTEGER = "INTEGER";
		public static final String REAL = "REAL";
		private final String name_;
		private final String type_;
		private final String suffix_;

		private Column(String name, String type) {
			name_ = name;
			type_ = type;
			suffix_ = "";
		}

		private Column(String name, String type, String suffix) {
			name_ = name;
			type_ = type;
			suffix_ = suffix;
		}

		public String getSchema() {
			return name_ + " " + type_ + " " + suffix_;
		}
	}

	List<Column> columns_ = new ArrayList<Column>();
	String tableName_;

	public RoadConditionSensorDatabaseSchema(String tableName) {
		tableName_ = tableName;
	}

	public String getTableName() {
		return tableName_;
	}

	public void addAutomaticId() {
		columns_.add(new Column(RoadConditionSensorDatabaseHelper.ID_FIELD, Column.INTEGER,
				"PRIMARY KEY AUTOINCREMENT"));
	}

	public void addAutomaticId(String columnName) {
		columns_.add(new Column(columnName, Column.INTEGER,
				"PRIMARY KEY AUTOINCREMENT"));
	}

	public void addColumn(String columnName, String columnType) {
		columns_.add(new Column(columnName, columnType));
	}

	public void addColumn(String columnName, String columnType, String suffix) {
		columns_.add(new Column(columnName, columnType, suffix));
	}

	public String getCreateSchema() {
		String sql = "CREATE TABLE ";
		sql += tableName_;
		sql += "(";
		final String sep1 = "";
		final String sep2 = ",";
		String sep = sep1;
		for (Column c : columns_) {
			sql += sep + c.getSchema();
			sep = sep2;
		}
		sql += ")";
		return sql;
	}
	public String getDropSchema() {
		return "DROP TABLE " + tableName_;
	}
	public String[] getColumnNames() {
		String[] names = new String[columns_.size()];
		for (int i = 0; i < columns_.size(); ++i) {
			names[i] = columns_.get(i).name_;
		}
		return names;
	}
}
