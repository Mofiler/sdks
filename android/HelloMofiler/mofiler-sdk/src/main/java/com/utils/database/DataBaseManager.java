package com.utils.database;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DataBaseManager {
	static Context mContext;
	private static SQLiteDatabase database;

	public static String DATA_BASE = "mofiler.db";

	public static final String TABLE_INSTALLATIONINFO = "mofiler";
	public static final String TABLE_IDENTITIES = "mofidentities";
	
	public static SQLiteDatabase getDatabase(Context context) {
		if (database == null) {
			database = new DBConnection(context, DATA_BASE).getDB();
		}
		return database;
	}

	public static void closeDatabase(Context context) {
		getDatabase(context).close();
	}

	public static void initializeDB(Context context) {
		mContext = context;
		createAndInitializeTables();
	}

	private static void createAndInitializeTables() {
		createMofilerTable();
		createMofilerIdentitiesTable();
	}

	public static Cursor getCursorFromQuery(String query, Context context) {
		return getDatabase(context).rawQuery(query, null);
	}

	public static String getRowValue(String table, String where,
			String columnIndex, Context context) {
		String data = "";
		String query = "SELECT * FROM " + table + " WHERE " + where;
		Cursor cursor = getDatabase(context).rawQuery(query, null);
		if (cursor != null) {
			int columnText = cursor.getColumnIndexOrThrow(columnIndex);
			while (cursor.moveToNext()) {
				data = cursor.getString(columnText);
			}
		}
		;
		cursor.close();
		return data;
	}
	

	private static void createMofilerTable() {
		DBConnection dbc = new DBConnection(mContext, DATA_BASE);
		
		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("_id", "INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT");
		fields.put("jsonstack", "VARCHAR");
		fields.put("_installation_id", "VARCHAR(200) NOT NULL");

		dbc.createTable(TABLE_INSTALLATIONINFO, fields);
		dbc.closeConnection();
	}

	private static void createMofilerIdentitiesTable() {
		DBConnection dbc = new DBConnection(mContext, DATA_BASE);
		
		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("_id", "INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT");
		fields.put("identity_key", "VARCHAR NOT NULL");
		fields.put("identity_value", "VARCHAR NOT NULL");

		dbc.createTable(TABLE_IDENTITIES, fields);
		dbc.closeConnection();
	}
	
}
