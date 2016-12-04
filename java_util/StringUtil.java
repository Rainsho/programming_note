package com.rainsho.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StringUtil {
	public static String randomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(62);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}

	public static String fmtTime(Timestamp t) {
		int delta = (int) (((new Date().getTime()) - t.getTime()) / 1000);
		if (delta < 60) {
			return "1分钟前";
		} else if (delta < 3600) {
			return delta / 60 + "分钟前";
		} else if (delta < 86400) {
			return delta / 3600 + "小时前";
		} else if (delta < 604800) {
			return delta / 86400 + "天前";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			return sdf.format(t);
		}
	}

	public static String MD5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			return new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
