package com.usb.serial;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Created by root on 13-11-16.
 */
public class UsbSerialCH341 extends UsbSerialCom {
    private static final String tag = UsbSerialCH341.class.getSimpleName();
    public static final int idVendor = 0x1A86;
    public static final int idProduct = 0x7523;

    public static final int DEFAULT_TIMEOUT = 2000;
    /* baudrate calculation factor */
    public static final int CH341_BAUDBASE_FACTOR = 1532620800;
    public static final int CH341_BAUDBASE_DIVMAX = 3;
    /* flags for IO-Bits */
    public static final int CH341_BIT_RTS = (1 << 6);
    public static final int CH341_BIT_DTR = (1 << 5);

    public int mBaudrate = UsbSerialCom.DEFAULT_BAUD_RATE;
    public int mLineControl = CH341_BIT_RTS | CH341_BIT_DTR;;

    public UsbSerialCH341(UsbDevice Device, Context context){
        super(Device, context);
        Log.d(tag, "Log.----------------------------UsbSerialCH341");
    }

    @Override
    public void open() throws IOException {
        super.open();
        int size = 8;
        byte[] buffer = new byte[8];

        /* expect two bytes 0x27 0x00 */
        int ret = ch341_control_in(0x5f, 0, 0, buffer, size);
        Log.d(tag,"ch341_control_in 0x5f ret["+ret+"]");

        ret = ch341_control_out(0xa1, 0, 0);
        Log.d(tag,"ch341_control_in 0xa1 ret["+ret+"]");
        ret = ch341_set_baudrate(DEFAULT_BAUD_RATE);
        Log.d(tag,"ch341_set_baudrate ret["+ret+"]");

        /* expect two bytes 0x56 0x00 */
        ret = ch341_control_in(0x95, 0x2518, 0, buffer, size);
        Log.d(tag,"ch341_control_in 0x95 0x2518 ret["+ret+"]");

        ret = ch341_control_out(0x9a, 0x2518, 0x0050);
        Log.d(tag,"ch341_control_out 0x95 0x2518 ret["+ret+"]");

	    /* expect 0xff 0xee */
        ret = ch341_get_status();
        Log.d(tag,"ch341_get_status ret["+ret+"]");

        ret = ch341_control_out(0xa1, 0x501f, 0xd90a);
        Log.d(tag,"ch341_control_out 0xa1 0x501f ret["+ret+"]");

        ret = ch341_set_baudrate(DEFAULT_BAUD_RATE);
        Log.d(tag,"ch341_set_baudrate ret["+ret+"]");

        ret = ch341_set_handshake(mLineControl);
        Log.d(tag,"ch341_set_handshake mLineControl["+mLineControl+"] ret ["+ret+"]");

	    /* expect 0x9f 0xee */
        ret = ch341_get_status();
        Log.d(tag,"ch341_get_status ret["+ret+"]");
        
        Log.d(tag,"ch341 mReadEndpoint getMaxPacketSize["+mReadEndpoint.getMaxPacketSize()+"]");
        Log.d(tag,"ch341 mWriteEndpoint getMaxPacketSize["+mWriteEndpoint.getMaxPacketSize()+"]");
    }

    @Override
    public void close() throws IOException {
        super.close();
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

    @Override
    public int read(byte[] dest, int timeoutMillis) throws IOException {

        int amtRead = 0;
        amtRead = mUsbDeviceConnection.bulkTransfer(mReadEndpoint, dest, dest.length,
                timeoutMillis);
        return amtRead;
    }

    @Override
    public int write(byte[] dest, int timeoutMillis) throws IOException {
        int amtWritten = 0;
        amtWritten = mUsbDeviceConnection.bulkTransfer(mWriteEndpoint, dest, dest.length,
                timeoutMillis);

        return amtWritten;
    }

    private int ch341_control_out(int request, int value, int index){
        return mUsbDeviceConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_OUT,
                request, value, index, null, 0, DEFAULT_TIMEOUT);
    }

    private int ch341_control_in(int request, int value, int index, byte[] buffer, int length){
        return mUsbDeviceConnection.controlTransfer(UsbConstants.USB_TYPE_VENDOR | UsbConstants.USB_DIR_IN,
                request, value, index, buffer, length, DEFAULT_TIMEOUT);
    }

    public int ch341_set_baudrate(int baud_rate)
    {
        int a, b;
        int ret;

        mBaudrate = baud_rate;

        switch (mBaudrate) {
            case 2400:
                a = 0xd901;
                b = 0x0038;
                break;
            case 4800:
                a = 0x6402;
                b = 0x001f;
                break;
            case 9600:
                a = 0xb202;
                b = 0x0013;
                break;
            case 19200:
                a = 0xd902;
                b = 0x000d;
                break;
            case 38400:
                a = 0x6403;
                b = 0x000a;
                break;
            case 115200:
                a = 0xcc03;
                b = 0x0008;
                break;
            default:
                return -1;
        }

        ret = ch341_control_out(0x9a, 0x1312, a);
        if (ret > 0){
            ret = ch341_control_out(0x9a, 0x0f2c, b);
        }

        return ret;
    }

    public int ch341_get_status()
    {
        int size = 8;
        byte[] buffer = new byte[size];
        int ret = ch341_control_in(0x95, 0x0706, 0, buffer, size);

        if (ret == 2) {
            ret = 0;
            // priv->line_status = (~(*buffer)) & CH341_BITS_MODEM_STAT;
        } else
            ret = -1;

        return ret;
    }

    public int ch341_set_handshake(int control)
    {
        return ch341_control_out(0xa4, ~control, 0);
    }

}
