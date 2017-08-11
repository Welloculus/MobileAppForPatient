/**
 *
 */
package com.transility.welloculus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.util.Log;

import com.transility.welloculus.beans.HealthDataBean;
import com.transility.welloculus.beans.HealthRecordBean;
import com.transility.welloculus.utils.AppUtility;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DBHelper class contains all the methods to perform database operations like
 * opening connection, closing connection, insert, update, read, delete and
 * other things.
 * <p>
 * It holds required variables like database name, database version, column
 * names. Also executes all the table creation statements in onCreate() method.
 *
 * @author arpit.garg
 */
public class DBHelper extends SQLiteOpenHelper {

    private static Context mContext;
    private static final String QUERY_DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";
    private String key = DBConstants.PASS_KEY;
    private static final int DATABASE_VERSION = 4;

    private DBHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DATABASE_VERSION);
        // initialize the sqlite cipher libraries
        SQLiteDatabase.loadLibs(context);
    }

    private static DBHelper dbInstance = null;

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static DBHelper getInstance(Context context) {
        mContext = context;
        if (dbInstance == null && context != null) {
            dbInstance = new DBHelper(context);
        }
        return dbInstance;
    }

    /**
     * DO not call this method manually
     */
    public void onCreate(SQLiteDatabase db) {
        SQLiteDatabase.loadLibs(mContext);
        db.execSQL("CREATE TABLE " + DBConstants.TABLE_HEALTH_DATA + " ("
                + DBConstants.COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.DATA_TYPE
                + " TEXT, "
                + DBConstants.DEVICE_ID
                + " TEXT, " + DBConstants.DATA + " TEXT UNIQUE, " + DBConstants.TIME + " INTEGER, "
                + DBConstants.DEVICE_NAME + " TEXT" + ")");
    }

    /**
     * Do not call this method manually
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SQLiteDatabase.loadLibs(mContext);
        db.execSQL(QUERY_DROP_TABLE_IF_EXISTS
                + DBConstants.TABLE_HEALTH_DATA);
        db.execSQL("CREATE TABLE " + DBConstants.TABLE_HEALTH_DATA + " ("
                + DBConstants.COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.DATA_TYPE
                + " TEXT, "
                + DBConstants.DEVICE_ID
                + " TEXT, " + DBConstants.DATA + " TEXT UNIQUE, " + DBConstants.TIME + " INTEGER, "
                + DBConstants.DEVICE_NAME + " TEXT" + ")");

    }

    /**
     * delete All Data
     */
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase(key);
        db.delete(DBConstants.TABLE_HEALTH_DATA, null, null);
    }

    /**
     * Inserts the heart rate info in database
     *
     * @param deviceId   the device id
     * @param deviceName the device name
     * @param time       the time
     * @param data       the data
     * @return long
     */
    public long insertHealthData(String deviceId, String deviceName, long time, String dataType, String data) {

        SQLiteDatabase db = this.getWritableDatabase(key);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.DEVICE_ID,
                deviceId);
        contentValues.put(DBConstants.DEVICE_NAME,
                deviceName);
        contentValues.put(DBConstants.DATA,
                data);
        contentValues.put(DBConstants.DATA_TYPE,
                dataType);
        contentValues.put(DBConstants.TIME, time);
        Log.v(AppUtility.TAG, "value inserted  deviceID : " + deviceId + " DATA_TYPE : " + dataType + " DATA : " + data + " TIME : " + time);
        long rowID = db.insertWithOnConflict(DBConstants.TABLE_HEALTH_DATA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.v(AppUtility.TAG, "DATA : " + data + " rowID : " + rowID);
        return rowID;
    }

    public List<HealthRecordBean> getHealthRecordsAfterRow(int lastSyncedRow) {
        List<HealthRecordBean> healthRecordsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(key);
        Cursor healthInfoCursor = db.rawQuery("SELECT * FROM HEALTH_INFO WHERE COLUMN_ID > ?", new String[]{Integer.toString(lastSyncedRow)});
        if (healthInfoCursor.moveToFirst()) {
            do {
                HealthRecordBean healthRecord = new HealthRecordBean();
                healthRecord.setRecordId(healthInfoCursor.getInt(healthInfoCursor.getColumnIndex(DBConstants.COLUMN_ID)));
                healthRecord.setDeviceName(healthInfoCursor.getString(healthInfoCursor.getColumnIndex(DBConstants.DEVICE_NAME)));
                healthRecord.setDeviceID(healthInfoCursor.getString(healthInfoCursor.getColumnIndex(DBConstants.DEVICE_ID)));
                healthRecord.setDataType(healthInfoCursor.getString(healthInfoCursor.getColumnIndex(DBConstants.DATA_TYPE)));
                healthRecord.setData(healthInfoCursor.getString(healthInfoCursor.getColumnIndex(DBConstants.DATA)));
                healthRecord.setTime(healthInfoCursor.getLong(healthInfoCursor.getColumnIndex(DBConstants.TIME)));
                healthRecordsList.add(healthRecord);
            }
            while (healthInfoCursor.moveToNext());
        }
        healthInfoCursor.close();
        return healthRecordsList;
    }

    public List<HealthRecordBean> getHealthRecordsInDateRange(String fromDate, String toDate) {
        List<HealthRecordBean> healthRecordsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(key);
        Cursor healthInfoCursor = db.rawQuery("SELECT * FROM HEALTH_INFO WHERE TIME >=? AND TIME <?", new String[]{fromDate, toDate});
        if (healthInfoCursor.moveToFirst()) {
            do {
                HealthRecordBean healthRecord = new HealthRecordBean();
                healthRecord.setDeviceName(healthInfoCursor.getString(healthInfoCursor.getColumnIndex(DBConstants.DEVICE_NAME)));
                healthRecord.setDeviceID(healthInfoCursor.getString(healthInfoCursor.getColumnIndex(DBConstants.DEVICE_ID)));
                healthRecord.setDataType(healthInfoCursor.getString(healthInfoCursor.getColumnIndex(DBConstants.DATA_TYPE)));
                healthRecord.setData(healthInfoCursor.getString(healthInfoCursor.getColumnIndex(DBConstants.DATA)));
                healthRecord.setTime(healthInfoCursor.getLong(healthInfoCursor.getColumnIndex(DBConstants.TIME)));
                healthRecordsList.add(healthRecord);
            }
            while (healthInfoCursor.moveToNext());
        }
        healthInfoCursor.close();
        return healthRecordsList;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase(key);
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}