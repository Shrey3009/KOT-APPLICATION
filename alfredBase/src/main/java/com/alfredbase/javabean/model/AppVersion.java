package com.alfredbase.javabean.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.text.TextUtils;

public class AppVersion {
	private static final String VERSION_DELIMITER = "\\.";
	private static final String ZERO_VERSION = "0.0.0";
	private String version;
	private int firstNum;
	private int secondNum;
	private int thirdNum;

	public AppVersion(String version) {
		if (TextUtils.isEmpty(version)) {
			version = ZERO_VERSION;
		}
		this.version = version;
		List<String> versionArray = parse(this.version);
		this.firstNum = Integer.parseInt(versionArray.get(0));
		this.secondNum = Integer.parseInt(versionArray.get(1));
		this.thirdNum = Integer.parseInt(versionArray.get(2));
	}

	private List<String> parse(String version) {
		List<String> versionArray = new ArrayList<String>();
		versionArray.addAll(Arrays.asList(version.split(VERSION_DELIMITER)));
		for (int i = versionArray.size(); i < 3; i++) {
			versionArray.add("0");
		}
		return versionArray;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getFirstNum() {
		return firstNum;
	}

	public void setFirstNum(int firstNum) {
		this.firstNum = firstNum;
	}

	public int getSecondNum() {
		return secondNum;
	}

	public void setSecondNum(int secondNum) {
		this.secondNum = secondNum;
	}

	public int getThirdNum() {
		return thirdNum;
	}

	public void setThirdNum(int thirdNum) {
		this.thirdNum = thirdNum;
	}

	public int compare(AppVersion serverAppVersion) {
		if (this.firstNum < serverAppVersion.firstNum) {
			return 1;
		} else if (this.firstNum == serverAppVersion.firstNum) {
			if (this.secondNum < serverAppVersion.secondNum) {
				return 1;
			} else if (this.secondNum == serverAppVersion.secondNum) {
				if (this.thirdNum < serverAppVersion.thirdNum) {
					return 1;
				}
			}
		}
		return -1;
	}

	@Override
	public String toString() {
		return "AppVersion [version=" + version + ", firstNum=" + firstNum
				+ ", secondNum=" + secondNum + ", thirdNum=" + thirdNum + "]";
	}

}
