package com.intput.Ctrl;

import java.util.ArrayList;

import com.inputapp.main.R;
import com.intput.Ctrl.ListCtrlItem.ItemCtrl;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

class ListTypeView1 extends LinearLayout {
	public static final String LOG_TAG = "ListTypeView1";
	private OnTouchListener mOnTouchListener = null;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public ListTypeView1(Context context, int Width, int Height,
			ListCtrlItem Items, OnTouchListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		LinearLayout.LayoutParams params;
		mOnTouchListener = listener;
		int nWidth = 0;
		for (int i = 0; i < Items.getItemCtrlsSize(); i++) {
			ItemCtrl mItem = Items.getItemCtrls(i);
			switch (mItem.getType()) {
			case ListCtrlItem.ListItemTextID:
				TextView ItemText = new TextView(context);
				ItemText.setId(i);
				if (mOnTouchListener != null) {
					ItemText.setOnTouchListener(mOnTouchListener);
				}
				params = new LinearLayout.LayoutParams(mItem.nWidth, Height);
				ItemText.setTextSize(24);
				ItemText.setGravity(mItem.getnGravity());
				ItemText.setText(mItem.getnTextId());
				ItemText.setSingleLine(true);
				ItemText.setEllipsize(TextUtils.TruncateAt.END);
				ItemText.setTextColor(mItem.getnColor());
				ItemText.setBackgroundResource(R.drawable.listbk_up);
				addView(ItemText, params);
				break;

			case ListCtrlItem.ListItemString:
				TextView ItemText1 = new TextView(context);
				ItemText1.setId(i);
				if (mOnTouchListener != null) {
					ItemText1.setOnTouchListener(mOnTouchListener);
				}
				params = new LinearLayout.LayoutParams(mItem.nWidth, Height);
				ItemText1.setTextSize(24);
				ItemText1.setGravity(mItem.getnGravity());
				ItemText1.setText(mItem.getnString());
				ItemText1.setSingleLine(true);
				ItemText1.setEllipsize(TextUtils.TruncateAt.END);
				ItemText1.setTextColor(mItem.getnColor());
				ItemText1.setBackgroundResource(R.drawable.listbk_up);
				addView(ItemText1, params);
				break;

			case ListCtrlItem.ListItemNone:
				TextView ItemText3 = new TextView(context);
				ItemText3.setBackgroundResource(R.drawable.listbk_up);
				params = new LinearLayout.LayoutParams(mItem.nWidth, Height);
				addView(ItemText3, params);
				break;

			default:
				break;
			}
			nWidth += mItem.nWidth;
			if (nWidth >= Width) {
				break;
			}
		}

		if (nWidth < Width) {
			TextView ItemText = new TextView(context);
			params = new LinearLayout.LayoutParams((Width - nWidth), Height);
			addView(ItemText, params);
		}
	}
}

class ListTypeView2 extends LinearLayout {
	public static final String LOG_TAG = "ListTypeView1";
	private OnTouchListener mOnTouchListener = null;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public ListTypeView2(Context context, int Width, int Height,
			ListCtrlItem Items, OnTouchListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		LinearLayout.LayoutParams params,params1;
		LinearLayout mLinearLayout = null;
		mOnTouchListener = listener;
		int nWidth = 0;
		for (int i = 0; i < Items.getItemCtrlsSize(); i++) {
			ItemCtrl mItem = Items.getItemCtrls(i);

			switch (mItem.getType()) {
			case ListCtrlItem.ListItemTextID:
				TextView ItemText = new TextView(context);
				ItemText.setId(i);
				if (mOnTouchListener != null) {
					ItemText.setOnTouchListener(mOnTouchListener);
				}
				params = new LinearLayout.LayoutParams(mItem.nWidth, Height);
				ItemText.setTextSize(24);
				ItemText.setGravity(mItem.getnGravity());
				ItemText.setText(mItem.getnTextId());
				ItemText.setSingleLine(true);
				ItemText.setEllipsize(TextUtils.TruncateAt.END);
				ItemText.setTextColor(mItem.getnColor());
				addView(ItemText, params);
				break;

			case ListCtrlItem.ListItemString:
				TextView ItemText1 = new TextView(context);
				ItemText1.setId(i);
				if (mOnTouchListener != null) {
					ItemText1.setOnTouchListener(mOnTouchListener);
				}
				params = new LinearLayout.LayoutParams(mItem.nWidth, Height);
				params1 = new LinearLayout.LayoutParams(mItem.nWidth-6, Height);
				ItemText1.setTextSize(24);
				ItemText1.setGravity(mItem.getnGravity());
				ItemText1.setText(mItem.getnString());
				ItemText1.setSingleLine(true);
				ItemText1.setEllipsize(TextUtils.TruncateAt.END);
				ItemText1.setTextColor(mItem.getnColor());
				mLinearLayout = new LinearLayout(context);
				mLinearLayout.setGravity(Gravity.CENTER);
				mLinearLayout.addView(ItemText1, params1);
				addView(mLinearLayout,params);
				break;

			case ListCtrlItem.ListItemNone:
				TextView ItemText3 = new TextView(context);
				params = new LinearLayout.LayoutParams(mItem.nWidth, Height);
				addView(ItemText3, params);
				break;

			default:
				break;
			}
			nWidth += mItem.nWidth;
			if (nWidth >= Width) {
				break;
			}
		}

		if (nWidth < Width) {
			TextView ItemText = new TextView(context);
			params = new LinearLayout.LayoutParams((Width - nWidth), Height);
			addView(ItemText, params);
		}
	}
}

/**
 * @ClassName: ListCtrlAdapter
 * @Description:TODO(������һ�仰��������������)
 * @author: Administrator
 * @date: 2013-4-12 ����5:23:28
 * 
 */

public class ListCtrlAdapter extends BaseAdapter {
	public static final String LOG_TAG = "ListCtrlAdapter";
	public static final int ListType1 = 0x01;
	public static final int ListType2 = 0x02;
	public static final int DefaultItemCount = 5;

	private ArrayList<ListCtrlItem> mListItem;
	private Context mContext;
	private int mListPageCount = 0;
	private int mListCount = 0;
	private int mListType = ListType1;
	private int mWidth;
	private int HeightLine;
	private int PosId = -1;
	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			PosId = arg0.getId();
			return false;
		}
	};

	/**
	 * 
	 * @param context
	 * @param objects
	 */
	public ListCtrlAdapter(Context context) {
		super();
		mListItem = new ArrayList<ListCtrlItem>();
		mContext = context;
		mListPageCount = 0;
	}

	private void ListCtrlAddEmpty() {
		ListCtrlItem listItem = new ListCtrlItem(ListCtrlItem.ListItemEmpty);
		mListItem.add(listItem);
	}

	public void ListCtrlCreate(int ListType, ListCtrl View) {
		mWidth = View.getListWidth();
		mListType = ListType;
		mListCount = 0;
		
		View.setVerticalScrollBarEnabled(false);
		View.setFadingEdgeLength(0);
		View.setDividerHeight(0);

		HeightLine = 56;

		setmListPageCount(View.getListHeight() / HeightLine);

		for (int i = 0; i < mListPageCount; i++) {
			ListCtrlAddEmpty();
		}
	}

	public void ListCtrlAdd(int TextId[], String nString[],
			int Width[], int Gravity[]) {
		ListCtrlItem listItem = new ListCtrlItem(TextId, nString, Width, Gravity);
		if (getCount() == mListPageCount) {
			int found = -1;
			for (int j = 0; j < mListPageCount; j++) {
				ListCtrlItem item = getItem(j);
				ItemCtrl mItemCtrl = item.getItemCtrls(0);

				if (mItemCtrl.getType() == ListCtrlItem.ListItemEmpty) {
					found = j;
					break;
				}
			}

			if (found == -1) {
				mListItem.add(listItem);
			} else {
				mListItem.set(found, listItem);
			}
		} else {
			mListItem.add(listItem);
		}
		mListCount++;
	}

	public void ListCtrlDel(int position) {
		mListCount--;
		mListItem.remove(position);
		if (getCount() < getmListPageCount()) {
			ListCtrlAddEmpty();
		}
		notifyDataSetChanged();
	}

	public void ListCtrlClear() {
		mListCount = 0;
		mListItem.clear();
		for (int i = 0; i < getmListPageCount(); i++) {
			ListCtrlAddEmpty();
		}
		notifyDataSetChanged();
	}

	public void ListCtrlSetTextId(int line, int position, int Text) {
		ListCtrlItem ListItem = getItem(line);
		if (ListItem != null) {
			ItemCtrl Item = ListItem.getItemCtrls(position);
			if (null != Item) {
				Item.setnTextId(Text);
				notifyDataSetChanged();
			}
		}
	}

	public int ListCtrlgetTextId(int line, int position) {
		ListCtrlItem ListItem = getItem(line);
		if (ListItem != null) {
			ItemCtrl Item = ListItem.getItemCtrls(position);
			if (null != Item) {
				return Item.getnTextId();
			}
		}

		return -1;
	}

	public void ListCtrlSetText(int line, int position, String string) {
		ListCtrlItem ListItem = getItem(line);
		if (ListItem != null) {
			ItemCtrl Item = ListItem.getItemCtrls(position);
			if (null != Item) {
				Item.setnString(string);
				notifyDataSetChanged();
			}
		}
	}

	public String ListCtrlgetText(int line, int position) {
		ListCtrlItem ListItem = getItem(line);
		if (ListItem != null) {
			ItemCtrl Item = ListItem.getItemCtrls(position);
			if (null != Item) {
				return Item.getnString();
			}
		}

		return null;
	}

	public void ListCtrlSetEnable(int line, Boolean enable) {
		ListCtrlItem ListItem = getItem(line);
		if (ListItem != null) {
			ListItem.setListCtrlItemEnable(enable);
		}
	}

	public Boolean ListCtrlGetEnable(int line) {
		ListCtrlItem ListItem = getItem(line);
		if (ListItem != null) {
			return ListItem.getListCtrlItemEnable();
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		ListCtrlItem item = mListItem.get(position);
		if (getCount() == mListPageCount) {
			if (item.getListCtrlItemEnable()) {
				ItemCtrl mItemCtrl = item.getItemCtrls(0);
				if (mItemCtrl.getType() != ListCtrlItem.ListItemEmpty) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		} else {
			if (item.getListCtrlItemEnable()) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListItem.size();
	}

	public int getAdapterCount() {
		return mListCount;
	}

	@Override
	public ListCtrlItem getItem(int position) {
		// TODO Auto-generated method stub
		if (null == mListItem) {
			return null;
		}
		return mListItem.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListCtrlItem Item = getItem(position);
		switch (mListType) {
		case ListType1:
			return new ListTypeView1(mContext, mWidth, HeightLine, Item, null);
			
		case ListType2:	
			return new ListTypeView2(mContext, mWidth, HeightLine, Item, null);
			
		default:
			return new ListTypeView1(mContext, mWidth, HeightLine, Item, null);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * @return the mListCount
	 */
	public int getmListPageCount() {
		return mListPageCount;
	}

	/**
	 * @param mListCount
	 *            the mListCount to set
	 */
	public void setmListPageCount(int mListCount) {
		mListPageCount = mListCount;
	}

	public int getListPosId() {
		return PosId;
	}
}
