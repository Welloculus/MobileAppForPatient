package com.transility.welloculus.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.transility.welloculus.R;
import com.transility.welloculus.beans.DeviceInfoBean;
import com.transility.welloculus.beans.HealthDataBean;
import com.transility.welloculus.db.DBHelper;
import com.transility.welloculus.utils.AppUtility;
import com.transility.welloculus.utils.CalendarDialog;
import com.transility.welloculus.utils.Constants;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Reports activity.
 */
public class ReportsActivity extends BaseActivity  implements View.OnClickListener,AdapterView.OnItemSelectedListener,CalendarDialog.OnDateSetListener {

    private Context mContext;
    /**
     * The Report container.
     */
    LinearLayout reportContainer;
    /**
     * The M tv to date.
     */
    TextView  mTvToDate;

    TextView  deviceNameText;
    /**
     * The M spin select parameter.
     */
    Spinner mSpinSelectParameter;
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
    Double[] xValues;
    /**
     * The Y values.
     */
    Double[] yValues;
    /**
     * The Attributes array.
     */
    String[] attributesArray;

    String[] attributesArray1;
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
    Map<String,ArrayList<HealthDataBean>> paintentReportMap;// key is parameter we are measuring ,value is HealthData

    Map<String,String> deviceNameType=new HashMap<String,String>();

    protected ArrayList<DeviceInfoBean> deviceInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_reports);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        TextView main_title = (TextView) findViewById(R.id.report_toolbar_title);
        main_title.setText(getString(R.string.reports_title));

        initUI();
        init();
    }

    @Override
    protected void initUI() {
        mContext = this;
        mBtnGetReport = (Button)findViewById(R.id.btn_get_reports);
        reportContainer = (LinearLayout) findViewById(R.id.report_chart_view_container);
        mTvToDate = (TextView) findViewById(R.id.btn_to);
        deviceNameText = (TextView) findViewById(R.id.deviceName);
        mSpinSelectParameter = (Spinner) findViewById(R.id.device_spin);
        mllGetReport = (LinearLayout)findViewById(R.id.btn_get_reports_ll) ;
        mTvToDate.setOnClickListener(this);
        mllGetReport.setOnClickListener(this);
        mBtnGetReport.setOnClickListener(this);
        mSpinSelectParameter.setOnItemSelectedListener(this);
    }

    private void init(){
        paintentReportMap = new HashMap<>();
        mDateBtnFormat = new SimpleDateFormat("dd/MM/yyyy");
        mDateFormat = new SimpleDateFormat("hh:mm:ss a");
        mFromDate = Calendar.getInstance();
        mFromDate.set(Calendar.MINUTE, 0);
        mFromDate.set(Calendar.SECOND, 0);
        mFromDate.set(Calendar.HOUR_OF_DAY, 0);

        mToDate  = Calendar.getInstance();
        mToDate.add(Calendar.DATE,1);
        mToDate.set(Calendar.MINUTE, 0);
        mToDate.set(Calendar.SECOND, 0);
        mToDate.set(Calendar.HOUR_OF_DAY, 0);

        displayDate(mFromDate.getTimeInMillis(), mTvToDate.getId(),mDateBtnFormat);

        insertValue(mFromDate.getTimeInMillis(),mToDate.getTimeInMillis());


    }

    private void insertValue(long a ,long b) {

        DBHelper dbHelper = DBHelper.getInstance(mContext);
        String fromdatenew=String.valueOf(a);
        String todatenew=String.valueOf(b);
        ArrayList<HealthDataBean> health_data = dbHelper.getHealthInfoFromDate("00:07:80:5A:3A:26",fromdatenew,todatenew);
        handleResponse(health_data);
    }

    public void handleResponse(ArrayList<HealthDataBean> health_data) {

        if (true) {
            deviceNameType.clear();
            DeviceInfoBean devicebean = new DeviceInfoBean("true","00:07:80:5A:3A:26","Zypher","Bluetooth");
            ArrayList<DeviceInfoBean> deviceDetails =  new ArrayList<DeviceInfoBean>();
            devicebean.setHealth_data(health_data);
            deviceDetails.add(devicebean);
            Log.e("deviceDetails.size---->", "-->" +deviceDetails.size());
            closeWaitDialog();

            if ((!deviceDetails.isEmpty())) {
                showNoReportFound(true,"");
                prepareDevicesDataAndShowReport(deviceDetails);
                Log.e(Constants.TAG, "" + deviceDetails.get(0).getDevice_type());
            } else {
                mSpinSelectParameter.setVisibility(View.GONE);
                deviceNameText.setVisibility(View.GONE);
                showNoReportFound(false,getString(R.string.no_report_found));
                clearChart();
                showToastMessage(mContext.getString(R.string.no_report_found),mContext);
            }
        } else {
            deviceNameText.setVisibility(View.GONE);
            mSpinSelectParameter.setVisibility(View.GONE);
            showNoReportFound(false,getString(R.string.no_report_found));
            clearChart();
            closeWaitDialog();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_to:
                requestedDateViewId = v.getId();
                showCalendar();
                break;
            case R.id.btn_get_reports_ll:
            case R.id.btn_get_reports:
                Log.e("report get clicked","report get clicked");
                getReport(mFromDate.getTimeInMillis(),mToDate.getTimeInMillis());
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(++checkSpinner>1) {
            requestedAttribute = position;
            Log.e("requestedAttribute ----",""+attributesArray+ "---------> " + paintentReportMap);
            initValuesFromDeviceData(paintentReportMap.get(attributesArray[requestedAttribute]),attributesArray[requestedAttribute]);
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

    protected void getReport(long fromDate,long toDate) {

        if(isNetworkAvailable(mContext,getString(R.string.not_internet_connectivity))) {
            DBHelper dbHelper = DBHelper.getInstance(mContext);
            String fromdatenew=String.valueOf(fromDate);
            String todatenew=String.valueOf(toDate);
            ArrayList<HealthDataBean> health_data = dbHelper.getHealthInfoFromDate("00:07:80:5A:3A:26",fromdatenew , todatenew);
            handleResponse(health_data);
        }


    }





    /**
     * Prepare devices data and show report.z
     *
     * @param deviceDetails the device details
     */
    void prepareDevicesDataAndShowReport(ArrayList<DeviceInfoBean> deviceDetails){
        attributesArray1 = null;
        deviceNameText.setVisibility(View.VISIBLE);
        mSpinSelectParameter.setVisibility(View.VISIBLE);
        ArrayList<HealthDataBean> healthDataList;
        int deviceListSize = deviceDetails.size();
        Log.e("deviceListSize----->",""+deviceListSize);
        deviceLists = new ArrayList<>();
        paintentReportMap.clear();
        DeviceInfoBean deviceInfo;
        for(int i = 0;i<deviceListSize;i++){
            deviceNameType.put("Zypher","heart_rate");
            deviceInfo = new DeviceInfoBean(deviceDetails.get(i).getDevice_udi(),deviceDetails.get(i).getDevice_name());
            deviceLists.add(deviceInfo) ;
            healthDataList = (ArrayList<HealthDataBean>)deviceDetails.get(i).getHealth_data();
            int healthDataSize = healthDataList.size();
            Log.e("HealthdataSize----->",""+healthDataSize);
            HealthDataBean healthData;
            for(int j = 0;j<healthDataSize;j++){
                healthData = healthDataList.get(j);
                if(!paintentReportMap.containsKey(healthData.getKey())) {
                    paintentReportMap.put(healthData.getKey(), new ArrayList<HealthDataBean>());
                }
                paintentReportMap.get(healthData.getKey()).add(healthData);
            }
        }
        Log.e("--------->MAp","----------------->"+deviceNameType);
        if(paintentReportMap !=null&& paintentReportMap.size()>0){
            //initialise attributes array from map
            attributesArray = (paintentReportMap.keySet()).toArray(new String[0]);
            Log.e("ReportActivity","attributeSize"+attributesArray.length);
            attributesArray1 = (deviceNameType.keySet()).toArray(new String[0]);
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,R.layout.simple_list_item,R.id.simple_text,attributesArray1);
            mSpinSelectParameter.setAdapter(spinnerArrayAdapter);
            initValuesFromDeviceData(paintentReportMap.get(attributesArray[requestedAttribute]),attributesArray[requestedAttribute]);

        }
        else{
            mSpinSelectParameter.setVisibility(View.GONE);
            deviceNameText.setVisibility(View.GONE);
            showNoReportFound(false,getString(R.string.no_report_found));
            clearChart();
            showToastMessage(mContext.getString(R.string.no_report_found),mContext);
        }
    }

    /**
     * Init values from device data.
     *
     * @param healthDataList the health data list
     * @param key            the key
     */
    void initValuesFromDeviceData(ArrayList<HealthDataBean> healthDataList, String key){

        String deviceName = "Hello";
        for(String key2: deviceNameType.keySet()){
            Log.e("--->","--->" + deviceNameType.get(key2) + "---->" + mSpinSelectParameter.getSelectedItem().toString());
            if(mSpinSelectParameter.getSelectedItem().toString().equals(key2)){
                deviceName = deviceNameType.get(key2);
            }
        }
        deviceNameText.setText(deviceName);

        String appendMeasure  = null;
        String xTitle = null,yTitle = null;
        double yMax = 0,yMin = 0;
        int color=Color.BLUE;
        int interval = 5;
        double maxLineYValue = 0.0;
        double minLineYValue = 0.0;
        HealthDataBean[] healthData;

        if(key.equalsIgnoreCase(Constants.HEARTRATE)){
            appendMeasure = "in bpm";
            interval = 1;
            color=Color.RED;
            xTitle = "Time Duration ";
            yTitle = "Heart Rate";
            yMax = 170;
            yMin = 35;
            maxLineYValue = AppUtility.CRITICAL_MAX_HEART_RATE;
            minLineYValue = AppUtility.CRITICAL_MIN_HEART_RATE;
            healthData = healthDataList.toArray(new HealthDataBean[0]);
            xLabels =  new String[healthData.length];
            xValues =  new Double[healthData.length];
            yValues =  new Double[healthData.length];
            int recordSize = healthData.length;
            Log.e("ReportsActivity","healthDataSize"+recordSize);
            for(int i = 0;i<recordSize;i++){
                xValues[i] = Double.parseDouble(healthData[i].getEntry_created());
                yValues[i] = Double.parseDouble(healthData[i].getValue());
                Date date = new Date(Long.parseLong(healthData[i].getEntry_created()));
                xLabels[i] = mDateFormat.format(date);
            }
        }
        else if(key.equalsIgnoreCase(Constants.TEMPERTURE)){
            appendMeasure = "in degree celsius";
            interval=5;
            color=Color.BLUE;
            yMax = 100;
            yMin = -100;
            maxLineYValue = AppUtility.CRITICAL_MAX_TEMPERATURE;
            minLineYValue = AppUtility.CRITICAL_MIN_TEMPERATURE;
            xTitle = "Time Duration ("+appendMeasure+")";
            yTitle = "Room Temperature";
            healthData = healthDataList.toArray(new HealthDataBean[0]);
            xLabels =  new String[healthData.length];
            xValues =  new Double[healthData.length];
            yValues =  new Double[healthData.length];
            int recordSize = healthData.length;
            Log.e("ReportsActivity","healthDataSize"+recordSize);

            for(int i = 0;i<recordSize;i++){
                xValues[i] = Double.parseDouble(healthData[i].getEntry_created());
                Log.d("String timeStamp",""+healthData[i].getEntry_created());
                Long time = Long.parseLong(healthData[i].getEntry_created());
                Log.d("Long timeStamp",""+time);
                Log.d("Double from long",""+Double.longBitsToDouble(time));
                Log.d("Double from String",""+xValues[i]);
                yValues[i] = Double.parseDouble(healthData[i].getValue());
                Date date = new Date(Long.parseLong(healthData[i].getEntry_created()));
                xLabels[i] = mDateFormat.format(date);
            }
        }

        Arrays.sort(yValues);
        setMaxMinValues(yValues[0],yValues[yValues.length-1],appendMeasure);

        openChart(xValues,yValues,xLabels,xTitle,yTitle,yMin,yMax,color,interval,minLineYValue,maxLineYValue);
    }



    private void openChart(Double[] xValues,Double[] yValues,String[] xLabels,String xTitle,String yTitle,double yMin,double yMax,int color,int yInterval,double minLineYValue,double maxLineYValue){
        int lineWidth = 3;
        int yLabelCount = 10;
        int xLabelCount = 0;
        int displayChartValueDistance = 40;
        int lableFontSize = 20;
        double xMin = 0;
        double xMax = xValues.length;
        float curveAngle = 0.5f;
        View mChart;
        String chartTitle = "Paitent Report";

        XYSeries maxbaseSeries = new XYSeries("");
        XYSeries minbaseSeries = new XYSeries("");

        XYSeries reportSeries = new XYSeries("");
        // Adding data to series
        for(int i=0;i<xValues.length;i++){
            //xValues[i];
            reportSeries.add(i, yValues[i]);
            maxbaseSeries.add(i,maxLineYValue);
            minbaseSeries.add(i,minLineYValue);
        }
        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(maxbaseSeries);
        dataset.addSeries(minbaseSeries);
        dataset.addSeries(reportSeries);

        // Creating XYSeriesRenderer to customize reportSeries

        XYSeriesRenderer maxSeriesRenderer = new XYSeriesRenderer();
        maxSeriesRenderer.setColor(R.color.colorPrimary);
        maxSeriesRenderer.setFillPoints(false);
        maxSeriesRenderer.setLineWidth(lineWidth);
        maxSeriesRenderer.setDisplayChartValues(false);
        maxSeriesRenderer.setStroke(BasicStroke.DASHED);

        XYSeriesRenderer minSeriesRenderer = new XYSeriesRenderer();
        minSeriesRenderer.setColor(R.color.colorPrimary);
        minSeriesRenderer.setFillPoints(false);
        minSeriesRenderer.setLineWidth(lineWidth);
        minSeriesRenderer.setDisplayChartValues(false);
        minSeriesRenderer.setStroke(BasicStroke.DASHED);

        XYSeriesRenderer reportRenderer = new XYSeriesRenderer();
        reportRenderer.setColor(color);
        reportRenderer.setPointStyle(PointStyle.CIRCLE);
        reportRenderer.setFillPoints(true);
        reportRenderer.setLineWidth(lineWidth);
        reportRenderer.setDisplayChartValues(true);
        reportRenderer.setDisplayChartValuesDistance(displayChartValueDistance);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setChartTitle(chartTitle);
        multiRenderer.setXTitle("\n\n\n"+xTitle);
        multiRenderer.setYTitle(yTitle);

        //hide autogenerated labels
        multiRenderer.setXLabels(xLabelCount);
        multiRenderer.setYLabels(yLabelCount);
        //set min max
        multiRenderer.setXAxisMin(xMin);
        multiRenderer.setXAxisMax(xMax);
        multiRenderer.setYAxisMax(yMax);
        multiRenderer.setYAxisMin(yMin);
        Log.e("x min"+multiRenderer.getXAxisMin(),"x max"+multiRenderer.getXAxisMax());
        Log.e("y min"+multiRenderer.getYAxisMin(),"y max"+multiRenderer.getYAxisMax());

        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setPanEnabled(true, true);
        multiRenderer.setZoomEnabled(true, true);

        multiRenderer.setExternalZoomEnabled(true);

        multiRenderer.setPanLimits(new double[] { multiRenderer.getXAxisMin(),multiRenderer.getXAxisMax(), multiRenderer.getYAxisMin(), multiRenderer.getYAxisMax()});
        multiRenderer.setZoomLimits(new double[] { multiRenderer.getXAxisMin(),multiRenderer.getXAxisMax(), multiRenderer.getYAxisMin(), multiRenderer.getYAxisMax()});

        multiRenderer.setInScroll(false);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0,Color.BLACK);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setShowGrid(true);
        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        multiRenderer.setMargins(new int[]{displayChartValueDistance,displayChartValueDistance,displayChartValueDistance,displayChartValueDistance});
        multiRenderer.setLabelsTextSize(lableFontSize);

        int recordSize = xValues.length;

        for(int i=0;i<recordSize;i++){
            multiRenderer.addXTextLabel(i, xLabels[i]);
        }

        multiRenderer.addSeriesRenderer(maxSeriesRenderer);
        multiRenderer.addSeriesRenderer(minSeriesRenderer);
        multiRenderer.addSeriesRenderer(reportRenderer);

        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.report_chart_view_container);
        // Creating a Line Chart
        mChart = ChartFactory.getCubeLineChartView(getBaseContext(), dataset, multiRenderer,curveAngle);
        // Adding the Line Chart to the LinearLayout
        chartContainer.removeAllViews();
        chartContainer.addView(mChart);

    }

    private void clearChart(){

        mSpinSelectParameter.setAdapter(null);
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.report_chart_view_container);
        chartContainer.removeAllViews();
        setMaxMinValues(0,0,"");
    }

    private void setMaxMinValues(double min,double max,String appendMeasure){

        TextView tvMin = (TextView)findViewById(R.id.tv_min_value);
        TextView tvMax = (TextView)findViewById(R.id.tv_max_value);
        tvMax.setText(" "+max+" "+appendMeasure);
        tvMin.setText(" "+min+" "+appendMeasure);

    }
    @Override
    public void onDateSet(int year, int month, int day) {

        if(requestedDateViewId == R.id.btn_to){
            mFromDate.set(Calendar.MONTH,month);
            mFromDate.set(Calendar.DAY_OF_MONTH, day);
            mFromDate.set(Calendar.YEAR, year);
            mFromDate.set(Calendar.HOUR_OF_DAY, 0);
            mFromDate.set(Calendar.MINUTE, 0);
            mFromDate.set(Calendar.SECOND, 0);
            mToDate.set(Calendar.MONTH,month);
            mToDate.set(Calendar.DAY_OF_MONTH, day);
            mToDate.set(Calendar.YEAR, year);
            mToDate.add(Calendar.DATE,1);
            mToDate.set(Calendar.HOUR_OF_DAY, 0);
            mToDate.set(Calendar.MINUTE, 0);
            mToDate.set(Calendar.SECOND, 0);
            displayDate(mFromDate.getTimeInMillis(), requestedDateViewId,mDateBtnFormat);
        }
    }

    private void showCalendar(){
        new CalendarDialog(this,this).showDatePicker();
    }

    private void showNoReportFound(boolean showingReport, String message){
        TextView tv = (TextView)findViewById(R.id.device_message);
        tv.setVisibility(showingReport?View.GONE:View.VISIBLE);
        tv.setText(message);
        LinearLayout ll = (LinearLayout)findViewById(R.id.report_container);
        ll.setVisibility(showingReport?View.VISIBLE:View.INVISIBLE);
    }

}

