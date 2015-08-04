package com.intput.Client;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.intput.Base.IntentDef;
import com.intput.Service.MainService;

public class BaseClient {
	private static final String tag="BaseClient";
	protected Context mContext = null;
	protected MainService mMainService = null;
	protected String mServiceName = null;
	protected ClientReceiver mClientReceiver = null;
	private Handler mHandler = null;
	protected IntentDef.OnCommDataReportListener mDataReportListener=null;
	
	public BaseClient(Context context)
	{
		mContext = context;
	}
	
	protected  ServiceConnection mServiceConn = new ServiceConnection(){

		public void onServiceConnected(ComponentName name, IBinder service) {
			mMainService = ((MainService.MainBinder)service).getMainService();
			Log.d(tag, "service connected mainservice..."+mMainService+" service "+service);
		}

		public void onServiceDisconnected(ComponentName name) {
			mMainService = null;
			Log.d(tag, "service Disconnected mainservice...");
		}
		
	};
	
	protected void StartIPC(Context context,String serviceName)
	{
		mServiceName = serviceName;
		Intent serviceIntent = new Intent(serviceName);
		context.startService(serviceIntent);
		context.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);		
	}
	
	protected void StopIPC(Context context, String serviceName)
	{
	    if ( null != mServiceConn )
	    {
	        context.unbindService(mServiceConn);
	        mServiceConn = null;
	    }
        Intent serviceIntent = new Intent(serviceName);
        context.stopService(serviceIntent);  
	}
	
	public void setmDataReportListener(
			IntentDef.OnCommDataReportListener mDataReportListener) {
		this.mDataReportListener = mDataReportListener;
	}
	
	protected void startReceiver(Context context, String broadcastName) {
		mHandler = new UartCommDataHandler(Looper.myLooper());
		mClientReceiver = new ClientReceiver();
		IntentFilter filter = new IntentFilter(broadcastName);
		context.registerReceiver(mClientReceiver, filter);
	}
	
	protected void startReceiver(Context context, String[] broadcastList) {
		mHandler = new UartCommDataHandler(Looper.getMainLooper());
		mClientReceiver = new ClientReceiver();
		IntentFilter filter = new IntentFilter();
		for (String s : broadcastList) {
			filter.addAction(s);
		}
		context.registerReceiver(mClientReceiver, filter);
	}
	
	protected void stopReceiver(Context context, String broadcastName) {        
	    if ( null != mClientReceiver )
	    {
	        context.unregisterReceiver(mClientReceiver);
	        mClientReceiver = null;
	    }
    }

	class ClientReceiver extends BroadcastReceiver {
		 
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub			
			if (mHandler != null) {
				Message m = mHandler.obtainMessage(1, 1, 1, intent);
				mHandler.sendMessage(m);
			}
		}
	}

	class UartCommDataHandler extends Handler {
		public UartCommDataHandler(Looper looper) {
			super(looper);
		}

		 
		public void handleMessage(Message msg) {
			Intent intent=(Intent)msg.obj;
			if (intent==null)
			{
				Log.e(tag, "msg.obj is null");
				return;
			}
			
			String action=intent.getAction();
			
			if (action.equals(IntentDef.MODULE_DISTRIBUTE)){
				byte[] data = null;
				int Id = intent.getIntExtra(IntentDef.INTENT_COMM_ID,IntentDef.INTENT__TYPE_INVALID);
				int cmd = intent.getIntExtra(IntentDef.INTENT_COMM_CMD,IntentDef.INTENT__TYPE_INVALID);
				int ack = intent.getIntExtra(IntentDef.INTENT_COMM_ACK,IntentDef.INTENT__TYPE_INVALID);
				int datalen = intent.getIntExtra(IntentDef.INTENT_COMM_DATALEN, 0);
				if(datalen > 0){
					data=intent.getByteArrayExtra(IntentDef.INTENT_COMM_DATA);
				}
				if (mDataReportListener==null)
				{
					Log.e(tag, "mDataReportListener is null");
					return;
				}
				mDataReportListener.OnDistributeReport(Id, cmd, ack, data, datalen);
			}else if (action.equals(IntentDef.MODULE_RESPONSION)){
				byte[] data = null;
				int Id = intent.getIntExtra(IntentDef.INTENT_COMM_ID,IntentDef.INTENT__TYPE_INVALID);
				int cmd = intent.getIntExtra(IntentDef.INTENT_COMM_CMD,IntentDef.INTENT__TYPE_INVALID);
				int ack = intent.getIntExtra(IntentDef.INTENT_COMM_ACK,IntentDef.INTENT__TYPE_INVALID);
				int datalen = intent.getIntExtra(IntentDef.INTENT_COMM_DATALEN, 0);
				if(datalen > 0){
					data=intent.getByteArrayExtra(IntentDef.INTENT_COMM_DATA);
				}
				if (mDataReportListener==null)
				{
					Log.e(tag, "mDataReportListener is null");
					return;
				}
				mDataReportListener.OnResponsionReport(Id, cmd, ack, data, datalen);
			}
    	}
	}
	
}

