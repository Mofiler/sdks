package com.mofiler.daos;

import java.util.Hashtable;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mofiler.MofilerInstallationInfo;
import com.mofiler.MofilerValueStack;
import com.utils.database.DataBaseManager;

public class MofilerIdentityDao {

	public static void saveIdentityDataInDB(Context mContext, String key, String value){

		SQLiteDatabase db = DataBaseManager.getDatabase(mContext);
		try {
			db.beginTransaction();
			ContentValues cv = new ContentValues();
			cv.put("identity_key", key);
			cv.put("identity_value", value);
			db.insert(DataBaseManager.TABLE_IDENTITIES, null, cv);
 			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}	
	}

	public static void deleteIdentityInDB(Context mContext, int objID)
	{
 		SQLiteDatabase db = DataBaseManager.getDatabase(mContext);

		try {
			
			db.beginTransaction();
			String id = String.valueOf(objID);
			
			long resultDel = db.delete(DataBaseManager.TABLE_IDENTITIES, "_id = ?", new String[] { id } );
 			System.out.println("DELETED N records: " + resultDel);
			
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}	
		
	}

	public static void deleteAllIdentitiesInDB(Context mContext)
	{
 		SQLiteDatabase db = DataBaseManager.getDatabase(mContext);
 		String id = "0"; //fake

		try {
			
			db.beginTransaction();
			long resultDel = db.delete(DataBaseManager.TABLE_IDENTITIES, "_id > ?", new String[] { id } );
 			System.out.println("DELETED N identity records: " + resultDel);
			
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}	
		
	}
	
	public static Hashtable getIdentities(Context context) {
		Hashtable identity = new Hashtable();

		boolean bFound = false;
		Cursor cursor = DataBaseManager.getCursorFromQuery("SELECT * FROM " + DataBaseManager.TABLE_IDENTITIES, context);
		if (cursor != null) { 
			int columnIdentityKey = cursor.getColumnIndexOrThrow("identity_key");
			int columnIdentityValue = cursor.getColumnIndexOrThrow("identity_value");

			while (cursor.moveToNext()) {
				bFound = true;
				String key = cursor.getString(columnIdentityKey);
				String value  = cursor.getString(columnIdentityValue);
				identity.put(key, value);
   			}
		}
		cursor.close();
		return identity;
 	}
	
	
 
}
