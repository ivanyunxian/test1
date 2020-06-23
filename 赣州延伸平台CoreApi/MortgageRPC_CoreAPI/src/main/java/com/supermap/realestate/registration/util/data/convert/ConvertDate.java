package com.supermap.realestate.registration.util.data.convert;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertDate {
	public static Date toDateTime(String value) throws Exception {
		return toDateTime(value, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date toDateTime(String value, String formatString)
			throws Exception {
		Date dt = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		try {
			dt = sdf.parse(value);
		} catch (Exception e) {
			throw e;
		}
		return dt;
	}

	public static Date toDateTime(Date value) throws Exception {
		return value;
	}

	public static Date toDateTime(char value) throws Exception {
		throw new Exception("从 char 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(short value) throws Exception {
		return new Date(value);
	}

	public static Date toDateTime(boolean value) throws Exception {
		throw new Exception("从 boolean 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(byte value) throws Exception {
		throw new Exception("从 byte 型 转换到 Date 时出现错误!");
	}

	public static Date toDateTime(int value) throws Exception {
		return new Date(value);
	}
}
