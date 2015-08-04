package com.intput.Base;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DeviceInfo {
	
	public static final String TAG = "DeviceMD5";
	public Context mContext = null;
	
	public DeviceInfo(Context context){
		mContext = context;
	}
	
	public String getIMEI(){
		TelephonyManager TelephonyMgr = (TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE); 
		return TelephonyMgr.getDeviceId(); 
	}
	
	public String getPseudoUniqueID(){
		String m_szDevIDShort = "35" + //we make this look like a valid IMEI
		Build.BOARD.length()%10 + 
		Build.BRAND.length()%10 + 
		Build.CPU_ABI.length()%10 + 
		Build.DEVICE.length()%10 + 
		Build.DISPLAY.length()%10 + 
		Build.HOST.length()%10 + 
		Build.ID.length()%10 + 
		Build.MANUFACTURER.length()%10 + 
		Build.MODEL.length()%10 + 
		Build.PRODUCT.length()%10 + 
		Build.TAGS.length()%10 + 
		Build.TYPE.length()%10 + 
		Build.USER.length()%10 ; //13 digits
		return m_szDevIDShort;
	}
	
	public String getAndroidID(){
		return Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
	}
	
	public String getWIFIMac(){
		WifiManager wm = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE); 
		return wm.getConnectionInfo().getMacAddress();
	}
	
	public String getBTMac(){      
		BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();      
		return m_BluetoothAdapter.getAddress();
	}
	
	public String getCombinedDeviceID(){
		String m_szImei = getIMEI();
		String m_szDevIDShort = getPseudoUniqueID();
		String m_szAndroidID = getAndroidID();
		String m_szWLANMAC = getWIFIMac();
			
		String m_szLongID = m_szImei + m_szDevIDShort 
			    + m_szAndroidID+ m_szWLANMAC;  
		// compute md5     
		 MessageDigest m = null;   
		try {
		 m = MessageDigest.getInstance("MD5");
		 } catch (NoSuchAlgorithmException e) {
		 e.printStackTrace();   
		}    
		m.update(m_szLongID.getBytes(),0,m_szLongID.length());   
		// get md5 bytes   
		byte p_md5Data[] = m.digest();   
		// create a hex string   
		String m_szUniqueID = new String();   
		for (int i=0;i<p_md5Data.length;i++) {   
		     int b =  (0xFF & p_md5Data[i]);    
		// if it is a single digit, make sure it have 0 in front (proper padding)    
		    if (b <= 0xF) 
		        m_szUniqueID+="0";    
		// add number to string    
		    m_szUniqueID+=Integer.toHexString(b); 
		   }   // hex string to uppercase   
		m_szUniqueID = m_szUniqueID.toUpperCase();
		return 	m_szUniqueID;
	}
	
	public String getCombinedCRC(AESUtils mAESUtils){
		String MD5 = getCombinedDeviceID();
		String NewMD5 = "";
		String SEED = "CORESOFT";	
		try {
			NewMD5 = mAESUtils.encrypt(SEED, MD5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NewMD5;
		
	}
}
