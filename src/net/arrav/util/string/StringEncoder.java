package net.arrav.util.string;

import net.arrav.util.io.Buffer;

public final class StringEncoder {

	/*
	 * Declared fields.
	 */
	//String data (100 characters)
	private static final char[] STRING_DATA = new char[100];
	//Buffer data (100 values)
	private static final Buffer BUFFER = new Buffer(new byte[100]);
	//Allowed characters
	private static char ALLOVED_CHARACTERS[] = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']', '>', '<', '^', '/', '_'};

	/**
	 * Initializes the String buffer.
	 */
	public static String getString(int value, Buffer buffer) {
		int position = 0;
		for(int l = 0; l < value; l++) {
			final int i1 = buffer.getUByte();
			STRING_DATA[position++] = ALLOVED_CHARACTERS[i1];
		}
		boolean sentenceBreak = true;
		for(int k1 = 0; k1 < position; k1++) {
			final char c = STRING_DATA[k1];
			if(sentenceBreak && c >= 'a' && c <= 'z') {
				STRING_DATA[k1] += '\uFFE0';
				sentenceBreak = false;
			}
			if(c == '.' || c == '!' || c == '?') {
				sentenceBreak = true;
			}
		}
		return new String(STRING_DATA, 0, position);
	}

	/**
	 * Parses the string to allowed characters.
	 */
	public static void putString(String message, Buffer buffer) {
		if(message.length() > 80) {
			message = message.substring(0, 80);
		}
		message = message.toLowerCase();
		for(int j = 0; j < message.length(); j++) {
			final char c = message.charAt(j);
			int k = 0;
			for(int l = 0; l < ALLOVED_CHARACTERS.length; l++) {
				if(c != ALLOVED_CHARACTERS[l]) {
					continue;
				}
				k = l;
				break;
			}
			buffer.putByte(k);
		}
	}

	/**
	 * Finishes the input operation.
	 */
	public static String processInput(String message) {
		BUFFER.pos = 0;
		putString(message, BUFFER);
		final int j = BUFFER.pos;
		BUFFER.pos = 0;
		return getString(j, BUFFER);
	}
}