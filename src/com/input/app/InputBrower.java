package com.input.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.http.util.EncodingUtils;

import com.inputapp.main.R;
import com.intput.Base.CalculationHelper;
import com.intput.Base.Common;
import com.intput.Ctrl.ListCtrl;
import com.intput.Ctrl.ListCtrlAdapter;
import com.intput.Service.FrameTypeDef.FrameBS;
import com.intput.Service.FrameTypeDef.FrameCSM;
import com.intput.Service.FrameTypeDef.FrameFF;
import com.intput.Service.FrameTypeDef.FrameGLLX;
import com.intput.Service.FrameTypeDef.FrameJD;
import com.intput.database.DataInfoDBHelper;
import com.intput.database.FileAccess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class InputBrower extends InputBase implements OnItemClickListener, 
		Button.OnClickListener{
	
	private final static String TAG = "InputBrower";
	private final static String ZZRQ_STRING = "ZZRQ";
	private final static int BROWER_LOAD = 0xF1;
	private final static int BROWER_NULLLOAD = 0xF2;
	private final static int BROWER_SEND_START = 0xF3;
	private final static int BROWER_SEND_STOP = 0xF4;
	private final static int BROWER_SHOW_DELECT = 0xF5;
	private final static int BROWER_CLEAR_LIST = 0xF6;
	private final static int BROWER_SEND_LIST = 0xF7;
	private final static int LISTWidth[] = {
		100, 240, 100,160};
	
	private ListCtrl mListView = null;
	private ListCtrl mListTop = null;
	private HorizontalScrollView mHorizontalScrollView = null;
	private DataInfoDBHelper mDataInfoDBHelper = null; 
	private ProgressDialog mProgressDialog; 
	private Button mNew = null;
	private Button mEdit = null;
	private Button mClear = null;
	private Button mTimer = null;
	private TextView mVERSION = null;
	private int mListSel = -1;
	private Handler mHandler;
	private String nZZRQ = null;
	private DatePickerDialog mDatePickerDialog = null;
	private ArrayList<String> mCQHList = null;	
	private boolean SendState = false;
	private CalculationHelper mCalculationHelper = null;
	private int mStart = 0;
	private int mEnd = 0;
	
	public InputBrower(Context context, String action, int Id, DataInfoDBHelper DB) {
		super(context, action, Id, DB);
		
		// TODO Auto-generated constructor stub
		((Activity) mContext).getLayoutInflater().inflate(
				R.layout.inputbrower, this);
		mDataInfoDBHelper = DB;
		mCQHList = new ArrayList<String>();
		mCalculationHelper = new CalculationHelper(mContext);
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.BrowerInfo);
		mListView = (ListCtrl)findViewById(R.id.BrowerCard);
		mListView.setOnItemClickListener(this);
		mListView.setBackgroundResource(R.drawable.listitem_select);

		mNew = (Button)findViewById(R.id.InputBrowerNew);
		mNew.setOnClickListener(this);
		mEdit = (Button)findViewById(R.id.InputBrowerEdit);
		mEdit.setOnClickListener(this);
		mClear = (Button)findViewById(R.id.InputBrowerClear);
		mClear.setOnClickListener(this);
		mTimer = (Button)findViewById(R.id.InputBrowerTime);
		mTimer.setOnClickListener(this);
		mVERSION = (TextView) findViewById(R.id.Text_VERSION);
		String Version = getAppInfo();
		mVERSION.setText(Version);
		
		mListTop = (ListCtrl)findViewById(R.id.BrowerTop);
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what){
				case BROWER_LOAD:
					Bundle b = msg.getData();
				    String zzrq = b.getString(ZZRQ_STRING);
				    ProgressDialogStart(R.string.MAIN_HITLOADING);
				    loadlist(zzrq);
				    ProgressDialogStop();
					break;
					
				case BROWER_NULLLOAD:
					ProgressDialogStart(R.string.MAIN_HITLOADING);
					loadlist(null);
					ProgressDialogStop();
					break;
					
				case BROWER_SEND_START:
					ProgressDialogStart(msg.arg1);
					break;
					
				case BROWER_SEND_STOP:
					ProgressDialogStop();
					break;	
					
				case BROWER_SHOW_DELECT:
					ShowDelectDialog();
					break;
					
				case BROWER_SEND_LIST:
					if(mCQHList.size() > 0 && SendState == false){
						ShowselectDialog();
					}
					break;
					
				case BROWER_CLEAR_LIST:
					if(mCQHList.size() > 0){
						clearList();
					}
					break;
				}
			}
			
		};
		loadtop();
	}
	
	private void ProgressDialogStart(int Id){
		String Loader = mContext.getResources().getString(Id);
		mProgressDialog = ProgressDialog.show(mContext, "", Loader);
	}
	
	private void ProgressDialogStop(){
		mProgressDialog.dismiss();
	}
	
	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub
		if(visibility == VISIBLE){
			nZZRQ = null;
			SendState = false;
			ReloadList();
		}
		super.setVisibility(visibility);
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

	public void loadtop(){
		int TextId[] = { 0, 0, 0, 0};
				
		String nString[] = {
			DataInfoDBHelper.TBL_GJH, 
			DataInfoDBHelper.TBL_GJMC,
			DataInfoDBHelper.TBL_QHDJ,
			DataInfoDBHelper.TBL_TIME, 
		};
		
		int nGravity[] = {
			Gravity.CENTER_VERTICAL | Gravity.CENTER,
			Gravity.CENTER_VERTICAL | Gravity.CENTER,
			Gravity.CENTER_VERTICAL | Gravity.CENTER,
			Gravity.CENTER_VERTICAL | Gravity.CENTER,
		};
				
		ListCtrlAdapter adapter = (ListCtrlAdapter) new ListCtrlAdapter(
				mContext);
		adapter.ListCtrlCreate(ListCtrlAdapter.ListType1, mListTop);
		adapter.ListCtrlAdd(TextId, nString, LISTWidth, nGravity);
		mListTop.setAdapter(adapter);
	}
	
	public void loadlist(String timer){
		int TextId[] = { 0, 0, 0, 0};
		String nString[] = {
			null, 
			null, 
			null,
			null,
		};
		
		int nGravity[] = {
			Gravity.CENTER_VERTICAL | Gravity.CENTER,
			Gravity.CENTER_VERTICAL | Gravity.CENTER,
			Gravity.CENTER_VERTICAL | Gravity.CENTER,
			Gravity.CENTER_VERTICAL | Gravity.CENTER,
		};
		HideProcDailog();
		ListCtrlAdapter adapter = (ListCtrlAdapter) new ListCtrlAdapter(
				mContext);
		adapter.ListCtrlCreate(ListCtrlAdapter.ListType2, mListView);
		Cursor mCursor = null;	
		if(timer != null)
		{
			mCursor = mDataInfoDBHelper.query(timer);
		}else{
			mCursor = mDataInfoDBHelper.query();
		}
		Resources res =getResources();
		int index = 0;
		String[] str = mContext.getResources().getStringArray(R.array.QHDJ);
		mCQHList.clear();
		mStart = 0;
		mEnd = 0;
		if(mCursor.getCount() > 0){
			mCursor.moveToLast();
    		do{
    			nString[0] = mCursor.getString(mCursor.getColumnIndex(DataInfoDBHelper.TBL_GJH));
    			if(mEnd == 0){
    				mEnd = Integer.valueOf(nString[0]);
    				mStart = mEnd;
    			}else{
    				mStart = Integer.valueOf(nString[0]);
    			}
    			nString[1] = mCursor.getString(mCursor.getColumnIndex(DataInfoDBHelper.TBL_GJMC));
    			nString[3] = mCursor.getString(mCursor.getColumnIndex(DataInfoDBHelper.TBL_TIME));
    			index = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_QHDJ));
    			nString[2] = str[index];
    			mCQHList.add(nString[0]);
    			adapter.ListCtrlAdd(TextId, nString, LISTWidth, nGravity);
    		}while(mCursor.moveToPrevious());
    	}else{
    		Toast.makeText(mContext, R.string.MAIN_HITLoadingEmpty, Toast.LENGTH_SHORT).show();
    	}
		if(!mCursor.isClosed())
    	{
			mCursor.close();
    	}
		mListView.setAdapter(adapter);
		mListSel = -1;
		HideProcDailog();
	}
	
	private void ShowProcDailog(){	
		mProgressDialog = ProgressDialog.show(mContext, "加载中", "Please wait...", true, false); 
	}
	
	private void HideProcDailog(){
		if(mProgressDialog != null){
			mProgressDialog.dismiss();
		}
	}	

	public void ReloadList(){
		LoadThread mLoadThread = new LoadThread(nZZRQ);
		mLoadThread.start();
	}

	private class LoadThread extends Thread{	
		private String mZZRQ = null;	
		public LoadThread(String zzrq){
			mZZRQ = zzrq;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Message m = new Message();
			if(mZZRQ == null){
				m.what = BROWER_NULLLOAD;
			}else{
				m.what = BROWER_LOAD;
				Bundle b = new Bundle();
				b.putString(ZZRQ_STRING, mZZRQ);
				m.setData(b);				
			}
			mHandler.sendMessage(m);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		mListView.clearFocus();  
		mListView.requestFocusFromTouch();
		mListSel = position;
	}
	
	public void ShowLogInDialog(){
		LayoutInflater factory = LayoutInflater.from(mContext);
		
		AlertDialog.Builder ADialog = new AlertDialog.Builder(mContext);
		ADialog.setCancelable(false);
		ADialog.setTitle(R.string.MAIN_HITDel);	
		ADialog.setNegativeButton(R.string.YES, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				String GJH = mCQHList.get(mListSel);
				mDataInfoDBHelper.delete(GJH);
				ReloadList();
			}
		});
		ADialog.setPositiveButton(R.string.NO, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		ADialog.show();
	}
	
	private void deleteList(){
		if(mListSel != -1){
			ShowLogInDialog();
		}
	}
	
	public void ShowClearDialog(){
		LayoutInflater factory = LayoutInflater.from(mContext);
		
		AlertDialog.Builder ADialog = new AlertDialog.Builder(mContext);
		ADialog.setCancelable(false);
		ADialog.setTitle(R.string.MAIN_HITClear);	
		ADialog.setNegativeButton(R.string.YES, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Message mMessage = new Message();
				mMessage.what =	BROWER_SHOW_DELECT;
				mHandler.sendMessage(mMessage);
			}
		});
		ADialog.setPositiveButton(R.string.NO, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		ADialog.show();
	}
	
	private void clearList(){
		ShowClearDialog();
	}
	
	class CalculationResult{
		int CQS;
		float[] CQData;
		float TH;
		float MaxCQData;
		float MinCQData;
		float CQAvg;
		float QDTU;
		float StandardDevition;
		public CalculationResult(int cqs){
			CQS = cqs;
			TH = 0;
			MaxCQData = 0;
			MinCQData = 0;
			CQAvg = 0;
			QDTU = 0;
			if(cqs > 0){
				CQData = new float[cqs];
			}
		}
	}
	
	public void CalculationCQ(CalculationResult mCalculationResult, byte[] CQData, int CQS, int FF, int BS, int GLLX){
		float[] tmp = new float[16];
		if (FF == FrameFF.FRAME_FF_HT && BS == FrameBS.FRAME_BS_YES){
			for (int i = 0; i < CQS; i++) {
				for (int j = 0; j < tmp.length; j++) {
					tmp[j] = (float) CQData[i*16+j];
				}
				mCalculationResult.CQData[i] = mCalculationHelper.getHTAndBS(tmp, mCalculationResult.TH);
			}
		}else if(FF == FrameFF.FRAME_FF_ZH){
				for (int i = 0; i < CQS; i++) {
					for (int j = 0; j < tmp.length; j++) {
						tmp[j] = (float) CQData[i*16+j];
					}
					mCalculationResult.CQData[i] = mCalculationHelper.getGQHT(tmp);
				}
		}else if (FF == FrameFF.FRAME_FF_HT && BS == FrameBS.FRAME_BS_NO && GLLX == FrameGLLX.FRAME_GLLX_SS){
			for (int i = 0; i < CQS; i++) {
				for (int j = 0; j < tmp.length; j++) {
					tmp[j] = (float) CQData[i*16+j];
				}
				mCalculationResult.CQData[i] = mCalculationHelper.getHTAndBSFAndSS(tmp, mCalculationResult.TH);
			}
		}else if (FF == FrameFF.FRAME_FF_HT && BS == FrameBS.FRAME_BS_NO && GLLX == FrameGLLX.FRAME_GLLX_LS){
			for (int i = 0; i < CQS; i++) {
				for (int j = 0; j < tmp.length; j++) {
					tmp[j] = (float) CQData[i*16+j];
				}
				mCalculationResult.CQData[i] = mCalculationHelper.getHTAndBSFAndLS(tmp, mCalculationResult.TH);
			}
		}
		
		for (int i = 0; i < mCalculationResult.CQS; i++) {
			Log.d(TAG,"mCalculationResult.CQData["+i+"] = "+mCalculationResult.CQData[i]);
		}
		
		Arrays.sort(mCalculationResult.CQData);
		mCalculationResult.CQAvg = mCalculationHelper.getDataAvg(mCalculationResult.CQData);
		mCalculationResult.StandardDevition = mCalculationHelper.getStandardDevition(mCalculationResult.CQData);

		if (FF == FrameFF.FRAME_FF_HT){
			mCalculationResult.QDTU = mCalculationHelper.getTDQDZAndHT(mCalculationResult.CQData, (byte)CQS);
		}else{
			mCalculationResult.QDTU = mCalculationHelper.getTDQDZAndGQHT(mCalculationResult.CQData,(byte)CQS);
		}
	}
	
	public CalculationResult GetCalculationResult(String GJH){
		CalculationResult mCalculationResult = null;
		Cursor mCursor = mDataInfoDBHelper.queryGJH(GJH);
		int CQS = 0;
		int FF = FrameFF.FRAME_FF_ZH;
		int GLLX = FrameGLLX.FRAME_GLLX_SS;
		int BS = FrameBS.FRAME_BS_NO;
		byte[] BTHdata = null;
		byte[] BData = null;
		float[] THdata = null;
		if(mCursor.getCount() > 0){
			while(mCursor.moveToNext()){
				 CQS = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_CQS));
				 FF = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_FF));
				 GLLX = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_GLLX));
				 BS = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_BS));
				 BTHdata = mCursor.getBlob(mCursor.getColumnIndex(DataInfoDBHelper.TBL_THDATA));
				 BData = mCursor.getBlob(mCursor.getColumnIndex(DataInfoDBHelper.TBL_DATA));
			}

			mCursor.close();
			mCalculationResult = new CalculationResult(CQS);
			
			THdata = new float[BTHdata.length];
			for (int i = 0; i < BTHdata.length; i++) {
				THdata[i] = Common.getTHData(BTHdata[i]);
			}
			mCalculationResult.TH = mCalculationHelper.getThArrayAvg(THdata);
			CalculationCQ(mCalculationResult, BData, CQS, FF, BS, GLLX);
		}
		return mCalculationResult;
	}
	
	public void ShowCalculationDialog(String GJH){
		CalculationResult mCalculationResult = GetCalculationResult(GJH);
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		View layout = inflater.inflate(R.layout.inputcalculation,(ViewGroup) findViewById(R.id.CalculationDialog));
		
		TextView mMINQD = (TextView) layout.findViewById(R.id.MINQD);
		TextView mAVGQD = (TextView) layout.findViewById(R.id.AVGQD);
		TextView mAVGTH = (TextView) layout.findViewById(R.id.AVGTH);
		TextView mBZC = (TextView) layout.findViewById(R.id.BZC);
		TextView mGJQD = (TextView) layout.findViewById(R.id.GJQD);
		TextView THSHOW = (TextView) layout.findViewById(R.id.Edit_THSHOW);  
		THSHOW.setText(R.string.MAIN_HITCQQD);
		TextView Text_Data1 = (TextView) layout.findViewById(R.id.Edit_TH_1);
		TextView Text_Data2 = (TextView) layout.findViewById(R.id.Edit_TH_2);
		TextView Text_Data3 = (TextView) layout.findViewById(R.id.Edit_TH_3);
		TextView Text_Data4 = (TextView) layout.findViewById(R.id.Edit_TH_4);
		TextView Text_Data5 = (TextView) layout.findViewById(R.id.Edit_TH_5);
		TextView Text_Data6 = (TextView) layout.findViewById(R.id.Edit_TH_6);
		TextView Text_Data7 = (TextView) layout.findViewById(R.id.Edit_TH_7);
		TextView Text_Data8 = (TextView) layout.findViewById(R.id.Edit_TH_8);
		TextView Text_Data9 = (TextView) layout.findViewById(R.id.Edit_TH_9);
		TextView Text_Data10 = (TextView) layout.findViewById(R.id.Edit_TH_10);
		TextView Text_Data11 = (TextView) layout.findViewById(R.id.Edit_TH_11);
		TextView Text_Data12 = (TextView) layout.findViewById(R.id.Edit_TH_12);
		TextView Text_Data13 = (TextView) layout.findViewById(R.id.Edit_TH_13);
		TextView Text_Data14 = (TextView) layout.findViewById(R.id.Edit_TH_14);
		TextView Text_Data15 = (TextView) layout.findViewById(R.id.Edit_TH_15);
		TextView Text_Data16 = (TextView) layout.findViewById(R.id.Edit_TH_16);
		
		String AVGQD = "";
		String AVGTH = "";
		String GJQD = "";
		String MINQD = "";
		String BZC = "";
		
		if(mCalculationResult != null){
			MINQD = mContext.getResources().getString(R.string.MAIN_HITMINQD)+": "+String.valueOf(mCalculationResult.CQData[0]);
			AVGQD = mContext.getResources().getString(R.string.MAIN_HITAVGQD)+": "+String.valueOf(mCalculationResult.CQAvg);
			AVGTH = mContext.getResources().getString(R.string.MAIN_HITAVGTH)+": "+String.valueOf(mCalculationResult.TH);
			GJQD = mContext.getResources().getString(R.string.MAIN_HITGJQDZ)+": ";
			BZC =  mContext.getResources().getString(R.string.MAIN_HITBZC)+": "+String.valueOf(mCalculationResult.StandardDevition);		
			if(mCalculationResult.QDTU == -1000){
				GJQD += " <10";
			}else{
				if(mCalculationResult.QDTU == -5000){
					GJQD += " <50";
				}else{
					GJQD += String.valueOf(mCalculationResult.QDTU);
				}
			}
		}
		
		mMINQD.setText(MINQD);
		mAVGQD.setText(AVGQD);
		mAVGTH.setText(AVGTH);
		mGJQD.setText(GJQD);
		mBZC.setText(BZC);
		TextView mTextView[] = {Text_Data1,Text_Data2,Text_Data3,Text_Data4,
				Text_Data5,Text_Data6,Text_Data7,Text_Data8,
				Text_Data9,Text_Data10,Text_Data11,Text_Data12,
				Text_Data13,Text_Data14,Text_Data15,Text_Data16,
				};
		for (int i = 0; i < 16; i++) {
			if(i < mCalculationResult.CQS){
				mTextView[i].setText(String.valueOf(mCalculationResult.CQData[i]));
			}else{
				mTextView[i].setText("--");
			}				
		}
		
		AlertDialog mAlertDialog = new AlertDialog.Builder(mContext)
		.setTitle("计算结果").setView(layout).create();
		Window lp = mAlertDialog.getWindow();
		lp.setLayout(150,500);
		mAlertDialog.show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.InputBrowerNew:
			onShowPage(MainActivity.ID_INPUTWRITE, -1);
			break;
		case R.id.InputBrowerEdit:
			if(mListSel != -1){
				String IndexStr = mCQHList.get(mListSel);
				int Index = Integer.valueOf(IndexStr);
				onShowPage(MainActivity.ID_INPUTWRITE, Index);	
			}
			break;
		case R.id.InputBrowerClear:
			deleteList();
			break;	
		case R.id.InputBrowerTime:
			if(mListSel != -1){
				String IndexStr = mCQHList.get(mListSel);
				ShowCalculationDialog(IndexStr);
			}
			break;
			
		}
	}
	
	public void gotoTimePage(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		mDatePickerDialog = new DatePickerDialog(mContext, 
                dateListener, 
                year, 
                month, 
                day);
		mDatePickerDialog.show();
	}
	
	DatePickerDialog.OnDateSetListener dateListener =  
		    new DatePickerDialog.OnDateSetListener(){

				@Override
				public void onDateSet(DatePicker arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					String data = String.format("%04d", arg1)+"-"+String.format("%02d", arg2+1)+"-"+String.format("%02d", arg3);
					nZZRQ = data;
					ReloadList();
				}
		
	};
	
	public void SetAlertDialog(DialogInterface arg0,boolean show){
		if(show){
			try {
				Field mField = arg0.getClass().getSuperclass().getDeclaredField("mShowing");
				mField.setAccessible(true);
				mField.set(arg0,true);
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				Field mField = arg0.getClass().getSuperclass().getDeclaredField("mShowing");
				mField.setAccessible(true);
				mField.set(arg0,false);
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void ShowselectDialog(){
		LayoutInflater factory = LayoutInflater.from(mContext);
		View LogView = factory.inflate(R.layout.inputclearlogin, null);
		
		final EditText mEditStart = (EditText) LogView.findViewById(R.id.EditStart);
		final EditText mEditEnd = (EditText) LogView.findViewById(R.id.EditEnd);
		mEditStart.setText(String.valueOf(mStart));
		mEditEnd.setText(String.valueOf(mEnd));

		AlertDialog.Builder ADialog = new AlertDialog.Builder(mContext);
		ADialog.setCancelable(false);
		ADialog.setView(LogView);		
		ADialog.setNegativeButton(R.string.NO , new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				SetAlertDialog(arg0,true);
			}
		});
		ADialog.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				String Start = mEditStart.getText().toString();
				String End = mEditEnd.getText().toString();
				String RealEnd = mDataInfoDBHelper.getlastGJH();
				int StartNum = Integer.valueOf(Start);
				int EndNum = Integer.valueOf(End);
				int EndReal = Integer.valueOf(RealEnd);
				
				if(StartNum > EndNum || EndReal < EndNum || EndReal < StartNum){
					Toast.makeText(mContext, R.string.MAIN_HITGJHERROR, Toast.LENGTH_SHORT).show();
				}else{
					String StartValue = "";
					if(Start.length() < 6){
						int len = 6 - Start.length();
						for (int i = 0; i< len; i++){
							StartValue = StartValue+"0";
						}
						Start =  StartValue + Start;
					}
					
					if(End.length() < 6){
						StartValue = "";
						int len = 6 - End.length();
						for (int i = 0; i< len; i++){
							StartValue = StartValue+"0";
						}
						End =  StartValue + End;
					}
					SendThread mSendThread = new SendThread(Start,End);
					mSendThread.start();
				}
			}
		});
		ADialog.show();
	}
	
	public void ShowDelectDialog(){
		LayoutInflater factory = LayoutInflater.from(mContext);
		View LogView = factory.inflate(R.layout.inputclearlogin, null);
		
		final EditText mEditStart = (EditText) LogView.findViewById(R.id.EditStart);
		final EditText mEditEnd = (EditText) LogView.findViewById(R.id.EditEnd);
		mEditStart.setText(String.valueOf(mStart));
		mEditEnd.setText(String.valueOf(mEnd));

		AlertDialog.Builder ADialog = new AlertDialog.Builder(mContext);
		ADialog.setCancelable(false);
		ADialog.setView(LogView);		
		ADialog.setNegativeButton(R.string.NO , new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				SetAlertDialog(arg0,true);
			}
		});
		ADialog.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				String Start = mEditStart.getText().toString();
				String End = mEditEnd.getText().toString();
				String RealEnd = mDataInfoDBHelper.getlastGJH();
				int StartNum = Integer.valueOf(Start);
				int EndNum = Integer.valueOf(End);
				int EndReal = Integer.valueOf(RealEnd);
				
				if(StartNum > EndNum || EndReal < EndNum || EndReal < StartNum){
					Toast.makeText(mContext, R.string.MAIN_HITGJHERROR, Toast.LENGTH_SHORT).show();
				}else{
					String StartValue = "";
					if(Start.length() < 6){
						int len = 6 - Start.length();
						for (int i = 0; i< len; i++){
							StartValue = StartValue+"0";
						}
						Start =  StartValue + Start;
					}
					
					if(End.length() < 6){
						StartValue = "";
						int len = 6 - End.length();
						for (int i = 0; i< len; i++){
							StartValue = StartValue+"0";
						}
						End =  StartValue + End;
					}

					DelectThread mDelectThread = new DelectThread(Start, End);
					mDelectThread.start();
				}
			}
		});
		ADialog.show();
	}
	
	private class SendThread extends Thread{	
		private String mStart = null;	
		private String mEnd = null;
		public SendThread(String start, String end){
			SendState = true;
			mStart = start;
			mEnd = end;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Cursor mCursor = mDataInfoDBHelper.query();	
			int nStart = Integer.valueOf(mStart);
			int nEnd = Integer.valueOf(mEnd);
			boolean head = true;
			String GJMC = "";
			String newSring = "";
			FileOutputStream outStream = null;
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");       
			String date = sDateFormat.format(new java.util.Date());    
//			if(mMainClient.getSendState())
			{
				if(mCursor.getCount() > 0){
					StringBuffer strbuffer = new StringBuffer();
					String[] CSMstr = mContext.getResources().getStringArray(R.array.CSM);
					String[] JDstr = mContext.getResources().getStringArray(R.array.JD);
					date += " "+mStart+"To"+mEnd;
					String Path = "/sdcard/"+date+".txt";
		    		File saveFile=new File(Path);
		    		try {
		    			outStream = new FileOutputStream(saveFile);	
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					newSring = String.valueOf(nEnd-nStart+1)+"\r\n";
					try {
						outStream.write(newSring.getBytes());
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}	
   				    GJMC = ""; 
   				 	date += " "+String.valueOf(nStart)+"To"+String.valueOf(nEnd);
					while(mCursor.moveToNext()){
						String GJH = mCursor.getString(mCursor.getColumnIndex(DataInfoDBHelper.TBL_GJH));
		    			 int nGJH = Integer.valueOf(GJH);
		    			 if(nGJH >= nStart && nGJH <= nEnd){
		    				 String GJMCtmp = mCursor.getString(mCursor.getColumnIndex(DataInfoDBHelper.TBL_GJMC));
		    				 if(GJMCtmp.length() == 0){
		    					 GJMCtmp = "无构件名称";
		    				 }
		 					 try {
		 						byte[] tmp = GJMCtmp.getBytes("gb2312");
								outStream.write(tmp);
							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}	
		 					 GJMC = "";
		    				 int CQS = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_CQS));
		    				 int CSM = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_CSM));
		    				 int JD = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_JD));
		    				 int FF = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_FF));
		    				 int BS = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_BS));
		    				 byte[] THdata = mCursor.getBlob(mCursor.getColumnIndex(DataInfoDBHelper.TBL_THDATA));
		    				 byte[] Data = mCursor.getBlob(mCursor.getColumnIndex(DataInfoDBHelper.TBL_DATA));
		    				 int Datalen = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_DATALEN));
		    				 byte[] Timer = mCursor.getBlob(mCursor.getColumnIndex(DataInfoDBHelper.TBL_TIMEBLOB));
		    				 //mMainClient.SendData(head, GJH, (byte)GQS, (byte)CSM, (byte)JD, (byte)FF, (byte)BS, Timer, Data, Datalen, THdata);
		    				 //head = false;
		    				 int index = Common.getCSMIndex(CSM);
		    				 int jdindex= Common.getJDIndex(JD);
//		    				 GJMC = " "+String.valueOf(CQS)+" "+CSMstr[index]+" "+String.valueOf(Common.getJDValueEx(JD))+"\r\n";
//		    				 try {
//								outStream.write(GJMC.getBytes());
//							} catch (IOException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}
		    				 GJMC = " "+String.valueOf(CQS)+" ";
		    				 try {
									outStream.write(GJMC.getBytes());
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}	
		    				 GJMC = CSMstr[index]+" ";
		    				 try {
									outStream.write(GJMC.getBytes("gb2312"));
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}	
		    				 GJMC = String.valueOf(Common.getJDValueEx(JD))+"\r\n";
		    				 try {
								outStream.write(GJMC.getBytes());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}		    				 
		    				 GJMC = "";
		    				 for (int i = 1; i < (16*CQS+1); i++) {
		    					 String DataStr = "";
		    					 if(Data[i-1] < 10){
		    						 DataStr += "0"+String.valueOf(Data[i-1]);
		    						 GJMC += DataStr;
		    					 }else{
		    						 GJMC = GJMC+String.valueOf(Data[i-1]); 
		    					 }
		    					 GJMC += " ";
		    					 if(i % 16 == 0){
		    						 try {
										outStream.write(GJMC.getBytes());
										outStream.write("\r\n".getBytes());
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		    						 
				    				 GJMC = "";
		    					 }
							}
		    			 }
					}
		    		//mMainClient.SendByte((byte) 0xFA);
		    		try {
						outStream.close();	
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
			}
			
			if(!mCursor.isClosed())
	    	{
				mCursor.close();
	    	}
			SendState = false;
		}
	}
	
	private class DelectThread extends Thread{	
		private String mStart = null;	
		private String mEnd = null;
		public DelectThread(String start, String end){
			mStart = start;
			mEnd = end;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Cursor mCursor = mDataInfoDBHelper.query();	
			int nStart = Integer.valueOf(mStart);
			int nEnd = Integer.valueOf(mEnd);
			if(mCursor.getCount() > 0){
				while(mCursor.moveToNext()){
					String GJH = mCursor.getString(mCursor.getColumnIndex(DataInfoDBHelper.TBL_GJH));
	    			 int nGJH = Integer.valueOf(GJH);
	    			 if(nGJH >= nStart && nGJH <= nEnd){
	    				 mDataInfoDBHelper.delete(GJH);
	    			 }
	    	    }
				mCursor.close();
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ReloadList();
			}
		}
	}
	
	@Override
	public void OnMenuEventReport(int param) {
		// TODO Auto-generated method stub
		switch(param){
		case R.string.MAIN_CLEAR:
			Message m = new Message();
			m.what = BROWER_CLEAR_LIST;
			mHandler.sendMessage(m);
			break;
			
		case R.string.MAIN_OUTPUT:
			Message ma = new Message();
			ma.what = BROWER_SEND_LIST;
			mHandler.sendMessage(ma);
			break;
			
		case R.string.MAIN_QUEUE:
			gotoTimePage();
			break;
		}
		super.OnMenuEventReport(param);
	}
	
	
}
