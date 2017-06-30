/**
 *
 */
package com.transility.welloculus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.transility.welloculus.beans.HealthDataBean;
import com.transility.welloculus.beans.HeartRateInfoBean;
import com.transility.welloculus.utils.AppUtility;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * @param context
     */
    private DBHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, 1);
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
                + DBConstants.DEVICE_ID
                + " TEXT, " + DBConstants.DATA + " TEXT UNIQUE, " + DBConstants.TIME + " INTEGER, "
                + DBConstants.DEVICE_NAME + " TEXT" + ")");
    }

    /**
     * Do not call this method manually
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(QUERY_DROP_TABLE_IF_EXISTS
                + DBConstants.TABLE_HEALTH_DATA);

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
    public long insertHealthData(String deviceId, String deviceName, long time, int data) {

        SQLiteDatabase db = this.getWritableDatabase(key);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.DEVICE_ID,
                deviceId);
        contentValues.put(DBConstants.DEVICE_NAME,
                deviceName);
        contentValues.put(DBConstants.DATA,
                data);
        contentValues.put(DBConstants.TIME, time);
        Log.v(AppUtility.TAG, "value inserted  deviceID : " + deviceId + " DATA : " + data + " TIME : " + time);
        long rowID = db.insertWithOnConflict(DBConstants.TABLE_HEALTH_DATA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.v(AppUtility.TAG, "DATA : " + data + " rowID : " + rowID);
        return rowID;
    }

    /**
     * Get health information from DB.
     *
     * @return HealthInfo from DB
     */
    public Map<String, List<HeartRateInfoBean>> getHealthInfo() {
        HashMap<String, List<HeartRateInfoBean>> healthData = new HashMap<>();
        List<HeartRateInfoBean> heartRateInfoList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase(key);
        Cursor deviceCursor = db.rawQuery("SELECT DISTINCT " + DBConstants.DEVICE_ID + " FROM " + DBConstants.TABLE_HEALTH_DATA, null);
        if (deviceCursor.moveToFirst()) {
            do {
                String deviceId = deviceCursor.getString(deviceCursor
                        .getColumnIndex(DBConstants.DEVICE_ID));
                Log.v(AppUtility.TAG, "deviceId : " + deviceId);
                heartRateInfoList.clear();
                healthData.put(deviceId, heartRateInfoList);
                Cursor healthInfoCursor = db.query(DBConstants.TABLE_HEALTH_DATA, null, DBConstants.DEVICE_ID + " = ?", new String[]{deviceId}, null, null, null, null);
                if (healthInfoCursor.moveToFirst()) {
                    do {
                        HeartRateInfoBean heartRateInfo = new HeartRateInfoBean();
                        heartRateInfo.setDeviceName(healthInfoCursor.getString(healthInfoCursor
                                .getColumnIndex(DBConstants.DEVICE_NAME)));
                        heartRateInfo.setDeviceID(healthInfoCursor.getString(healthInfoCursor
                                .getColumnIndex(DBConstants.DEVICE_ID)));
                        heartRateInfo.setHeartRate(healthInfoCursor.getInt(healthInfoCursor
                                .getColumnIndex(DBConstants.DATA)));
                        heartRateInfo.setTime(healthInfoCursor.getLong(healthInfoCursor
                                .getColumnIndex(DBConstants.TIME)));
                        heartRateInfoList.add(heartRateInfo);
                        Log.v(AppUtility.TAG, "data : " + heartRateInfo.getHeartRate() + " TIME : " + heartRateInfo.getTime());
                    }
                    while (healthInfoCursor.moveToNext());
                }
                healthData.put(deviceId, heartRateInfoList);
                healthInfoCursor.close();
            } while (deviceCursor.moveToNext());
        }
        deviceCursor.close();
        return healthData;
    }

    public ArrayList<HealthDataBean> getHealthInfoFromDate(String deviceID,  String fromTodate ,String todate) {
        HashMap<String, List<HeartRateInfoBean>> healthData = new HashMap<>();
        List<HeartRateInfoBean> heartRateInfoList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase(key);
        Cursor deviceCursor = db.rawQuery("SELECT *  FROM " + DBConstants.TABLE_HEALTH_DATA + " WHERE " + DBConstants.TIME + ">= ? AND " + DBConstants.TIME  + "<= ? " , new String[] {fromTodate,todate});
        ArrayList<HealthDataBean> health_data = new ArrayList<HealthDataBean>();
        if (deviceCursor.moveToFirst()) {
            do {
                String deviceId = deviceCursor.getString(deviceCursor
                        .getColumnIndex(DBConstants.DEVICE_ID));
                String Time = deviceCursor.getString(deviceCursor
                        .getColumnIndex(DBConstants.TIME));
                String data = deviceCursor.getString(deviceCursor
                        .getColumnIndex(DBConstants.DATA));
                Log.v(AppUtility.TAG, "data :------> " + deviceCursor.getCount());
                Log.v(AppUtility.TAG, "deviceId :------> " + deviceId);
                Log.v(AppUtility.TAG, "time:------> " + Time);
                Log.v(AppUtility.TAG, "data :------> " + data);

                    HealthDataBean datahealth = new HealthDataBean();
                datahealth.setEntry_created(Time);
                datahealth.setKey("heart_rate");
                datahealth.setValue(data);
                health_data.add(datahealth);




                /*heartRateInfoList.clear();
                healthData.put(deviceId, heartRateInfoList);
                Cursor healthInfoCursor = db.query(DBConstants.TABLE_HEALTH_DATA, null, DBConstants.DEVICE_ID + " = ?", new String[]{deviceId}, null, null, null, null);
                if (healthInfoCursor.moveToFirst()) {
                    do {
                        HeartRateInfoBean heartRateInfo = new HeartRateInfoBean();
                        heartRateInfo.setDeviceName(healthInfoCursor.getString(healthInfoCursor
                                .getColumnIndex(DBConstants.DEVICE_NAME)));
                        heartRateInfo.setDeviceID(healthInfoCursor.getString(healthInfoCursor
                                .getColumnIndex(DBConstants.DEVICE_ID)));
                        heartRateInfo.setHeartRate(healthInfoCursor.getInt(healthInfoCursor
                                .getColumnIndex(DBConstants.DATA)));
                        heartRateInfo.setTime(healthInfoCursor.getLong(healthInfoCursor
                                .getColumnIndex(DBConstants.TIME)));
                        heartRateInfoList.add(heartRateInfo);
                        Log.v(AppUtility.TAG, "data :---------> " + heartRateInfo.getHeartRate() + " TIME : ------->" + heartRateInfo.getTime());
                    }
                    while (healthInfoCursor.moveToNext());
                }
                healthData.put(deviceId, heartRateInfoList);
                healthInfoCursor.close();*/

            } while (deviceCursor.moveToNext());
        }
        deviceCursor.close();
        return health_data;
    }


}