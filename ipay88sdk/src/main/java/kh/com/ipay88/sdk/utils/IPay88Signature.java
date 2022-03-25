package kh.com.ipay88.sdk.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * IPay88 SHA1 + SHA256
 */
public class IPay88Signature {

	/**
	 * Bytes Array to HEX String
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String bytesToHexString(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = data[i] >>> 4 & 0xF;
			int two_halfs = 0;
			do {
				if ((halfbyte >= 0) && (halfbyte <= 9)) {
					buf.append((char)(48 + halfbyte));
				} else {
					buf.append((char)(97 + (halfbyte - 10)));
				}
				halfbyte = data[i] & 0xF;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	/**
	 * SHA1
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String SHA1(String text) {
		String a = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] sha1hash = new byte[40];
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			sha1hash = md.digest();
			a = new String(Base64.encodeBase64(sha1hash));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * SHA256
	 * @param base
	 * @return
	 */
	public static String SHA256(String base){
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
}
