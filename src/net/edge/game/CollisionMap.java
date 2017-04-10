package net.edge.game;

public final class CollisionMap {

	private final int offsetX;
	private final int offsetY;
	private final int sizeX;
	private final int sizeY;
	public final int[][] flagMap;

	public CollisionMap() {
		offsetX = 0;
		offsetY = 0;
		sizeX = 104;
		sizeY = 104;
		flagMap = new int[sizeX][sizeY];
		clear();
	}

	public void clear() {
		for(int x = 0; x < sizeX; x++) {
			for(int y = 0; y < sizeY; y++) {
				if(x == 0 || y == 0 || x == sizeX - 1 || y == sizeY - 1) {
					flagMap[x][y] = 0xffffff;
				} else {
					flagMap[x][y] = 0x1000000;
				}
			}
		}

	}

	void method211(int y, int dir1, int x, int dir2, boolean flag) {
		x -= offsetX;
		y -= offsetY;
		if(dir2 == 0) {
			if(dir1 == 0) {
				setTrue(x, y, 128);
				setTrue(x - 1, y, 8);
			}
			if(dir1 == 1) {
				setTrue(x, y, 2);
				setTrue(x, y + 1, 32);
			}
			if(dir1 == 2) {
				setTrue(x, y, 8);
				setTrue(x + 1, y, 128);
			}
			if(dir1 == 3) {
				setTrue(x, y, 32);
				setTrue(x, y - 1, 2);
			}
		}
		if(dir2 == 1 || dir2 == 3) {
			if(dir1 == 0) {
				setTrue(x, y, 1);
				setTrue(x - 1, y + 1, 16);
			}
			if(dir1 == 1) {
				setTrue(x, y, 4);
				setTrue(x + 1, y + 1, 64);
			}
			if(dir1 == 2) {
				setTrue(x, y, 16);
				setTrue(x + 1, y - 1, 1);
			}
			if(dir1 == 3) {
				setTrue(x, y, 64);
				setTrue(x - 1, y - 1, 4);
			}
		}
		if(dir2 == 2) {
			if(dir1 == 0) {
				setTrue(x, y, 130);
				setTrue(x - 1, y, 8);
				setTrue(x, y + 1, 32);
			}
			if(dir1 == 1) {
				setTrue(x, y, 10);
				setTrue(x, y + 1, 32);
				setTrue(x + 1, y, 128);
			}
			if(dir1 == 2) {
				setTrue(x, y, 40);
				setTrue(x + 1, y, 128);
				setTrue(x, y - 1, 2);
			}
			if(dir1 == 3) {
				setTrue(x, y, 160);
				setTrue(x, y - 1, 2);
				setTrue(x - 1, y, 8);
			}
		}
		if(flag) {
			if(dir2 == 0) {
				if(dir1 == 0) {
					setTrue(x, y, 0x10000);
					setTrue(x - 1, y, 4096);
				}
				if(dir1 == 1) {
					setTrue(x, y, 1024);
					setTrue(x, y + 1, 16384);
				}
				if(dir1 == 2) {
					setTrue(x, y, 4096);
					setTrue(x + 1, y, 0x10000);
				}
				if(dir1 == 3) {
					setTrue(x, y, 16384);
					setTrue(x, y - 1, 1024);
				}
			}
			if(dir2 == 1 || dir2 == 3) {
				if(dir1 == 0) {
					setTrue(x, y, 512);
					setTrue(x - 1, y + 1, 8192);
				}
				if(dir1 == 1) {
					setTrue(x, y, 2048);
					setTrue(x + 1, y + 1, 32768);
				}
				if(dir1 == 2) {
					setTrue(x, y, 8192);
					setTrue(x + 1, y - 1, 512);
				}
				if(dir1 == 3) {
					setTrue(x, y, 32768);
					setTrue(x - 1, y - 1, 2048);
				}
			}
			if(dir2 == 2) {
				if(dir1 == 0) {
					setTrue(x, y, 0x10400);
					setTrue(x - 1, y, 4096);
					setTrue(x, y + 1, 16384);
				}
				if(dir1 == 1) {
					setTrue(x, y, 5120);
					setTrue(x, y + 1, 16384);
					setTrue(x + 1, y, 0x10000);
				}
				if(dir1 == 2) {
					setTrue(x, y, 20480);
					setTrue(x + 1, y, 0x10000);
					setTrue(x, y - 1, 1024);
				}
				if(dir1 == 3) {
					setTrue(x, y, 0x14000);
					setTrue(x, y - 1, 1024);
					setTrue(x - 1, y, 4096);
				}
			}
		}
	}

	void method212(boolean bool, int j, int k, int x, int y, int j1) {
		int flag = 256;
		if(bool) {
			flag += 0x20000;
		}
		x -= offsetX;
		y -= offsetY;
		if(j1 == 1 || j1 == 3) {
			final int l1 = j;
			j = k;
			k = l1;
		}
		for(int x1 = x; x1 < x + j; x1++) {
			if(x1 >= 0 && x1 < sizeX) {
				for(int y1 = y; y1 < y + k; y1++) {
					if(y1 >= 0 && y1 < sizeY) {
						setTrue(x1, y1, flag);
					}
				}
			}
		}

	}

	void setBlocked(int x, int y) {
		x -= offsetX;
		y -= offsetY;
		flagMap[x][y] |= 0x200000;
	}

	private void setTrue(int x, int y, int flag) {
		flagMap[x][y] |= flag;
	}

	public void method215(int i, int j, boolean flag, int x, int y) {
		x -= offsetX;
		y -= offsetY;
		if(j == 0) {
			if(i == 0) {
				setFalse(x, y, 128);
				setFalse(x - 1, y, 8);
			}
			if(i == 1) {
				setFalse(x, y, 2);
				setFalse(x, y + 1, 32);
			}
			if(i == 2) {
				setFalse(x, y, 8);
				setFalse(x + 1, y, 128);
			}
			if(i == 3) {
				setFalse(x, y, 32);
				setFalse(x, y - 1, 2);
			}
		}
		if(j == 1 || j == 3) {
			if(i == 0) {
				setFalse(x, y, 1);
				setFalse(x - 1, y + 1, 16);
			}
			if(i == 1) {
				setFalse(x, y, 4);
				setFalse(x + 1, y + 1, 64);
			}
			if(i == 2) {
				setFalse(x, y, 16);
				setFalse(x + 1, y - 1, 1);
			}
			if(i == 3) {
				setFalse(x, y, 64);
				setFalse(x - 1, y - 1, 4);
			}
		}
		if(j == 2) {
			if(i == 0) {
				setFalse(x, y, 130);
				setFalse(x - 1, y, 8);
				setFalse(x, y + 1, 32);
			}
			if(i == 1) {
				setFalse(x, y, 10);
				setFalse(x, y + 1, 32);
				setFalse(x + 1, y, 128);
			}
			if(i == 2) {
				setFalse(x, y, 40);
				setFalse(x + 1, y, 128);
				setFalse(x, y - 1, 2);
			}
			if(i == 3) {
				setFalse(x, y, 160);
				setFalse(x, y - 1, 2);
				setFalse(x - 1, y, 8);
			}
		}
		if(flag) {
			if(j == 0) {
				if(i == 0) {
					setFalse(x, y, 0x10000);
					setFalse(x - 1, y, 4096);
				}
				if(i == 1) {
					setFalse(x, y, 1024);
					setFalse(x, y + 1, 16384);
				}
				if(i == 2) {
					setFalse(x, y, 4096);
					setFalse(x + 1, y, 0x10000);
				}
				if(i == 3) {
					setFalse(x, y, 16384);
					setFalse(x, y - 1, 1024);
				}
			}
			if(j == 1 || j == 3) {
				if(i == 0) {
					setFalse(x, y, 512);
					setFalse(x - 1, y + 1, 8192);
				}
				if(i == 1) {
					setFalse(x, y, 2048);
					setFalse(x + 1, y + 1, 32768);
				}
				if(i == 2) {
					setFalse(x, y, 8192);
					setFalse(x + 1, y - 1, 512);
				}
				if(i == 3) {
					setFalse(x, y, 32768);
					setFalse(x - 1, y - 1, 2048);
				}
			}
			if(j == 2) {
				if(i == 0) {
					setFalse(x, y, 0x10400);
					setFalse(x - 1, y, 4096);
					setFalse(x, y + 1, 16384);
				}
				if(i == 1) {
					setFalse(x, y, 5120);
					setFalse(x, y + 1, 16384);
					setFalse(x + 1, y, 0x10000);
				}
				if(i == 2) {
					setFalse(x, y, 20480);
					setFalse(x + 1, y, 0x10000);
					setFalse(x, y - 1, 1024);
				}
				if(i == 3) {
					setFalse(x, y, 0x14000);
					setFalse(x, y - 1, 1024);
					setFalse(x - 1, y, 4096);
				}
			}
		}
	}

	public void method216(int i, int j, int k, int l, int i1, boolean flag) {
		int j1 = 256;
		if(flag) {
			j1 += 0x20000;
		}
		k -= offsetX;
		l -= offsetY;
		if(i == 1 || i == 3) {
			final int k1 = j;
			j = i1;
			i1 = k1;
		}
		for(int l1 = k; l1 < k + j; l1++) {
			if(l1 >= 0 && l1 < sizeX) {
				for(int i2 = l; i2 < l + i1; i2++) {
					if(i2 >= 0 && i2 < sizeY) {
						setFalse(l1, i2, j1);
					}
				}
			}
		}

	}

	private void setFalse(int x, int y, int flag) {
		flagMap[x][y] &= 0xffffff - flag;
	}

	public void setUnblocked(int x, int y) {
		x -= offsetX;
		y -= offsetY;
		flagMap[x][y] &= 0xdfffff;
	}

	public boolean method219(int x1, int y1, int x2, int y2, int i1, int j1) {
		if(x1 == x2 && y1 == y2) {
			return true;
		}
		x1 -= offsetX;
		y1 -= offsetY;
		x2 -= offsetX;
		y2 -= offsetY;
		if(j1 == 0) {
			if(i1 == 0) {
				if(x1 == x2 - 1 && y1 == y2) {
					return true;
				}
				if(x1 == x2 && y1 == y2 + 1 && (flagMap[x1][y1] & 0x1280120) == 0) {
					return true;
				}
				if(x1 == x2 && y1 == y2 - 1 && (flagMap[x1][y1] & 0x1280102) == 0) {
					return true;
				}
			} else if(i1 == 1) {
				if(x1 == x2 && y1 == y2 + 1) {
					return true;
				}
				if(x1 == x2 - 1 && y1 == y2 && (flagMap[x1][y1] & 0x1280108) == 0) {
					return true;
				}
				if(x1 == x2 + 1 && y1 == y2 && (flagMap[x1][y1] & 0x1280180) == 0) {
					return true;
				}
			} else if(i1 == 2) {
				if(x1 == x2 + 1 && y1 == y2) {
					return true;
				}
				if(x1 == x2 && y1 == y2 + 1 && (flagMap[x1][y1] & 0x1280120) == 0) {
					return true;
				}
				if(x1 == x2 && y1 == y2 - 1 && (flagMap[x1][y1] & 0x1280102) == 0) {
					return true;
				}
			} else if(i1 == 3) {
				if(x1 == x2 && y1 == y2 - 1) {
					return true;
				}
				if(x1 == x2 - 1 && y1 == y2 && (flagMap[x1][y1] & 0x1280108) == 0) {
					return true;
				}
				if(x1 == x2 + 1 && y1 == y2 && (flagMap[x1][y1] & 0x1280180) == 0) {
					return true;
				}
			}
		}
		if(j1 == 2) {
			if(i1 == 0) {
				if(x1 == x2 - 1 && y1 == y2) {
					return true;
				}
				if(x1 == x2 && y1 == y2 + 1) {
					return true;
				}
				if(x1 == x2 + 1 && y1 == y2 && (flagMap[x1][y1] & 0x1280180) == 0) {
					return true;
				}
				if(x1 == x2 && y1 == y2 - 1 && (flagMap[x1][y1] & 0x1280102) == 0) {
					return true;
				}
			} else if(i1 == 1) {
				if(x1 == x2 - 1 && y1 == y2 && (flagMap[x1][y1] & 0x1280108) == 0) {
					return true;
				}
				if(x1 == x2 && y1 == y2 + 1) {
					return true;
				}
				if(x1 == x2 + 1 && y1 == y2) {
					return true;
				}
				if(x1 == x2 && y1 == y2 - 1 && (flagMap[x1][y1] & 0x1280102) == 0) {
					return true;
				}
			} else if(i1 == 2) {
				if(x1 == x2 - 1 && y1 == y2 && (flagMap[x1][y1] & 0x1280108) == 0) {
					return true;
				}
				if(x1 == x2 && y1 == y2 + 1 && (flagMap[x1][y1] & 0x1280120) == 0) {
					return true;
				}
				if(x1 == x2 + 1 && y1 == y2) {
					return true;
				}
				if(x1 == x2 && y1 == y2 - 1) {
					return true;
				}
			} else if(i1 == 3) {
				if(x1 == x2 - 1 && y1 == y2) {
					return true;
				}
				if(x1 == x2 && y1 == y2 + 1 && (flagMap[x1][y1] & 0x1280120) == 0) {
					return true;
				}
				if(x1 == x2 + 1 && y1 == y2 && (flagMap[x1][y1] & 0x1280180) == 0) {
					return true;
				}
				if(x1 == x2 && y1 == y2 - 1) {
					return true;
				}
			}
		}
		if(j1 == 9) {
			if(x1 == x2 && y1 == y2 + 1 && (flagMap[x1][y1] & 0x20) == 0) {
				return true;
			}
			if(x1 == x2 && y1 == y2 - 1 && (flagMap[x1][y1] & 2) == 0) {
				return true;
			}
			if(x1 == x2 - 1 && y1 == y2 && (flagMap[x1][y1] & 8) == 0) {
				return true;
			}
			if(x1 == x2 + 1 && y1 == y2 && (flagMap[x1][y1] & 0x80) == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean method220(int i, int j, int k, int l, int i1, int j1) {
		if(j1 == i && k == j) {
			return true;
		}
		j1 -= offsetX;
		k -= offsetY;
		i -= offsetX;
		j -= offsetY;
		if(l == 6 || l == 7) {
			if(l == 7) {
				i1 = i1 + 2 & 3;
			}
			if(i1 == 0) {
				if(j1 == i + 1 && k == j && (flagMap[j1][k] & 0x80) == 0) {
					return true;
				}
				if(j1 == i && k == j - 1 && (flagMap[j1][k] & 2) == 0) {
					return true;
				}
			} else if(i1 == 1) {
				if(j1 == i - 1 && k == j && (flagMap[j1][k] & 8) == 0) {
					return true;
				}
				if(j1 == i && k == j - 1 && (flagMap[j1][k] & 2) == 0) {
					return true;
				}
			} else if(i1 == 2) {
				if(j1 == i - 1 && k == j && (flagMap[j1][k] & 8) == 0) {
					return true;
				}
				if(j1 == i && k == j + 1 && (flagMap[j1][k] & 0x20) == 0) {
					return true;
				}
			} else if(i1 == 3) {
				if(j1 == i + 1 && k == j && (flagMap[j1][k] & 0x80) == 0) {
					return true;
				}
				if(j1 == i && k == j + 1 && (flagMap[j1][k] & 0x20) == 0) {
					return true;
				}
			}
		}
		if(l == 8) {
			if(j1 == i && k == j + 1 && (flagMap[j1][k] & 0x20) == 0) {
				return true;
			}
			if(j1 == i && k == j - 1 && (flagMap[j1][k] & 2) == 0) {
				return true;
			}
			if(j1 == i - 1 && k == j && (flagMap[j1][k] & 8) == 0) {
				return true;
			}
			if(j1 == i + 1 && k == j && (flagMap[j1][k] & 0x80) == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean method221(int i, int j, int k, int l, int i1, int j1, int k1) {
		final int l1 = j + j1 - 1;
		final int i2 = i + l - 1;
		if(k >= j && k <= l1 && k1 >= i && k1 <= i2) {
			return true;
		}
		if(k == j - 1 && k1 >= i && k1 <= i2 && (flagMap[k - offsetX][k1 - offsetY] & 8) == 0 && (i1 & 8) == 0) {
			return true;
		}
		if(k == l1 + 1 && k1 >= i && k1 <= i2 && (flagMap[k - offsetX][k1 - offsetY] & 0x80) == 0 && (i1 & 2) == 0) {
			return true;
		}
		return k1 == i - 1 && k >= j && k <= l1 && (flagMap[k - offsetX][k1 - offsetY] & 2) == 0 && (i1 & 4) == 0 || k1 == i2 + 1 && k >= j && k <= l1 && (flagMap[k - offsetX][k1 - offsetY] & 0x20) == 0 && (i1 & 1) == 0;
	}
}