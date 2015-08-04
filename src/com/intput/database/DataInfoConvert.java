package com.intput.database;

import com.intput.Base.Common;
import com.intput.Service.FrameTypeDef.FrameGLLX;
import com.intput.Service.FrameTypeDef.FrameQHDJ;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DataInfoConvert {
	
	private static final String TAG = "DataInfoConvert";
	private Context mContext = null;
	private DataInfoDBHelper mDataInfoDBHelper = null;
	private DataInfoDBHelper00 mDataInfoDBHelper00 = null;
	public static final String dbPath = android.os.Environment
			   .getExternalStorageDirectory().getAbsolutePath() +"/"+ "InputDB";
	
	public DataInfoConvert(Context context){
		mContext = context;
	}
	
	public void Convert(){
		String[] filename = {DataInfoDBHelper.DATABASEV00_NAME};
		String FullName = dbPath+"/"+DataInfoDBHelper.DATABASE_NAME;
		int Index = -1;
		if(!Common.geFileIsExists(FullName)){
			for(int i = 0; i < filename.length; i++){
				String Path = dbPath+"/"+filename[i];
				if(Common.geFileIsExists(Path)){
					Index = i;
					break;
				}
			}

			if(Index != -1){
				switch(Index){
				case 0:
					Convert00();
					break;
					
				default:
					break;
				}
			}
		}
	}
	
	public void Convert00(){
		mDataInfoDBHelper00 = new DataInfoDBHelper00(mContext);
		mDataInfoDBHelper = new DataInfoDBHelper(mContext);	
		Cursor mCursor = null;	
		mCursor = mDataInfoDBHelper00.query();
		if(mCursor.getCount() > 0){		
			while(mCursor.moveToNext()){
				String TIME = mCursor.getString(mCursor.getColumnIndex(DataInfoDBHelper00.TBL_TIME));
				String GJH = mCursor.getString(mCursor.getColumnIndex(DataInfoDBHelper00.TBL_GJH));
				String GJMC = mCursor.getString(mCursor.getColumnIndex(DataInfoDBHelper00.TBL_GJMC));
				byte CQS = (byte) mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper00.TBL_CQS));
				byte CSM = (byte)mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper00.TBL_CSM));
				byte JD = (byte)mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper00.TBL_JD));
				byte FF = (byte)mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper00.TBL_FF));
				byte BS = (byte)mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper00.TBL_BS));
				int DATALEN = mCursor.getInt(mCursor.getColumnIndex(DataInfoDBHelper00.TBL_DATALEN));
				byte[] TIMEBLOB = mCursor.getBlob(mCursor.getColumnIndex(DataInfoDBHelper.TBL_TIMEBLOB));
				byte[] THDATA = mCursor.getBlob(mCursor.getColumnIndex(DataInfoDBHelper.TBL_THDATA));
				byte[] DATA = mCursor.getBlob(mCursor.getColumnIndex(DataInfoDBHelper.TBL_DATA));				
				mDataInfoDBHelper.addrecord(TIME, GJH, GJMC, CQS, CSM, JD, FF, BS, (byte)FrameGLLX.FRAME_GLLX_SS, (byte)FrameQHDJ.FRAME_QHDJ_C30, DATALEN, TIMEBLOB, DATA, THDATA);
			}
    	}
		
		if(!mCursor.isClosed())
    	{
			mCursor.close();
    	}
		
		mDataInfoDBHelper.close();
		mDataInfoDBHelper00.close();
	}
}
