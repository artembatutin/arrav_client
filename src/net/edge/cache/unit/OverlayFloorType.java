package net.edge.cache.unit;

import net.edge.cache.CacheArchive;

import java.nio.ByteBuffer;

public class OverlayFloorType {

	public boolean occlude;//To add
	public static OverlayFloorType[] floorTypes;

	public int textureId = -1;
	public int groundColorOverlay;
	public boolean boolean_5;
	public int detailedColor;
	public int int_9;
	public boolean boolean_10;
	public int int_11;
	public boolean boolean_12;
	public int int_13;
	public int int_14;
	public int int_15;
	public int int_16;

	/**
	 * Hsl
	 */
	public int hue;
	public int saturation;
	public int lightness;
	public int anInt397;
	public int anInt398;
	public int hsl;

	public static void unpack(CacheArchive streamLoader) {
		ByteBuffer buffer = ByteBuffer.wrap(streamLoader.getFile("flo2.dat"));
		int count = buffer.getShort();
		floorTypes = new OverlayFloorType[count];
		for(int i = 0; i < count; i++) {
			if(floorTypes[i] == null)
				floorTypes[i] = new OverlayFloorType();
			floorTypes[i].parse(buffer);
		}
		System.out.println("[loading] flo2 size: " + count);
	}

	private void parse(ByteBuffer byteBuffer) {
		for(; ; ) {
			int attributeId = byteBuffer.get();
			if(attributeId == 0)
				break;
			else if(attributeId == 1) {
				groundColorOverlay = ((byteBuffer.get() & 0xff) << 16) + ((byteBuffer.get() & 0xff) << 8) + (byteBuffer.get() & 0xff);
				method262(groundColorOverlay);
			} else if(attributeId == 2) {
				textureId = byteBuffer.get() & 0xff;
			} else if(attributeId == 3) {
				textureId = byteBuffer.getShort() & 0xffff;
				if(textureId == 65535) {
					textureId = -1;
				}
			} else if(attributeId == 4) {

			} else if(attributeId == 5) {
				boolean_5 = false;
			} else if(attributeId == 6) {

			} else if(attributeId == 7) {
				detailedColor = ((byteBuffer.get() & 0xff) << 16) + ((byteBuffer.get() & 0xff) << 8) + (byteBuffer.get() & 0xff);
			} else if(attributeId == 8) {

			} else if(attributeId == 9) {
				int_9 = byteBuffer.getShort() & 0xffff;
			} else if(attributeId == 10) {
				boolean_10 = false;
			} else if(attributeId == 11) {
				int_11 = byteBuffer.get() & 0xff;
			} else if(attributeId == 12) {
				boolean_12 = true;
			} else if(attributeId == 13) {
				int_13 = ((byteBuffer.get() & 0xff) << 16) + ((byteBuffer.get() & 0xff) << 8) + (byteBuffer.get() & 0xff);
			} else if(attributeId == 14) {
				int_14 = byteBuffer.get() & 0xff;
			} else if(attributeId == 15) {
				int_15 = byteBuffer.getShort() & 0xffff;
				if(int_15 == 65535) {
					int_15 = -1;
				}
			} else if(attributeId == 16) {
				int_16 = byteBuffer.get() & 0xff;
			} else {
				System.err.println("[OverlayFloor] Missing AttributeId: " + attributeId);
			}
		}
	}

	private void method262(int arg0) {
		double d = (double) (arg0 >> 16 & 0xff) / 256.0;
		double d_5_ = (double) (arg0 >> 8 & 0xff) / 256.0;
		double d_6_ = (double) (arg0 & 0xff) / 256.0;
		double d_7_ = d;
		if(d_5_ < d_7_)
			d_7_ = d_5_;
		if(d_6_ < d_7_)
			d_7_ = d_6_;
		double d_8_ = d;
		if(d_5_ > d_8_)
			d_8_ = d_5_;
		if(d_6_ > d_8_)
			d_8_ = d_6_;
		double d_9_ = 0.0;
		double d_10_ = 0.0;
		double d_11_ = (d_7_ + d_8_) / 2.0;
		if(d_7_ != d_8_) {
			if(d_11_ < 0.5)
				d_10_ = (d_8_ - d_7_) / (d_8_ + d_7_);
			if(d_11_ >= 0.5)
				d_10_ = (d_8_ - d_7_) / (2.0 - d_8_ - d_7_);
			if(d == d_8_)
				d_9_ = (d_5_ - d_6_) / (d_8_ - d_7_);
			else if(d_5_ == d_8_)
				d_9_ = 2.0 + (d_6_ - d) / (d_8_ - d_7_);
			else if(d_6_ == d_8_)
				d_9_ = 4.0 + (d - d_5_) / (d_8_ - d_7_);
		}
		d_9_ /= 6.0;
		hue = (int) (d_9_ * 256.0);
		saturation = (int) (d_10_ * 256.0);
		lightness = (int) (d_11_ * 256.0);
		if(saturation < 0)
			saturation = 0;
		else if(saturation > 255)
			saturation = 255;
		if(lightness < 0)
			lightness = 0;
		else if(lightness > 255)
			lightness = 255;
		if(d_11_ > 0.5)
			anInt398 = (int) ((1.0 - d_11_) * d_10_ * 512.0);
		else
			anInt398 = (int) (d_11_ * d_10_ * 512.0);
		if(anInt398 < 1)
			anInt398 = 1;
		anInt397 = (int) (d_9_ * (double) anInt398);
		int i = hue + (int) (Math.random() * 16.0) - 8;
		if(i < 0)
			i = 0;
		else if(i > 255)
			i = 255;
		int i_12_ = saturation + (int) (Math.random() * 48.0) - 24;
		if(i_12_ < 0)
			i_12_ = 0;
		else if(i_12_ > 255)
			i_12_ = 255;
		int i_13_ = lightness + (int) (Math.random() * 48.0) - 24;
		if(i_13_ < 0)
			i_13_ = 0;
		else if(i_13_ > 255)
			i_13_ = 255;
		hsl = method263(i, i_12_, i_13_);
	}

	private final int method263(int arg0, int arg1, int arg2) {
		if(arg2 > 179)
			arg1 /= 2;
		if(arg2 > 192)
			arg1 /= 2;
		if(arg2 > 217)
			arg1 /= 2;
		if(arg2 > 243)
			arg1 /= 2;
		int i = (arg0 / 4 << 10) + (arg1 / 32 << 7) + arg2 / 2;
		return i;
	}

}
