package com.usb.core;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.intput.Base.IntentDef;
import com.intput.Base.IntentDef.OnUsbCoreReportListener;
import com.usb.serial.UsbSerialCH341;
import com.usb.serial.UsbSerialPL2303;
import com.usb.storage.UsbStorageGembird;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by root on 13-11-3.
 */
public class UsbCore {
    public static final String TAG = "UsbCore";
    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    public Context mContext = null;
    private UsbManager mUsbManager = null;
    private UsbId mUsbId = null;
    private UsbProber mUsbProber = null;
    private ArrayList<UsbComm> mUsbDevice = null;
    private OnUsbCoreReportListener mOnUsbCoreReportListener = null;

    public UsbCore(Context context){
        mContext = context;
        mUsbId = new UsbId();
        mUsbDevice = new ArrayList<UsbComm>();
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        UsbCoreReceiver();
        UsbRegisterDriver();
        mUsbProber = new UsbProber(mContext,mUsbManager,mUsbId);
    }

    public void setUsbCoreReportListener(OnUsbCoreReportListener Listener){
        mOnUsbCoreReportListener = Listener;
    }

    public void UsbCoreGetDeviceList(){
        mUsbProber.UsbProberGetList();
    }

    private void UsbRegisterDriver(){
        if(mUsbId != null){
            mUsbId.UsbRegisterId(UsbSerialPL2303.idVendor, UsbSerialPL2303.idProduct);
            mUsbId.UsbRegisterId(UsbSerialCH341.idVendor, UsbSerialCH341.idProduct);
            mUsbId.UsbRegisterId(UsbStorageGembird.idVendor,UsbStorageGembird.idProduct);

        }
    }
    
    public void UsbCoreReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mContext.registerReceiver(mUsbReceiver, filter);
    }
    
    public void UsbCoreUnReceiver(){
    	if(mUsbReceiver != null){
    		mContext.unregisterReceiver(mUsbReceiver);
    		mUsbReceiver = null;
    	}
    }

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)){
                Log.d(TAG,"UsbManager.ACTION_USB_DEVICE_ATTACHED");
            }

            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)){
                Log.d(TAG,"UsbManager.ACTION_USB_DEVICE_DETACHED");
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if(mUsbId.IsUsbIDRegister(device.getVendorId(),device.getProductId()))
                {
                	if(device != null){
                        Log.d(TAG,"UsbCoreDelDevice getVendorId ["+device.getVendorId()+"] getProductId ["+device.getProductId()+"]");
                        UsbCoreDelDevice(device);
                        if(null != mOnUsbCoreReportListener){
                            mOnUsbCoreReportListener.OnUsbCoreReport(IntentDef.USBCORE_DISCONNECT);
                        }
                    }
                }
            }

            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                   UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                   if(mUsbId.IsUsbIDRegister(device.getVendorId(),device.getProductId()))
                   {
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if(device != null){
                                Log.d(TAG,"UsbCoreAddDevice getVendorId ["+device.getVendorId()+"] getProductId ["+device.getProductId()+"]");
                                if(UsbCoreAddDevice(device)){
                                	if(null != mOnUsbCoreReportListener){
                                        mOnUsbCoreReportListener.OnUsbCoreReport(IntentDef.USBCORE_CONNECT);
                                    }
                                }
                            }
                        }
                        else {
                            Log.d(TAG, "permission denied for device " + device);
                        }
                    }
                }
            }
        }
    };

    public int UsbCoreFindDevice(int VendorId, int ProductId){
        int index = -1;
        Iterator<UsbComm> deviceIterator = mUsbDevice.iterator();
        while(deviceIterator.hasNext()){
            UsbComm device = deviceIterator.next();
            if(device.getIdProduct() == ProductId && device.getIdVendor() == VendorId)
            {
                if(index == -1){
                    index = 0;
                }else{
                    index++;
                }
                break;
            }
        }
        return index;
    }

    public UsbComm UsbCoreGetDevice(){
        return mUsbDevice.get(0);
    }

    private boolean UsbCoreAddDevice(UsbDevice device){
    	int Max = mUsbDevice.size();
    	boolean ret = false;
    	Log.d(TAG,"UsbCoreAddDevice now ["+Max+"]");
    	
    	for(int i = 0; i < Max; i++){
    		UsbComm mUsbComm = mUsbDevice.get(i);
    		if (mUsbComm.getIdVendor() == device.getVendorId() || mUsbComm.getIdProduct() == device.getProductId()){
    			ret = true;
    			break;
    		}
    	}
    	
    	if(ret){
    		Log.d(TAG,"i have usb device=================================");
    		return false;
    	}
    	
        /**
         * Usb Serial
         */
        if(device.getVendorId() == UsbSerialPL2303.idVendor){
            switch (device.getProductId()){
                case UsbSerialPL2303.idProduct:
                    UsbSerialPL2303 mUsbSerialPL2303 = new UsbSerialPL2303(device,mContext);
                    mUsbDevice.add((UsbComm)mUsbSerialPL2303);
                    break;
            }
        }

        if(device.getVendorId() == UsbSerialCH341.idVendor){
            switch (device.getProductId()){
                case UsbSerialCH341.idProduct:
                    UsbSerialCH341 mUsbSerialHL340 = new UsbSerialCH341(device,mContext);
                    mUsbDevice.add((UsbComm)mUsbSerialHL340);
                    break;
            }
        }

        /**
         * Usb Storage
         */

        if (device.getVendorId() == UsbStorageGembird.idVendor){
            switch (device.getProductId()){
                case UsbStorageGembird.idProduct:
                    UsbStorageGembird mUsbStorageGembird = new UsbStorageGembird(device,mContext);
                    mUsbDevice.add((UsbComm)mUsbStorageGembird);
                    break;
            }
        }
        
        return true;
    }

    private void UsbCoreDelDevice(UsbDevice device){
        int index = UsbCoreFindDevice(device.getVendorId(),device.getProductId());
        Log.d(TAG,"UsbCoreDelDevice ["+index+"]");
        if(index != -1){
            mUsbDevice.remove(index);
        }
    }
}
