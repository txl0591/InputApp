package com.intput.Base;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer;

import com.inputapp.main.R;
import com.intput.Service.FrameTypeDef.FrameBS;
import com.intput.Service.FrameTypeDef.FrameCSM;
import com.intput.Service.FrameTypeDef.FrameFF;
import com.intput.Service.FrameTypeDef.FrameGLLX;
import com.intput.Service.FrameTypeDef.FrameJD;
import com.intput.Service.FrameTypeDef.FrameQHDJ;
import com.intput.Service.FrameTypeDef.FrameTH;

import android.os.Environment;
import android.util.Log;


public class Common {
	
	/***
	 * byte[]转换成 short
	 * @param data  
	 * 		byte数组
	 * @param start 
	 * 		byte数组的开始位置
	 * @return 
	 */
	public static short bytes2short(byte[] data, int start){
		short ret;
		if (data.length - start >= 2) {
			ret = (short) (((data[start+1] & 0xff) << 8) | (data[start] & 0xff));
			return ret;
		} else
			return 0;
	}
	
	public static short bytes2short_2(byte[] data, int start){
		short ret;
		if (data.length - start >= 2) {
			ret = (short) (((data[start] & 0xff) << 8) | (data[start + 1] & 0xff));
			return ret;
		} else
			return 0;
	}
	/**
	 * short 转换成 byte[]
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] short2bytes(short s) {
		byte[] b = new byte[4];
		for (int i = 0; i < 2; i++) {
			b[i] = (byte) (s >>> (8 - i * 8));
		}
		return b;
	}

	/**
	 * byte[]转换成 int
	 * 
	 * @param data
	 *            ：数组
	 * @param start
	 *            :byte数组的开始位置
	 * 
	 * @return
	 */
	public static int bytes2int(byte[] data, int start) {
		int ret;
		if (data.length - start >= 4) {
			ret = (int) (((data[start + 3] & 0xff) << 24)
					| ((data[start + 2] & 0xff) << 16)
					| ((data[start + 1] & 0xff) << 8) | (data[start] & 0xff));
			return ret;
		} else
			return 0;
	}

	/**
	 * int变成byte[]
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	// IP地址转换：从int转换String
	public static String int2IPAddr(int num) {
		byte[] b = int2bytes(num);
		int[] iAry = new int[4];
		for (int i = 0; i < 4; i++) {
			iAry[i] = b[i] & 0xff;
		}
		String IPAddr = String.format("%d.%d.%d.%d", iAry[0], iAry[1], iAry[2],
				iAry[3]);
		return IPAddr;
	}

	/** IP地址转换：从String转换int
	 * @param IPAddr
	 * @return
	 */
	public static int IPAddr2int(String IPAddr)
	{
		byte[] ip=new byte[4]; 
		int position1=IPAddr.indexOf("."); 
		int position2=IPAddr.indexOf(".",position1+1); 
		int position3=IPAddr.indexOf(".",position2+1); 
		ip[0]=Byte.parseByte(IPAddr.substring(0,position1)); 
		ip[1]=Byte.parseByte(IPAddr.substring(position1+1,position2)); 
		ip[2]=Byte.parseByte(IPAddr.substring(position2+1,position3)); 
		ip[3]=Byte.parseByte(IPAddr.substring(position3+1)); 
		return bytes2int(ip,0); 
	}
	
	/** IP地址转换：从String转换int
	 * @param IPAddr
	 * @return
	 */
	public static int ipToint(String strIp){   
		int[] ip = new int[4];   
	    //先找到IP地址字符串中.的位置   
	    int position1 = strIp.indexOf(".");   
	    int position2 = strIp.indexOf(".", position1 + 1);   
	    int position3 = strIp.indexOf(".", position2 + 1);   
	    //将每个.之间的字符串转换成整型   
	    ip[0] = Integer.parseInt(strIp.substring(0, position1));   
	    ip[1] = Integer.parseInt(strIp.substring(position1+1, position2));   
	    ip[2] = Integer.parseInt(strIp.substring(position2+1, position3));   
	    ip[3] = Integer.parseInt(strIp.substring(position3+1));  
	    return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];   
	}
	
	/** IP地址转换：从int转换String
	 * @param IPAddr
	 * @return
	 */
	 public static String intToIP(int intIp) {   
         StringBuffer sb = new StringBuffer("");   
        //直接右移24位   
         sb.append(String.valueOf((intIp >>> 24)));   
         sb.append(".");   
        //将高8位置0，然后右移16位   
         sb.append(String.valueOf((intIp & 0x00FFFFFF) >>> 16));   
         sb.append(".");   
        //将高16位置0，然后右移8位   
         sb.append(String.valueOf((intIp & 0x0000FFFF) >>> 8));   
         sb.append(".");   
        //将高24位置0   
         sb.append(String.valueOf((intIp & 0x000000FF)));   
        return sb.toString();   
     }
	
	 /**
	  * 字符串转换(消除C语言中的结束符等转义符)
	  * @param data
	  * @return
	  * @throws UnsupportedEncodingException
	  */
	 public static String byteToString(byte[] data){
		 int index = data.length;
		 for(int i = 0; i < data.length; i++){
			 if(data[i] == 0){
				 index = i;
				 break;
			 }
		 }
		 byte[] temp = new byte[index];
		 Arrays.fill(temp, (byte) 0);
		 System.arraycopy(data,0,temp,0,index);
		 String str;
		try {
			str = new String(temp,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		 return str;
	 }
	 
	 /**
	  * 字符串截取(消除通配符)
	  * @param data
	  * @return
	  * @throws UnsupportedEncodingException
	  */
	 public static String getNewString(String str){
		 byte[] data = str.getBytes();
		 int index = data.length;
		 for(int i = 0; i < data.length; i++){
			 if(data[i] < 48 || data[i] > 57){
				 index = i;
				 break;
			 }
		 }
		 byte[] temp = new byte[index];
		 System.arraycopy(data,0,temp,0,index);
		 String res;
		try {
			res = new String(temp,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		 return res;
	 }
	 /*** 获取文件个数 by path***/
	 public static long getfilenumber(String path){
		 long size =0;
		 File file = new File(path);
		 size = getfile(file);
		 return size;
	 }
	 
	 /*** 获取文件个数 ***/
	public static long getfile(File f) {// 递归求取目录文件个数
		
		long size = 0;
		File flist[] = f.listFiles();
		if(flist != null){
			size = flist.length;
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getfile(flist[i]);
					size--;
				}
			}
		}else{
			size = -1;
		}
		return size;
	}
	
	/*** 获取最早的文件***/
	public static String getlastfile(String Path) {
		File file = new File(Path);
		if (!file.exists())
			file.mkdir();
		File flist[] = file.listFiles();
		long[] arr = new long[flist.length];
		long max = 0;
		int i = 0;
		String name;
		String path;
		if (flist.length == 0) {
			return null;
		}
		for (i = 0; i < flist.length; i++) {
			name = flist[i].getName();
			StringTokenizer st = new StringTokenizer(name, ".");
			arr[i] = Long.valueOf(st.nextToken());
		}
		max = getMax(arr);
		path = Path + Long.toString(max) + ".jpg";
		return path;
	}
	
	/*** 获取最后的文件***/
	public static String getfristfile(String Path) {
		File file = new File(Path);
		if (!file.exists())
			file.mkdir();
		File flist[] = file.listFiles();
		long[] arr = new long[flist.length];
		long min = 0;
		int i = 0;
		String name;
		String path;
		for (i = 0; i < flist.length; i++) {
			name = flist[i].getName();
			StringTokenizer st = new StringTokenizer(name, ".");
			arr[i] = Long.valueOf(st.nextToken());
		}
		min = getMin(arr);
		path = Path + Long.toString(min) + ".jpg";
		return path;

	}
	
	public static String getlastfile(String filepath_1,String filepath_2){
		String lastpath = null;
		long long_1,long_2;
		if(filepath_1 == null && filepath_2 == null){
			return null;
		}else if(filepath_1 == null && filepath_2 != null){
			return filepath_2;
		}else if(filepath_1 != null && filepath_2 == null){
			return filepath_1;
		}else{
			String[] strings_1 = filepath_1.split("/");
			StringTokenizer st_1 = new StringTokenizer(strings_1[strings_1.length-1], ".");
			long_1 = Long.valueOf(st_1.nextToken());
			String[] strings_2 = filepath_2.split("/");
			StringTokenizer st_2 = new StringTokenizer(strings_2[strings_2.length-1], ".");
			long_2 = Long.valueOf(st_2.nextToken());
			if(long_1 > long_2){
				lastpath = filepath_1;
			}else if(long_1 == long_2){
				lastpath = filepath_1;
			}else{
				lastpath = filepath_2;
			}
			return lastpath;
		}	
	}

	/*** 取最大值***/	
	public static long getMax(long[] arr) {
		long max = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		return max;
	}
	
	public static double getMax(double[] arr) {
		double max = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		return max;
	}
	
	public static int getMax(int[] arr) {
		int max = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		return max;
	}
	
	/*** 取最小值***/
	public static long getMin(long[] arr) {
		long min = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] < min) {
				min = arr[i];
			}
		}
		return min;
	}

	public static void delete_file(String path) {
		File file = new File(path);
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			}
		}
	}
	
	/**
     * 递归删除文件和文件夹
     * @param file    要删除的根目录
     */
    public static void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

	
	public static void isExist(String path) {
		File file = new File(path);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	public static String DateToStr(Date date) {
		  
		   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   String str = format.format(date);
		   return str;
		} 

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return date;
	}

	// 判断SD卡是否存在
	public static boolean hasSdCard() {
		boolean sResult = false;

		String stateString = null;
		try {
			Class c = Class.forName("android.os.SystemProperties");
			Method m[] = c.getDeclaredMethods();

			try {
				try {
					stateString = (String) m[1].invoke(null,
							"EXTERNAL_STORAGE_STATE", "unmounted");
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (stateString != null) {
			if (stateString.equals(Environment.MEDIA_MOUNTED)) {
				sResult = true;
			}
		}
		return sResult;
	}

	public static void Decrypt(byte[] InChar, byte[] OutChar) {
		int i, c;

		int jiemibox[] = { 15, 18, 3, 20, 21, 16, 10, 11, 14, 8, 24, 12, 9, 5,
				1, 6, 19, 2, 22, 17, 13, 7, 23, 4 };
		for (i = 0; i < InChar.length; i++) {
			c = i / 24;
			if (InChar[i] > jiemibox[i % 24]) {
				OutChar[jiemibox[i % 24] + c * 24 - 1] = (byte) (InChar[i]
						- jiemibox[i % 24] + 1);
			} else {
				OutChar[jiemibox[i % 24] + c * 24 - 1] = (byte) (InChar[i]
						+ 256 - jiemibox[i % 24] + 1);
			}
		}
	}

	public static void Encryption(byte[] InChar, byte[] OutChar) {
		int i, c;

		int jiemibox[] = { 15, 18, 3, 20, 21, 16, 10, 11, 14, 8, 24, 12, 9, 5,
				1, 6, 19, 2, 22, 17, 13, 7, 23, 4 };

		for (i = 0; i < InChar.length; i++) {
			c = i / 24;
			OutChar[i] = (byte) (InChar[jiemibox[i % 24] + c * 24 - 1]
					+ jiemibox[i % 24] - 1);
		}
	}

	public static String getMac() {
		String mac = null;
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec(
					"cat /sys/class/net/wlan0/address");
			// Process pp =
			// Runtime.getRuntime().exec("cat /sys/class/net/eth0/address");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();
					break;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		if (macSerial != null) {
			macSerial = macSerial.replace(":", "");
			macSerial = macSerial.replace("a", "A");
			macSerial = macSerial.replace("b", "B");
			macSerial = macSerial.replace("c", "C");
			macSerial = macSerial.replace("d", "D");
			macSerial = macSerial.replace("e", "E");
			mac = macSerial.replace("f", "F");
		}

		return mac;

	}

	public static byte[] STR2BCD(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	public static void PrintHex(String Tag, byte[] buffer, int size) {
		String msg = "";
		for (int i = 0; i < size; i++) {
			msg = String.format("%s 0x%02X", msg, buffer[i]);
		}
		Log.d(Tag, "HexData=[" + msg + "]");
	}

	public static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	public static int getJDIndex(int JD) {
		int index = -1;
		int FrameJDList[] = { FrameJD.FRAME_JD_90F, FrameJD.FRAME_JD_60F,
				FrameJD.FRAME_JD_45F, FrameJD.FRAME_JD_30F, FrameJD.FRAME_JD_0,
				FrameJD.FRAME_JD_30Z, FrameJD.FRAME_JD_45Z,
				FrameJD.FRAME_JD_60Z, FrameJD.FRAME_JD_90Z, };

		for (int i = 0; i < FrameJDList.length; i++) {
			if (FrameJDList[i] == JD) {
				index = i;
				break;
			}
		}

		return index;
	}
	
	public static int getJDValueEx(int JD) {
		int index = -1;
		int FrameJDList[] = { FrameJD.FRAME_JD_90F, FrameJD.FRAME_JD_60F,
				FrameJD.FRAME_JD_45F, FrameJD.FRAME_JD_30F, FrameJD.FRAME_JD_0,
				FrameJD.FRAME_JD_30Z, FrameJD.FRAME_JD_45Z,
				FrameJD.FRAME_JD_60Z, FrameJD.FRAME_JD_90Z, };
		int FrameJD[] = {-90,-60,-45,-30,0,30,45,60,90};
		for (int i = 0; i < FrameJDList.length; i++) {
			if (FrameJDList[i] == JD) {
				index = i;
				break;
			}
		}
		return FrameJD[index];
	}

	public static int getFFIndex(int FF) {
		int index = -1;
		int FrameFFList[] = { FrameFF.FRAME_FF_HT, FrameFF.FRAME_FF_ZH };

		for (int i = 0; i < FrameFFList.length; i++) {
			if (FrameFFList[i] == FF) {
				index = i;
				break;
			}
		}

		return index;
	}

	public static int getBSIndex(int BS) {
		int index = -1;
		int FrameBSList[] = { FrameBS.FRAME_BS_YES, FrameBS.FRAME_BS_NO };

		for (int i = 0; i < FrameBSList.length; i++) {
			if (FrameBSList[i] == BS) {
				index = i;
				break;
			}
		}

		return index;
	}

	public static int getCSMIndex(int CSM) {
		int index = -1;
		int FrameCSMList[] = { FrameCSM.FRAME_CSM_S, FrameCSM.FRAME_CSM_B,
				FrameCSM.FRAME_CSM_F };

		for (int i = 0; i < FrameCSMList.length; i++) {
			if (FrameCSMList[i] == CSM) {
				index = i;
				break;
			}
		}

		return index;
	}

	public static int getTHIndex(int TH) {
		int index = -1;
		int FrameTHList[] = { FrameTH.FRAME_TH_00, FrameTH.FRAME_TH_05,
				FrameTH.FRAME_TH_10, FrameTH.FRAME_TH_15, FrameTH.FRAME_TH_20,
				FrameTH.FRAME_TH_25, FrameTH.FRAME_TH_30, FrameTH.FRAME_TH_35,
				FrameTH.FRAME_TH_40, FrameTH.FRAME_TH_45, FrameTH.FRAME_TH_50,
				FrameTH.FRAME_TH_55, FrameTH.FRAME_TH_60, };

		for (int i = 0; i < FrameTHList.length; i++) {
			if (FrameTHList[i] == TH) {
				index = i;
				break;
			}
		}

		return index;
	}

	public static float getTHData(int TH) {
		float[] Fth = { (float) 0.0, (float) 0.5, (float) 1.0, (float) 1.5,
				(float) 2.0, (float) 2.5, (float) 3.0, (float) 3.5,
				(float) 4.0, (float) 4.5, (float) 5.0, (float) 5.5, (float) 6.0 };
		int index = getTHIndex(TH);

		return Fth[index];
	}

	public static String bcd2StrIn(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
				.toString().substring(1) : temp.toString();
	}

	public static float bcd2Str(byte bytes) {
		byte[] tmp = new byte[1];
		tmp[0] = bytes;
		String Data = bcd2StrIn(tmp);
		return Float.valueOf(Data);
	}

	public static int getGLLXIndex(int GLLX) {
		int index = -1;
		int FrameGLLXList[] = { 
				FrameGLLX.FRAME_GLLX_SS,FrameGLLX.FRAME_GLLX_LS};

		for (int i = 0; i < FrameGLLXList.length; i++) {
			if (FrameGLLXList[i] == GLLX) {
				index = i;
				break;
			}
		}

		return index;
	}

	public static int getQHDJIndex(int QHDJ) {
		int index = -1;
		int FrameQHDJList[] = { FrameQHDJ.FRAME_QHDJ_C10,
				FrameQHDJ.FRAME_QHDJ_C15, FrameQHDJ.FRAME_QHDJ_C20,
				FrameQHDJ.FRAME_QHDJ_C25, FrameQHDJ.FRAME_QHDJ_C30,
				FrameQHDJ.FRAME_QHDJ_C35, FrameQHDJ.FRAME_QHDJ_C40,
				FrameQHDJ.FRAME_QHDJ_C45, FrameQHDJ.FRAME_QHDJ_C50,
				FrameQHDJ.FRAME_QHDJ_C55, FrameQHDJ.FRAME_QHDJ_C60,
				FrameQHDJ.FRAME_QHDJ_C65, FrameQHDJ.FRAME_QHDJ_C70,
				FrameQHDJ.FRAME_QHDJ_C75, FrameQHDJ.FRAME_QHDJ_C80,
				FrameQHDJ.FRAME_QHDJ_C85, FrameQHDJ.FRAME_QHDJ_C90, };

		for (int i = 0; i < FrameQHDJList.length; i++) {
			if (FrameQHDJList[i] == QHDJ) {
				index = i;
				break;
			}
		}

		return index;
	}

	public static String getMacVersionID() {
		int index = 0;
		String Mac = getMac();
		String CrcOk = "";
		byte[] MacByte = Mac.getBytes();
		String[] Crc = { "F", "3", "A", "B", "E", "8", "C", "9", "5", "1", "6",
				"2", "0", "D", "7", "4" };
		byte[] MacByteCrc = new byte[MacByte.length];
		for (int i = 0; i < MacByte.length; i++) {
			byte tmp = MacByte[i];
			if (tmp >= 0x30 && tmp <= 0x39) {
				index = tmp - 0x30;
			} else {
				index = tmp - 0x41 + 10;
			}
			CrcOk = CrcOk + Crc[index];
		}
		return CrcOk;
	}

	public static boolean geFileIsExists(String Path) {

		if (Path == null) {
			return false;
		}

		try {
			File f = new File(Path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

		return true;
	}
	
	public static float ByteTOBCD(byte param){
		byte high = 0;
		byte low = 0;
		
		low = (byte) (param&0x0F);
		high =  (byte) (param>>4);
		
		float ret = high*10+low;
		return ret;
	}
}
