package net.edge.util.string;

import net.edge.sign.SignLink;

public final class StringUtils {

	//Character maping values.
	private static final char[] CHARACTER_MAP = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

	/**
	 * Formats a username so every word begins by a capital and
	 * other characters are lower case.
	 */
	public static String formatName(String name) {
		if(name.length() > 0) {
			final char chars[] = name.toCharArray();
			for(int j = 0; j < chars.length; j++) {
				if(chars[j] == '_') {
					chars[j] = ' ';
					if(j + 1 < chars.length && chars[j + 1] >= 'a' && chars[j + 1] <= 'z') {
						chars[j + 1] = (char) (chars[j + 1] + 65 - 97);
					}
				}
			}
			if(chars[0] >= 'a' && chars[0] <= 'z') {
				chars[0] = (char) (chars[0] + 65 - 97);
			}
			return new String(chars);
		} else {
			return name;
		}
	}

	/**
	 * Formats the chat message so each sentence will begin by a capital and
	 * only first letter of each word can be capitalized.
	 */
	public static String formatChat(String msg) {
		char[] chars = msg.toCharArray();
		byte mode = 1;
		for(int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if(c == ' ') {
				if(mode == 2) {
					mode = 0;
				}
			} else if(c == '.' || c == '!' || c == '?') {
				mode = 1;
			} else {
				if(mode == 1 && c >= 'a' && c <= 'z') {
					chars[i] &= ~32;
				} else if(mode == 2 && c >= 'A' && c <= 'Z') {
					chars[i] |= 32;
				}
				mode = 2;
			}
		}
		return new String(chars);
	}

	/**
	 * Encrypts a long value.
	 */
	public static long encryptName(String name) {
		long l = 0L;
		for(int i = 0; i < name.length() && i < 12; i++) {
			final char c = name.charAt(i);
			l *= 37L;
			if(c >= 'A' && c <= 'Z') {
				l += 1 + c - 65;
			} else if(c >= 'a' && c <= 'z') {
				l += 1 + c - 97;
			} else if(c >= '0' && c <= '9') {
				l += 27 + c - 48;
			}
		}

		for(; l % 37L == 0L && l != 0L; l /= 37L);
		return l;
	}

	/**
	 * Encrypts the value to numbers.
	 */
	public static long toHash(String value) {
		value = value.toUpperCase();
		long l = 0L;
		for(int i = 0; i < value.length(); i++) {
			l = l * 61L + value.charAt(i) - 32L;
			l = l + (l >> 56) & 0xffffffffffffffL;
		}
		return l;
	}

	/**
	 * Integer to byte value string.
	 * Example: 0xff0080ff returns 255.0.128.255
	 */
	public static String intToByteValueString(int number) {
		return (number >> 24 & 0xff) + "." + (number >> 16 & 0xff) + "." + (number >> 8 & 0xff) + "." + (number & 0xff);
	}

	/**
	 * Decrypt names.
	 */
	public static String decryptName(long l) {
		try {
			if(l <= 0L || l >= 0x5b5b57f8a98a5dd1L) {
				return "invalid_name";
			}
			if(l % 37L == 0L) {
				return "invalid_name";
			}
			int i = 0;
			final char ac[] = new char[12];
			while(l != 0L) {
				final long l1 = l;
				l /= 37L;
				ac[11 - i++] = CHARACTER_MAP[(int) (l1 - l * 37L)];
			}
			return new String(ac, 12 - i, i);
		} catch(final RuntimeException runtimeexception) {
			SignLink.reportError("81570, " + l + ", " + (byte) -99 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	/**
	 * Enables an asterisks at the beginning of the message.
	 * Usage: Password encryption.
	 * @param message source
	 * @return asterisks
	 */
	public static String toAsterisks(String message) {
		final StringBuilder stringbuffer = new StringBuilder();
		for(int j = 0; j < message.length(); j++) {
			stringbuffer.append("*");
		}
		return stringbuffer.toString();
	}
}
