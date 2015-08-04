package com.usb.serial;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.util.Log;

import com.usb.core.UsbComm;

import java.io.IOException;

/**
 * Created by root on 13-11-4.
 */
public abstract class UsbSerialCom extends UsbComm {

    private static final String tag = UsbSerialCom.class.getSimpleName();
    public  static final int DEFAULT_BAUD_RATE = 9600;
    public UsbEndpoint mReadEndpoint = null;
    public UsbEndpoint mWriteEndpoint = null;

    public UsbSerialCom(UsbDevice Device, Context context) {
        super(Device, context);

    }

    @Override
    public void open() throws IOException {
        super.open();
        for (int i = 0; i < mUsbDevice.getInterfaceCount(); i++) {
            UsbInterface usbIface = mUsbDevice.getInterface(i);
            if (mUsbDeviceConnection.claimInterface(usbIface, true)) {
                Log.d(tag, "Interface " + i + " SUCCESS");
            } else {
                Log.d(tag, "Interface " + i + " FAIL");
            }
        }

        UsbInterface dataIface = mUsbDevice.getInterface(mUsbDevice.getInterfaceCount() - 1);
        for (int i = 0; i < dataIface.getEndpointCount(); i++) {
            UsbEndpoint ep = dataIface.getEndpoint(i);
            Log.d(tag,"UsbInterface ["+i+"]");
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
                    mReadEndpoint = ep;
                } else {
                    mWriteEndpoint = ep;
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    public abstract void setParameters();
}
