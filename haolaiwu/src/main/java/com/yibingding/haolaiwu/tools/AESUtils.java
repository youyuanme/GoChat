package com.yibingding.haolaiwu.tools;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class AESUtils {
	/**
	 * AES加密算法
	 * 
	 * @param content
	 *            �?要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static String key ="3BRNLO6PS185CKZDGUXA4EMY2HWF7TI0";
								 
	public static String encode(String stringToEncode) throws NullPointerException {

		try {
			SecretKeySpec skeySpec = getKey(key);
			byte[] clearText = stringToEncode.getBytes("UTF-8");
			final byte[] iv = new byte[16];
			Arrays.fill(iv, (byte) 0x00);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
			String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
			return encrypedValue.replace("\n", "");

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return "";
		// return stringToEncode;
	}

	private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {
		int keyLength = 256;
		byte[] keyBytes = new byte[keyLength / 8];
		Arrays.fill(keyBytes, (byte) 0x0);
		byte[] passwordBytes = password.getBytes("UTF-8");
		int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
		System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		return key;
	}
}
