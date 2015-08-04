package com.intput.Ctrl;

import java.util.ArrayList;

import android.graphics.Color;

public class ListItem {
	public static final int ITEM_HEAD = 0x00;
	public static final int ITEM_DATA = 0x01;
	private ArrayList<ItemInfo> mItemInfo = null;
	
	public ListItem(){
		mItemInfo = new ArrayList<ItemInfo>();
	}
	
	public void ListItemAdd(int Type[], int TextID[], int ImageID[],String TextStr[]){
		for(int i = 0; i < Type.length; i++){
			mItemInfo.add(new ItemInfo(Type[i],TextID[i],ImageID[i],TextStr[i]));
		}
	}
	
	public void ListItemAdd(int Type, int TextID, int ImageID,String TextStr){
		mItemInfo.add(new ItemInfo(Type,TextID,ImageID,TextStr));
	}
	
	public ItemInfo getItemInfo(int index){
		return mItemInfo.get(index);
	}
	
	public int getCount(){
		return mItemInfo.size();
	}
	
	class ItemInfo{	
		public int nType;
		public int nTextId;
		public int nImageId;
		public String nTextString = null;
		
		public ItemInfo(int Type, int TextID, int ImageID, String TextStr){
			nType = Type;
			nTextId = TextID;
			nImageId = ImageID;
			nTextString = TextStr;
		}
	}
}
