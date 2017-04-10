package null_util;

/**
 * JAGHash2String can be used to figure out which strings are possible for the hash code.
 * @author Rob Bubletan
 * @version 1
 */
final class JAGHash2String implements Runnable {

	private static final int[] JAG_HASHES = {1219084034, 940977918, -1004178375, -70295659, 523617556, -1857300557, 1451391714, -888498683, 1844915687, 1831069846, 22834782, 2004158547, -384541308, -90207845, 450862262};
	private static final String[] FILE_EXTENSIONS = {".dat"};
	private static final int MINIMUM_LENGTH = 1;
	private static final int MAXIMUM_LENGTH = 10;

	private boolean running;
	private char[] curr;
	private byte len;

	@Override
	public void run() {
		init();
		while(running) {
			next();
		}
	}

	private final void init() {
		len = MINIMUM_LENGTH;
		curr = new char[len];
		for(int i = 0; i < len; i++) {
			curr[i] = 'A';
		}
		running = true;
		System.out.println("========== length " + len + " ==========");
		System.out.println("1 / 27");
	}

	private final void next() {
		int i = 0;
		int hash;
		for(; i < FILE_EXTENSIONS.length; i++) {
			hash = toJAGHash(curr, FILE_EXTENSIONS[i]);
			for(int i1 = 0; i1 < JAG_HASHES.length; i1++) {
				if(hash == JAG_HASHES[i1]) {
					System.err.println("Collision found: " + (JAG_HASHES[i1] + " [0x" + Integer.toHexString(JAG_HASHES[i1]) + "]") +
							" --> " + (new String(curr).toLowerCase() + FILE_EXTENSIONS[i]));
				}
			}
		}
		for(i = len - 1; i >= 0; i--) {
			if(curr[i] != '_') {
				if(curr[i] != 'Z') {
					curr[i]++;
					if(i == 0) {
						System.out.println((curr[i] - 64) + " / 27");
					}
				} else {
					curr[i] = '_';
					if(i == 0) {
						System.out.println("27 / 27");
					}
				}
				break;
			} else {
				if(i == 0) {
					if(len == MAXIMUM_LENGTH) {
						running = false;
					}
					len++;
					System.out.println("========== length " + len + " ==========");
					System.out.println("1 / 27");
					curr = new char[len];
					for(int i1 = 0; i1 < len; i1++) {
						curr[i1] = 'A';
					}
				} else {
					for(int i1 = len - 1; i1 >= i; i1--) {
						curr[i1] = 'A';
					}
				}
			}
		}
	}

	private static final int toJAGHash(char[] c, String ex) {
		int hash = 0;
		int i = 0;
		for(; i < c.length; i++) {
			hash = hash * 61 + c[i] - 32;
		}
		ex = ex.toUpperCase();
		for(i = 0; i < ex.length(); i++) {
			hash = hash * 61 + ex.charAt(i) - 32;
		}
		return hash;
	}

	public static void main(String[] args) {
		System.out.println("Initializing JAGHash2String...");
		System.out.println();
		String hashes = "";
		for(int hash : JAG_HASHES) {
			hashes += "" + hash + " [0x" + Integer.toHexString(hash) + "]" + ", ";
		}
		hashes = hashes.substring(0, hashes.length() - 2);
		System.out.println("Searching for collisions for the hashes: {" + hashes + "}");
		String extensions = "";
		for(String extension : FILE_EXTENSIONS) {
			extensions += "\"" + extension + "\", ";
		}
		extensions = extensions.substring(0, extensions.length() - 2);
		System.out.println("with the following file extensions: {" + extensions + "}");
		System.out.println("and a length from " + MINIMUM_LENGTH + " to " + MAXIMUM_LENGTH + " characters.");
		System.out.println();
		new JAGHash2String().run();
		System.out.println();
		System.out.println("Every possible choise has been checked.");
		System.gc();
		System.exit(0);
	}
}
