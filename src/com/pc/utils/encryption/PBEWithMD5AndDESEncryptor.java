/**
 * @(#)StringEncrypter.java
 * @author jsl
 * @date 2013-9-6 Copyright 2013 it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.utils.encryption;

/**
 * @author jsl
 * @date 2013-9-6
 */

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import android.util.Base64;

public class PBEWithMD5AndDESEncryptor {

	/**
	 * 加密解密的密钥
	 */
	private final String mSeed;

	// 8-bytes Salt
	private final byte[] salt = {
			(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03
	};

	private final int iterationCount = 19;

	private SecretKey mSecretKey;
	private AlgorithmParameterSpec mParamSpec;

	public PBEWithMD5AndDESEncryptor(String seed) {
		mSeed = seed;

		// Iteration count
		try {
			KeySpec keySpec = new PBEKeySpec(seed.toCharArray(), salt, iterationCount);
			mSecretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

			// Prepare the parameters to the cipthers
			mParamSpec = new PBEParameterSpec(salt, iterationCount);
		} catch (InvalidKeySpecException e) {
		} catch (NoSuchAlgorithmException e) {
		}
	}

	/**
	 * Takes a single String as an argument and returns an Encrypted version of
	 * that String.
	 * @param str String to be encrypted
	 * @return <code>String</code> Encrypted version of the provided String
	 */
	public String encrypt(String str) {
		try {
			Cipher ecipher = Cipher.getInstance(mSecretKey.getAlgorithm());
			ecipher.init(Cipher.ENCRYPT_MODE, mSecretKey, mParamSpec);

			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			// return new sun.misc.BASE64Encoder().encode(enc);
			return new String(Base64.encode(enc, 0));
		} catch (BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		} catch (Exception e) {
		}

		return str;
	}

	/**
	 * Takes a encrypted String as an argument, decrypts and returns the
	 * decrypted String.
	 * @param str Encrypted String to be decrypted
	 * @return <code>String</code> Decrypted version of the provided String
	 */
	public String decrypt(String s) {
		try {
			Cipher dcipher = Cipher.getInstance(mSecretKey.getAlgorithm());
			dcipher.init(Cipher.DECRYPT_MODE, mSecretKey, mParamSpec);

			// Decode base64 to get bytes
			// byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
			// byte[] dec = Base64Coder.decode(str);

			// Decrypt
			byte[] dec = Base64.decode(s, 0);
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");
		} catch (BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * test
	 * @param key : 12345678
	 */
	public static void main(String[] args) throws Exception {
		String seed = "1234567890";
		String passwd = "passwd";
		String newPasswd = null;

		// 加密
		try {

			// newPasswd = AESEncryptor.encrypt(SEED, newPasswd);
			newPasswd = new PBEWithMD5AndDESEncryptor(seed).encrypt(passwd);

		} catch (Exception e) {
			newPasswd = passwd;
		}

		System.out.println("passwd ：" + newPasswd);

		// 解密
		try {
			String decryptPwd = new PBEWithMD5AndDESEncryptor(seed).decrypt(newPasswd);
			System.out.println("passwd " + decryptPwd);
		} catch (Exception e) {
		}
	}

}
