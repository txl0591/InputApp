package com.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

















import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.intput.Base.DeviceInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class MailOper {
	private Context mContext = null;
	
	public void SendRegMail(Context context) {		
		mContext = context;
		SendMailThread mSendMailThread = new SendMailThread(context);
		mSendMailThread.start();
	}
	
	public boolean ReadRegSharedPreferences()
	{
		SharedPreferences user = mContext.getSharedPreferences("com.inputapp.main",0);
		boolean ret = user.getBoolean("RegState", false);
		return ret;
	}
	public void WriteSharedPreferences(boolean state){
		SharedPreferences user = mContext.getSharedPreferences("com.inputapp.main",0);
		Editor editor = user.edit();
		editor.putBoolean("RegState", state);
		editor.commit();
	}
	
	private String getAppInfo() {
 		try {
 			String pkName = mContext.getPackageName();
 			String versionName = mContext.getPackageManager().getPackageInfo(
 					pkName, 0).versionName;
 			versionName = "V" + versionName;
 			return versionName;
 		} catch (Exception e) {
 		}
 		return null;
 	}
	
	class SendMailThread extends Thread{
		private Context mcontext = null;
		public SendMailThread(Context context){
			mcontext = context;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated mthod stub
			super.run();
			boolean regstate = ReadRegSharedPreferences();
			if(regstate == false){
				WifiManager mWifiManager = (WifiManager) mcontext.getSystemService(Context.WIFI_SERVICE);
				if(!mWifiManager.isWifiEnabled()){
					mWifiManager.setWifiEnabled(true); 
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			boolean netstate = isNetworkAvailable(mcontext);
			if(false == regstate && netstate){
				DeviceInfo mDeviceInfo = new DeviceInfo(mcontext);
				String WIFI = "录入仪设备:  "+mDeviceInfo.getWIFIMac();
				boolean send = sendEmail("注册信息", WIFI,  "SN: "+mDeviceInfo.getCombinedDeviceID()+" Version: "+getAppInfo());
				if (send){
					WriteSharedPreferences(send);
				}
			}
			
		}
		
	}
	
	public class PassAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = "coresoft0591@163.com";
			String pwd = "tangxl19820807";
			return new PasswordAuthentication(username, pwd);
		}
	}

	public boolean sendEmail(String Device, String Head, String Data) {
		boolean ret = false;
		Properties props = new Properties();
		props.put("mail.smtp.protocol", "smtp");
		props.put("mail.smtp.auth", "true"); // 设置要验证
		props.put("mail.smtp.host", "smtp.163.com"); // 设置host
		props.put("mail.smtp.port", "25"); // 设置端口
		PassAuthenticator pass = new PassAuthenticator(); // 获取帐号密码
		Session session = Session.getInstance(props, pass); // 获取验证会话
		try {
			// 配置发送及接收邮箱
			InternetAddress fromAddress, toAddress;
			fromAddress = new InternetAddress("coresoft0591@163.com", Device);
			toAddress = new InternetAddress("tangxl0591@163.com", "");
			// 配置发送信息
			MimeMessage message = new MimeMessage(session);
			message.setContent(Data, "text/plain");
			message.setSubject(Head);
			message.setFrom(fromAddress);
			message.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
			message.saveChanges();
			// 连接邮箱并发送
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.163.com", "coresoft0591@163.com",
					"tangxl19820807");
			transport.send(message);
			transport.close();
			ret = true;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			Log.i("Msg", e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static boolean isNetworkAvailable(Context context) {   
        ConnectivityManager cm = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (cm == null) {   
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
                        return true;   
                    }   
                }   
            }   
        }   
        return false;   
    }
}
