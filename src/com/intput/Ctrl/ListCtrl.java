package com.intput.Ctrl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @ClassName: ListCtrl
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: Administrator
 * @date: 2013-4-23 下午3:02:17
 * 
 */

public class ListCtrl extends ListView {
	/**
	 * @param context
	 * @param attrs
	 */
	public ListCtrl(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public int getListHeight() {
		return getLayoutParams().height;
	}

	public int getListWidth() {
		return getLayoutParams().width;
	}
	
	public void SetText(int line, int position, int Text) {
		ListCtrlAdapter adapter = (ListCtrlAdapter) getAdapter();
		if (adapter != null) {
			adapter.ListCtrlSetTextId(line, position, Text);
		}
	}

	public void SetText(int line, int position, String Text) {
		ListCtrlAdapter adapter = (ListCtrlAdapter) getAdapter();
		if (adapter != null) {
			adapter.ListCtrlSetText(line, position, Text);
		}
	}
	
	public String GetText(int line, int position) {
		ListCtrlAdapter adapter = (ListCtrlAdapter) getAdapter();
		if (adapter != null) {
			return adapter.ListCtrlgetText(line, position);
		}
		return null;
	}
	
	public int GetTextId(int line, int position) {
		ListCtrlAdapter adapter = (ListCtrlAdapter) getAdapter();
		if (adapter != null) {
			return adapter.ListCtrlgetTextId(line, position);
		}
		return -1;
	}

	public void SetEnable(int line, Boolean enable) {
		ListCtrlAdapter adapter = (ListCtrlAdapter) getAdapter();
		if (adapter != null) {
			adapter.ListCtrlSetEnable(line, enable);
		}
	}

	public Boolean GetEnable(int line) {
		ListCtrlAdapter adapter = (ListCtrlAdapter) getAdapter();
		if (adapter != null) {
			return adapter.ListCtrlGetEnable(line);
		}

		return false;
	}

	public int GetCount() {
		ListCtrlAdapter adapter = (ListCtrlAdapter) getAdapter();
		if (adapter != null) {
			return adapter.getAdapterCount();
		}

		return 0;
	}

	public void Clear() {
		ListCtrlAdapter adapter = (ListCtrlAdapter) getAdapter();
		if (adapter != null) {
			adapter.ListCtrlClear();
		}
	}

	public void Del(int pos) {
		ListCtrlAdapter adapter = (ListCtrlAdapter) getAdapter();
		if (adapter != null) {
			adapter.ListCtrlDel(pos);
		}
	}

	public int getPosId() {
		ListCtrlAdapter adapter = (ListCtrlAdapter) getAdapter();
		if (adapter != null) {
			return adapter.getListPosId();
		}

		return -1;
	}
}
