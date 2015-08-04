package com.intput.Ctrl;

import com.inputapp.main.R;
import com.intput.Ctrl.ListItem.ItemInfo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class ListAdapter extends BaseAdapter{
	private static final String Tag = "ListAdapter";
	private Context mContext = null;
	private ListItem mListItem = null;
	private int mMode;
	private int mSelectItem = -1;
	public final static int LISTMODE_1 = 0x01;
	public final static int LISTMODE_2 = 0x02;
	
	public ListAdapter(Context context,int mode){
		mContext = context;
		mMode = mode;
		mListItem = new ListItem();
	}
	
	public void Add(int Type[], int TextID[], int ImageID[], String TextStr[]){
		mListItem.ListItemAdd(Type, TextID, ImageID,TextStr);
	}
	
	public void Add(int Type, int TextID, int ImageID, String TextStr){
		mListItem.ListItemAdd(Type, TextID, ImageID,TextStr);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListItem.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(mMode == LISTMODE_1)
		{	
			return getViewMode1(arg0);
		}
		else
		{
			return getViewMode2(arg0);
		}	
	}
	
	public View getViewMode1(int index){
		TextView mTextView = null;
		TextView mTextStr = null;
		ImageView mImageView = null;
		View mView = LayoutInflater.from(mContext).inflate(R.layout.listitem, null);
		ItemInfo mItemInfo = mListItem.getItemInfo(index);
				
		if(mItemInfo.nImageId > 0 && mItemInfo.nType != ListItem.ITEM_HEAD){
			mImageView = (ImageView) mView.findViewById(R.id.ItemImage);
			mImageView.setImageResource(mItemInfo.nImageId);
		}
		if(mItemInfo.nTextId > 0){
			if(mItemInfo.nType == ListItem.ITEM_HEAD){
				mTextView = (TextView)mView.findViewById(R.id.ItemTextHead);
			}else{
				mTextView = (TextView)mView.findViewById(R.id.ItemText);
			}
			mTextView.setText(mItemInfo.nTextId);
		}else{
			if(null != mItemInfo.nTextString)
			{
				mTextStr =(TextView)mView.findViewById(R.id.ItemTextStr);
				mTextStr.setText(mItemInfo.nTextString);
			}	
		}
		
		if(mItemInfo.nType == ListItem.ITEM_HEAD)
		{
			if(null != mTextView){
				mTextView.setTextSize(16);
				mView.setMinimumHeight(16);
			}
		}else{
			mView.setMinimumHeight(32);
		}
		
		return mView;
	}
	
	public View getViewMode2(int index){
		TextView mTextView = null;
		TextView mTextStr = null;
		ImageView mImageView = null;
		View mView = LayoutInflater.from(mContext).inflate(R.layout.listitem, null);
		ItemInfo mItemInfo = mListItem.getItemInfo(index);
				
		if(mItemInfo.nImageId > 0 && mItemInfo.nType != ListItem.ITEM_HEAD){
			mImageView = (ImageView) mView.findViewById(R.id.ItemImage);
			mImageView.setImageResource(mItemInfo.nImageId);
		}
		mTextView = (TextView)mView.findViewById(R.id.ItemText);
		mTextStr  = (TextView)mView.findViewById(R.id.ItemTextStr);
		if(mItemInfo.nTextId > 0){
			mTextView.setText(mItemInfo.nTextId);
		}
		if(null != mItemInfo.nTextString)
		{
			mTextStr.setText(mItemInfo.nTextString);
		}	
		mTextView.setTextSize(16);
		mTextStr.setTextSize(16);
		mTextView.setTextColor(Color.BLACK);
		mTextStr.setTextColor(Color.BLACK);
		mView.setMinimumHeight(20);
		
		return mView;
	}
	
	public void SetSelectItem(int Select){
		mSelectItem = Select;
	}
	
	public int GetSelectItem(){
		return mSelectItem;
	}
}
