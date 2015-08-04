package com.intput.Service;

import android.content.Context;
import android.util.Log;

import com.intput.Base.IntentDef;
import com.intput.Service.FrameTypeDef.FrameIndex;
import com.intput.Base.Common;

public class FrameData {
    public static final int FRAME_MIN = 9;
    public static final String tag = "FRAME_DATA";
    public static byte ID = 0;
    public Context mContext = null;
    public IntentDef.OnCommDataReportListener mSerialDataListener =null;

    public void SerialDataUnPackListener(IntentDef.OnCommDataReportListener Listener){
        mSerialDataListener = Listener;
    }


    public void SerialDataUnPack(byte[] data, int dataLen)
    {
    	
    }

    public int SerialDataPack(byte[] data, boolean Head, String FrameGJH, byte FrameCQS, byte FrameCSM, byte FrameJD, byte FrameFF, byte FrameBS, 
    		byte Year, byte Mon, byte Day, byte Hour, byte Min, byte Sec, byte[] buffer, int bufferlen, byte[] datath)
    {
    	int base = 0;
    	if(Head){
    		base = 256;
    		for(int j = 0; j < base; j++){
        		data[j] = (byte) 0xFF;
        	}
    	}
    	
    	char[] strChar = FrameGJH.toCharArray();
    	data[base] = (byte) 0xD9;
    	data[base+1] = (byte) 0x98;
    	data[base+2] = (byte) 0xFF;
    	data[base+3] = (byte) 0xFF;
    	   
    	for(int i = 0 ; i < FrameGJH.length(); i++){
    		char bm = strChar[i];
    		String str = String.valueOf(bm);
    		byte[] am = Common.STR2BCD(str);
    		data[base+FrameIndex.FRAME_GJH+i] = am[0];
    	}

    	data[base+FrameIndex.FRAME_CQS] = FrameCQS;
    	data[base+FrameIndex.FRAME_FF] = FrameFF;
    	data[base+FrameIndex.FRAME_CSM] = FrameCSM;
    	data[base+FrameIndex.FRAME_JD] = FrameJD;
    	data[base+FrameIndex.FRAME_CSM] = FrameCSM;
    	data[base+FrameIndex.FRAME_BS] = FrameBS;
    	
    	for(int i = 0; i < 9; i++){
    		data[base+FrameIndex.FRAME_DEFAULT+i] = (byte) 0xFF; 
    	}

    	data[base+FrameIndex.FRAME_UNKNOW] = 0x00;
    	
    	String sYear = String.valueOf(Year);
    	byte[] bYear = Common.STR2BCD(sYear);
    	data[base+FrameIndex.FRAME_YEAR] = bYear[0];
    	String sMon = String.valueOf(Mon);
    	byte[] bMon = Common.STR2BCD(sMon);
    	data[base+FrameIndex.FRAME_MON] = bMon[0];
    	String sDay = String.valueOf(Day);
    	byte[] bDay = Common.STR2BCD(sDay);
    	data[base+FrameIndex.FRAME_DAY] = bDay[0];
    	String sHour = String.valueOf(Hour);
    	byte[] bHour = Common.STR2BCD(sHour);
    	data[base+FrameIndex.FRAME_HOUR] = bHour[0];
    	String sMin = String.valueOf(Min);
    	byte[] bMin = Common.STR2BCD(sMin);
    	data[base+FrameIndex.FRAME_MIN] = bMin[0];
    	String sSec = String.valueOf(Sec);
    	byte[] bSec = Common.STR2BCD(sSec);
    	data[base+FrameIndex.FRAME_SEC] = bSec[0];
    	
    	for(int j = 0; j < buffer.length; j++){
    		data[base+FrameIndex.FRAME_DATA+j] = buffer[j];
    	}
    	
    	for(int i = 0; i < 16; i++){
    		data[base+FrameIndex.FRAME_DATA+bufferlen+i] = datath[i];
    	}
//    	data[base+FrameIndex.FRAME_DATA+bufferlen+16] = (byte) 0xFA;
		return FrameIndex.FRAME_DATA+bufferlen+16+base;
    }

}
