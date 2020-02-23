package com.alfredposclient.http.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;

import com.alfredbase.APPConfig;

public class FileHandler {
	private PrintStream out;
	private String request;

	public FileHandler(PrintStream out, String request) {
		this.out = out;
		this.request = request;
	}

	public void handler() {
		File f = new File(APPConfig.FILE_SERVER_DIRECTORY + request);
		if (!f.isDirectory()) {
			if (f.exists())
				sendFile(f);
			else {
				send404();
			}
		} else {
			send404();
		}

	}

	private void send404() {
		out.println("HTTP/1.1 404 Not Found");
		out.println();
	}

	private void send200() {
		out.println("HTTP/1.1 200 OK");
		out.println();
	}

	private void sendFile(File f) {
		try {
			send200();
			int BUFFER_SIZE = 1024 * 20;
			byte buffer[] = new byte[BUFFER_SIZE];
			FileInputStream fis = new FileInputStream(f);
			int length = -1;
			while ((length = fis.read(buffer, 0, BUFFER_SIZE)) != -1) {
				out.write(buffer, 0, length);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
