package com.alfredposclient.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.alfredbase.utils.LogUtil;

public class AlfredRootCmdUtil {
	private static final String TAG = AlfredRootCmdUtil.class.getSimpleName();
	private static final String COMMAND_SH = "sh";
	private static final String COMMAND_LINE_END = "\n";
	private static final String COMMAND_EXIT = "exit\n";
	private static final String COMMAND_DUMP = ".dump";
	private static final String COMMAND_IN = "<";
	private static final String COMMAND_OUT = ">";
	@SuppressLint("SdCardPath") 
	public static final String COPY_FILE = Environment.getExternalStorageDirectory().getPath() + "alfred/com.puscene.posclient_assistant.sql";

	
	public static void executeOutputDB(String packagePathc){
		execute(packagePathc + " " + COMMAND_DUMP + " " + COMMAND_OUT + " " + COPY_FILE);
	}
	
	public static void executeInputDB(String packagePathc, String dbName){
		execute(packagePathc + " " + COMMAND_IN + " " + COPY_FILE);
	}
	
	/**
	 * 执行单条命令
	 * 
	 * @param command
	 * @return
	 */
	public static List<String> execute(String command) {
		return execute(new String[] { command });
	}

	/**
	 * 可执行多行命令（bat）
	 * 
	 * @param commands
	 * @return
	 */
	public static List<String> execute(String[] commands) {
		List<String> results = new ArrayList<String>();
		int status = -1;
		if (commands == null || commands.length == 0) {
			return null;
		}
		LogUtil.d(TAG, "execute command start : " + commands);
		Process process = null;
		BufferedReader successReader = null;
		BufferedReader errorReader = null;
		StringBuilder errorMsg = null;

		DataOutputStream dos = null;
		try {
			// TODO
			process = Runtime.getRuntime().exec(COMMAND_SH);
			dos = new DataOutputStream(process.getOutputStream());
			for (String command : commands) {
				if (command == null) {
					continue;
				}
				dos.write(command.getBytes());
				dos.writeBytes(COMMAND_LINE_END);
				dos.flush();
			}
			dos.writeBytes(COMMAND_EXIT);
			dos.flush();

			status = process.waitFor();

			errorMsg = new StringBuilder();
			successReader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			errorReader = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));
			String lineStr;
			while ((lineStr = successReader.readLine()) != null) {
				results.add(lineStr);
				LogUtil.d(TAG, " command line item : " + lineStr);
			}
			while ((lineStr = errorReader.readLine()) != null) {
				errorMsg.append(lineStr);
			}
			System.out.println("errorMsg" + errorMsg);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (successReader != null) {
					successReader.close();
				}
				if (errorReader != null) {
					errorReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (process != null) {
				process.destroy();
			}
		}
		LogUtil.d(TAG, String.format(Locale.CHINA,
				"execute command end,errorMsg:%s,and status %d: ", errorMsg,
				status));
		return results;
	}
}
