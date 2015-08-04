package com.intput.database;

import java.io.File;
import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CDKeyDBHelper extends SQLiteOpenHelper{
	
	public static final String tag = "CDKeyDBHelper";
	
	public static final String DATABASE_NAME = "KeyInfo.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TBL_NAME = "KeyBase";
	
	public static final String TBL_ID = "ID";
	public static final String TBL_KEY = "SN";
	private static final String CREATE_TBL = " create table " + TBL_NAME
			+ "(" + TBL_ID + " INTEGER primary key autoincrement,"
			+ TBL_KEY+ " varchar(20))";
	
	private SQLiteDatabase mSQLiteDatabase;
	public static final String dbPath = android.os.Environment
			   .getExternalStorageDirectory().getAbsolutePath() +"/"+ "InputDB";
	public static final String DB_NAME = dbPath + "/" + DATABASE_NAME;
	
	public CDKeyDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		mSQLiteDatabase = arg0;  
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
	
	public void close(){  
        if (mSQLiteDatabase != null){
        	mSQLiteDatabase.close();  
        	mSQLiteDatabase = null;
        } 	
    } 
	
	public void insert(ContentValues values) {  
        SQLiteDatabase db = getWritableDatabase();  
        mSQLiteDatabase = db;  
        db.insert(TBL_NAME, null, values);   
    }  
	
	public void SaveSN(String SN){
		ContentValues mContentValues = new ContentValues();
		mContentValues.put(TBL_KEY, SN);
    	insert(mContentValues); 
	}
	
	public String GetSN(){
		String SN = "000000000000";
    	SQLiteDatabase db = getWritableDatabase();  
    	mSQLiteDatabase = db;
        Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);  
        if(c.getCount() > 0){
        	c.moveToLast();
        	do{
        		SN = c.getString(c.getColumnIndex(TBL_KEY));
        		break;
        	}
        	while(c.moveToPrevious());
        }
        c.close();
        return SN;
	}
}
