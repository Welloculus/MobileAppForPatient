package com.transility.welloculus.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.transility.welloculus.R;
import com.transility.welloculus.beans.DeviceInfoBean;
import com.transility.welloculus.beans.HealthDataBean;
import com.transility.welloculus.beans.HealthRecordBean;
import com.transility.welloculus.db.DBHelper;
import com.transility.welloculus.utils.CalendarDialog;
import com.transility.welloculus.utils.Constants;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * The type Reports activity.
 */
public class ReportsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CalendarDialog.OnDateSetListener {

    private Context mContext;
    /**
     * The Report container.
     */
    LinearLayout chartContainer;
    /**
     * The M tv to date.
     */
    TextView mTvToDate;
    TextView deviceMessage;
    TextView deviceNameText;
    LinearLayout reportContainer;
    /**
     * The M spin select device.
     */
    Spinner mDeviceSpin;
    /**
     * The Requested date view id.
     */
    int requestedDateViewId = 0;
    /**
     * The Requested attribute.
     */
    int requestedAttribute = 0;
    /**
     * The M from date.
     */
    Calendar mFromDate, /**
     * The M to date.
     */
    mToDate;
    /**
     * The M date btn format.
     */
    DateFormat mDateBtnFormat, /**
     * The M date format.
     */
    mDateFormat;
    /**
     * The Mll get report.
     */
    LinearLayout mllGetReport;
    /**
     * The Device lists.
     */
    ArrayList<DeviceInfoBean> deviceLists;
    /**
     * The X labels.
     */
    String[] xLabels;
    /**
     * The X values.
     */
    long[] xValues;
    /**
     * The Y values.
     */
    int[] yValues;
    /**
     * The Attributes array.
     */
    String[] attributesArray;
    /**
     * The Check spinner.
     */
    int checkSpinner = 0;
    /**
     * The M btn get report.
     */
    Button mBtnGetReport;
    /**
     * The Paintent report map.
     */
    Map<String, ArrayList<HealthDataBean>> paintentReportMap;// key is parameter we are measuring ,value is HealthData

    Map<String, String> deviceNameType = new HashMap<String, String>();

    protected ArrayList<DeviceInfoBean> deviceInfoList = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private Map<String, JSONObject> formattedRecordsJson = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_reports);
        if(toolbar!=null){
            toolbar.setTitle("");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit();
                }
            });
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        TextView main_title = (TextView) findViewById(R.id.report_toolbar_title);
        main_title.setText(getString(R.string.reports_title));

        initUI();
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void initUI() {
        mContext = this;
        mBtnGetReport = (Button) findViewById(R.id.btn_get_reports);
        chartContainer = (LinearLayout) findViewById(R.id.report_chart_view_container);
        reportContainer = (LinearLayout) findViewById(R.id.report_container);
        deviceMessage = (TextView) findViewById(R.id.device_message);
        mTvToDate = (TextView) findViewById(R.id.btn_to);
        deviceNameText = (TextView) findViewById(R.id.deviceName);
        mDeviceSpin = (Spinner) findViewById(R.id.device_spin);
        mllGetReport = (LinearLayout) findViewById(R.id.btn_get_reports_ll);
        mTvToDate.setOnClickListener(this);
        mllGetReport.setOnClickListener(this);
        mBtnGetReport.setOnClickListener(this);
        mDeviceSpin.setOnItemSelectedListener(this);
    }

    private void init() {
        paintentReportMap = new HashMap<>();
        mDateBtnFormat = new SimpleDateFormat("dd/MM/yyyy");
        mDateFormat = new SimpleDateFormat("hh:mm:ss a");
        mFromDate = Calendar.getInstance();
        mFromDate.set(Calendar.MINUTE, 0);
        mFromDate.set(Calendar.SECOND, 0);
        mFromDate.set(Calendar.MILLISECOND,0);
        mFromDate.set(Calendar.HOUR_OF_DAY, 0);

        mToDate = Calendar.getInstance();
        mToDate.add(Calendar.DATE, 1);
        mToDate.set(Calendar.MINUTE, 0);
        mToDate.set(Calendar.SECOND, 0);
        mToDate.set(Calendar.MILLISECOND,0);
        mToDate.set(Calendar.HOUR_OF_DAY, 0);

        displayDate(mFromDate.getTimeInMillis(), mTvToDate.getId(), mDateBtnFormat);
        updateReport(mFromDate.getTimeInMillis(), mToDate.getTimeInMillis());

    }

    private void updateReport(long fromDate, long toDate) {
        try {
            List<HealthRecordBean> healthRecordList = fetchHealthRecordsFromDB(fromDate, toDate);
            formattedRecordsJson = formatRecordsJson(healthRecordList);
            String deviceName = updateUIForHealthRecords();
            loadMapForDevice(deviceName);
        } catch (JSONException e) {
            Log.e("updateReport", e.getMessage());
        }
    }

    /**

     */
    private void loadMapForDevice(String deviceName) {
        if (deviceName == null) {
            return;
        }
        try {
            JSONObject deviceJson = formattedRecordsJson.get(deviceName);
            JSONObject dataObject = deviceJson.getJSONObject(Constants.DATA);
            Iterator<String> keyIterator = dataObject.keys();
            Map<String, Double[]> valuesMap = new HashMap<>();
            double yMax = -1000;
            double yMin = 1000;
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                JSONArray values = dataObject.getJSONArray(key);
                Double[] valuesArray = new Double[values.length()];
                for (int i = 0; i < values.length(); i++) {
                    valuesArray[i] = Double.parseDouble(values.getString(i));
                    if (valuesArray[i] > yMax) {
                        yMax = valuesArray[i];
                    }
                    if (valuesArray[i] < yMin) {
                        yMin = valuesArray[i];
                    }
                }
                valuesMap.put(key, valuesArray);
            }
            JSONArray times = deviceJson.getJSONArray(Constants.TIME);
            String[] timeArray = new String[times.length()];
            for (int i = 0; i < times.length(); i++) {
                timeArray[i] = times.getString(i);
            }
            String dataType = deviceJson.getString(Constants.DATA_TYPE);
            openChart(valuesMap, timeArray, dataType, yMax, yMin);
        } catch (JSONException e) {
            Log.e("ReportsActivity", e.getMessage());
        }
    }

    private void openChart(Map<String, Double[]> valuesMap, String[] timeArray, String yTitle, double yMax, double yMin) {
        int[] colors = new int[]{Color.RED, Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.GREEN, Color.BLACK, Color.GRAY};
        String xTitle = "Time";
        int displayChartValueDistance = 40;
        int yLabelCount = 10;
        int xLabelCount = 0;
        int labelFontSize = 10;
        double xMin = 0;
        double xMax = timeArray.length;
        float curveAngle = 0.5f;
        View mChart;
        String chartTitle = "Health Report";
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setChartTitle(chartTitle);

        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        // Adding data to series
        Set<String> keys = valuesMap.keySet();
        int j = 0;
        for (String key :
                keys) {
            Double[] valuesArray = valuesMap.get(key);
            XYSeries reportSeries = new XYSeries(key);
            for (int i = 0; i < timeArray.length; i++) {
                reportSeries.add(i, valuesArray[i]);
            }
            dataSet.addSeries(reportSeries);
            XYSeriesRenderer reportRenderer = getReportRenderer(colors[(j++) % colors.length]);
            multiRenderer.addSeriesRenderer(reportRenderer);
        }


        multiRenderer.setXTitle("\n\n\n" + xTitle);
        multiRenderer.setYTitle(yTitle);
        multiRenderer.setAxisTitleTextSize(40);
        multiRenderer.setXAxisColor(Color.DKGRAY);
        multiRenderer.setYAxisColor(Color.DKGRAY);
        multiRenderer.setLegendTextSize(30);
        multiRenderer.setXRoundedLabels(false);
        multiRenderer.setXLabels(xLabelCount);
        multiRenderer.setYLabels(yLabelCount);
        //set min max
        multiRenderer.setXAxisMin(xMin);
        multiRenderer.setXAxisMax(xMax);
        multiRenderer.setYAxisMax(yMax);
        multiRenderer.setYAxisMin(yMin);
        Log.e("x min" + multiRenderer.getXAxisMin(), "x max" + multiRenderer.getXAxisMax());
        Log.e("y min" + multiRenderer.getYAxisMin(), "y max" + multiRenderer.getYAxisMax());

        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setPanEnabled(true, true);
        multiRenderer.setZoomEnabled(true, true);

        multiRenderer.setExternalZoomEnabled(true);

        multiRenderer.setPanLimits(new double[]{multiRenderer.getXAxisMin(), multiRenderer.getXAxisMax(), multiRenderer.getYAxisMin(), multiRenderer.getYAxisMax()});
        multiRenderer.setZoomLimits(new double[]{multiRenderer.getXAxisMin(), multiRenderer.getXAxisMax(), multiRenderer.getYAxisMin(), multiRenderer.getYAxisMax()});

        multiRenderer.setInScroll(false);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setShowGrid(true);
        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        multiRenderer.setMargins(new int[]{displayChartValueDistance, displayChartValueDistance, displayChartValueDistance, displayChartValueDistance});
        multiRenderer.setLabelsTextSize(labelFontSize);

        int recordSize = timeArray.length;

        for (int i = 0; i < recordSize; i++) {
            multiRenderer.addXTextLabel(i, timeArray[i]);
        }
        // Creating a Line Chart
        mChart = ChartFactory.getCubeLineChartView(getBaseContext(), dataSet, multiRenderer, curveAngle);
        // Adding the Line Chart to the LinearLayout
        chartContainer.removeAllViews();
        chartContainer.addView(mChart);

    }

    private XYSeriesRenderer getReportRenderer(int color) {
        int displayChartValueDistance = 40;
        int lineWidth = 3;
        XYSeriesRenderer reportRenderer = new XYSeriesRenderer();
        reportRenderer.setPointStyle(PointStyle.CIRCLE);
        reportRenderer.setFillPoints(true);
        reportRenderer.setColor(color);
        reportRenderer.setLineWidth(lineWidth);
        reportRenderer.setDisplayChartValues(true);
        reportRenderer.setDisplayChartValuesDistance(displayChartValueDistance);
        return reportRenderer;
    }

    private String updateUIForHealthRecords() throws JSONException {
        if (formattedRecordsJson != null && !formattedRecordsJson.isEmpty()) {
            String[] keys = new String[formattedRecordsJson.size()];
            attributesArray = (formattedRecordsJson.keySet()).toArray(new String[formattedRecordsJson.size()]);
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.simple_list_item, R.id.simple_text, attributesArray);
            mDeviceSpin.setAdapter(spinnerArrayAdapter);
            mDeviceSpin.setSelection(0);
            showHideReport(true);
            return attributesArray[0];
        } else {
            showHideReport(false);
            clearChart();
            showToastMessage(mContext.getString(R.string.no_report_found), mContext);
            return null;
        }
    }

    /**
     * * FORMAT:
     * {
     * "xyz": {
     * "DeviceId": "abc",
     * "DeviceName": "xyz",
     * "data": {
     * "sys": [100,102,115],
     * "dia": [80,78,85],
     * "pulse": [72,78,84]
     * }
     * }
     * }
     *
     * @throws JSONException
     */
    private Map<String, JSONObject> formatRecordsJson(List<HealthRecordBean> recordsList) throws JSONException {
        Map<String, JSONObject> recordsMap = new HashMap<>();
        for (int i = 0; i < recordsList.size(); i++) {
            HealthRecordBean healthRecord = recordsList.get(i);
            String deviceName = healthRecord.getDeviceName();
            if (!recordsMap.containsKey(deviceName)) {
                JSONObject deviceObject = new JSONObject();
                deviceObject.put(Constants.DATA_TYPE, healthRecord.getDataType());
                deviceObject.put(Constants.DEVICE_ID, healthRecord.getDeviceID());
                deviceObject.put(Constants.DEVICE_NAME, healthRecord.getDeviceName());
                deviceObject.put(Constants.USER_DEVICE_ID, healthRecord.getUserDeviceID());
                deviceObject.put(Constants.DATA, new JSONObject());
                deviceObject.put(Constants.TIME, new JSONArray());
                recordsMap.put(deviceName, deviceObject);
            }
            JSONObject deviceObject = recordsMap.get(deviceName);
            JSONObject dataObject = recordsMap.get(deviceName).getJSONObject(Constants.DATA);
            JSONObject itemJson = new JSONObject(healthRecord.getData());
            Iterator<String> keyset = itemJson.keys();
            while (keyset.hasNext()) {
                String key = keyset.next();
                Object value = itemJson.get(key);
                if(!key.equals(Constants.TIME)){
                    if (!dataObject.has(key)) {
                        dataObject.put(key, new JSONArray());
                    }
                    JSONArray attributeValues = dataObject.getJSONArray(key);
                    attributeValues.put(value);
                }
            }
            Date date = new Date(healthRecord.getTime());
            SimpleDateFormat timeLabelFormat = new SimpleDateFormat("HH:mm:ss");
            String dateLabel = timeLabelFormat.format(date);
            deviceObject.getJSONArray(Constants.TIME).put(dateLabel);
        }
        return recordsMap;
    }

    private List<HealthRecordBean> fetchHealthRecordsFromDB(long fromDate, long toDate) {
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        String fromDateString = String.valueOf(fromDate);
        String toDateString = String.valueOf(toDate);
        return dbHelper.getHealthRecordsInDateRange(fromDateString, toDateString);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to:
                requestedDateViewId = v.getId();
                showCalendar();
                break;
            case R.id.btn_get_reports_ll:
            case R.id.btn_get_reports:
                Log.e("report get clicked", "report get clicked");
                //insertDummyValues();
                updateReport(mFromDate.getTimeInMillis(), mToDate.getTimeInMillis());
                break;
            default:
                break;
        }
    }

    private void insertDummyValues() {
        for (int i=0;i<50;i++){
            try {
                JSONObject dataObject = new JSONObject();
                dataObject.put("systolic", (int)(100 + Math.random()*40));
                dataObject.put("diastolic",(int)(100 - Math.random()*40));
                dataObject.put("pulse",(int)(140-Math.random()*40));
                DBHelper dbHelper = DBHelper.getInstance(mContext);
                dbHelper.insertHealthData("1021", "Fora BP monitor", mDateBtnFormat.parse("10/08/2017").getTime(), Constants.DATA_TYPE_BLOOLD_PRESSURE, dataObject.toString());
            }catch (Exception e){
                Log.e("Welloculus", e.getMessage());
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (++checkSpinner > 1) {
            Log.e("onItemSelected", "position selected: " + position);
            loadMapForDevice(attributesArray[position]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void exit() {

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void clearChart() {
        mDeviceSpin.setAdapter(null);
        chartContainer.removeAllViews();
    }

    @Override
    public void onDateSet(int year, int month, int day) {

        if (requestedDateViewId == R.id.btn_to) {
            mFromDate.set(Calendar.MONTH, month);
            mFromDate.set(Calendar.DAY_OF_MONTH, day);
            mFromDate.set(Calendar.YEAR, year);
            mFromDate.set(Calendar.HOUR_OF_DAY, 0);
            mFromDate.set(Calendar.MINUTE, 0);
            mFromDate.set(Calendar.SECOND, 0);
            mFromDate.set(Calendar.MILLISECOND, 0);
            mToDate.set(Calendar.MONTH, month);
            mToDate.set(Calendar.DAY_OF_MONTH, day);
            mToDate.set(Calendar.YEAR, year);
            mToDate.add(Calendar.DATE, 1);
            mToDate.set(Calendar.HOUR_OF_DAY, 0);
            mToDate.set(Calendar.MINUTE, 0);
            mToDate.set(Calendar.SECOND, 0);
            mToDate.set(Calendar.MILLISECOND, 0);
            displayDate(mFromDate.getTimeInMillis(), requestedDateViewId, mDateBtnFormat);
        }
    }

    private void showCalendar() {
        new CalendarDialog(this, this).showDatePicker();
    }

    private void showHideReport(boolean shouldShow) {
        if(shouldShow){
            mDeviceSpin.setVisibility(View.VISIBLE);
            deviceNameText.setVisibility(View.VISIBLE);
            reportContainer.setVisibility(View.VISIBLE);
            deviceMessage.setVisibility(View.GONE);
        }else{
            mDeviceSpin.setVisibility(View.GONE);
            deviceNameText.setVisibility(View.GONE);
            reportContainer.setVisibility(View.GONE);
            deviceMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Reports Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.transility.welloculus/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Reports Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.transility.welloculus/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

