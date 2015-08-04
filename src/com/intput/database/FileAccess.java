package com.intput.database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;

public class FileAccess{
	
	public Context mContext = null;
	
	public FileAccess(Context context){
		mContext = context;
	}
	
	/**
	 * һ��˽���ļ����µ��ļ���ȡ��/data/data/����/files��
	 * 
	 * @param fileName
	 * @param message
	 */
	public void writeFileData(String fileName, String message) {
		try {
			FileOutputStream fout = mContext.openFileOutput(fileName, mContext.MODE_PRIVATE);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * //���ļ���./data/data/����/files/����
	 * 
	 * @param fileName
	 * @return
	 */
	public String readFileData(String fileName) {
		String res = "";
		try {
			FileInputStream fin = mContext.openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * д�� ��sdcardĿ¼�ϵ��ļ���Ҫ��FileOutputStream�� ������openFileOutput
	 * ��ͬ�㣺openFileOutput����raw�������ģ�FileOutputStream���κ��ļ�������
	 * @param fileName
	 * @param message
	 */
	// д��/mnt/sdcard/Ŀ¼������ļ�
	public void writeFileSdcard(String fileName, String message) {

		try {

			// FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

			FileOutputStream fout = new FileOutputStream(fileName);

			byte[] bytes = message.getBytes();

			fout.write(bytes);

			fout.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

	// ����/mnt/sdcard/Ŀ¼������ļ�

	public String readFileSdcard(String fileName) {

		String res = "";

		try {

			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];

			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			fin.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		return res;

	}


	/**
	 * ������resource�е�raw�ļ����л�ȡ�ļ�����ȡ���ݣ���Դ�ļ�ֻ�ܶ�����д��
	 * 
	 * @param fileInRaw
	 * @return
	 */
	public String readFromRaw(int fileInRaw) {
		String res = "";
		try {
			InputStream in = mContext.getResources().openRawResource(fileInRaw);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "GBK");
			// res = new String(buffer,"GBK");
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * ������asset�л�ȡ�ļ�����ȡ���ݣ���Դ�ļ�ֻ�ܶ�����д��
	 * 
	 * @param fileName
	 * @return
	 */
	public String readFromAsset(String fileName) {
		String res = "";
		try {
			InputStream in = mContext.getResources().getAssets().open(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}



}

