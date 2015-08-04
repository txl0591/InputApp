package com.usb.core;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by root on 13-11-3.
 */
public class UsbProber {
    public static final String tag = "UsbProber";

    public Context mContext = null;
    public UsbManager mUsbManager = null;
    public PendingIntent mPermissionIntent = null;
    public UsbId mUsbId = null;


    public UsbProber(Context context, UsbManager manager, UsbId id){
        mContext = context;
        mUsbManager = manager;
        mUsbId = id;
        mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(UsbCore.ACTION_USB_PERMISSION), 0);
        UsbProberGetList();
    }

    public void UsbProberGetList(){
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            mUsbManager.requestPermission(device, mPermissionIntent);
        }
    }
}
