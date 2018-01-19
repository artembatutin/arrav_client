package net.arrav.game;

final class TileUtils {

	static int method155(int code, int j, int k) {
		code &= 3;
		if(code == 0) {
			return k;
		}
		if(code == 1) {
			return j;
		}
		if(code == 2) {
			return 7 - k;
		} else {
			return 7 - j;
		}
	}

	static int method156(int i, int code, int l) {
		code &= 3;
		if(code == 0) {
			return i;
		}
		if(code == 1) {
			return 7 - l;
		}
		if(code == 2) {
			return 7 - i;
		} else {
			return l;
		}
	}

	static int method157(int code, int j, int k, int l, int i1) {
		code &= 3;
		if(code == 0) {
			return k;
		}
		if(code == 1) {
			return l;
		}
		if(code == 2) {
			return 7 - k - (i1 - 1);
		} else {
			return 7 - l - (j - 1);
		}
	}

	static int method158(int j, int k, int code, int i1, int j1) {
		code &= 3;
		if(code == 0) {
			return j;
		}
		if(code == 1) {
			return 7 - j1 - (i1 - 1);
		}
		if(code == 2) {
			return 7 - j - (k - 1);
		} else {
			return j1;
		}
	}
}