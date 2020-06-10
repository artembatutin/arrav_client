package net.arrav.cache.unit;

import net.arrav.Constants;
import net.arrav.cache.CacheArchive;
import net.arrav.net.SignLink;
import net.arrav.util.DataToolkit;
import net.arrav.util.io.Buffer;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles the frame sequences of animations.
 */
public final class DeformSequence {

	private static final boolean REPACK = false;

	public static DeformSequence[] cache;
	public int length;
	public int frameList[];
	public int anIntArray354[];
	public int[] cycleList;
	public int animCycle;
	public int flowControl[];
	public boolean dynamic;
	public int priority;
	public int leftHandItem;
	public int rightHandITem;
	public int maximumLoops;
	public int precedenceAnimating;
	public int precedenceWalking;
	public int replayMode;

	private DeformSequence() {
		animCycle = -1;
		dynamic = false;
		priority = 5;
		leftHandItem = -1;
		rightHandITem = -1;
		maximumLoops = 99;
		precedenceAnimating = -1;
		precedenceWalking = -1;
		replayMode = 2;
	}

	public static void unpack(CacheArchive archive) {
		final Buffer buffer;
		if(Constants.USER_HOME_FILE_STORE) {
			buffer = new Buffer(archive.getFile("seq.dat"));
		} else {
			buffer = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/anim/new_seq.dat"));
		}
		final int length = buffer.getUShort();
		System.out.println("[loading] seq size: " + length);
		if(cache == null) {
			cache = new DeformSequence[length];
		}
		for(int index = 0; index < length; index++) {
			if(cache[index] == null) {
				cache[index] = new DeformSequence();
			}
			cache[index].read(buffer);
		}

		if(REPACK) {
			try {
				fixAnimations();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void read(Buffer buffer) {
		do {
			int code = buffer.getUByte();
			if(code == 0) {
				break;
			}
			if(code == 1) {
				length = buffer.getUShort();
				frameList = new int[length];
				anIntArray354 = new int[length];
				cycleList = new int[length];
				for(int i = 0; i < length; i++) {
					frameList[i] = buffer.getInt();
					anIntArray354[i] = -1;
				}
				for(int i = 0; i < length; i++) {
					cycleList[i] = buffer.getUByte();
				}
			} else if(code == 2) {
				animCycle = buffer.getUShort();
			} else if(code == 3) {
				int flowCount = buffer.getUByte();
				flowControl = new int[flowCount + 1];
				for(int i = 0; i < flowCount; i++) {
					flowControl[i] = buffer.getUByte();
				}
				flowControl[flowCount] = 9999999;
			} else if(code == 4) {
				dynamic = true;
			} else if(code == 5) {
				priority = buffer.getUByte();
			} else if(code == 6) {
				leftHandItem = buffer.getUShort();
				if(leftHandItem != 0) {
					leftHandItem += 32256;
				}
			} else if(code == 7) {
				rightHandITem = buffer.getUShort();
				if(rightHandITem != 0) {
					rightHandITem += 32256;
				}
			} else if(code == 8) {
				maximumLoops = buffer.getUByte();
			} else if(code == 9) {
				/*
				 * when animating, 0 -> block walking, 1 -> yield to walking, 2
				 * -> interleave with walking
				 */
				precedenceAnimating = buffer.getUByte();
			} else if(code == 10) {
				/*
				 * when animating, 0 -> block walking, 1 -> yield to walking, 2
				 * -> interleave with walking
				 */
				precedenceWalking = buffer.getUByte();
			} else if(code == 11) {
				replayMode = buffer.getUByte();
			} else {
				System.out.println("Error unrecognised seq config code: " + code);
			}
		} while(true);
		if(length == 0) {
			length = 1;
			frameList = new int[1];
			frameList[0] = -1;
			anIntArray354 = new int[1];
			anIntArray354[0] = -1;
			cycleList = new int[1];
			cycleList[0] = -1;
		}
		if(precedenceAnimating == -1)
			if(flowControl != null) {
				precedenceAnimating = 2;
			} else {
				precedenceAnimating = 0;
			}
		if(precedenceWalking == -1) {
			if(flowControl != null) {
				precedenceWalking = 2;
				return;
			}
			precedenceWalking = 0;
		}
		if(leftHandItem == 65535) {
			leftHandItem = 0;
		}
		if(rightHandITem == 65535) {
			rightHandITem = 0;
		}
	}

	private void write(DataOutputStream out) throws IOException {
		Set<Integer> written = new HashSet<>();
		do {
			if(length != 1 && !written.contains(1)) {
				out.writeByte(1);
				out.writeShort(length);
				for(int i = 0; i < length; i++) {
					out.writeInt(frameList[i]);
				}
				for(int i = 0; i < length; i++) {
					out.writeByte(cycleList[i]);
				}
				written.add(1);
			} else if(animCycle != -1 && !written.contains(2)) {
				out.writeByte(2);
				out.writeShort(animCycle);
				written.add(2);
			} else if(flowControl != null && !written.contains(3)) {
				out.writeByte(3);
				out.writeByte(flowControl.length - 1);
				for(int i = 0; i < flowControl.length - 1; i++) {
					out.writeByte(flowControl[i]);
				}
				written.add(3);
			} else if(dynamic && !written.contains(4)) {
				out.writeByte(4);
				written.add(4);
			} else if(priority != 5 && !written.contains(5)) {
				out.writeByte(5);
				out.writeByte(priority);
				written.add(5);
			} else if(leftHandItem != -1 && !written.contains(6)) {
				out.writeByte(6);
				if(leftHandItem != 0)
					leftHandItem -= 32256;
				out.writeShort(leftHandItem);
				written.add(6);
			} else if(rightHandITem != -1 && !written.contains(7)) {
				out.writeByte(7);
				if(rightHandITem != 0)
					rightHandITem -= 32256;
				out.writeShort(rightHandITem);
				written.add(7);
			} else if(maximumLoops != 9 && !written.contains(8)) {
				out.writeByte(8);
				out.writeByte(maximumLoops);
				written.add(8);
			} else if(precedenceAnimating != -1 && !written.contains(9)) {
				out.writeByte(9);
				out.writeByte(precedenceAnimating);
				written.add(9);
			} else if(precedenceWalking != -1 && !written.contains(10)) {
				out.writeByte(10);
				out.writeByte(precedenceWalking);
				written.add(10);
			} else if(replayMode != 2 && !written.contains(11)) {
				out.writeByte(11);
				out.writeByte(replayMode);
				written.add(11);
			} else {
				out.writeByte(0);
				break;
			}
		} while(true);
	}

	/**
	 * An utility method to fix several animations.
	 */
	private static void fixAnimations() throws IOException {
		//Animations to fix.
		int[] anims = {};
		if(anims.length == 0) {
			return;
		}
		final Buffer buf = new Buffer(DataToolkit.readFile(SignLink.getCacheDir() + "/util/anim/seq645.dat"));
		final int len = buf.getUShort();
		System.out.println("seq (645) size: " + len);
		DeformSequence[] dup = new DeformSequence[len];
		for(int index = 0; index < len; index++) {
			if(dup[index] == null) {
				dup[index] = new DeformSequence();
			}
			dup[index].read(buf);
			for(int fix : anims) {
				if(fix == index) {
					cache[index] = dup[index];
				}
			}
		}
		try {
			int size = cache.length;
			DataOutputStream os = new DataOutputStream(new FileOutputStream(SignLink.getCacheDir() + "/util/anim/new_seq.dat"));
			os.writeShort(size);
			for(DeformSequence aCache : cache) {
				aCache.write(os);
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getFrame(int index) {
		if(index > cycleList.length)
			return 1;
		int frameLength = cycleList[index];
		if(frameLength == 0) {
			final AnimationFrame frame = AnimationFrame.get(frameList[index]);
			if(frame != null) {
				frameLength = cycleList[index] = frame.anInt636;
			}
		}
		if(frameLength == 0) {
			frameLength = 1;
		}
		return frameLength;
	}
}