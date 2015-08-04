package com.intput.Service;

/**
 * Created by root on 13-12-28.
 */
public class FrameTypeDef {
	public class FrameIndex{
        public static final int FRAME_GJH = 4;
        public static final int FRAME_CQS = 10;
        public static final int FRAME_FF = 11;
        public static final int FRAME_CSM = 12;
        public static final int FRAME_JD = 13;
        public static final int FRAME_BS = 14;
        public static final int FRAME_DEFAULT = 15;
        
        public static final int FRAME_UNKNOW = 29;
        
        public static final int FRAME_YEAR = 30;
        public static final int FRAME_MON = 28;
        public static final int FRAME_DAY = 27;
        public static final int FRAME_HOUR = 26;
        public static final int FRAME_MIN = 25;
        public static final int FRAME_SEC = 24;
        
        public static final int FRAME_DATA = 32;
    }
	
    public class FrameCSM{
        public static final int FRAME_CSM_S = 0x00;
        public static final int FRAME_CSM_B = 0x01;
        public static final int FRAME_CSM_F = 0x02;
    }

    public class FrameBS{
        public static final int FRAME_BS_YES = 0x01;
        public static final int FRAME_BS_NO = 0x00;
    }
    
    public class FrameFF{
    	public static final int FRAME_FF_HT = 0x10;
        public static final int FRAME_FF_ZH = 0x08;
    }
    
    public class FrameJD{
    	public static final int FRAME_JD_90F = 0x10;
    	public static final int FRAME_JD_60F = 0xC4;
    	public static final int FRAME_JD_45F = 0xD3;
    	public static final int FRAME_JD_30F = 0xE2;
    	public static final int FRAME_JD_0 = 0x00;
        public static final int FRAME_JD_30Z = 0x1E;
        public static final int FRAME_JD_45Z = 0x2D;
        public static final int FRAME_JD_60Z = 0x3C;
        public static final int FRAME_JD_90Z = 0x5A;
    }
    
    public class FrameTH{
    	public final static int FRAME_TH_00 = 0;
    	public final static int FRAME_TH_05 = 5;
    	public final static int FRAME_TH_10 = 10;
    	public final static int FRAME_TH_15 = 15;
    	public final static int FRAME_TH_20 = 20;
    	public final static int FRAME_TH_25 = 25;
    	public final static int FRAME_TH_30 = 30;
    	public final static int FRAME_TH_35 = 35;
    	public final static int FRAME_TH_40 = 40;
    	public final static int FRAME_TH_45 = 45;
    	public final static int FRAME_TH_50 = 50;
    	public final static int FRAME_TH_55 = 55;
    	public final static int FRAME_TH_60 = 60;
    }
    
    public class FrameGLLX{
    	public final static int FRAME_GLLX_SS = 0;
    	public final static int FRAME_GLLX_LS = 1;
    }
    
    public class FrameQHDJ{
    	public final static int FRAME_QHDJ_C10 = 0;
    	public final static int FRAME_QHDJ_C15 = 1;
    	public final static int FRAME_QHDJ_C20 = 2;
    	public final static int FRAME_QHDJ_C25 = 3;
    	public final static int FRAME_QHDJ_C30 = 4;
    	public final static int FRAME_QHDJ_C35 = 5;
    	public final static int FRAME_QHDJ_C40 = 6;
    	public final static int FRAME_QHDJ_C45 = 7;
    	public final static int FRAME_QHDJ_C50 = 8;
    	public final static int FRAME_QHDJ_C55 = 9;
    	public final static int FRAME_QHDJ_C60 = 10;
    	public final static int FRAME_QHDJ_C65 = 11;
    	public final static int FRAME_QHDJ_C70 = 12;
    	public final static int FRAME_QHDJ_C75 = 13;
    	public final static int FRAME_QHDJ_C80 = 14;
    	public final static int FRAME_QHDJ_C85 = 15;
    	public final static int FRAME_QHDJ_C90 = 16;
    	
    	
    }
}
