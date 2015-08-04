package com.intput.Client;

import com.intput.Base.IntentDef;
import com.intput.Base.IntentDef.OnStateReportListener;
import com.intput.Service.FrameData;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

public class MainClient extends BaseClient implements IntentDef.OnCommDataReportListener {
	
	public final static String tag = "MainClient" ;
	public Context mContext = null;
	public OnStateReportListener mOnStateReportListener = null;
	private FrameData mFrameData = null;
	
	public MainClient(Context context) {
		super(context);
		mContext = context;
		mFrameData = new FrameData();
		String RecvAction[] = {IntentDef.MODULE_DISTRIBUTE,IntentDef.MODULE_RESPONSION};
		startReceiver(context,RecvAction);
		StartIPC(context,IntentDef.SERVICE_NAME_MAIN);
		setmDataReportListener(this);
	}
	
	public void MainClientStop(){
		stopReceiver(mContext, IntentDef.MODULE_DISTRIBUTE);
		StopIPC(mContext, IntentDef.SERVICE_NAME_MAIN);
	}
	
	public void setStateReportListener(OnStateReportListener Listener){
		mOnStateReportListener = Listener;
	}
	
	public void setStateReport(int State, byte[] Param){
		if(mOnStateReportListener != null){
			mOnStateReportListener.OnStateReport(State, Param);
		}
	}

	@Override
	public void OnResponsionReport(int Id, int Cmd, int Ack, byte[] Data, int DataLen) {
		
		
	}

	@Override
	public void OnDistributeReport(int Id, int Cmd, int Ack, byte[] Data, int DataLen) {
	}
    
	public void SendData(boolean head, String FrameGJH, byte FrameCQS, byte FrameCSM, byte FrameJD, byte FrameFF, byte FrameBS, 
			byte[] Timer, byte[]data, int datalen, byte[] datath){
		if(mMainService.getSendState()){
			byte[] buffer = new byte[1024];		
			int bufferlen = mFrameData.SerialDataPack(buffer, head, FrameGJH, FrameCQS, FrameCSM, FrameJD, FrameFF, FrameBS,
					Timer[0], Timer[1], Timer[2], Timer[3], Timer[4], Timer[5], data, datalen, datath);
			mMainService.SendData(buffer, bufferlen);
		}
		
	}
	
	public void SendData(byte[]buffer, int bufferlen){
		mMainService.SendData(buffer, bufferlen);
	}
	
	public void SendByte(byte buffer){
		byte[] data = new byte[1];
		data[0] = buffer;
		mMainService.SendData(data, 1);
	}
	
	public boolean getSendState(){
		return mMainService.getSendState();
	}
}

