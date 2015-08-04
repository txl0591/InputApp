package com.usb.core;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by root on 13-11-5.
 */
public abstract class UsbComm implements UsbDriver{
    private static final String tag = UsbComm.class.getSimpleName();
    public UsbDevice mUsbDevice = null;
    public UsbDeviceConnection mUsbDeviceConnection = null;
    public Context mContext = null;

    public UsbComm(UsbDevice Device,Context context){
        mContext = context;
        mUsbDevice = Device;

        UsbManager mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        mUsbDeviceConnection = mUsbManager.openDevice(mUsbDevice);
    }


    @Override
    public void open() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }

    public abstract int read(byte[] dest, int timeoutMillis) throws IOException;
    public abstract int write(byte[] dest, int timeoutMillis) throws IOException;
    public abstract int getIdVendor();
    public abstract int getIdProduct();
}
