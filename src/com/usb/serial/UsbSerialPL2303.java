package com.usb.serial;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.util.Log;

import java.io.IOException;

/**
 * Created by root on 13-11-12.
 */
public class UsbSerialPL2303 extends UsbSerialCom {
    @Override
    public int write(byte[] dest, int timeoutMillis) throws IOException {
        return 0;
    }

    private static final String tag = "UsbSerialPL2303";
    public static final int idVendor = 0x067b;
    public static final int idProduct = 0x2303;

    public UsbSerialPL2303(UsbDevice Device, Context context) {
        super(Device, context);
        Log.d(tag, "Log.----------------------------UsbSerialPL2303");
    }

    @Override
    public int read(byte[] dest, int timeoutMillis) throws IOException {
        return 0;
    }

    @Override
    public void setParameters() {

    }

    @Override
    public int getIdVendor() {
        return idVendor;
    }

    @Override
    public int getIdProduct() {
        return idProduct;
    }
}
