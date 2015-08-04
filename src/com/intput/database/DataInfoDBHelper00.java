package com.intput.database;

import java.io.File;
import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataInfoDBHelper00 extends SQLiteOpenHelper{
	
	public static final String tag = "DataInfoDBHelper";
	
	public static final String DATABASE_NAME = "DataInfo.db";

	public static final int DATABASE_VERSION = 1;
	public static final String TBL_NAME = "DataDataBase";
	
	public static final String TBL_ID = "ID";
	public static final String TBL_GJH = "构件号";
	public static final String TBL_GJMC = "构件名称";
	public static final String TBL_CQS = "测区数";
	public static final String TBL_CSM = "测试面";
	public static final String TBL_JD = "角度";
	public static final String TBL_FF = "方法";
	public static final String TBL_BS = "泵送";
	public static final String TBL_THDATA = "碳化数据";
	public static final String TBL_DATALEN = "数据长度";
	public static final String TBL_DATA = "数据";
	public static final String TBL_TIME = "时间";
	public static final String TBL_TIMEBLOB = "时间数组";
	private static final String CREATE_TBL = " create table " + TBL_NAME
			+ "(" + TBL_ID + " INTEGER primary key autoincrement,"
			+ TBL_TIME+ " varchar(20)," 
			+ TBL_GJH + " varchar(6)," 
			+ TBL_GJMC + " varchar(100),"
			+ TBL_CQS + " INTEGER,"
			+ TBL_CSM + " INTEGER,"
			+ TBL_JD + " INTEGER,"
			+ TBL_FF + " INTEGER,"
			+ TBL_BS + " INTEGER,"
			+ TBL_DATALEN + " INTEGER,"
			+ TBL_TIMEBLOB + " BLOB,"
			+ TBL_THDATA + " BLOB,"
			+ TBL_DATA + " BLOB)";
	
	private SQLiteDatabase mSQLiteDatabase;
	public static final String dbPath = android.os.Environment
			   .getExternalStorageDirectory().getAbsolutePath() +"/"+ "InputDB";
	public static final String DB_NAME = dbPath + "/" + DATABASE_NAME;
	
	public DataInfoDBHelper00(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		mSQLiteDatabase = arg0;  
		//mSQLiteDatabase.execSQL(CREATE_TBL);
	}
		
	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
	  File dbp = new File(dbPath);
	  File dbf = new File(DB_NAME);
	  if (!dbp.exists()) 
	  {
		  dbp.mkdir();
	  }

	  boolean isFileCreateSuccess = false;
	  if (!dbf.exists()) 
	  {
		   try {
		    isFileCreateSuccess = dbf.createNewFile();
		    if(isFileCreateSuccess)
		    {
		    	SQLiteDatabase.openOrCreateDatabase(dbf, null).execSQL(CREATE_TBL);
		    }
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
	  } else {
	   isFileCreateSuccess = true;
	  }
	  if (isFileCreateSuccess) {
		  return SQLiteDatabase.openOrCreateDatabase(dbf, null);
	  }
	  else {
		  return null;
	  }
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
		
	public Cursor query(){
    	SQLiteDatabase db = getWritableDatabase();  
    	mSQLiteDatabase = db;
        Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);  
        return c;  
    }
	
	public Cursor query(String ZZRQ){
    	SQLiteDatabase db = getWritableDatabase();  
    	mSQLiteDatabase = db;
    	String selection = TBL_TIME+"=?";
    	String[] selectionArgs = new String[]{ZZRQ};
        Cursor c = db.query(TBL_NAME, null, selection, selectionArgs, null, null, null); 
        return c;  
    }
	
	public Cursor queryGJH(String GJH){
    	SQLiteDatabase db = getWritableDatabase();  
    	mSQLiteDatabase = db;
    	String selection = TBL_GJH+"=?";
    	String[] selectionArgs = new String[]{GJH};
        Cursor c = db.query(TBL_NAME, null, selection, selectionArgs, null, null, null); 
        return c;  
    }

	public void insert(ContentValues values) {  
        SQLiteDatabase db = getWritableDatabase();  
        mSQLiteDatabase = db;  
        db.insert(TBL_NAME, null, values);   
    }  
	
	public void close(){  
        if (mSQLiteDatabase != null){
        	mSQLiteDatabase.close();  
        	mSQLiteDatabase = null;
        } 	
    }  

	public void addrecord(String Timer,
			String GJH,
			String GJMC,
			byte CQS,
			byte CSM,
			byte JD,
			byte FF,
			byte BS,
			int DATALEN,
			byte[] TIME,
			byte[] DATA,
			byte[] THDATA){
		
		ContentValues mContentValues = new ContentValues();
		mContentValues.put(TBL_GJH, GJH);
		mContentValues.put(TBL_GJMC, GJMC);
		mContentValues.put(TBL_TIME, Timer);
    	mContentValues.put(TBL_CQS, CQS);
    	mContentValues.put(TBL_CSM, CSM);
    	mContentValues.put(TBL_JD, JD);
    	mContentValues.put(TBL_FF, FF);
    	mContentValues.put(TBL_BS, BS);
    	mContentValues.put(TBL_THDATA, THDATA);
    	mContentValues.put(TBL_DATALEN, DATALEN);
    	mContentValues.put(TBL_DATA, DATA);
    	mContentValues.put(TBL_TIMEBLOB, TIME);
    	insert(mContentValues); 
	}
	
	public void changedata(String Timer,
			String GJH,
			String GJMC,
			byte CSM,
			byte JD,
			byte FF,
			byte BS,
			int DATALEN,
			byte[] TIME,
			byte[] DATA,
			byte[] THDATA){
		SQLiteDatabase db = getWritableDatabase(); 
		mSQLiteDatabase = db;
		
		ContentValues mContentValues = new ContentValues();
		mContentValues.put(TBL_GJMC, GJMC);
		mContentValues.put(TBL_TIME, Timer);
    	mContentValues.put(TBL_CSM, CSM);
    	mContentValues.put(TBL_JD, JD);
    	mContentValues.put(TBL_FF, FF);
    	mContentValues.put(TBL_BS, BS);
    	mContentValues.put(TBL_THDATA, THDATA);
    	mContentValues.put(TBL_DATALEN, DATALEN);
    	mContentValues.put(TBL_DATA, DATA);
    	mContentValues.put(TBL_TIMEBLOB, TIME);
    	String selection = TBL_GJH+"=?";
    	String[] selectionArgs = new String[]{GJH};
    	db.update(TBL_NAME, mContentValues, selection, selectionArgs);
	}
	
	public String getlastGJH(){
		String GJH = "000000";
    	SQLiteDatabase db = getWritableDatabase();  
    	mSQLiteDatabase = db;
        Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);  
        if(c.getCount() > 0){
        	c.moveToLast();
        	do{
        		GJH = c.getString(c.getColumnIndex(TBL_GJH));
        		break;
        	}
        	while(c.moveToPrevious());
        }
        c.close();
        return GJH;
	}
	
	public void delete(String GJH){
		SQLiteDatabase db = getWritableDatabase(); 
		mSQLiteDatabase = db;
		String selection = TBL_GJH+"=?";
    	String[] selectionArgs = new String[]{GJH};
    	db.delete(TBL_NAME, selection, selectionArgs);
	}
	
	public void deletetimer(String Timer){
		SQLiteDatabase db = getWritableDatabase(); 
		mSQLiteDatabase = db;
		if(Timer == null){
	    	db.delete(TBL_NAME, null, null);
		}else{
			String selection = TBL_TIME+"=?";
	    	String[] selectionArgs = new String[]{Timer};
	    	db.delete(TBL_NAME, selection, selectionArgs);
		}
	}
}
