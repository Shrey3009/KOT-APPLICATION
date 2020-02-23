package com.alfredbase.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

public class MD5Util {
	private static final String HEXES = "0123456789abcdef";
	public static String md5(String data) {
		if(TextUtils.isEmpty(data))
			return "";
		MessageDigest engine = null;
		try {
			engine = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] digest = engine.digest(data.getBytes());
		StringBuffer hexBuilder = new StringBuffer(digest.length * 2);
		for (final byte b : digest)
			hexBuilder.append(HEXES.charAt((b & 0xf0) >> 4)).append(
					HEXES.charAt((b & 0x0f)));
		return hexBuilder.toString();
	}
}
