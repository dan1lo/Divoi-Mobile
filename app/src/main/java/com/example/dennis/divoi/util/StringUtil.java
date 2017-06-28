package com.example.dennis.divoi.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * Este método verifica se uma {@link String} é um email
	 * 
	 * @param s
	 *            string que será validada
	 * @return {@link Boolean}
	 */
	public static boolean validEmail(String s) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		return Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE).matcher(s).matches();
	}

	public static boolean validPassword(String password, String confirmPassword)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return StringUtil.SHA1(password).equals(StringUtil.SHA1(confirmPassword));
	}

	public static int search(List<String> pathsAdministradorForGet, String str) {
		for (String string : pathsAdministradorForGet) {
			System.out.println(string);
		}
		return -1;
	}

	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	/**
	 * Criptografa {@link String} para SHA1
	 * 
	 * @param text
	 * @return {@link String}
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
		return convertToHex(sha1hash);
	}

	public static String setCpfUnformatted(String cpf) {
		return cpf.replaceAll("\\.|\\-|\\ ", "");
	}

	public static String getCpfFormatted(String cpf) {

		return cpf.substring(0, 3) + '.' + cpf.substring(3, 6) + '.' + cpf.substring(6, 9) + '-' + cpf.substring(9, 11);
	}

	public static String generateRandomicPassword(String firstObj, String secondObj)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String oldValue = secondObj + firstObj;

		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[40];
		md.update(oldValue.getBytes("iso-8859-1"), 0, oldValue.length());
		sha1hash = md.digest();
		return convertToHex(sha1hash);
	}

	public static String generateRandomToken() {
		return new BigInteger(130, new SecureRandom()).toString(32);
	}

}
