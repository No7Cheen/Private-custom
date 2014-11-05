package com.pc.utils.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 */
public class PcMd5 {

	// 全局数组
	public final static String[] strDigits = {
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
	};

	public final static char hexDigits[] = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};

	/**
	 * MD5加密
	 * 
	 * @param str 要加密的字符串
	 * @return String 加密的字符串
	 */
	public final static String MD5(String str) {
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
		};

		try {
			byte[] strTemp = str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte tmp[] = mdTemp.digest(); // MD5 的计算结果是一个 128 位的长整数，

			// 用字节表示就是 16 个字节，每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
			char strs[] = new char[16 * 2];
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节，转换成 16
											// 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>
															// 为逻辑右移，将符号位一起右移
				strs[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			// return new String(strs).toUpperCase(); // 换后的结果转换为字符串
			return String.valueOf(strs).toUpperCase();
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * MD5加密
	 *
	 * @param str
	 * @return
	 */
	public final static String MD52(String str) {
		try {
			byte[] strTemp = str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			String pwd = new BigInteger(1, mdTemp.digest()).toString(16);
			return pwd.toUpperCase(); // 换后的结果转换为字符串
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * SHA 加密
	 *
	 * @param str
	 * @return
	 */
	public final static String SHA(String str) {
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
		};

		try {
			byte[] strTemp = str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("SHA");
			mdTemp.update(strTemp);
			byte tmp[] = mdTemp.digest(); // MD5 的计算结果是一个 128 位的长整数，

			// 用字节表示就是 16 个字节，每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
			char strs[] = new char[16 * 2];
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节，转换成 16
				// 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>
				// 为逻辑右移，将符号位一起右移
				strs[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			return String.valueOf(strs).toUpperCase(); // 换后的结果转换为字符串
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 返回形式为数字跟字符串
	 *
	 * @param bByte
	 * @return
	 */
	public static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	/**
	 * 返回形式只为数字
	 *
	 * @param bByte
	 * @return
	 */
	public static String byteToNum(byte bByte) {
		int iRet = bByte;
		System.out.println("iRet1=" + iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		return String.valueOf(iRet);
	}

	/**
	 * 转换字节数组为16进制字串
	 *
	 * @param bByte
	 * @return
	 */
	public static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	/**
	 * MD5加密
	 *
	 * @param strObj
	 * @return
	 */
	public static String GetMD5Code(String strObj) {
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.digest() 该函数返回值为存放哈希值结果的byte数组
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			return strObj;
		}

		return resultString.toUpperCase();
	}

	/**
	 * The main method
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println(PcMd5.MD5("2011123456").toLowerCase());

		String s = "http://lsd2113821830.blog.163.com/blog/static/17234105620117104948646/";
		System.out.println(s);
		System.out.println(MD5(s));

		System.out.println(s);
		System.out.println(MD52(s));

		System.out.println("-----------");
		System.out.println(GetMD5Code(s));
	}

}
