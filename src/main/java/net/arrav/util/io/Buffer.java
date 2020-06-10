package net.arrav.util.io;

import net.arrav.net.SignLink;
import net.arrav.util.collect.DoublyLinkableEntry;
import net.arrav.util.collect.LinkedDeque;

import java.math.BigInteger;

public final class Buffer extends DoublyLinkableEntry {

	/* Declared values */
	public byte[] data;
	public int pos = 0;
	public int bit;
	public ISAACCipher cipher;

	private static int cache_size;
	private static final LinkedDeque cache = new LinkedDeque();
	private static final int[] BIT_MASK = {0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1};
	private static final BigInteger RSA_MODULUS = new BigInteger("94306533927366675756465748344550949689550982334568289470527341681445613288505954291473168510012417401156971344988779343797488043615702971738296505168869556915772193568338164756326915583511871429998053169912492097791139829802309908513249248934714848531624001166946082342750924060600795950241816621880914628143");
	private static final BigInteger RSA_EXPONENT = new BigInteger("65537");

	public Buffer(byte[] data) {
		this.data = data;
	}

	public static Buffer newPooledBuffer() {
		synchronized(cache) {
			Buffer buffer = null;
			if(cache_size > 0) {
				cache_size--;
				buffer = (Buffer) cache.removeFirst();
			}
			if(buffer != null) {
				buffer.pos = 0;
				return buffer;
			}
		}
		final Buffer buffer = new Buffer(null);
		buffer.pos = 0;
		buffer.data = new byte[5000];
		return buffer;
	}

	public byte[] getData() {
		byte[] temp = new byte[pos];
		System.arraycopy(data, 0, temp, 0, pos);
		return temp;
	}

	/* XXX: Output methods */

	/**
	 * Puts an <code>int</code> as an 8 bit value.
	 */
	public void putByte(int value) {
		data[pos++] = (byte) value;
	}

	public void putOppositeByte(int value) {
		data[pos++] = (byte) -value;
	}

	public void putReversedByte(int value) {
		data[pos++] = (byte) (128 - value);
	}

	/**
	 * Puts an <code>int</code> as a big-endian 16 bit value.
	 */
	public void putShort(int value) {
		data[pos++] = (byte) (value >> 8);
		data[pos++] = (byte) value;
	}

	/**
	 * Puts an <code>int</code> as a little-endian 16 bit value.
	 */
	public void putLitEndShort(int value) {
		data[pos++] = (byte) value;
		data[pos++] = (byte) (value >> 8);
	}

	public void putShortPlus128(int value) {
		data[pos++] = (byte) (value >> 8);
		data[pos++] = (byte) (value + 128);
	}

	public void putLitEndShortPlus128(int value) {
		data[pos++] = (byte) (value + 128);
		data[pos++] = (byte) (value >> 8);
	}

	/**
	 * Puts an <code>int</code> as a big-endian 24 bit value.
	 */
	public void putMedium(int value) {
		data[pos++] = (byte) (value >> 16);
		data[pos++] = (byte) (value >> 8);
		data[pos++] = (byte) value;
	}

	/**
	 * Puts an <code>int</code> as a big-endian 32 bit value.
	 */
	public void putInt(int value) {
		data[pos++] = (byte) (value >> 24);
		data[pos++] = (byte) (value >> 16);
		data[pos++] = (byte) (value >> 8);
		data[pos++] = (byte) value;
	}

	/**
	 * Puts an <code>int</code> as a little-endian 32 bit value.
	 */
	public void putLitEndInt(int value) {
		data[pos++] = (byte) value;
		data[pos++] = (byte) (value >> 8);
		data[pos++] = (byte) (value >> 16);
		data[pos++] = (byte) (value >> 24);
	}

	/**
	 * Puts a <code>long</code> as a big-endian 64 bit value.
	 */
	public void putLong(long value) {
		try {
			data[pos++] = (byte) (int) (value >> 56);
			data[pos++] = (byte) (int) (value >> 48);
			data[pos++] = (byte) (int) (value >> 40);
			data[pos++] = (byte) (int) (value >> 32);
			data[pos++] = (byte) (int) (value >> 24);
			data[pos++] = (byte) (int) (value >> 16);
			data[pos++] = (byte) (int) (value >> 8);
			data[pos++] = (byte) (int) value;
		} catch(final RuntimeException runtimeexception) {
			SignLink.reportError("14395, " + 5 + ", " + value + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	/**
	 * Puts a <code>boolean</code> as an 8 bit value.
	 */
	public void putBoolean(boolean value) {
		putByte(value ? 1 : 0);
	}

	/**
	 * Puts a <code>float</code> as a 32 bit value.
	 */
	public void putFloat(float value) {
		putInt(Float.floatToIntBits(value));
	}

	/**
	 * Puts a <code>double</code> as a 64 bit value.
	 */
	public void putDouble(double value) {
		putLong(Double.doubleToLongBits(value));
	}

	/**
	 * Puts a <code>String</code> as bytes ending with a line break character (10).
	 */
	public void putLine(String value) {
		System.arraycopy(value.getBytes(), 0, data, pos, value.length());
		pos += value.length();
		data[pos++] = 10;
	}

	/**
	 * Puts a <code>String</code> as bytes ending with a null character (0).
	 */
	public void putString(String value) {
		System.arraycopy(value.getBytes(), 0, data, pos, value.length());
		pos += value.length();
		data[pos++] = 0;
	}

	/**
	 * Puts a <code>byte[]</code> as an array copy.
	 */
	public void putBytes(byte[] src, int offset, int length) {
		for(int i = offset; i < offset + length; i++) {
			data[pos++] = src[i];
		}
	}

	public void putBytesReversedOrderPlus128(byte[] src, int offset, int length) {
		for(int i = offset + length - 1; i >= offset; i--) {
			data[pos++] = (byte) (src[i] + 128);
		}
	}

	public void putOpcode(int value) {
		data[pos++] = (byte) (value + cipher.nextInt());
	}

	public void putLengthAfterwards(int value) {
		data[pos - value - 1] = (byte) value;
	}

	public void putShortSpaceSaver(int i) {
		data[pos++] = (byte) ((i >> 8) + 1);
		data[pos++] = (byte) i;
	}
	
	/* XXX: Input methods */

	/**
	 * Gets an unsigned 8 bit value as an <code>int</code>.
	 */
	public int getUByte() {
		return data[pos++] & 0xff;
	}

	/**
	 * Gets a signed 8 bit value as a <code>byte</code>.
	 */
	public byte getSByte() {
		return data[pos++];
	}

	public void skip(int amt) {
		pos+=amt;
	}

	public int getOppositeUByte() {
		return -data[pos++] & 0xff;
	}

	public byte getOppositeSByte() {
		return (byte) -data[pos++];
	}

	public int getReversedUByte() {
		return 128 - data[pos++] & 0xff;
	}

	public byte getReversedSByte() {
		return (byte) (128 - data[pos++]);
	}

	public int getReversedOppositeUByte() {
		return data[pos++] - 128 & 0xff;
	}

	/**
	 * Gets a signed big-endian 16 bit value as an <code>int</code>.
	 */
	public int getSShort() {
		pos += 2;
		int temp = ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] & 0xff);
		if(temp > 32767) {
			temp -= 65536;
		}
		return temp;
	}

	/**
	 * Gets an unsigned big-endian 16 bit value as an <code>int</code>.
	 */
	public int getUShort() {
		pos += 2;
		return ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] & 0xff);
	}

	/**
	 * Gets an unsigned little-endian 16 bit value as an <code>int</code>.
	 */
	public int getLitEndUShort() {
		pos += 2;
		return ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] & 0xff);
	}

	/**
	 * Gets a signed little-endian 16 bit value as an <code>int</code>.
	 */
	public int getLitEndSShort() {
		pos += 2;
		int temp = ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] & 0xff);
		if(temp > 32767) {
			temp -= 65536;
		}
		return temp;
	}

	public int getUShortMinus128() {
		pos += 2;
		return ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] - 128 & 0xff);
	}

	public int getLitEndUShortMinus128() {
		pos += 2;
		return ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] - 128 & 0xff);
	}

	public int getLitEndSShortMinus128() {
		pos += 2;
		int temp = ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] - 128 & 0xff);
		if(temp > 32767) {
			temp -= 65536;
		}
		return temp;
	}

	/**
	 * Gets a signed big-endian 24 bit value as an <code>int</code>.
	 */
	public int getSMedium() {
		pos += 3;
		int temp = ((data[pos - 3] & 0xff) << 16) + ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] & 0xff);
		if(temp > 8388607) {
			temp -= 16777216;
		}
		return temp;
	}

	public int getSmartInt() {
		if((~data[pos]) <= -1) {
			int value = getUShort();
			if(value == 32767) {
				return -1;
			}
			return value;
		}
		return getInt() & 0x7fffffff;
	}

	/**
	 * Gets an unsigned big-endian 24 bit value as an <code>int</code>.
	 */
	public int getUMedium() {
		pos += 3;
		return ((data[pos - 3] & 0xff) << 16) + ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] & 0xff);
	}

	/**
	 * Gets a signed big-endian 32 bit value as an <code>int</code>.
	 */
	public int getInt() {
		pos += 4;
		return ((data[pos - 4] & 0xff) << 24) + ((data[pos - 3] & 0xff) << 16) + ((data[pos - 2] & 0xff) << 8) + (data[pos - 1] & 0xff);
	}

	/**
	 * Gets a signed mixed-endian 32 bit value as an <code>int</code>.
	 */
	public int getMixEndInt() {
		pos += 4;
		return ((data[pos - 2] & 0xff) << 24) + ((data[pos - 1] & 0xff) << 16) + ((data[pos - 4] & 0xff) << 8) + (data[pos - 3] & 0xff);
	}

	/**
	 * Gets a signed middle-endian 32 bit value as an <code>int</code>.
	 */
	public int getMidEndInt() {
		pos += 4;
		return ((data[pos - 3] & 0xff) << 24) + ((data[pos - 4] & 0xff) << 16) + ((data[pos - 1] & 0xff) << 8) + (data[pos - 2] & 0xff);
	}

	/**
	 * Gets a signed big-endian 64 bit value as a <code>long</code>.
	 */
	public long getLong() {
		final long l = getInt() & 0xffffffffL;
		final long l1 = getInt() & 0xffffffffL;
		return (l << 32) + l1;
	}

	/**
	 * Gets an 8 bit value as a <code>boolean</code>.
	 */
	public boolean getBoolean() {
		return getUByte() == 1;
	}

	/**
	 * Gets a 32 bit value as a <code>float</code>.
	 */
	public float getFloat() {
		return Float.intBitsToFloat(getInt());
	}

	/**
	 * Gets a 64 bit value as a <code>double</code>.
	 */
	public double getDouble() {
		return Double.longBitsToDouble(getLong());
	}

	/**
	 * Gets a <code>String</code> created from bytes ending with a line break character (10).
	 */
	public String getLine() {
		final int begin = pos;
		while(data[pos++] != 10);
		return new String(data, begin, pos - begin - 1);
	}

	/**
	 * Gets a <code>String</code> created from bytes ending with a null character (0).
	 */
	public String getString() {
		final int begin = pos;
		while(data[pos++] != 0);
		return new String(data, begin, pos - begin - 1);
	}

	/**
	 * Gets a <code>byte[]</code> that represents a <code>String</code> created from bytes ending
	 * with a line break character (10).
	 */
	public byte[] getLineBytes() {
		final int start = pos;
		while(data[pos++] != 10);
		final byte[] temp = new byte[pos - start - 1];
		System.arraycopy(data, start, temp, 0, pos - 1 - start);
		return temp;
	}

	/**
	 * Gets a <code>byte[]</code> as an array copy.
	 */
	public void getBytes(byte[] dest, int offset, int length) {
		for(int i = offset; i < offset + length; i++) {
			dest[i] = data[pos++];
		}
	}

	public void getBytesReversedOrder(byte[] dest, int offset, int length) {
		for(int i = offset + length - 1; i >= offset; i--) {
			dest[i] = data[pos++];
		}
	}

	/**
	 * Gets a signed smart (8 or 16 bit) value as an <code>int</code>.
	 */
	public int getSSmart() {
		final int temp = data[pos] & 0xff;
		if(temp < 128) {
			return getUByte() - 64;
		} else {
			return getUShort() - 49152;
		}
	}

	//?
	public int getUShortMinusOne() {
		final int temp = data[pos] & 0xff;
		if(temp < 128)
			return getUByte() - 1;
		return getUShort() - 32769;
	}

	/**
	 * Gets an unsigned smart (8 or 16 bit) value as an <code>int</code>.
	 */
	public int getUSmart() {
		final int temp = data[pos] & 0xff;
		if(temp < 128) {
			return getUByte();
		} else {
			return getUShort() - 32768;
		}
	}
	
	/* XXX: Bit access methods */

	/**
	 * Begins a bit access block.
	 */
	public void beginBitAccess() {
		bit = pos << 3;
	}

	/**
	 * Ends a bit access block.
	 */
	public void endBitAccess() {
		pos = (bit + 7) >> 3;
	}

	/**
	 * Puts an <code>int</code> as an 'n' bit value.<br>
	 * Can only be used in a bit access block.
	 */
	public void putBits(int n, int value) {
		int bytepos = bit >> 3;
		int bitoff = 8 - (bit & 7);
		bit += n;
		for(; n > bitoff; bitoff = 8) {
			data[bytepos] &= ~BIT_MASK[bitoff];
			data[bytepos++] |= (value >> (n - bitoff)) & BIT_MASK[bitoff];
			n -= bitoff;
		}
		if(n == bitoff) {
			data[bytepos] &= ~BIT_MASK[bitoff];
			data[bytepos] |= value & BIT_MASK[bitoff];
		} else {
			data[bytepos] &= ~(BIT_MASK[n] << (bitoff - n));
			data[bytepos] |= (value & BIT_MASK[n]) << (bitoff - n);
		}
	}

	/**
	 * Gets an 'n' bit value as an <code>int</code>.<br>
	 * Can only be used in a bit access block.
	 */
	public int getBits(int n) {
		int bytepos = bit >> 3;
		int bitoff = 8 - (bit & 7);
		int value = 0;
		bit += n;
		for(; n > bitoff; bitoff = 8) {
			value += (data[bytepos++] & BIT_MASK[bitoff]) << n - bitoff;
			n -= bitoff;
		}
		if(n == bitoff) {
			value += data[bytepos] & BIT_MASK[bitoff];
		} else {
			value += data[bytepos] >> bitoff - n & BIT_MASK[n];
		}
		return value;
	}
	
	/* XXX: Special methods */

	public void doKeys() {
		final int length = pos;
		pos = 0;
		byte[] buffer = new byte[length];
		getBytes(buffer, 0, length);
		byte[] bytes = buffer;
		pos = 0;
		putByte(bytes.length);
		putBytes(bytes, 0, bytes.length);
	}
}
