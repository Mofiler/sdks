package com.mofiler.daos;

import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mofiler.MofilerInstallationInfo;
import com.mofiler.MofilerValueStack;
import com.utils.database.DataBaseManager;

public class MofilerDao {

    private static void saveCurrentDataInDB(Context mContext, int objID, MofilerInstallationInfo cfgobj, MofilerValueStack valuestack) {

        SQLiteDatabase db = DataBaseManager.getDatabase(mContext);
        try {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            if (objID != -1)
                cv.put("_id", objID);
            cv.put("jsonstack", valuestack.getJsonStack().toString());
            cv.put("_installation_id", cfgobj.getInstallationId());
            db.insert(DataBaseManager.TABLE_INSTALLATIONINFO, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    public static void saveCurrentDataInDB(Context mContext, MofilerInstallationInfo cfgobj, MofilerValueStack valuestack) {
        deleteCurrentDataInDB(mContext, 1);
        saveCurrentDataInDB(mContext, 1, cfgobj, valuestack);
    }


    public static void deleteCurrentDataInDB(Context mContext, int objID) {
        SQLiteDatabase db = DataBaseManager.getDatabase(mContext);

        try {

            db.beginTransaction();
            String id = String.valueOf(objID);

            long resultDel = db.delete(DataBaseManager.TABLE_INSTALLATIONINFO, "_id = ?", new String[]{id});
            System.out.println("DELETED N records: " + resultDel);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    public static Vector getCurrentDataInDB(Context context, int objID) {
        MofilerInstallationInfo cfgobj = new MofilerInstallationInfo();
        MofilerValueStack valuestack = new MofilerValueStack(null);
        boolean bFound = false;
        Cursor cursor = DataBaseManager.getCursorFromQuery("SELECT * FROM " + DataBaseManager.TABLE_INSTALLATIONINFO + " WHERE _id=" + objID, context);
        if (cursor != null) {
            int columnId = cursor.getColumnIndexOrThrow("_id");
            int columnValueStack = cursor.getColumnIndexOrThrow("jsonstack");
            int columnInstallID = cursor.getColumnIndexOrThrow("_installation_id");

            while (cursor.moveToNext()) {
                bFound = true;
                cfgobj.setInstallationId(cursor.getString(columnInstallID));
                valuestack.setJsonStack(cursor.getString(columnValueStack));
            }
        }
        cursor.close();
        Vector vectToReturn = null;
        if (bFound) {
            vectToReturn = new Vector();
            vectToReturn.addElement(cfgobj);
            vectToReturn.addElement(valuestack);
        }
        return vectToReturn;
    }


}
