/*******************************************************************************
 * [BZip2Data.java] BZIP2 Data.
 * @note REFACTORING ISN'T REQUIRED.
 ******************************************************************************/
package net.edge.cache.bzip2;

final class BZip2Data {

	//Static
	public static int[] ll8;
	//Final
	final boolean[] inUse = new boolean[256];
	final boolean[] inUse16 = new boolean[16];
	final byte[] seqToUnseq = new byte[256];
	final byte[] yy = new byte[4096];
	final byte[] selector = new byte[18002];
	final byte[] selectorMtf = new byte[18002];
	final byte[][] len = new byte[6][258];
	final int[] minLens = new int[6];
	final int[] unzftab = new int[256];
	final int[] cftab = new int[257];
	final int[] anIntArray593 = new int[16];
	final int[][] limit = new int[6][258];
	final int[][] base = new int[6][258];
	final int[][] perm = new int[6][258];
	//Normal
	boolean blockRandomised;
	byte[] src;
	byte[] dest;
	byte aByte573;
	int srcPos;
	int srcSize;
	int anInt566;
	int anInt567;
	int destPos;
	int destSize;
	int anInt571;
	int anInt572;
	int anInt574;
	int bsBuff;
	int bsLive;
	int blockSize100k;
	int anInt579;
	int origPtr;
	int anInt581;
	int anInt582;
	int anInt584;
	int nInUse;
	int anInt601;

	BZip2Data() {
	}
}
