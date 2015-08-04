package com.input.app;

import java.lang.reflect.Field;

import com.inputapp.main.R;
import com.intput.Base.AESUtils;
import com.intput.Base.CalculationHelper;
import com.intput.Base.Common;
import com.intput.Base.DeviceInfo;
import com.intput.Base.IntentDef;
import com.intput.Client.MainClient;
import com.intput.Ctrl.ViewCtrl;
import com.intput.database.CDKeyDBHelper;
import com.intput.database.DataInfoConvert;
import com.intput.database.DataInfoDBHelper;
import com.mail.MailOper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnItemClickListener{
	
	public static final String TAG = "IntputApp";
	
	public static final int ID_INPUTWRITE = 0x01;
	public static final int ID_INPUTBROWER = 0x02;
	public static final int ID_SETTING = 0x03;
	public static final String ACTION = "com.inputapp.main";
	
	private ViewCtrl mViewCtrl;
	private RelativeLayout mRelativeLayout = null;
	private IntputWrite mIntputWrite = null;
	private InputBrower mInputBrower = null;
	private MainClient mMainClient = null;
	private WifiManager mWifiManager = null;
	private DataInfoDBHelper mDataInfoDBHelper = null;
	private CDKeyDBHelper mCDKeyDBHelper = null;
	private AESUtils mAESUtils = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mCDKeyDBHelper = new CDKeyDBHelper(this);
		
		StartRecvMsg();
		mViewCtrl = new ViewCtrl(this,ACTION){

			@Override
			public void OnExitPage() {
				// TODO Auto-generated method stub
			}

			@Override
			public void OnUpdatePage(int param) {
				// TODO Auto-generated method stub
				if (mViewCtrl.getActiveId() == ID_INPUTWRITE){
					if(-1 == param){
						mIntputWrite.setPageType(null);
					}else{
						String str = String.valueOf(param);
						if(str.length() < 6){
							int len = 6 - str.length();
							String real = "";
							for (int i = 0; i < len; i++){
								real = real + "0";
							}
							real = real+str;
							mIntputWrite.setPageType(real);
						}else{
							mIntputWrite.setPageType(str);
						}
					}
				}
			}
			
		};
		
		mViewCtrl.ViewStartBroadcast();
		mRelativeLayout = (RelativeLayout) findViewById(R.id.RFIDWindow);
		DataInfoConvert mDataInfoConvert = new DataInfoConvert(this);
		mDataInfoConvert.Convert();
		mDataInfoDBHelper = new DataInfoDBHelper(this);
		mIntputWrite = new IntputWrite(this, ACTION, ID_INPUTWRITE , mDataInfoDBHelper);
		mInputBrower = new InputBrower(this, ACTION, ID_INPUTBROWER, mDataInfoDBHelper);

		mRelativeLayout.addView(mInputBrower);
		mRelativeLayout.addView(mIntputWrite);
		mViewCtrl.ViewRegister((View) mInputBrower, mViewCtrl.LEVEL_1);
		mViewCtrl.ViewRegister((View) mIntputWrite, mViewCtrl.LEVEL_2);

		mMainClient = new MainClient(this);
		mIntputWrite.setBaseClient(mMainClient);
		mInputBrower.setBaseClient(mMainClient);
		
		mAESUtils =  new AESUtils();
		DeviceInfo mDeviceMD5 = new DeviceInfo(this);
		String MD5 = mDeviceMD5.getCombinedCRC(mAESUtils);
		String SystemReg = getSystemReg();
		//DelectApk();
		MailOper mMailOper = new MailOper();
		mMailOper.SendRegMail(this);
		
//		if(SystemReg.equals(MD5)){
//			mViewCtrl.AddShow(ID_INPUTBROWER);
//		}else{
//			ShowLogInDialog();
//		}
		mViewCtrl.AddShow(ID_INPUTBROWER);
		
	}
	
	public void StartRecvMsg(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(IntentDef.BROADCAST_MAIN);
		this.registerReceiver(MainReceiver, filter);
	}
	
	private String getSystemReg(){
		return mCDKeyDBHelper.GetSN();
	}
	
	private void setSystemReg(String reg){
		mCDKeyDBHelper.SaveSN(reg);
	}
	
	private void DelectApk(){
		Intent intent=new Intent(Intent.ACTION_DELETE,Uri.parse("package:"+"com.devicekey.main"));
	    startActivity(intent);
	}
	
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
	
	public boolean getMacCrc(String In, String Crc){
		boolean ret = false;
		if(In.equals(Crc)){
			ret = true;
		}
		return ret;
	}
	
	public void ShowLogInDialog(){
		LayoutInflater factory = LayoutInflater.from(this);
		View LogView = factory.inflate(R.layout.inputlogin, null);
		
		final EditText mEditReg = (EditText) LogView.findViewById(R.id.EditReg);
		final TextView mUserMac = (TextView) LogView.findViewById(R.id.UserMac);
		
		String ret = getResources().getString(R.string.MAIN_REG);
		mUserMac.setText(ret);
		AlertDialog.Builder ADialog = new AlertDialog.Builder(this);
		ADialog.setCancelable(false);
		ADialog.setView(LogView);		
		ADialog.setNegativeButton(R.string.NO , new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				SetAlertDialog(arg0,true);
				finish();
			}
		});
		ADialog.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				String Reg = mEditReg.getText().toString();
				String RegOk = Common.getMacVersionID();
				if(getMacCrc(Reg,RegOk)){
					setSystemReg(Reg);
					SetAlertDialog(arg0,true);
					mViewCtrl.AddShow(ID_INPUTBROWER);
				}else{
					Toast mToast = Toast.makeText(MainActivity.this, R.string.MAIN_REGEROR, Toast.LENGTH_SHORT);
					mToast.show();
				}
			}
		});
		ADialog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (mViewCtrl.getActiveId() == ID_INPUTWRITE){
			menu.clear();
			menu.add(0, R.string.MAIN_RANDOM, 1, R.string.MAIN_RANDOM);
			menu.add(0, R.string.Main_Help, 2, R.string.Main_Help);
		}else if (mViewCtrl.getActiveId() == ID_INPUTBROWER){
			menu.clear();
			menu.add(0, R.string.MAIN_OUTPUT, 1, R.string.MAIN_OUTPUT);
			menu.add(0, R.string.MAIN_QUEUE, 2, R.string.MAIN_QUEUE);
			menu.add(0, R.string.MAIN_CLEAR, 3, R.string.MAIN_CLEAR);
			menu.add(0, R.string.Main_Help, 4, R.string.Main_Help);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		InputBase mView = (InputBase) mViewCtrl.getActiveView();
		int Id = item.getItemId();
		mView.OnMenuEventReport(Id);
		switch(Id){
		case R.string.MAIN_VERSION:
			ShowVersionDialog();
			break;
			
		case R.string.Main_Help:

			ShowHelpDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int TextID[] = {R.string.Main_Input,R.string.Main_Seach,R.string.Main_Setting};
		switch(TextID[position]){
		case R.string.Main_Input:
			mViewCtrl.AddShow(ID_INPUTWRITE);
			break;
		case R.string.Main_Seach:
			mViewCtrl.AddShow(ID_INPUTBROWER);
			break;
		case R.string.Main_Setting:
			
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mDataInfoDBHelper != null){
			mDataInfoDBHelper.close();
		}
		mCDKeyDBHelper.close();
		unregisterReceiver(MainReceiver);
		mViewCtrl.ViewStopBroadcast();
		mMainClient.MainClientStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(mViewCtrl.getActiveId() == ID_INPUTWRITE){
			}else{
				finish();
			}
		}
		return true;
	}
	
	public BroadcastReceiver MainReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			String action = arg1.getAction();
			if(action.equals(IntentDef.BROADCAST_MAIN)){
				finish();
			}
		}
		
	};
	
	private String getAppInfo() {
 		try {
 			String pkName = this.getPackageName();
 			String versionName = this.getPackageManager().getPackageInfo(
 					pkName, 0).versionName;
 			versionName = "V" + versionName;
 			return versionName;
 		} catch (Exception e) {
 		}
 		return null;
 	}
	
	public void ShowVersionDialog(){
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.inputinfo,(ViewGroup) findViewById(R.id.VersionDialog));
		TextView mVersionS = (TextView) layout.findViewById(R.id.Version);
		String Version = getAppInfo();
		if(null != Version){
			mVersionS.setText(Version);
		}
		AlertDialog mAlertDialog = new AlertDialog.Builder(this)
		.setTitle("ÏµÍ³°æ±¾").setView(layout).create();
		Window lp = mAlertDialog.getWindow();
		lp.setLayout(150,100);
		mAlertDialog.show();
	}
	
	public void ShowHelpDialog(){
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.inputhelp,(ViewGroup) findViewById(R.id.HelpDialog));
		TextView mCompany = (TextView) layout.findViewById(R.id.Company);
		TextView mTelephone = (TextView) layout.findViewById(R.id.Telephone);
		mCompany.setText(R.string.MAIN_Company);
		mTelephone.setText(R.string.MAIN_Telephone);
		AlertDialog mAlertDialog = new AlertDialog.Builder(this)
		.setTitle("°ïÖú").setView(layout).create();
		Window lp = mAlertDialog.getWindow();
		lp.setLayout(150,100);
		mAlertDialog.show();
	}
	
}
