package com.usb.core;

import java.io.IOException;

/**
 * Created by root on 13-11-16.
 */
public interface UsbDriver {

    public void open() throws IOException;

    public void close() throws IOException;
}
