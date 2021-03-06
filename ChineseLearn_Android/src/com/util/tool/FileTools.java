package com.util.tool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

import com.hw.chineseLearn.base.CustomApplication;

public class FileTools {

	public static BufferedReader bufread;
	// 指定文件路径和名称
	private static String readStr = "";
	private static String SDPATH = "";

	/**
	 * 创建文本文件.
	 * 
	 * @throws IOException
	 * 
	 */
	public static void creatTxtFile(File filename) throws IOException {
		if (!filename.exists()) {
			filename.createNewFile();
			System.err.println(filename + "已创建！");
		}
	}

	public static boolean isExists(File file) {
		boolean b = false;
		if (file.exists())
			b = true;
		else
			b = false;
		return b;
	}

	public static void updateFile(String url, String filename, String savepath) {
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		HttpClient client = new DefaultHttpClient();
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			long length = entity.getContentLength();
			InputStream is = entity.getContent();
			FileOutputStream fileOutputStream = null;
			if (is != null) {
				File path = new File(savepath);
				if (!path.exists()) {
					path.mkdirs();
				}
				File file = new File(savepath, filename);
				if (!file.exists()) {
					fileOutputStream = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int ch = -1;
					int count = 0;
					while ((ch = is.read(buf)) != -1) {
						fileOutputStream.write(buf, 0, ch);
						count += ch;
						if (length > 0) {
						}
					}

				}
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("FileError", e.toString());
		}

	}

	/**
	 * 读取文本文件.
	 * 
	 */
	public static String readTxtFile(File filename) {
		String read;
		FileReader fileread;
		try {
			fileread = new FileReader(filename);
			bufread = new BufferedReader(fileread);
			try {
				while ((read = bufread.readLine()) != null) {
					readStr = readStr + read;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("文件内容是:" + "\r\n" + readStr);
		return readStr;
	}

	/**
	 * 读取文本文件.
	 * 
	 * @throws Exception
	 * 
	 */
	public static String readTxtFile(InputStream is) throws Exception {
		int size = is.available();
		// Read the entire asset into a local byte buffer.
		byte[] buffer = new byte[size];
		is.read(buffer);
		is.close();
		// Convert the buffer into a string.
		String text = new String(buffer, "UTF-8");
		return text;
	}

	/**
	 * 写文件.
	 * 
	 */
	public static void writeTxtFile(File filename, String newStr)
			throws IOException {
		// 先读取原有文件内容，然后进行写入操作
		String filein = newStr + "\r\n" + readStr + "\r\n";
		RandomAccessFile mm = null;
		try {
			mm = new RandomAccessFile(filename, "rw");
			mm.writeBytes(filein);
		} catch (IOException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		} finally {
			if (mm != null) {
				try {
					mm.close();
				} catch (IOException e2) {
					// TODO 自动生成 catch 块
					e2.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将文件中指定内容的第一行替换为其它内容.
	 * 
	 * @param oldStr
	 *            查找内容
	 * @param replaceStr
	 *            替换内容
	 */
	public static void replaceTxtByStr(File filename, String oldStr,
			String replaceStr) {
		String temp = "";
		try {
			// File file = new File(path);
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();

			// 保存该行前面的内容
			for (int j = 1; (temp = br.readLine()) != null
					&& !temp.equals(oldStr); j++) {
				buf = buf.append(temp);
				buf = buf.append(System.getProperty("line.separator"));
			}

			// 将内容插
			buf = buf.append(replaceStr);

			// 保存该行后面的内容
			while ((temp = br.readLine()) != null) {
				buf = buf.append(System.getProperty("line.separator"));
				buf = buf.append(temp);
			}

			br.close();
			FileOutputStream fos = new FileOutputStream(filename);
			PrintWriter pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param dbName
	 */
	public static void copyDb(String dbName) {
		try {
			InputStream inputStream = CustomApplication.app.getAssets().open(
					dbName);

			File filesDir = CustomApplication.app.getFilesDir();

			File addressDbFile = new File(filesDir, dbName);
			if (addressDbFile.exists() && addressDbFile.length() > 13000) {
				Log.d("....", "addressDbFile.exists");
			} else {

				FileOutputStream fout = new FileOutputStream(addressDbFile);
				int len = -1;
				byte[] buffer = new byte[1024];

				while ((len = inputStream.read(buffer)) != -1) {
					fout.write(buffer, 0, len);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压zip文件
	 * 
	 * @param context
	 * @param assetName
	 * @param outputDirectory
	 * @throws IOException
	 */
	public static void unZip(Context context, String assetName,
			String outputDirectory) throws IOException {
		// 创建解压目标目录
		File file = new File(outputDirectory);
		// 如果目标目录不存在，则创建
		if (!file.exists()) {
			file.mkdirs();
		}
		InputStream inputStream = null;
		// 打开压缩文件
		inputStream = context.getAssets().open(assetName);
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		// 读取一个进入点
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		// 使用1Mbuffer
		byte[] buffer = new byte[1024 * 1024];
		// 解压时字节计数
		int count = 0;
		// 如果进入点为空说明已经遍历完所有压缩包中文件和目录
		while (zipEntry != null) {
			// 如果是一个目录
			if (zipEntry.isDirectory()) {
				// String name = zipEntry.getName();
				// name = name.substring(0, name.length() - 1);
				file = new File(outputDirectory + File.separator
						+ zipEntry.getName());
				file.mkdir();
			} else {
				// 如果是文件
				file = new File(outputDirectory + File.separator
						+ zipEntry.getName());
				// 创建该文件
				file.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				while ((count = zipInputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, count);
				}
				fileOutputStream.close();
			}
			// 定位到下一个文件入口
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
	}

	public static boolean unZip(String unZipfileName, String mDestPath) {
		if (!mDestPath.endsWith("/")) {
			mDestPath = mDestPath + "/";
		}
		Log.d("unZip()", "目标文件：" + unZipfileName);
		Log.d("unZip()", "解压地址：" + mDestPath);

		FileOutputStream fileOut = null;
		ZipInputStream zipIn = null;
		ZipEntry zipEntry = null;
		File file = null;
		int readedBytes = 0;
		byte buf[] = new byte[4096];
		try {
			zipIn = new ZipInputStream(new BufferedInputStream(
					new FileInputStream(unZipfileName)));
			while ((zipEntry = zipIn.getNextEntry()) != null) {
				file = new File(mDestPath + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					file.mkdirs();
				} else {
					// 如果指定文件的目录不存在,则创建之.
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					fileOut = new FileOutputStream(file);
					while ((readedBytes = zipIn.read(buf)) > 0) {
						fileOut.write(buf, 0, readedBytes);
					}
					fileOut.close();
				}
				zipIn.closeEntry();
			}
			Log.d("unZip()", "解压成功！");
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		Log.e("unZip()", "解压失败！");
		return false;
	}

	/**
	 * @param context
	 * @param assetsPath
	 * @return
	 */
	public static String getJsonFromFile(Context context, String filename) {
		try {

			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line);
			}
			br.close();
			fis.close();
			return stringBuilder.toString();
		} catch (Exception e) {
			Log.e("getJsonFromFlie", "get json failed!");
		}
		return "";
	}

	/**
	 * @param context
	 * @param assetsPath
	 * @return
	 */
	public static String getJsonFromAsset(Context context, String assetsPath) {
		try {

			InputStreamReader inputStreamReader = new InputStreamReader(context
					.getAssets().open(assetsPath), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			bufferedReader.close();
			inputStreamReader.close();
			return stringBuilder.toString();
		} catch (Exception e) {
			Log.e("getJsonFromAsset", "get json failed!");
		}
		return "";
	}
}
