package com.transility.welloculus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.transility.welloculus.R;
import com.transility.welloculus.beans.ItemToDisplayBean;
import com.transility.welloculus.utils.AppUtility;
import com.transility.welloculus.utils.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type User attributes adapter.
 */
public class UserAttributesAdapter extends BaseAdapter {
    private int mCount;
    private LayoutInflater mLayoutInflater;
    private DateFormat mdateFormat;

    /**
     * Instantiates a new User attributes adapter.
     *
     * @param context    the context
     * @param dateFormat the date format
     */
    public UserAttributesAdapter(Context context, DateFormat dateFormat) {



        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mdateFormat = dateFormat;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view = convertView;
        if (view == null) {

            view = mLayoutInflater.inflate(R.layout.fields_generic, null);
            holder = new Holder();
            holder.label = (TextView) view.findViewById(R.id.textViewUserDetailLabel);
            holder.data = (TextView) view.findViewById(R.id.editTextUserDetailInput);
            holder.message = (TextView) view.findViewById(R.id.textViewUserDetailMessage);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        List<ItemToDisplayBean> itemsList =  new ArrayList<ItemToDisplayBean>();
        ItemToDisplayBean item = new ItemToDisplayBean();
        item.setDataText("sneha");
        item.setDataBackground(0);
        item.setDataColor(12303292);
        item.setDataDrawable(null);
        item.setLabelColor(16777216);
        item.setLabelText("Given_Name");
        item.setMessageText("asdf");



        //ItemToDisplayBean item = CognitoHelper.getItemForDisplay(position);
        String dataText = item.getDataText();
        if (item.getLabelText().equalsIgnoreCase(Constants.BIRTHDATE_KEY)) {
            try {
                Date date = new Date(Long.parseLong(dataText));
                dataText = mdateFormat.format(date);
            } catch (Exception e) {
                Log.e(AppUtility.TAG, Log.getStackTraceString(e));
            }

        }
        holder.label.setText(item.getLabelText());
        holder.label.setTextColor(item.getLabelColor());
        holder.data.setHint(item.getLabelText());
        holder.data.setText(dataText);
        holder.data.setTextColor(item.getDataColor());
        holder.message.setText(item.getMessageText());
        holder.message.setTextColor(item.getMessageColor());

        return view;
    }

    /**
     * The type Holder.
     */
// Helper class to recycle View's
    static class Holder {
        /**
         * The Label.
         */
        TextView label;
        /**
         * The Data.
         */
        TextView data;
        /**
         * The Message.
         */
        TextView message;
    }
}
