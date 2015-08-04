package com.intput.Base;

import java.util.Arrays;

import android.content.Context;
import android.util.Log;

public class CalculationHelper {
	
	public static final String TAG = "CalculationHelper";
	public Context mContext = null;
	
	public float AvgTH = 0;
	
	public CalculationHelper(Context context){
		mContext = context;
	}
	
	public String CalculationInputDB(){
		return null;
	}
	
	public float[] getDataByte(float[] data){
		float[] newdata = new float[10];
		Arrays.sort(data);  //进行排序
		for(int i = 0; i < 10; i++){
			newdata[i] = (data[i+3]);
		}
		return newdata;
	}
	
	public float getDataAvg(float[] data){
		float avg = 0x00;
		float allcount = 0x00;
		for(int i = 0; i < data.length; i++){
			allcount += data[i];
		}
		avg = (allcount/data.length);
		float tmp = Math.round(avg*10);
		avg = tmp/10; 
		return avg;
	}
	
	public float getDataAvgH(float[] data){
		float avg = 0x00;
		float allcount = 0x00;
		for(int i = 0; i < data.length; i++){
			allcount += data[i];
		}
		avg = (allcount/data.length);
		float tmp = Math.round(avg*100);
		avg = tmp/100; 
		return avg;
	}
		
	public float getThAvg(float[] data){
		float avg = 0;
		float allcount = 0;
		for(int i = (data.length-3); i < data.length; i++){
			allcount += data[i];
		}
		avg = (allcount/3);
		return avg;
	}
	
	public float getDataArrayAvg(float[] data){
		float[] newdata = getDataByte(data);
		float avg = getDataAvg(newdata);
		return avg;
	}
	
	public float getThArrayAvg(float[] data){
		Arrays.sort(data);  //进行排序
		float avg = getThAvg(data);
		float tmp = (float)Math.round (avg*2);
		avg = (float) (tmp/2);
		return avg;
	}
	
	public float getHTAndBS(float[] data1,float th){
		float data = getDataArrayAvg(data1);
		//float dm = (float) (-0.0173*th);
		//float ret = (float) (0.034488*Math.pow(data, 1.9400)*Math.pow(10,dm));
		float dm = (float) (-0.010737*th);
		float ret = (float) (0.024408*Math.pow(data, 2.03222)*Math.pow(10,dm));
		float tmp = Math.round(ret*10);
		ret = tmp/10; 
		return ret; 
	}
	
	public float getHTAndBSFAndSS(float[] data1,float th){
		float data = getDataArrayAvg(data1);
		float dm1 = (float) Math.pow(1.3241,(-1*th));
		float dm = (float) (-0.2433*(1.0-dm1));
		float ret = (float) (0.013187*Math.pow(data,2.1890)*Math.pow(10,dm));
		float tmp = Math.round(ret*10);
		ret = tmp/10; 
		return ret;
	}
	
	public float getHTAndBSFAndLS(float[] data1,float th){
		float data = getDataArrayAvg(data1);
		float dm1 = (float) Math.pow(1.4841,(-1*th));
		float dm = (float) (-0.2252*(1.0-dm1));
		float ret = (float) (0.007153*Math.pow(data,2.3244)*Math.pow(10,dm));
		float tmp = Math.round(ret*10);
		ret = tmp/10; 
		return ret;
	}
	
	public float getGQHT(float[] data1){
		float data = getDataArrayAvg(data1);
		float ret = (float) (2.4121*Math.pow(data,0.9023));
		float tmp = Math.round(ret*10);
		ret = tmp/10; 
		return ret;
	}
	
//	public float getStandardDevition(float[] data1){
//		float sum = 0;
//        float avg = getDataAvgH(data1);
//        for(int i = 0;i < data1.length;i++){
//            sum += Math.sqrt((data1[i] -avg) * (data1[i] -avg));
//        }
//        if(data1.length == 1){
//        	return sum;
//        }
//        float ret = (sum / (data1.length - 1)); 
//        float tmp = Math.round(ret*100);
//		ret = tmp/100; 
//		return ret;
//    }
	
	public float getStandardDevition(float[] data1){
		float sum = 0;
        float avg = getDataAvgH(data1);
        for(int i = 0;i < data1.length;i++){
            sum += (data1[i] -avg) * (data1[i] -avg);
        }
        if(data1.length == 1){
        	return 0;
        }
        float ret = (float) Math.sqrt(sum / (data1.length - 1)); 
        float tmp = Math.round(ret*100);
		ret = tmp/100; 
		return ret;
    }

	
	public float getTDQDZAndHT(float[] data1, byte CQS){
		float ret = -1;
		float min = 1000;
		float max = 0;
		boolean low =false;
		boolean high =false;
		float avg = getDataAvg(data1);
		float bzc = getStandardDevition(data1);
		for (int i = 0; i < data1.length; i++) {
			if(data1[i] < 10){
				low = true;
			}else{
				if(data1[i] > 60){
					high = true;
				}
			}
			if(min > data1[i]){
				min = data1[i];
			}else{
				if(max < data1[i]){
					max = data1[i];
				}
			}
		}
		
		if(low)
		{
			return -1000;
		}
		else
		{
			if(high || CQS < 10){
				return min;
			}
		}
		ret = (float) (avg - 1.645*bzc);
		float tmp = Math.round(ret*10);
		ret = tmp/10; 
		return ret;
	}
	
	public float getTDQDZAndGQHT(float[] data1, byte CQS){
		float ret = -1;
		float min = 1000;
		float max = 0;
		boolean low =false;
		boolean high =false;
		float avg = getDataAvg(data1);
		float bzc = getStandardDevition(data1);
		for (int i = 0; i < data1.length; i++) {
			if(data1[i] < 50){
				low = true;
			}else{
				if(data1[i] > 90){
					high = true;
				}
			}
			if(min > data1[i]){
				min = data1[i];
			}else{
				if(max < data1[i]){
					max = data1[i];
				}
			}
		}
		
		if(low)
		{
			return -1000;
		}
		else
		{
			if(high || CQS < 10){
				return min;
			}
		}
		ret = (float) (avg - 1.645*bzc);
		float tmp = Math.round(ret*10);
		ret = tmp/10; 
		return ret;
	}
}
