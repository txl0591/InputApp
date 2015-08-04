package com.intput.Ctrl;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;

public abstract class ViewCtrl
{
  public static final String VIEWCTRL_OPER = "com.rfid.ctrl.viewctrl.oper";
  public static final String VIEWCTRL_ID = "com.rfid.ctrl.viewctrl.id";
  public static final String VIEWCTRL_PARAM = "com.rfid.ctrl.viewctrl.param";
  
  public static final int VIEW_IN = 0x01;
  public static final int VIEW_OUT = 0x02;
  public static final int VIEW_EXIT = 0x03;
  
  public static final int LEVEL_1 = 0x00;
  public static final int LEVEL_2 = 0x01;
  public static final int LEVEL_3 = 0x02;
  public static final int LEVEL_4 = 0x03;
  public static final int LEVEL_5 = 0x04;

  public int ActiveId = -1;
  public Context mContext;
  public String mAction;
  public int param = -1;
  
  class ViewBase
  {
    public View mView;
    public int mLevel;
  }

  private ArrayList<ViewBase> ViewList;
  private ArrayList<ViewBase> ShowList;
  private ViewCtrlBroadcastReceiver mViewCtrlBroadcastReceiver = null;

  public ViewCtrl(Context context, String action)
  {
    mContext = context;
    mAction = action;
    ViewList = new ArrayList<ViewBase>();
    ShowList = new ArrayList<ViewBase>();
  }
  
  public void ViewStartBroadcast()
  {
    mViewCtrlBroadcastReceiver = new ViewCtrlBroadcastReceiver();
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(mAction);
    mContext.registerReceiver(mViewCtrlBroadcastReceiver, intentFilter);
  }
  
  public void ViewStopBroadcast()
  {
    if (null != mViewCtrlBroadcastReceiver) 
    {
      mContext.unregisterReceiver(mViewCtrlBroadcastReceiver);
      mViewCtrlBroadcastReceiver = null;
    }
  }
    
  public void ViewRegister(View view, int Level)
  {
    ViewBase mViewBase = new ViewBase();
    mViewBase.mLevel = Level;
    mViewBase.mView = view;
       
    if (null != mViewBase.mView)
    {
      mViewBase.mView.setVisibility(View.INVISIBLE);
    }
    else
    {
      Log.d("","mViewBase.mView null Id "+mViewBase.mView.getId());
    }  
    if (ViewList != null)
    {
      ViewList.add(mViewBase);
    }
  }

  public void ViewRegisterClear()
  {
    if (ViewList != null)
    {
      ViewList.clear();
    }
  }
  
  public int getActiveId()
  {
    return ActiveId;
  }
  
  public View getActiveView()
  {
	ViewBase mViewBase;
	int len = ViewList.size();
    for (int i = 0; i < len; i++)
    {
      mViewBase = ViewList.get(i);      
      if (mViewBase != null && mViewBase.mView != null && 
          mViewBase.mView.getId() == ActiveId){
    	  return mViewBase.mView;
      }
    }
    return null;
  }
  
  public void AddShow(int Id)
  {
    ViewBase mViewBase, mViewShow;
    
    if(getActiveId() != Id){
    	int len = ViewList.size();
        for (int i = 0; i < len; i++)
        {
          mViewBase = ViewList.get(i);      
          if (mViewBase != null && mViewBase.mView != null && 
              mViewBase.mView.getId() == Id)
          {
            mViewBase.mView.setVisibility(View.VISIBLE);
            if (ShowList.size() > 0)
            {
              mViewShow = ShowList.get(ShowList.size()-1);
              mViewShow.mView.setVisibility(View.INVISIBLE);
              while(mViewBase.mLevel <= mViewShow.mLevel)
              {
                ShowList.remove(mViewShow);
                mViewShow.mView.setVisibility(View.INVISIBLE);
                if (ShowList.size() > 0)
                {
                mViewShow = ShowList.get(ShowList.size()-1);
                }else{
                  break;
                }
              } 
            }
            ShowList.add(mViewBase);
            ActiveId = Id;
            break;
          }
        }
    }
  }
  
  public void AddShow(int Id, int param)
  {
    ViewBase mViewBase, mViewShow;
    int len = ViewList.size();
    for (int i = 0; i < len; i++)
    {
      mViewBase = ViewList.get(i);
      
      if (mViewBase != null && mViewBase.mView != null && 
          mViewBase.mView.getId() == Id)
      {
    	ActiveId = Id;  
    	OnUpdatePage(param);  
        mViewBase.mView.setVisibility(View.VISIBLE);
        if (ShowList.size() > 0)
        {
          mViewShow = ShowList.get(ShowList.size()-1);
          mViewShow.mView.setVisibility(View.INVISIBLE);
          while(mViewBase.mLevel <= mViewShow.mLevel)
          {
            ShowList.remove(mViewShow);
            mViewShow.mView.setVisibility(View.INVISIBLE);
            if (ShowList.size() > 0)
            {
              mViewShow = ShowList.get(ShowList.size()-1);
            }else
            {
              break;
            }
          } 
        }
        ShowList.add(mViewBase);
        break;
      }
    }
  }
  
  public void DelShow(int param)
  {
    int len = ViewList.size();
    ViewBase mViewBase = ViewList.get(len-1);
    
    if (mViewBase != null && mViewBase.mView != null)
    {
      ShowList.remove(mViewBase);
      if (ShowList.size() > 0)
      {
        mViewBase.mView.setVisibility(View.INVISIBLE); 
        View mView = ShowList.get(ShowList.size()-1).mView;
        if (mView != null)
        {
          ActiveId = mView.getId();
          mView.setVisibility(View.VISIBLE); 
        }
      }
    }
    if (0 == ShowList.size())
    {
      OnExitPage();
    } 
    else
    {
      OnUpdatePage(param);  	
      ShowList.get(ShowList.size()-1).mView.setVisibility(View.VISIBLE);
    } 
  }
  
  
  public void DelShow(int Id, int param)
  {
    ViewBase mViewBase;
    int len = ViewList.size();
    
    for (int i = 0; i < len; i++)
    {
      mViewBase = ViewList.get(i);
      if (mViewBase != null && mViewBase.mView != null
          && mViewBase.mView.getId() == Id)
      {
        ShowList.remove(mViewBase);
        
        if (ShowList.size() > 0)
        {
          mViewBase.mView.setVisibility(View.INVISIBLE);
          View mView = ShowList.get(ShowList.size()-1).mView;
          if (mView != null)
          {
            ActiveId = mView.getId();
            mView.setVisibility(View.VISIBLE); 
          }
        }
        break;
      }
    }

    if (0 == ShowList.size())
    {
      OnExitPage();
    }
    else
    {
      OnUpdatePage(param);  	
      ShowList.get(ShowList.size()-1).mView.setVisibility(View.VISIBLE);
    } 
  }
  
  // ÍË³ö²Ù×÷
  public abstract void OnExitPage();
  // 
  public abstract void OnUpdatePage(int param);
  
  class ViewCtrlBroadcastReceiver extends BroadcastReceiver
  {

    @Override
    public void onReceive(Context context, Intent intent)
    {
      // TODO Auto-generated method stub
      String action = intent.getAction();

      if (intent.getAction().equals(mAction))
      {
        int oper = intent.getIntExtra(VIEWCTRL_OPER, -1);
        int id = intent.getIntExtra(VIEWCTRL_ID, -1);
        int param = intent.getIntExtra(VIEWCTRL_PARAM, -1);
            
        Log.d("", "oper ["+oper+"] id ["+id+"]");
        
        switch (oper)
        {
          case VIEW_IN:
            AddShow(id,param);
            break;
            
          case VIEW_OUT:
            if (-1 == id)
            {
              DelShow(param);
            }
            else
            {
              DelShow(id,param);
            }
            break;
            
          case VIEW_EXIT:
            OnExitPage();
            break;
        }
      }
    }
    
  }
}
