package com.input.app;


import com.intput.Client.MainClient;
import com.intput.Ctrl.ViewCtrl;
import com.intput.database.DataInfoDBHelper;

import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;

public class InputBase extends RelativeLayout{
	public MainClient mMainClient = null;
	public Context mContext = null;	
	public String mAction;
	private DataInfoDBHelper mDataInfoDBHelper = null;
	
	public InputBase(Context context,String action, int Id, DataInfoDBHelper DB) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		mAction = action;
		mDataInfoDBHelper = DB;
		setId(Id);
	}
	public void setBaseClient(MainClient Client) 
	{
		mMainClient = Client;
	}
	
	public boolean sendBroadcast(int oper, int Id, int param)
	{
		Intent intent = new Intent(mAction);
		intent.putExtra(ViewCtrl.VIEWCTRL_OPER, oper);
		intent.putExtra(ViewCtrl.VIEWCTRL_ID, Id);
		intent.putExtra(ViewCtrl.VIEWCTRL_PARAM, param);
		mContext.sendBroadcast(intent);
		return true;
	}
	
	public boolean sendBroadcast(int oper, int Id)
	{
		Intent intent = new Intent(mAction);
		intent.putExtra(ViewCtrl.VIEWCTRL_OPER, oper);
		intent.putExtra(ViewCtrl.VIEWCTRL_ID, Id);
		mContext.sendBroadcast(intent);
		return true;
	}
	
	public void onHidePage()
	{
		sendBroadcast(ViewCtrl.VIEW_OUT,getId());
	}
		
	
	public void onHidePage(int Id)
	{
		sendBroadcast(ViewCtrl.VIEW_OUT, Id);
	}
	
	public void onShowPage(int Id)
	{
		sendBroadcast(ViewCtrl.VIEW_IN,Id);
	}
		
	public void onShowPage(int Id, int param)
	{
		sendBroadcast(ViewCtrl.VIEW_IN,Id,param);
	}
	
	public void OnMenuEventReport(int param) {
		// TODO Auto-generated method stub
		
	}
}
