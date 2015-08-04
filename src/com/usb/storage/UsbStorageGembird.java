package com.usb.storage;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.util.Log;

/**
 * Created by root on 13-11-12.
 */
public class UsbStorageGembird extends UsbStorageCom {

    private static final String tag = "UsbStorageGembird";
    public static final int idVendor = 0x1908;
    public static final int idProduct = 0x1320;

    public UsbStorageGembird(UsbDevice Device, Context context) {
        super(Device, context);
        Log.d(tag, "Log.----------------------------UsbStorageGembird");
    }
}
