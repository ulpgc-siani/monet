package org.monet.deployservice.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

	private MessageDigest md = null;
	static private Md5 md5 = null;
	private static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6',
	    '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Constructor is private so you must use the getInstance method
	 */
	private Md5() throws NoSuchAlgorithmException {
		md = MessageDigest.getInstance("Md5");
	}

	/**
	 * This returns the singleton instance
	 */
	public static Md5 getInstance() throws NoSuchAlgorithmException {

		if (md5 == null) {
			md5 = new Md5();
		}

		return (md5);
	}

	public String hashData(byte[] dataToHash) {
		return hexStringFromBytes((calculateHash(dataToHash))).toLowerCase();
	}

	private byte[] calculateHash(byte[] dataToHash)

	{
		md.update(dataToHash, 0, dataToHash.length);

		return (md.digest());
	}

	public String hexStringFromBytes(byte[] b) {
		String hex = "";

		int msb;

		int lsb = 0;
		int i;

		// MSB maps to idx 0

		StringBuilder buf = new StringBuilder();
		
		for (i = 0; i < b.length; i++) {
			msb = ((int) b[i] & 0x000000FF) / 16;

			lsb = ((int) b[i] & 0x000000FF) % 16;
			buf.append(hex + hexChars[msb] + hexChars[lsb]);
			
//			hex = hex + hexChars[msb] + hexChars[lsb];
		}
		hex = buf.toString();
		
		
		return (hex);
	}

	public static void main(String[] args) {
		try {

			Md5 md = Md5.getInstance();
			System.out.println(md.hashData("hello".getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(System.out);
		}
	}
}

/*
 * public class Md5 { public String convertToMd5(String strPass) { byte[] hash =
 * null;
 * 
 * try { MessageDigest md5 = MessageDigest.getInstance("MD5");
 * 
 * byte[] nulledMessage = new byte[strPass.getBytes().length * 2];
 * 
 * for (int i = 0; i < strPass.getBytes().length; i++) { nulledMessage[i * 2] =
 * strPass.getBytes()[i]; nulledMessage[(i * 2) + 1] = (byte) 0; }
 * 
 * md5.update(nulledMessage); hash = md5.digest();
 * 
 * } catch (NoSuchAlgorithmException e) { e.printStackTrace();
 * System.out.println(e.getStackTrace()); }
 * 
 * String result = null; try { result = new String(hash, "UTF-16LE"); } catch
 * (UnsupportedEncodingException e) { e.printStackTrace(); }
 * 
 * return result; } }
 */
