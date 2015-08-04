package com.intput.Base;


/**
 * Created by root on 13-12-28.
 */
public class IntentDef {
    public static final String SERVICE_NAME_MAIN	="com.intput.Service.MainService";

    public static final String MODULE_MAIN			="intput.intent.action.MODULE_MAIN";
    public static final String BROADCAST_MAIN = "com.intput.action.MainActivity";
    public static final String MODULE_RESPONSION	="intput.intent.action.MODULE_RESPONSION";
    public static final String MODULE_DISTRIBUTE	="intput.intent.action.MODULE_DISTRIBUTE";

    public static final String INTENT_COMM_ID				="intput.intent.netcomm.ID";
    public static final String INTENT_COMM_CMD				="intput.intent.netcomm.CMD";
    public static final String INTENT_COMM_ACK				="intput.intent.netcomm.ACK";
    public static final String INTENT_COMM_DATA				="intput.intent.netcomm.DATA";
    public static final String INTENT_COMM_DATALEN			="intput.intent.netcomm.DATALEN";
    public static final int    INTENT__TYPE_INVALID           		=-1;
    
        
    public interface OnCommDataReportListener
    {
        public void OnResponsionReport(int Id, int Cmd, int Ack, byte[] Data, int DataLen);
        public void OnDistributeReport(int Id, int Cmd, int Ack, byte[] Data, int DataLen);
    }
    
    public interface OnStateReportListener
    {
        public void OnStateReport(int State, byte[] Param);
    }

    public static final int USBCORE_CONNECT = 0x01;
    public static final int USBCORE_DISCONNECT = 0x00;

    public interface OnUsbCoreReportListener
    {
        public void OnUsbCoreReport(int State);
    }
    
    public interface OnMenuEventListener
    {
    	 public void OnMenuEventReport(int param);
    }
    
}
