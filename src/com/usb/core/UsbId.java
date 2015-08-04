package com.usb.core;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by root on 13-11-3.
 */
public class UsbId {
    public static final String tag = "UsbId";
    private ArrayList<UsbRegId> mUsbIDList = null;

    public UsbId(){
        mUsbIDList = new ArrayList<UsbRegId>();
    }

    public void UsbRegisterId(int VendorId, int ProductId){
        UsbRegId mUsbRegisterId =  new UsbRegId(VendorId, ProductId);
        mUsbIDList.add(mUsbRegisterId);
    }

    public Boolean IsUsbIDRegister(int VendorId, int ProductId){
        Boolean mBoolean = false;
       if(!mUsbIDList.isEmpty()){
           for (int i = 0; i < mUsbIDList.size(); i++){
               UsbRegId mUsbReg = mUsbIDList.get(i);
               if(mUsbReg.ProductId == ProductId && mUsbReg.VendorId == VendorId){
                   mBoolean = true;
                   break;
               }
           }
       }

       return mBoolean;
    }

    class UsbRegId{
        int VendorId;
        int ProductId;

        public UsbRegId(int Vendor, int Product){
            VendorId = Vendor;
            ProductId = Product;
        }
    }
}
