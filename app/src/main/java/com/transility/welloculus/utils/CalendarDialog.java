package com.transility.welloculus.utils;

/**
 * Created by sneha.bansal on 4/17/2017.
 */

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;

import com.transility.welloculus.R;

import java.util.Calendar;

public class CalendarDialog {

    /**
     * Interface to implement for listening date picker actions.
     */
    public interface OnDateSetListener {
        void onDateSet(int Year, int Month, int Day);
    }

    private OnDateSetListener mDateSetlistener;
    private Activity mActivity;
    private DatePickerDialog mCurrentDatePicker;
    private DatePickHandler mDatePickerHandler;

    public CalendarDialog(Activity activity, OnDateSetListener listener) {
        this.mActivity = activity;
        this.mDateSetlistener = listener;
        mDatePickerHandler = new DatePickHandler();
    }

    /**
     * Method to display the date picker
     */
    public void showDatePicker(){
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        mCalendar.set(Calendar.ERA,1);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCurrentDatePicker = new DatePickerDialog(mActivity,mDatePickerHandler, year, month, day);
        mCurrentDatePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, mActivity.getString(R.string.ok),new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatePicker datePicker = mCurrentDatePicker.getDatePicker();
                mDatePickerHandler.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            }
        });

        mCurrentDatePicker.show();
    }

    /**
     * Handler for listening change in date
     */
    private class DatePickHandler implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year,int month, int day) {

            mDateSetlistener.onDateSet(year,month,day);
            mCurrentDatePicker.hide();
        }
    }
}



