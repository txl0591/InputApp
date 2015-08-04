package com.usb.storage;

import android.content.Context;
import android.hardware.usb.UsbDevice;

import com.usb.core.UsbComm;

import java.io.IOException;

/**
 * Created by root on 13-11-12.
 */
public class UsbStorageCom extends UsbComm {
    public UsbStorageCom(UsbDevice Device, Context context) {
        super(Device, context);
    }

    @Override
    public int write(byte[] dest, int timeoutMillis) throws IOException {
        return 0;
    }

    @Override
    public int getIdVendor() {
        return 0;
    }

    @Override
    public int getIdProduct() {
        return 0;
    }

    @Override
    public int read(byte[] dest, int timeoutMillis) throws IOException {
        return 0;
    }
}
