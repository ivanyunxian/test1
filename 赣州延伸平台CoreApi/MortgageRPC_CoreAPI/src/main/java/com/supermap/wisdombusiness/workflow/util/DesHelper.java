package com.supermap.wisdombusiness.workflow.util;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 
 * 作者： zzq 时间： Jun 30, 2016 1:17:34 PM 功能： des加密解密帮助类
 */
public class DesHelper {
	private byte[] desKey;
	private static void test() throws Exception {
		String key = "??6??>${";
		String value = "2123";
		String jiami = java.net.URLEncoder.encode(value, "utf-8").toLowerCase();
		System.out.println("加密数据:" + jiami);
		String a = toHexString(encrypt(jiami, key)).toUpperCase();

		System.out.println("加密后的数据为:" + a);
		String b = java.net.URLDecoder.decode(decrypt(a, key), "utf-8");
		System.out.println("解密后的数据:" + b);
	}

	/**
	 * 海口市加密方法
	 * 
	 * @param message
	 * @return
	 */
	public static String encrypt_haikou(String value) {
		try {
			String key = "??6??>${";
			String jiami = java.net.URLEncoder.encode(value, "utf-8").toLowerCase();
			String s = toHexString(encrypt(jiami, key)).toUpperCase();
			return s;
		} catch (Exception e) {
			return value;
		}
	}

	/**
	 * 海口市解密方法
	 * 
	 * @param message
	 * @return
	 */
	public static String decrypt_haikou(String message) {
		try {
			String key = "??6??>${";
			String decr = decrypt(message, key);
			//System.out.println("半解密后：" + decr);
			String s = java.net.URLDecoder.decode(decr, "utf-8");
			//System.out.println("全解密后：" + s);
			return s;
		} catch (Exception e) {
			return message;
		}
	}

	// 解密数据
	public static String decrypt(String message, String key) throws Exception {
		byte[] bytesrc = convertHexString(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte);
	}

	// 加密
	public static byte[] encrypt(String message, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		return cipher.doFinal(message.getBytes("UTF-8"));
	}

	private static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}
		return digest;
	}

	private static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}
		return hexString.toString();
	}
}
