
package com.intput.Ctrl;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

/**
 * @ClassName: ListCtrlItem
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: Administrator
 * @date: 2013-4-12 下午5:25:17
 * 
 */

public class ListCtrlItem {
	public static final int ListItemNone = 0x00;
	public static final int ListItemTextID = 0x02;
	public static final int ListItemString = 0x03;
	public static final int ListItemEmpty = 0x08;

	public Boolean Enable = true;
	public ArrayList<ItemCtrl> mItemCtrls = null;

	public ListCtrlItem(int TextId[], String nString[],
			int Width[], int Gravity[]) {
		int max = TextId.length;
		if (null == mItemCtrls) {
			mItemCtrls = new ArrayList<ItemCtrl>();
		}

		for (int i = 0; i < max; i++) {
			if (null != TextId && TextId[i] > 0) {
				mItemCtrls
						.add(new ItemCtrl(ListCtrlItem.ListItemTextID,
								TextId[i], nString[i], Width[i],
								Gravity[i]));
			} else {
					if (null != nString[i] && nString[i].length() > 0) {
						mItemCtrls.add(new ItemCtrl(
								ListCtrlItem.ListItemString, TextId[i],
								nString[i], Width[i], Gravity[i]));
					} else {
						mItemCtrls.add(new ItemCtrl(ListCtrlItem.ListItemNone,
								TextId[i], null, Width[i],
								Gravity[i]));
					}
				}
			}
	}


	public ListCtrlItem(int type) {
		if (null == mItemCtrls) {
			mItemCtrls = new ArrayList<ItemCtrl>();
		}

		mItemCtrls.add(new ItemCtrl(ListCtrlItem.ListItemEmpty, 0, null, 0,
				Gravity.LEFT));
	}

	public ItemCtrl getItemCtrls(int pos) {
		return mItemCtrls.get(pos);
	}

	public int getItemCtrlsSize() {
		return mItemCtrls.size();
	}

	public void setListCtrlItemEnable(Boolean enable) {
		Enable = enable;
	}

	public Boolean getListCtrlItemEnable() {
		return Enable;
	}

	class ItemCtrl {
		private StringBuffer nString;
		private int nTextId;
		private int nType;
		public int nWidth;
		private int nGravity;
		private int nColor = Color.BLACK;

		public ItemCtrl(int Type, int TextId,String StringId,
				int Width, int Gravity) {
			setnTextId(TextId);
			nType = Type;
			nString = new StringBuffer();

			if (null == StringId) {
				nString.setLength(0);
			} else {
				if (null != StringId && StringId.length() > 0) {
					nString.append(StringId);
				} else {
					nString.append(0);
				}

			}
			nWidth = Width;
			setnGravity(Gravity);
		}

		public ItemCtrl(int Type, int TextId, String StringId,
				int Width, int Gravity, int Color) {
			setnTextId(TextId);
			nType = Type;
			nString = new StringBuffer();

			if (null == StringId) {
				nString.setLength(0);
			} else {
				if (null != StringId && StringId.length() > 0) {
					nString.append(StringId);
				} else {
					nString.append(0);
				}

			}
			nWidth = Width;
			setnGravity(Gravity);
			setnColor(Color);
		}

		public String getnString() {
			return nString.toString();
		}

		public void setnString(String str) {
			if (null == str) {
				nString.setLength(0);
			} else {
				if (null != str && str.length() > 0) {
					nString.setLength(0);
					nString.append(str);
				} else {
					nString.append(0);
				}

			}
		}

		public int getType() {
			return nType;
		}

		public void setType(int type) {
			nType = type;
		}

		public int getnTextId() {
			return nTextId;
		}

		public void setnTextId(int nTextId) {
			this.nTextId = nTextId;
		}

		public int getnGravity() {
			return nGravity;
		}

		public void setnGravity(int nGravity) {
			this.nGravity = nGravity;
		}

		public int getnColor() {
			return nColor;
		}

		public void setnColor(int nColor) {
			this.nColor = nColor;
		}
	}

}
