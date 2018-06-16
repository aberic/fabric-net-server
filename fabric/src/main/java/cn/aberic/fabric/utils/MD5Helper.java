package cn.aberic.fabric.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Aberic
 */
public class MD5Helper {

	private static MD5Helper instance;

	public static MD5Helper obtain() {
		if (null == instance) {
			synchronized (MD5Helper.class) {
				if (null == instance) {
					instance = new MD5Helper();
				}
			}
		}
		return instance;
	}

	private MD5Helper() {
	}

	/**
	 * 将字符串md5散列值24位
	 * 
	 * @param content
	 *            散列值内容
	 * @return 散列值结果
	 */
	public String md524(String content) {
		String str = getMD5(content);
		assert str != null;
		return str.substring(6, 30);
	}

	/**
	 * 将字符串md5散列值24位
	 * 
	 * @param content
	 *            散列值内容
	 * @return 散列值结果
	 */
	public String md524(int content) {
		return md524(String.valueOf(content));
	}

	/**
	 * 将字符串md5散列值32位
	 * 
	 * @param content
	 *            散列值内容
	 * @return 散列值结果
	 */
	public String md532(String content) {
		return getMD5(content);
	}

	/**
	 * 将整型md5散列值32位
	 * 
	 * @param content
	 *            散列值内容
	 * @return 散列值结果
	 */
	public String md532(int content) {
		return md532(String.valueOf(content));
	}

	/**
	 * 将字符串md5散列值
	 * 
	 * @param content
	 *            散列值内容
	 * @return 散列值结果
	 */
	private String getMD5(String content) {
		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(content.getBytes("UTF-8"));
			return this.getHashString(mDigest);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException var3) {
			var3.printStackTrace();
			return null;
		}
	}

	private String getHashString(MessageDigest digest) {
		StringBuilder builder = new StringBuilder();
		byte[] var3 = digest.digest();
		int var4 = var3.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			byte b = var3[var5];
			builder.append(Integer.toHexString(b >> 4 & 15));
			builder.append(Integer.toHexString(b & 15));
		}
		return builder.toString();
	}

}
