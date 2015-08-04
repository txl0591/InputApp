package com.input.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.inputapp.main.R;
import com.intput.Base.Common;
import com.intput.Service.FrameTypeDef.FrameBS;
import com.intput.Service.FrameTypeDef.FrameCSM;
import com.intput.Service.FrameTypeDef.FrameFF;
import com.intput.Service.FrameTypeDef.FrameGLLX;
import com.intput.Service.FrameTypeDef.FrameJD;
import com.intput.Service.FrameTypeDef.FrameQHDJ;
import com.intput.Service.FrameTypeDef.FrameTH;
import com.intput.database.DataInfoDBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class IntputWrite extends InputBase implements OnItemClickListener, Button.OnClickListener, EditText.OnFocusChangeListener{
	private final static String TAG = "IntputWrite"; 
	private final static int MAXDATA = 512;
	
	public final static int PAGETYPE_ADD = 0x01;
	public final static int PAGETYPE_EDIT = 0x02;
	
	private final static int PAGE_INFO = 0x0A;
	private final static int PAGE_CQ = 0x0B;
	
	private final static int DEFAULE_CQ = 10;
	
	private TextView TextGJMC = null;
	private EditText EditGJMC = null;
	
	private TextView TextGJH = null;
	private EditText EditGJH = null;
	private TextView Text_CQS = null;
	private EditText Edit_CQS = null;
		
	private TextView Text_CSM = null;
	private TextView Text_JD = null;
	private TextView Text_FF = null;
	private TextView Text_BS = null;
	private TextView Text_QHDJ = null;
	private TextView Text_GLLX = null;
	
	private TextView Edit_CSM = null;
	private TextView Edit_JD = null;
	private TextView Edit_FF = null;
	private TextView Edit_BS = null;
	private TextView Edit_QHDJ = null;
	private TextView Edit_GLLX = null;
	
	private TextView Edit_CQSSHOW = null;
	private EditText Edit_Data1 = null;
	private EditText Edit_Data2 = null;
	private EditText Edit_Data3 = null;
	private EditText Edit_Data4 = null;
	private EditText Edit_Data5 = null;
	private EditText Edit_Data6 = null;
	private EditText Edit_Data7 = null;
	private EditText Edit_Data8 = null;
	private EditText Edit_Data9 = null;
	private EditText Edit_Data10 = null;
	private EditText Edit_Data11 = null;
	private EditText Edit_Data12 = null;
	private EditText Edit_Data13 = null;
	private EditText Edit_Data14 = null;
	private EditText Edit_Data15 = null;
	private EditText Edit_Data16 = null;
	
	private TextView Edit_THSHOW = null;
	private TextView Text_Data1 = null;
	private TextView Text_Data2 = null;
	private TextView Text_Data3 = null;
	private TextView Text_Data4 = null;
	private TextView Text_Data5 = null;
	private TextView Text_Data6 = null;
	private TextView Text_Data7 = null;
	private TextView Text_Data8 = null;
	private TextView Text_Data9 = null;
	private TextView Text_Data10 = null;
	private TextView Text_Data11 = null;
	private TextView Text_Data12 = null;
	private TextView Text_Data13 = null;
	private TextView Text_Data14 = null;
	private TextView Text_Data15 = null;
	private TextView Text_Data16 = null;
	private Button mExit = null;
	
	private Button mSave = null;
	private byte mNowCQS = 1;
	private byte mMaxCQS = 16;
	
	private byte mCSM = FrameCSM.FRAME_CSM_S;
	private byte mJD = FrameJD.FRAME_JD_0;
	private byte mFF = FrameFF.FRAME_FF_HT;
	private byte mBS = FrameBS.FRAME_BS_NO;
	private byte mQHDJ = FrameQHDJ.FRAME_QHDJ_C30;
	private byte mGLLX = FrameGLLX.FRAME_GLLX_LS;
	
	private byte[] mData;
	private byte[] mDataTH;
	private TextView AvtiveEdit = null;
	private PopupWindow mPopupWindow = null;
	private ListView mListView = null;
	private ArrayList<String> mPopArrayData = new ArrayList<String>();
	private ArrayList<EditText> mEditList = new ArrayList<EditText>();
	private ArrayList<TextView> mTextList = new ArrayList<TextView>();
	private DataInfoDBHelper mDataInfoDBHelper = null;
	private int mPageType = PAGETYPE_EDIT;
	private int mPageMode = PAGE_INFO;
	private String mEditGJH = null;
	private Button mLast = null;
	private Button mNext = null;
	private int mActiveId = -1;
	private boolean mEditOper = false;
	
	public IntputWrite(Context context, String action, int Id, DataInfoDBHelper DB) {
		super(context, action, Id, DB);
		// TODO Auto-generated constructor stub
		((Activity) mContext).getLayoutInflater().inflate(
				R.layout.inputwrite, this);
		
		mDataInfoDBHelper = DB;
		mData = new byte[MAXDATA];
		mDataTH = new byte[16];

		TextGJMC = (TextView) findViewById(R.id.Text_GJMC);
		EditGJMC = (EditText) findViewById(R.id.Edit_GJMC);
		
		TextGJH = (TextView) findViewById(R.id.Text_GJH);
		EditGJH = (EditText) findViewById(R.id.Edit_GJH);
		EditGJH.setText("000000");
		EditGJH.setEnabled(false);
		EditGJH.setTextColor(Color.BLACK);
		
		Text_CQS = (TextView) findViewById(R.id.Text_CQS);
		Edit_CQS = (EditText) findViewById(R.id.Edit_CQS);
		Edit_CQS.setOnClickListener(this);
		Edit_CQS.setInputType(InputType.TYPE_NULL);
		Edit_CQS.setText("10");
		Edit_CQS.setTextColor(Color.BLACK);
		
		Text_CSM = (TextView) findViewById(R.id.Text_CSM);
		Edit_CSM = (TextView) findViewById(R.id.Edit_CSM);
		Edit_CSM.setOnClickListener(this);
		Edit_CSM.setText("侧面");
		Edit_CSM.setTextColor(Color.BLACK);
		
		Text_JD = (TextView) findViewById(R.id.Text_JD);
		Edit_JD = (TextView) findViewById(R.id.Edit_JD);
		Edit_JD.setOnClickListener(this);
		Edit_JD.setText("水平0度");
		Edit_JD.setTextColor(Color.BLACK);
		
		Text_FF = (TextView) findViewById(R.id.Text_FF);
		Edit_FF = (TextView) findViewById(R.id.Edit_FF);
		Edit_FF.setOnClickListener(this);
		Edit_FF.setText("回弹");
		
		Text_BS = (TextView) findViewById(R.id.Text_BS);
		Edit_BS = (TextView) findViewById(R.id.Edit_BS);
		Edit_BS.setOnClickListener(this);
		Edit_BS.setText("非");
		Edit_BS.setTextColor(Color.BLACK);
		
		Text_QHDJ = (TextView) findViewById(R.id.Text_QHDJ);
		Edit_QHDJ = (TextView) findViewById(R.id.Edit_QHDJ);
		Edit_QHDJ.setOnClickListener(this);
		Edit_QHDJ.setText("C30");
		Edit_QHDJ.setTextColor(Color.BLACK);
		
		Text_GLLX = (TextView) findViewById(R.id.Text_GLLX);
		Edit_GLLX = (TextView) findViewById(R.id.Edit_GLLX);
		Edit_GLLX.setOnClickListener(this);
		Edit_GLLX.setText("碎石");
		Edit_GLLX.setTextColor(Color.BLACK);
		
		Edit_CQSSHOW = (TextView) findViewById(R.id.Edit_CQSSHOW);
		Edit_CQSSHOW.setText("");
		Edit_CQSSHOW.setTextColor(Color.BLACK);
		mLast = (Button) findViewById(R.id.Last);
		mLast.setOnClickListener(this);
		mNext = (Button) findViewById(R.id.Next);
		mNext.setOnClickListener(this);
		mExit = (Button) findViewById(R.id.Exit);
		mExit.setOnClickListener(this);
		
		
		
		Edit_THSHOW = (TextView) findViewById(R.id.Edit_THSHOW);
		String NowCQS = "碳化值";
		Edit_THSHOW.setText(NowCQS);
		Edit_THSHOW.setTextColor(Color.BLACK);
		
		int CtrlId[] = {R.id.Edit_1,R.id.Edit_2,R.id.Edit_3,R.id.Edit_4,
				R.id.Edit_5,R.id.Edit_6,R.id.Edit_7,R.id.Edit_8,
				R.id.Edit_9,R.id.Edit_10,R.id.Edit_11,R.id.Edit_12,
				R.id.Edit_13,R.id.Edit_14,R.id.Edit_15,R.id.Edit_16,
				};
		EditText mEditText[] = {Edit_Data1,Edit_Data2,Edit_Data3,Edit_Data4,
				Edit_Data5,Edit_Data6,Edit_Data7,Edit_Data8,
				Edit_Data9,Edit_Data10,Edit_Data11,Edit_Data12,
				Edit_Data13,Edit_Data14,Edit_Data15,Edit_Data16,
				};
		
		int HTId[] = {R.id.Edit_TH_1,R.id.Edit_TH_2,R.id.Edit_TH_3,R.id.Edit_TH_4,
				R.id.Edit_TH_5,R.id.Edit_TH_6,R.id.Edit_TH_7,R.id.Edit_TH_8,
				R.id.Edit_TH_9,R.id.Edit_TH_10,R.id.Edit_TH_11,R.id.Edit_TH_12,
				R.id.Edit_TH_13,R.id.Edit_TH_14,R.id.Edit_TH_15,R.id.Edit_TH_16,
				};
		
		TextView mTextView[] = {Text_Data1,Text_Data2,Text_Data3,Text_Data4,
				Text_Data5,Text_Data6,Text_Data7,Text_Data8,
				Text_Data9,Text_Data10,Text_Data11,Text_Data12,
				Text_Data13,Text_Data14,Text_Data15,Text_Data16,
				};
		
		TextWatcher mTextWatcher = new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if(s.length() == 2){
					mEditOper = true;
					int done_input_id = ((Activity) mContext).getCurrentFocus().getId();
					mActiveId = done_input_id;
					int index = (mNowCQS-1)*16;	
					for (int i = 0; i < mEditList.size(); i++){
						if (mEditList.get(i).getId() == done_input_id) {
							EditText next_edit = mEditList.get(i);
							String str = next_edit.getText().toString();
							if(str != null && str.length() > 0){
								mData[index+i] = Byte.valueOf(str); 
								Log.d(TAG,"mData aa ["+(index+i)+"] =["+mData[index+i]+"]");
								if(done_input_id < R.id.Edit_16){
									int next_index = i + 1;
									next_edit = mEditList.get(next_index);
									next_edit.requestFocus();
								}
							}
							
						}
					}
				}else if(s.length() == 0){
					mEditOper = true;
					View mActivity = ((Activity) mContext).getCurrentFocus();
					if(mActivity != null){
						int done_input_id = mActivity.getId();
						int index = (mNowCQS-1)*16;	
						for (int i = 0; i < mEditList.size(); i++){
							if (mEditList.get(i).getId() == done_input_id) {
								mData[index+i] = 0; 
								if(done_input_id > R.id.Edit_1){
									int next_index = i - 1;
									EditText next_edit = mEditList.get(next_index);
									next_edit.requestFocus();
								}
							}
						}
					}
					
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub				
			}
		};
		
		for (int i = 0; i < 16; i++){
			mEditText[i] = (EditText) findViewById(CtrlId[i]); 
			mEditText[i].setText("");
			mEditList.add(mEditText[i]);
			mEditText[i].addTextChangedListener(mTextWatcher);
			mEditText[i].setOnFocusChangeListener(this);
		}
		
		for (int i = 0; i < 16; i++){
			mTextView[i] = (TextView) findViewById(HTId[i]); 
			mTextView[i].setOnClickListener(this);
			mTextView[i].setText("0.0");
			mTextView[i].setVisibility(INVISIBLE);
			mTextList.add(mTextView[i]);
		}
		
		mSave = (Button) findViewById(R.id.Save);
		mSave.setOnClickListener(this);
		
		View contentView = ((Activity) mContext).getLayoutInflater().inflate(
				R.layout.intputpopwindow, null);
		
		mListView = (ListView) contentView.findViewById(R.id.PopList);
		ArrayAdapter<String> mApAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,mPopArrayData);
		mListView.setAdapter(mApAdapter);
		mListView.setOnItemClickListener(this);
		mPopupWindow = new PopupWindow(contentView, 200, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);
		mNowCQS = 1;
	}

	private void loaddefaultdata(){
		EditGJMC.setText("");
		mActiveId = -1;
		mNowCQS = 1;
		mMaxCQS = DEFAULE_CQ;
		mCSM = FrameCSM.FRAME_CSM_S;
		mJD = FrameJD.FRAME_JD_0;
		mFF = FrameFF.FRAME_FF_HT;
		mBS = FrameBS.FRAME_BS_YES;
		mQHDJ = FrameQHDJ.FRAME_QHDJ_C30;
		mGLLX = FrameGLLX.FRAME_GLLX_SS;
		for(int i = 0; i < mData.length; i++){
			mData[i]  = 0x00;
		}
		for(int i = 0; i < 16; i++){
			mDataTH[i] = 0x00;
		}
	}
	
	public void setPageType(String GJH){
		if(GJH != null){
			mPageType = PAGETYPE_EDIT;
			mEditGJH = GJH;
		}else{
			mPageType = PAGETYPE_ADD;
		}
	}
	
	public void ShowCQS(){
		String NowCQS = "测区"+String.valueOf(mNowCQS);
		Edit_CQSSHOW.setText(NowCQS);
	}
	
	private String getCJH(){
		String date = "";
		if(mPageType == PAGETYPE_ADD){
			String str = mDataInfoDBHelper.getlastGJH();
			int tmp = Integer.valueOf(str);
			tmp++;
			String indexstr = String.valueOf(tmp);
			if(indexstr.length() < 6){
				int len = 6-indexstr.length();
				for(int i = 0; i < len; i++){
					date = date+"0";
				}
				date = date+indexstr;
			}else{
				date = indexstr;
			}
		}else{
			int dataindex = Integer.valueOf(mEditGJH);
			date = String.valueOf(dataindex);
		}
		return date;
	}
		
	public void loaddefaultdatafromdb(){
		Cursor mCursor = mDataInfoDBHelper.queryGJH(mEditGJH);
		Resources res =getResources();
		int index = 0;
		if(mCursor.getCount() > 0){
    		while(mCursor.moveToNext()){
    			String GJMC = mCursor.getString(mCursor.getColumnIndex(DataInfoDBHelper.TBL_GJMC));
    			EditGJMC.setText(GJMC);
    			mMaxCQS = (byte) mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_CQS));
    			mCSM = (byte) mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_CSM));
    			mFF = (byte) mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_FF));
    			mBS = (byte) mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_BS));
    			mJD = (byte) mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_JD));
    			mQHDJ = (byte) mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_QHDJ));
    			mGLLX = (byte) mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper.TBL_GLLX));
    			byte[] databyte = mCursor.getBlob(mCursor.getColumnIndex(DataInfoDBHelper.TBL_DATA));
    			for(int i = 0; i < databyte.length; i++){
    				mData[i]  = databyte[i];
    			}
    			byte[] datath = mCursor.getBlob(mCursor.getColumnIndex(DataInfoDBHelper.TBL_THDATA));
    			for(int i = 0; i < datath.length; i++){
    				mDataTH[i]  = datath[i];
    			}
    		}
    	}
		if(null != mCursor){
			mCursor.close();
		}
	}
		
	public void loaddefaultpage(){
		int DefaultCQS = DEFAULE_CQ;
		if(mPageType == PAGETYPE_ADD)
		{
			loaddefaultdata();	
			mEditGJH = getCJH();
		}
		else{
			loaddefaultdatafromdb();
			DefaultCQS = mMaxCQS;
		}
		EditGJH.setText(mEditGJH);
		Edit_CQS.setText(String.valueOf(DefaultCQS));
		
		String[] str = mContext.getResources().getStringArray(R.array.CQS);	
		int index = Common.getCSMIndex(mCSM);
		if(index < str.length && index != -1){
			Edit_CSM.setText(str[index]);
		}
		
		str = mContext.getResources().getStringArray(R.array.BS);
		index = Common.getBSIndex(mBS);
		if(index < str.length&& index != -1){
			Edit_BS.setText(str[index]);
		}

		str = mContext.getResources().getStringArray(R.array.FF);
		index = Common.getFFIndex(mFF);
		if(index < str.length && index != -1){
			Edit_FF.setText(str[index]);
		}
		
		str = mContext.getResources().getStringArray(R.array.JD);
		index = Common.getJDIndex(mJD);
		if(index < str.length && index != -1){
			Edit_JD.setText(str[index]);
		}
		
		str = mContext.getResources().getStringArray(R.array.CSM);
		index = Common.getCSMIndex(mCSM);
		if(index < str.length && index != -1){
			Edit_CSM.setText(str[index]);
		}	
		
		str = mContext.getResources().getStringArray(R.array.QHDJ);
		index = Common.getQHDJIndex(mQHDJ);
		if(index < str.length && index != -1){
			Edit_QHDJ.setText(str[index]);
		}
		
		str = mContext.getResources().getStringArray(R.array.GLLX);
		index = Common.getGLLXIndex(mGLLX);
		if(index < str.length && index != -1){
			Edit_GLLX.setText(str[index]);
		}
		loadcq();
		loadth();
		showthpage();
	}
	
	public void loadcq(){
		int base1 = (mNowCQS-1)*16;
		for(int k = 0; k < 16; k++){
			EditText mEditText =  mEditList.get(k);
			int index = base1+k;
			int data = mData[index];
			mEditText.setText(String.valueOf(data));
		}
		ShowCQS();
	}
	
	public void loadth(){		
		for (int i = 0; i < 16; i++){
			TextView mTextView = mTextList.get(i);
			byte data = mDataTH[i];
			int index = Common.getTHIndex(data);
			String[] str = mContext.getResources().getStringArray(R.array.TH);
			if(index != -1 && index < str.length){
				if(str[index].equals("0.0")){
					mTextView.setText("/");
				}else{
					mTextView.setText(str[index]);
				}
			}
			if(i < mMaxCQS){
				mTextView.setVisibility(VISIBLE);
			}
			else
			{
				mTextView.setVisibility(INVISIBLE);
			}
		}
	}
	
	public void gotolastpage(){
		if(mNowCQS > 0)
		{
			mNowCQS--;
		}
		if(0 == mNowCQS){
			mNowCQS =  mMaxCQS;
		}
		for (int i = 0; i < mEditList.size(); i++) {
			EditText edit = mEditList.get(i);
			edit.clearFocus();
		}
		loadcq();
		EditText next_edit = mEditList.get(0);
		next_edit.requestFocus();
		next_edit.selectAll();
		mActiveId = next_edit.getId();
	}
	
	public void gotonextpage(){
		mNowCQS++;
		if(mNowCQS > mMaxCQS){
			mNowCQS =  1;
		}
		for (int i = 0; i < mEditList.size(); i++) {
			EditText edit = mEditList.get(i);
			edit.clearFocus();
		}
		loadcq();
		EditText next_edit = mEditList.get(0);
		next_edit.requestFocus();
		next_edit.selectAll();
		mActiveId = next_edit.getId();
	}
	
	public void setpagemode(int page){ 
		if(-1 != page){
			mPageMode = page;
		}
		switch(mPageMode)
		{
		case PAGE_INFO:
			loaddefaultpage();
			break;
		case PAGE_CQ:
			break;
		}
	}

	public void showthpage(){
		if(mFF == FrameFF.FRAME_FF_HT){
			for (int i = 0; i < 16; i++){
				TextView mTextView = mTextList.get(i);
				if(i < mMaxCQS){
					mTextView.setVisibility(VISIBLE);
				}
				else
				{
					mTextView.setVisibility(INVISIBLE);
				}
			}
			Edit_THSHOW.setVisibility(VISIBLE);
		}else{
			for (int i = 0; i < 16; i++){
				TextView mTextView = mTextList.get(i);
				mTextView.setVisibility(INVISIBLE);
			}
			Edit_THSHOW.setVisibility(INVISIBLE);
		}
	}
		
	
	private void SaveParam(){
		String mFrameGJH =  EditGJH.getText().toString();
		String mGJMC = EditGJMC.getText().toString();
		if(null == mGJMC){
			mGJMC = "构件";
		}
		mPageMode = PAGE_CQ;
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String date = sDateFormat.format(new java.util.Date());  
		
		SimpleDateFormat sDateFormat1 = new SimpleDateFormat("yyyy-MM-dd"); 
		String date1 = sDateFormat1.format(new java.util.Date());  
		Date datefmt;
		byte[] dategrp = new byte[6];
		try {
			datefmt = sDateFormat.parse(date);
			dategrp[0] = (byte) (datefmt.getYear()%10);
			dategrp[1] = (byte) datefmt.getMonth();
			dategrp[2] = (byte) datefmt.getDay();
			dategrp[3] = (byte) datefmt.getHours();
			dategrp[4] = (byte) datefmt.getMinutes();
			dategrp[5] = (byte) datefmt.getSeconds();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		if(mPageType == PAGETYPE_ADD)
		{
			mDataInfoDBHelper.addrecord(date1,mFrameGJH, mGJMC, mMaxCQS, mCSM, mJD, mFF, mBS, mGLLX, mQHDJ, mMaxCQS*16, dategrp, mData, mDataTH);
//			loaddefaultpage();
		}else{
			mDataInfoDBHelper.changedata(date1,mFrameGJH, mGJMC, mCSM, mJD, mFF, mBS, mGLLX, mQHDJ, mMaxCQS*16, dategrp, mData, mDataTH);
		}
		Toast.makeText(mContext, R.string.MAIN_SAVEOK, Toast.LENGTH_SHORT).show();
		onHidePage();
	}

	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub
		if(VISIBLE == visibility){
			mEditOper = false;
			setpagemode(PAGE_INFO);
		}
		super.setVisibility(visibility);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		int FrameJDList[] = {FrameJD.FRAME_JD_90F,
				FrameJD.FRAME_JD_60F,
				FrameJD.FRAME_JD_45F,
				FrameJD.FRAME_JD_30F,
				FrameJD.FRAME_JD_0,
				FrameJD.FRAME_JD_30Z,
				FrameJD.FRAME_JD_45Z,
				FrameJD.FRAME_JD_60Z,
				FrameJD.FRAME_JD_90Z,
				};
		 
		int FrameFFList[] = {FrameFF.FRAME_FF_HT,FrameFF.FRAME_FF_ZH};
		int FrameBSList[] = {FrameBS.FRAME_BS_YES,FrameBS.FRAME_BS_NO};
		int FrameTHList[] = {
				FrameTH.FRAME_TH_00,
				FrameTH.FRAME_TH_05,
				FrameTH.FRAME_TH_10,
				FrameTH.FRAME_TH_15,
				FrameTH.FRAME_TH_20,
				FrameTH.FRAME_TH_25,
				FrameTH.FRAME_TH_30,
				FrameTH.FRAME_TH_35,
				FrameTH.FRAME_TH_40,
				FrameTH.FRAME_TH_45,
				FrameTH.FRAME_TH_50,
				FrameTH.FRAME_TH_55,
				FrameTH.FRAME_TH_60,
				};
		int FrameGLLXList[] = {
				FrameGLLX.FRAME_GLLX_SS,
				FrameGLLX.FRAME_GLLX_LS,
		};
		int FrameQHDJList[] = {
				FrameQHDJ.FRAME_QHDJ_C10,
				FrameQHDJ.FRAME_QHDJ_C15,
				FrameQHDJ.FRAME_QHDJ_C20,
				FrameQHDJ.FRAME_QHDJ_C25,
				FrameQHDJ.FRAME_QHDJ_C30,
				FrameQHDJ.FRAME_QHDJ_C35,
				FrameQHDJ.FRAME_QHDJ_C40,
				FrameQHDJ.FRAME_QHDJ_C45,
				FrameQHDJ.FRAME_QHDJ_C50,
				FrameQHDJ.FRAME_QHDJ_C55,
				FrameQHDJ.FRAME_QHDJ_C60,
				FrameQHDJ.FRAME_QHDJ_C65,
				FrameQHDJ.FRAME_QHDJ_C70,
				FrameQHDJ.FRAME_QHDJ_C75,
				FrameQHDJ.FRAME_QHDJ_C80,
				FrameQHDJ.FRAME_QHDJ_C85,
				FrameQHDJ.FRAME_QHDJ_C90,
		};
		AvtiveEdit.setText(mPopArrayData.get(position));
		switch(AvtiveEdit.getId()){
		case R.id.Edit_CQS:
			String CQS = Edit_CQS.getText().toString();
			mMaxCQS = Byte.valueOf(CQS);
			loadth();	
			break;
			
		case R.id.Edit_JD:
			mJD = (byte)(FrameJDList[position]&0xFF);
			break;
			
		case R.id.Edit_FF:
			mFF = (byte)(FrameFFList[position]&0xFF);
			showthpage();
			break;
			
		case R.id.Edit_BS:
			mBS = (byte)(FrameBSList[position]&0xFF);
			break;
			
		case R.id.Edit_QHDJ:
			mQHDJ = (byte)(FrameQHDJList[position]&0xFF);
			break;
				
		case R.id.Edit_GLLX:
			mGLLX = (byte)(FrameGLLXList[position]&0xFF);
			break;
	
		case R.id.Edit_TH_1:
			mDataTH[0] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_2:	
			mDataTH[1] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_3:	
			mDataTH[2] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_4:
			mDataTH[3] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_5:	
			mDataTH[4] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_6:
			mDataTH[5] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_7:
			mDataTH[6] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_8:
			mDataTH[7] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_9:
			mDataTH[8] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_10:
			mDataTH[9] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_11:
			mDataTH[10] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_12:
			mDataTH[11] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_13:
			mDataTH[12] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_14:
			mDataTH[13] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_15:
			mDataTH[14] = (byte)(FrameTHList[position]&0xFF);
			break;
		case R.id.Edit_TH_16:
			mDataTH[15] = (byte)(FrameTHList[position]&0xFF);
			break;	
		}
		mPopupWindow.dismiss();
	}
	
	public void showpopwindow(View V){
		String[] str = null;
		mPopArrayData.clear();
		InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		switch(V.getId()){
		case R.id.Edit_CQS:
			AvtiveEdit = Edit_CQS;
			str = mContext.getResources().getStringArray(R.array.CQS);
			break;
			
		case R.id.Edit_BS:
			AvtiveEdit = Edit_BS;
			str = mContext.getResources().getStringArray(R.array.BS);
			break;
		case R.id.Edit_FF:
			AvtiveEdit = Edit_FF;
			str = mContext.getResources().getStringArray(R.array.FF);
			break;
		case R.id.Edit_JD:
			AvtiveEdit = Edit_JD;
			str = mContext.getResources().getStringArray(R.array.JD);
			break;
		case R.id.Edit_CSM:	
			AvtiveEdit = Edit_CSM;
			str = mContext.getResources().getStringArray(R.array.CSM);
			break;
		case R.id.Edit_QHDJ:	
			AvtiveEdit = Edit_QHDJ;
			str = mContext.getResources().getStringArray(R.array.QHDJ);
			break;
		case R.id.Edit_GLLX:	
			AvtiveEdit = Edit_GLLX;
			str = mContext.getResources().getStringArray(R.array.GLLX);
			break;	
		case R.id.Edit_TH_1:
			AvtiveEdit = mTextList.get(0);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_2:	
			AvtiveEdit = mTextList.get(1);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_3:	
			AvtiveEdit = mTextList.get(2);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_4:
			AvtiveEdit = mTextList.get(3);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_5:	
			AvtiveEdit = mTextList.get(4);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_6:	
			AvtiveEdit = mTextList.get(5);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_7:	
			AvtiveEdit = mTextList.get(6);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_8:	
			AvtiveEdit = mTextList.get(7);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_9:	
			AvtiveEdit = mTextList.get(8);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_10:	
			AvtiveEdit = mTextList.get(9);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_11:	
			AvtiveEdit = mTextList.get(10);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_12:
			AvtiveEdit = mTextList.get(11);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_13:	
			AvtiveEdit = mTextList.get(12);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_14:	
			AvtiveEdit = mTextList.get(13);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_15:	
			AvtiveEdit = mTextList.get(14);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
		case R.id.Edit_TH_16:
			AvtiveEdit = mTextList.get(15);
			str = mContext.getResources().getStringArray(R.array.TH);
			break;
			
		}
		imm.hideSoftInputFromWindow(AvtiveEdit.getWindowToken(), 0);
		
		for(int i = 0; i < str.length; i++){
			mPopArrayData.add(str[i]);
		}
		
		ArrayAdapter<String> mApAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,mPopArrayData);
		mListView.setAdapter(mApAdapter);
		mPopupWindow.setWidth(AvtiveEdit.getWidth());
		mPopupWindow.showAsDropDown(AvtiveEdit);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.Edit_BS:
		case R.id.Edit_FF:
		case R.id.Edit_JD:	
		case R.id.Edit_CSM:
		case R.id.Edit_QHDJ:
		case R.id.Edit_GLLX:	
			if(mPageMode == PAGE_INFO){
				mEditOper = true;
				showpopwindow(v);
			}
			break;
		case R.id.Edit_CQS:	
			if(mPageMode == PAGE_INFO && mPageType == PAGETYPE_ADD){
//				showpopwindow(v);
				InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
				ShowCQSInDialog();
			}
			break;

		case R.id.Edit_TH_1:	
		case R.id.Edit_TH_2:	
		case R.id.Edit_TH_3:	
		case R.id.Edit_TH_4:
		case R.id.Edit_TH_5:	
		case R.id.Edit_TH_6:	
		case R.id.Edit_TH_7:	
		case R.id.Edit_TH_8:	
		case R.id.Edit_TH_9:	
		case R.id.Edit_TH_10:	
		case R.id.Edit_TH_11:	
		case R.id.Edit_TH_12:
		case R.id.Edit_TH_13:	
		case R.id.Edit_TH_14:	
		case R.id.Edit_TH_15:	
		case R.id.Edit_TH_16:
			if(mPageMode == PAGE_CQ){
				mEditOper = true;
				showpopwindow(v);
			}
			break;
			
		case R.id.Save:
			SaveParam();
			break;
			
		case R.id.Last:
			gotolastpage();
			break;
			
		case R.id.Next:
			gotonextpage();
			break;
			
		case R.id.Exit:
			if(mEditOper){
				ShowLogInDialog();
			}else{
				onHidePage();
			}
			break;
			
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(mPageMode == PAGE_INFO && hasFocus == true){
			mPageMode = PAGE_CQ;
		}
	}
	
	private void setrandom(){
		EditText next_edit = mEditList.get(4);
		String Name =  next_edit.getText().toString();
		int all = 0;
		if(Name != null && Name.length() >= 1){
			int base = (mNowCQS-1)*16;
			int text = Integer.valueOf(Name);
			if(text > 10){
				for (int j = 0; j < 16; j++){
					EditText mEditText =  mEditList.get(j);
					mData[base] = (byte) ((byte) (-4+8*Math.random())+(-4+8*Math.random())+text);
					int value = mData[base];
					base++;				
					mEditText.setText(String.valueOf(value));
				}
			}
		}
	}
	
	@Override
	public void OnMenuEventReport(int param) {
		// TODO Auto-generated method stub
		if(param == R.string.MAIN_RANDOM){
			setrandom(); 
		}
		super.OnMenuEventReport(param);
	}
	
	
	public void ShowLogInDialog(){
		LayoutInflater factory = LayoutInflater.from(mContext);
		AlertDialog.Builder ADialog = new AlertDialog.Builder(mContext);
		ADialog.setCancelable(false);
		ADialog.setTitle(R.string.MAIN_HITNOSAVE);	
		ADialog.setNegativeButton(R.string.OK , new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				SaveParam();
				onHidePage();
			}
		});
		ADialog.setPositiveButton(R.string.EXIT, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				onHidePage();
			}
		});
		ADialog.show();
	} 
	
	public void ShowCQSInDialog(){
		LayoutInflater factory = LayoutInflater.from(mContext);
		View LogView = factory.inflate(R.layout.inputlogin, null);
		
		final EditText mEditReg = (EditText) LogView.findViewById(R.id.EditReg);
		final TextView mUserMac = (TextView) LogView.findViewById(R.id.UserMac);
		mEditReg.setInputType(InputType.TYPE_CLASS_NUMBER);
		String ret = getResources().getString(R.string.MAIN_INPUTCQS);
		mUserMac.setText(ret);
		AlertDialog.Builder ADialog = new AlertDialog.Builder(mContext);
		ADialog.setCancelable(false);
		ADialog.setView(LogView);		
		ADialog.setNegativeButton(R.string.NO , new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
			}
		});
		ADialog.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  	
				String CQS = mEditReg.getText().toString();
				if(CQS != null && CQS.length() > 0){
					mMaxCQS = Byte.valueOf(CQS);
					if(mMaxCQS > 16)
					{
						mMaxCQS = 16;
					}else{
						if(mMaxCQS < 1){
							mMaxCQS = 1;
						}
					}
					mNowCQS =  1;
					Edit_CQS.setText(String.valueOf(mMaxCQS));
					loadth();	
				}	
			}
		});
		ADialog.show();
	}
}
