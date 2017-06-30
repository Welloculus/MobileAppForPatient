package com.transility.welloculus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.transility.welloculus.R;
import com.transility.welloculus.app.receiver.OnDeviceConnectListener;
import com.transility.welloculus.beans.DeviceInfoBean;
import com.transility.welloculus.beans.DeviceResponseBean;
import com.transility.welloculus.bluetooth.BluetoothHandler;
import com.transility.welloculus.httpresponse.BaseResponse;
import com.transility.welloculus.httpresponse.DevicesListResponse;
import com.transility.welloculus.httpresponse.HttpResponseHandler;
import com.transility.welloculus.utils.AppUtility;

import java.util.List;

/**
 * Adapter to show the list of devices associated with the user.
 *
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ListItemViewHolder> {

    private List<DeviceInfoBean> deviceInfoList;
    private OnDeviceConnectListener listener = null;
    private BluetoothHandler mBluetoothHandler;
    private Context mContext;
    private Boolean allDevice = false;

    /**
     * The type My view holder.
     */
    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDeviceMacAddress;
        private TextView txtDeviceName;
        private TextView txtConnectionType;
        protected TextView txtaddedOn;
        protected TextView txtaddedOnDetail;
        protected TextView txtDeviceAttr;
        private Button btnDeviceConnect;
        private Button btnStartStop;


        /**
         * Instantiates a new My view holder.
         *
         * @param view the view
         */
        public ListItemViewHolder(View view) {
            super(view);
            txtDeviceMacAddress = (TextView) view.findViewById(R.id.txtdeviceId);
            txtDeviceName = (TextView) view.findViewById(R.id.txtDeviceName);
            txtConnectionType = (TextView) view.findViewById(R.id.txtConnectionType);
            txtaddedOn = (TextView) view.findViewById(R.id.txtaddedOn1);
            txtaddedOnDetail = (TextView) view.findViewById(R.id.txtaddedOn);
            btnDeviceConnect = (Button) view.findViewById(R.id.btnDeviceConnect);
            btnStartStop = (Button) view.findViewById(R.id.btnStartStop);
          //  txtDeviceAttr = (TextView)view.findViewById(R.id.txtDeviceAttr);
        }
    }

    /**
     * Instantiates a new Device list adapter.
     *
     * @param context        the context
     * @param deviceInfoList the device info list
     * @param listener       the listener
     */
    public DeviceListAdapter(Context context, List<DeviceInfoBean> deviceInfoList, OnDeviceConnectListener listener) {
        this.mContext = context;
        this.deviceInfoList = deviceInfoList;
        this.listener = listener;
        mBluetoothHandler = BluetoothHandler.getInstance();
    }

    public DeviceListAdapter(Context context, List<DeviceInfoBean> deviceInfoList, OnDeviceConnectListener listener, Boolean all) {
        this.mContext = context;
        this.deviceInfoList = deviceInfoList;
        this.listener = listener;
        mBluetoothHandler = BluetoothHandler.getInstance();
        this.allDevice = all;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_list_row, parent, false);

        return new ListItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ListItemViewHolder holder, final int position) {
        final DeviceInfoBean deviceInfo = deviceInfoList.get(position);

                holder.txtDeviceMacAddress.setText(deviceInfo.getDevice_udi());
                holder.txtDeviceName.setText(deviceInfo.getDevice_name());
                holder.txtaddedOnDetail.setText(deviceInfo.getStart_date());
                holder.txtConnectionType.setText(deviceInfo.getDevice_type());

            holder.btnDeviceConnect.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onConnectClick(deviceInfo);

                }

            });

        holder.btnStartStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onConnectClick(deviceInfo, v);

            }

        });

            if (deviceInfo.getDevice_type() != null && AppUtility.BLUETOOTH.equalsIgnoreCase(deviceInfo.getDevice_type()) && !allDevice) {
                //holder.txtDeviceAttr.setText("Heart Rate");
                holder.btnDeviceConnect.setVisibility(View.VISIBLE);
            } else {
                //holder.txtDeviceAttr.setText("Room Tempreture");
                holder.btnDeviceConnect.setVisibility(View.GONE);
            }
            if(allDevice){
                holder.txtaddedOn.setVisibility(View.GONE);
                holder.txtaddedOnDetail.setVisibility(View.GONE);
                holder.btnStartStop.setVisibility(View.VISIBLE);
                if (allDevice && (!deviceInfo.getIn_use())) {
                    holder.btnStartStop.setText("Start Using");
                } else {
                    holder.btnStartStop.setText("Stop Using");
                }
            }else{
                holder.btnStartStop.setVisibility(View.GONE);
            }


            if (mBluetoothHandler.isConnected()) {
                DeviceInfoBean connectedDevice = mBluetoothHandler.getConnectedDevice();
                if (connectedDevice != null && connectedDevice.getDevice_udi().equalsIgnoreCase(deviceInfo.getDevice_udi())) {
                    holder.btnDeviceConnect.setText(mContext.getString(R.string.disconnect));
                } else {
                    holder.btnDeviceConnect.setText(mContext.getString(R.string.connect));
                }
            } else {
                holder.btnDeviceConnect.setText(mContext.getString(R.string.connect));
            }

    }

    @Override
    public int getItemCount() {
        return deviceInfoList != null ? deviceInfoList.size() : 0;
    }

    private class GetDeviceListResponseHandler implements HttpResponseHandler {
        @Override
        public void handleResponse(BaseResponse baseResponse) {
            DevicesListResponse deviceListResponse = (DevicesListResponse) baseResponse;
            DeviceResponseBean response = deviceListResponse.getDeviceResponse();
        }



        @Override
        public void handleError(BaseResponse baseResponse) {
            Log.e(AppUtility.TAG, "response " + baseResponse);
            Log.e(AppUtility.TAG, "response " + baseResponse.getmStatus());
        }
    }


}
