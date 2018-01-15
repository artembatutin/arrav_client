package net.edge.activity.ui.util;

import net.edge.Client;
import net.edge.Config;
import net.edge.cache.unit.ImageCache;
import net.edge.game.model.Mobile;
import net.edge.media.Rasterizer2D;
import net.edge.media.img.BitmapImage;

public class AssetDrawer {

	/**
	 * Client instance.
	 */
	public Client client;

	/**
	 * AssetDrawer constructor.
	 * @param client instance
	 */
	public AssetDrawer(Client client) {
		this.client = client;
	}

	private int hitBarFill(int current, int max, int size) {
		double res = (current * size * 1.f / max);
		if(res > size) {
			return size;
		}
		return (int) res;
	}

	public void drawBar(Mobile obj) {
		if(obj.special != 101) {
			int offset = -5;
			switch(Config.def.hitbar()) {
				case 0:
					int size = 20;
					int fill = hitBarFill(obj.special, 100, size);
					Rasterizer2D.fillRectangle(client.spriteDrawX - size / 2, client.spriteDrawY - 3 + offset, fill, 5, 65280);
					Rasterizer2D.fillRectangle(client.spriteDrawX - size / 2 + fill, client.spriteDrawY - 3, size - fill + offset, 5, 0xff0000);
					break;
				
				case 1:
					if(obj.maxHealth < 2000) {
						int size2 = 46;
						int fill2 = hitBarFill(obj.special, 100, size2);
						ImageCache.get(1656).drawImage(client.spriteDrawX - 28, client.spriteDrawY - 5 + offset);
						Rasterizer2D.setClip(client.spriteDrawX - 28, client.spriteDrawY - 5, client.spriteDrawX - 28 + fill2, client.spriteDrawY + 2 + offset);
						ImageCache.get(1655).drawImage(client.spriteDrawX - 28, client.spriteDrawY - 5 + offset);
						Rasterizer2D.removeClip();
					} else {
						int size2 = 80;
						int fill2 = hitBarFill(obj.special, 100, size2);
						ImageCache.get(1658).drawImage(client.spriteDrawX - 45, client.spriteDrawY - 5 + offset);
						Rasterizer2D.setClip(client.spriteDrawX - 45, client.spriteDrawY - 5, client.spriteDrawX - 45 + fill2, client.spriteDrawY + 2 + offset);
						ImageCache.get(1657).drawImage(client.spriteDrawX - 45, client.spriteDrawY - 5 + offset);
						Rasterizer2D.removeClip();
					}
					break;
				case 2:
					int size2 = 50;
					int fill2 = hitBarFill(obj.special, 100, size2);
					Rasterizer2D.fillRoundedRectangle(client.spriteDrawX - size2 / 2 - 1, client.spriteDrawY - 4 + offset, size2 + 2, 7, 2, 0x000000, 120);
					Rasterizer2D.fillRoundedRectangle(client.spriteDrawX - size2 / 2, client.spriteDrawY - 3 + offset, fill2, 5, 3, 65280, 180);
					break;
			}
		}
		switch(Config.def.hitbar()) {
			case 0:
				int size = obj.maxHealth > 2000 ? 60 : 30;
				int fill = hitBarFill(obj.currentHealth, obj.maxHealth, size);
				Rasterizer2D.fillRectangle(client.spriteDrawX - size / 2, client.spriteDrawY - 3, fill, 5, 65280);
				Rasterizer2D.fillRectangle(client.spriteDrawX - size / 2 + fill, client.spriteDrawY - 3, size - fill, 5, 0xff0000);
				break;
			
			case 1:
				if(obj.maxHealth < 2000) {
					int fill2 = hitBarFill(obj.currentHealth, obj.maxHealth, 56);
					ImageCache.get(1656).drawImage(client.spriteDrawX - 28, client.spriteDrawY - 5);
					Rasterizer2D.setClip(client.spriteDrawX - 28, client.spriteDrawY - 5, client.spriteDrawX - 28 + fill2, client.spriteDrawY + 2);
					ImageCache.get(1655).drawImage(client.spriteDrawX - 28, client.spriteDrawY - 5);
					Rasterizer2D.removeClip();
				} else {
					int fill2 = hitBarFill(obj.currentHealth, obj.maxHealth, 90);
					ImageCache.get(1658).drawImage(client.spriteDrawX - 45, client.spriteDrawY - 5);
					Rasterizer2D.setClip(client.spriteDrawX - 45, client.spriteDrawY - 5, client.spriteDrawX - 45 + fill2, client.spriteDrawY + 2);
					ImageCache.get(1657).drawImage(client.spriteDrawX - 45, client.spriteDrawY - 5);
					Rasterizer2D.removeClip();
				}
				break;
			case 2:
				int size2 = obj.maxHealth > 2000 ? 100 : 60;
				int fill2 = hitBarFill(obj.currentHealth, obj.maxHealth, size2);
				Rasterizer2D.fillRoundedRectangle(client.spriteDrawX - size2 / 2 - 1, client.spriteDrawY - 4, size2 + 2, 7, 2, 0x000000, 120);
				Rasterizer2D.fillRoundedRectangle(client.spriteDrawX - size2 / 2, client.spriteDrawY - 3, fill2, 5, 3, 65280, 180);
				break;
		}
	}

	/**
	 * Draws a single hitsplat.
	 */
	public void drawHit(Mobile obj, int id) {
		int hit = Config.def.hitsplat();
		switch(hit) {
			case 0:
			case 1:
				if(client.spriteDrawX > -1) {
					if(id == 1) {
						client.spriteDrawY -= 20;
					}
					if(id == 2) {
						client.spriteDrawX -= 15;
						client.spriteDrawY -= 10;
					}
					if(id == 3) {
						client.spriteDrawX += 15;
						client.spriteDrawY -= 10;
					}
					if(Config.def.hitsplat() == 0)
						ImageCache.get(988 + obj.oldHitMarkTypes[id]).drawImage(client.spriteDrawX - 12, client.spriteDrawY - 12);
					else
						ImageCache.get(997 + obj.oldHitMarkTypes[id]).drawImage(client.spriteDrawX - 12, client.spriteDrawY - 12);
					client.smallFont.drawCenteredString(obj.hitArray[id] + "", client.spriteDrawX, client.spriteDrawY + 4, 0);
					client.smallFont.drawCenteredString(obj.hitArray[id] + "", client.spriteDrawX - 1, client.spriteDrawY + 3, 0xffffff);
				}
				break;
			case 2:
			case 3:
				if(client.spriteDrawX > -1) {
					if(obj.moveTimer[id] == 0) {
						if(obj.hitmarkMove[id] > -25) {
							obj.hitmarkMove[id]--;
						}
						obj.moveTimer[id] = 1;
					} else {
						obj.moveTimer[id]--;
					}
					if(obj.hitmarkMove[id] <= -19) {
						obj.hitmarkTrans[id] -= 10;
					}
					int hitLength = (int) Math.log10(obj.hitArray[id]) + 1;
					hitmarkDraw(hit, obj, hitLength, obj.hitMarkTypes[id], obj.hitIcon[id], obj.hitArray[id], obj.soakDamage[id], obj.hitmarkMove[id], obj.hitmarkTrans[id], id);
				}
				break;

		}
	}

	/**
	 * Draws the floating 600+ hitsplat.
	 */
	private void hitmarkDraw(int config, Mobile e, int hitLength, int type, int icon, int damage, int soak, int move, int opacity, int mask) {
		int drawPos = 0;
		if(mask == 0) {
			e.hitMarkPos[0] = client.spriteDrawY + move;
			drawPos = e.hitMarkPos[0];
		}
		if(mask != 0) {
			e.hitMarkPos[mask] = e.hitMarkPos[0] + (19 * mask);
			drawPos = e.hitMarkPos[mask];
		}
		if(damage > 0) {
			BitmapImage end1, middle, end2;
			int x = 0;
			switch(hitLength) {
				case 1:
					x = 8;
					break;
				case 2:
					x = 4;
					break;
				case 3:
					x = 1;
					break;
			}
			if(soak > 0) {
				x -= 16;
			}
			if(config == 3) {
				if(icon > 2)
					icon = 255;
				if(type == 2 || type == 3)
					icon = 4;
				if(type == 5)
					icon = 5;
			}
			if(icon != 255) {
				ImageCache.get(icon + (config == 3 ? 1659 : 183)).drawImage(client.spriteDrawX - 34 + x + (config == 3 ? 5 : 0), drawPos - 29, opacity);
			}

			if(config == 2) {
				end1 = ImageCache.get((type * 3) + 150);
				middle = ImageCache.get((type * 3) + 151);
				end2 = ImageCache.get((type * 3) + 152);
				end1.drawImage(client.spriteDrawX - 12 + x, drawPos - 27, opacity);
				x += 4;
				for(int i = 0; i < hitLength * 2; i++) {
					middle.drawImage(client.spriteDrawX - 12 + x, drawPos - 27, opacity);
					x += 4;
				}
				end2.drawImage(client.spriteDrawX - 12 + x, drawPos - 27, opacity);
				(type == 1 ? client.bigHitFont : client.smallHitFont).drawCenteredString(damage + "", client.spriteDrawX + 4 + (soak > 0 ? -16 : 0), drawPos + (type == 1 ? 2 : 32) - 15, 0xffffff, opacity);
			} else {
				String text = damage + "";
				Rasterizer2D.fillRoundedRectangle(client.spriteDrawX + (soak > 0 ? -16 : 0) - 3, drawPos - 27, (type == 1 ? client.bigHitFont : client.smallHitFont).getStringWidth(text) + 6, 19, 5, 0x000000, opacity / 2);
				(type == 1 ? client.bigHitFont : client.smallHitFont).drawLeftAlignedString(text, client.spriteDrawX + (soak > 0 ? -16 : 0), drawPos + (type == 1 ? 5 : 34) - 19, 0xffffff);
			}

			if(soak > 0) {
				client.drawSoak(soak, opacity, drawPos - 15, x);
			}
		} else {
			ImageCache.get(149).drawImage(client.spriteDrawX - 12, drawPos - 29, opacity);
		}
	}

	/**
	 * Draws the menu.
	 * @param dx x_position
	 * @param dy y_position
	 */
	public void drawMenu(int dx, int dy, boolean demo) {
		int x;
		int y;
		int w;
		int h;
		int mx = 0;
		int my = 0;
		if(demo) {
			x = dx;
			y = dy;
			w = 160;
			h = 56;
			client.menuItemName[0] = "Examine @lre@Coins";
			client.menuItemName[1] = "Drop @lre@Coins";
			client.menuPos = 2;
		} else {
			x = client.menuX + dx;
			y = client.menuY + dy;
			w = client.menuWidth;
			h = client.menuHeight + 1;
			mx = client.mouseX + dx;
			my = client.mouseY + dy;
		}
		switch(Config.def.menu()) {
			case 1:
				Rasterizer2D.fillRectangle(x, y, w, h, 0x5d5447);
				Rasterizer2D.fillRectangle(x + 1, y + 1, w - 2, 16, 0);
				Rasterizer2D.drawRectangle(x + 1, y + 18, w - 2, h - 19, 0);
				client.boldFont.drawLeftAlignedString("Choose Option", x + 3, y + 14, 0x5d5447);
				for(int l1 = 0; l1 < client.menuPos; l1++) {
					final int i2 = y + 31 + (client.menuPos - 1 - l1) * 15;
					int j2 = 0xffffff;
					if(mx > x && mx < x + w && my > i2 - 13 && my < i2 + 3) {
						j2 = 0xffff00;
					}
					client.boldFont.drawLeftAlignedEffectString(client.menuItemName[l1], x + 3, i2, j2, true);
				}
				if(demo)
					client.boldFont.drawLeftAlignedEffectString(client.menuItemName[0], x + 3, y + 46, 0xffff00, true);
				break;

			case 2:
				Rasterizer2D.fillRectangle(x, y, w, h, 0x5d5447, 150);
				Rasterizer2D.fillRectangle(x + 1, y + 1, w - 2, 16, 0, 150);
				Rasterizer2D.drawRectangle(x + 1, y + 18, w - 2, h - 19, 0, 150);
				client.boldFont.drawLeftAlignedString("Choose Option", x + 3, y + 14, 0x5d5447);
				for(int l1 = 0; l1 < client.menuPos; l1++) {
					final int i2 = y + 31 + (client.menuPos - 1 - l1) * 15;
					int j2 = 0xffffff;
					if(mx > x && mx < x + w && my > i2 - 13 && my < i2 + 3) {
						j2 = 0xffff00;
					}
					client.boldFont.drawLeftAlignedEffectString(client.menuItemName[l1], x + 3, i2, j2, true);
				}
				if(demo)
					client.boldFont.drawLeftAlignedEffectString(client.menuItemName[0], x + 3, y + 46, 0xffff00, true);
				break;

			case 3:
				Rasterizer2D.drawRectangle(x, y + 2, w, h - 4, 0x706a5e);
				Rasterizer2D.drawRectangle(x + 1, y + 1, w - 2, h - 2, 0x706a5e);
				Rasterizer2D.drawRectangle(x + 2, y, w - 4, h, 0x706a5e);
				Rasterizer2D.drawRectangle(x + 3, y + 1, w - 6, h - 2, 0x2d2822);
				Rasterizer2D.drawRectangle(x + 2, y + 2, w - 4, h - 4, 0x2d2822);
				Rasterizer2D.drawRectangle(x + 1, y + 3, w - 2, h - 6, 0x2d2822);
				Rasterizer2D.drawRectangle(x + 2, y + 19, w - 4, h - 22, 0x524a3d);
				Rasterizer2D.drawRectangle(x + 3, y + 20, w - 6, h - 22, 0x524a3d);
				Rasterizer2D.drawRectangle(x + 3, y + 20, w - 6, h - 23, 0x2b271c);
				Rasterizer2D.fillRectangle(x + 3, y + 2, w - 6, 1, 0x2a291b);
				Rasterizer2D.fillRectangle(x + 2, y + 3, w - 4, 1, 0x2a261b);
				Rasterizer2D.fillRectangle(x + 2, y + 4, w - 4, 1, 0x252116);
				Rasterizer2D.fillRectangle(x + 2, y + 5, w - 4, 1, 0x211e15);
				Rasterizer2D.fillRectangle(x + 2, y + 6, w - 4, 1, 0x1e1b12);
				Rasterizer2D.fillRectangle(x + 2, y + 7, w - 4, 1, 0x1a170e);
				Rasterizer2D.fillRectangle(x + 2, y + 8, w - 4, 2, 0x15120b);
				Rasterizer2D.fillRectangle(x + 2, y + 10, w - 4, 1, 0x100d08);
				Rasterizer2D.fillRectangle(x + 2, y + 11, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 12, w - 4, 1, 0x080703);
				Rasterizer2D.fillRectangle(x + 2, y + 13, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 14, w - 4, 1, 0x070802);
				Rasterizer2D.fillRectangle(x + 2, y + 15, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 16, w - 4, 1, 0x070802);
				Rasterizer2D.fillRectangle(x + 2, y + 17, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 18, w - 4, 1, 0x2a291b);
				Rasterizer2D.fillRectangle(x + 3, y + 19, w - 6, 1, 0x564943);
				Rasterizer2D.fillRectangle(x + 4, y + 21, w - 8, h - 25, 0x2B271C);
				client.boldFont.drawLeftAlignedString("Choose Option", x + 3, y + 14, 0xc6b895);
				for(int l1 = 0; l1 < client.menuPos; l1++) {
					final int i2 = y + 31 + (client.menuPos - 1 - l1) * 15;
					int j2 = 0xc6b895;
					if(mx > x && mx < x + w && my > i2 - 13 && my < i2 + 3) {
						Rasterizer2D.fillRectangle(x + 3, i2 - 11, w - 6, 15, 0x6f695d);
						j2 = 0xeee5c6;
					}
					client.boldFont.drawLeftAlignedEffectString(client.menuItemName[l1], x + 4, i2, j2, true);
				}
				if(demo) {
					Rasterizer2D.fillRectangle(x + 3, y + 35, w - 6, 15, 0x6f695d);
					client.boldFont.drawLeftAlignedEffectString(client.menuItemName[0], x + 4, y + 46, 0xeee5c6, true);
				}
				break;

			case 4:
				Rasterizer2D.drawRectangle(x, y + 2, w, h - 4, 0x706a5e);
				Rasterizer2D.drawRectangle(x + 1, y + 1, w - 2, h - 2, 0x706a5e);
				Rasterizer2D.drawRectangle(x + 2, y, w - 4, h, 0x706a5e);
				Rasterizer2D.drawRectangle(x + 3, y + 1, w - 6, h - 2, 0x2d2822);
				Rasterizer2D.drawRectangle(x + 2, y + 2, w - 4, h - 4, 0x2d2822);
				Rasterizer2D.drawRectangle(x + 1, y + 3, w - 2, h - 6, 0x2d2822);
				Rasterizer2D.drawRectangle(x + 2, y + 19, w - 4, h - 22, 0x524a3d);
				Rasterizer2D.drawRectangle(x + 3, y + 20, w - 6, h - 22, 0x524a3d);
				Rasterizer2D.drawRectangle(x + 3, y + 20, w - 6, h - 23, 0x2b271c);
				Rasterizer2D.fillRectangle(x + 3, y + 2, w - 6, 1, 0x2a291b);
				Rasterizer2D.fillRectangle(x + 2, y + 3, w - 4, 1, 0x2a261b);
				Rasterizer2D.fillRectangle(x + 2, y + 4, w - 4, 1, 0x252116);
				Rasterizer2D.fillRectangle(x + 2, y + 5, w - 4, 1, 0x211e15);
				Rasterizer2D.fillRectangle(x + 2, y + 6, w - 4, 1, 0x1e1b12);
				Rasterizer2D.fillRectangle(x + 2, y + 7, w - 4, 1, 0x1a170e);
				Rasterizer2D.fillRectangle(x + 2, y + 8, w - 4, 2, 0x15120b);
				Rasterizer2D.fillRectangle(x + 2, y + 10, w - 4, 1, 0x100d08);
				Rasterizer2D.fillRectangle(x + 2, y + 11, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 12, w - 4, 1, 0x080703);
				Rasterizer2D.fillRectangle(x + 2, y + 13, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 14, w - 4, 1, 0x070802);
				Rasterizer2D.fillRectangle(x + 2, y + 15, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 16, w - 4, 1, 0x070802);
				Rasterizer2D.fillRectangle(x + 2, y + 17, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 18, w - 4, 1, 0x2a291b);
				Rasterizer2D.fillRectangle(x + 3, y + 19, w - 6, 1, 0x564943);
				Rasterizer2D.fillRectangle(x + 4, y + 21, w - 8, h - 25, 0x112329);
				client.plainFont.drawLeftAlignedString("Choose Option", x + 3, y + 14, 0xc6b895);
				for(int l1 = 0; l1 < client.menuPos; l1++) {
					final int i2 = y + 31 + (client.menuPos - 1 - l1) * 15;
					int j2 = 0xc6b895;
					if(mx > x && mx < x + w && my > i2 - 13 && my < i2 + 3) {
						Rasterizer2D.fillRectangle(x + 3, i2 - 11, w - 6, 15, 0x26566C);
						j2 = 0xeee5c6;
					}
					client.plainFont.drawLeftAlignedEffectString(client.menuItemName[l1], x + 4, i2, j2, true);
				}
				if(demo) {
					Rasterizer2D.fillRectangle(x + 3, y + 46 - 11, w - 6, 15, 0x26566C);
					client.plainFont.drawLeftAlignedEffectString(client.menuItemName[0], x + 4, y + 46, 0xeee5c6, true);
				}
				break;

			case 5:
				Rasterizer2D.drawRectangle(x, y + 2, w, h - 4, 0x706a5e);
				Rasterizer2D.drawRectangle(x + 1, y + 1, w - 2, h - 2, 0x706a5e);
				Rasterizer2D.drawRectangle(x + 2, y, w - 4, h, 0x706a5e);
				Rasterizer2D.drawRectangle(x + 3, y + 1, w - 6, h - 2, 0x2d2822);
				Rasterizer2D.drawRectangle(x + 2, y + 2, w - 4, h - 4, 0x2d2822);
				Rasterizer2D.drawRectangle(x + 1, y + 3, w - 2, h - 6, 0x2d2822);
				Rasterizer2D.drawRectangle(x + 2, y + 19, w - 4, h - 22, 0x524a3d);
				Rasterizer2D.drawRectangle(x + 3, y + 20, w - 6, h - 22, 0x524a3d);
				Rasterizer2D.drawRectangle(x + 3, y + 20, w - 6, h - 23, 0x2b271c);
				Rasterizer2D.fillRectangle(x + 3, y + 2, w - 6, 1, 0x2a291b);
				Rasterizer2D.fillRectangle(x + 2, y + 3, w - 4, 1, 0x2a261b);
				Rasterizer2D.fillRectangle(x + 2, y + 4, w - 4, 1, 0x252116);
				Rasterizer2D.fillRectangle(x + 2, y + 5, w - 4, 1, 0x211e15);
				Rasterizer2D.fillRectangle(x + 2, y + 6, w - 4, 1, 0x1e1b12);
				Rasterizer2D.fillRectangle(x + 2, y + 7, w - 4, 1, 0x1a170e);
				Rasterizer2D.fillRectangle(x + 2, y + 8, w - 4, 2, 0x15120b);
				Rasterizer2D.fillRectangle(x + 2, y + 10, w - 4, 1, 0x100d08);
				Rasterizer2D.fillRectangle(x + 2, y + 11, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 12, w - 4, 1, 0x080703);
				Rasterizer2D.fillRectangle(x + 2, y + 13, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 14, w - 4, 1, 0x070802);
				Rasterizer2D.fillRectangle(x + 2, y + 15, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 16, w - 4, 1, 0x070802);
				Rasterizer2D.fillRectangle(x + 2, y + 17, w - 4, 1, 0x090a04);
				Rasterizer2D.fillRectangle(x + 2, y + 18, w - 4, 1, 0x2a291b);
				Rasterizer2D.fillRectangle(x + 3, y + 19, w - 6, 1, 0x564943);
				Rasterizer2D.fillRectangle(x + 4, y + 21, w - 8, h - 25, 0x112329, 200);
				client.plainFont.drawLeftAlignedString("Choose Option", x + 3, y + 14, 0xc6b895);
				for(int l1 = 0; l1 < client.menuPos; l1++) {
					final int i2 = y + 31 + (client.menuPos - 1 - l1) * 15;
					int j2 = 0xc6b895;
					if(mx > x && mx < x + w && my > i2 - 13 && my < i2 + 3) {
						Rasterizer2D.fillRectangle(x + 3, i2 - 11, w - 6, 15, 0x26566C, 150);
						j2 = 0xeee5c6;
					}
					client.plainFont.drawLeftAlignedEffectString(client.menuItemName[l1], x + 4, i2, j2, true);
				}
				if(demo) {
					Rasterizer2D.fillRectangle(x + 3, y + 46 - 11, w - 6, 15, 0x26566C, 150);
					client.plainFont.drawLeftAlignedEffectString(client.menuItemName[0], x + 4, y + 46, 0xeee5c6, true);
				}
				break;

			case 6:
				Rasterizer2D.fillRectangle(x - 2, y + 11, w + 8, h - 13, 0x000000, 100);
				Rasterizer2D.fillRectangle(x, y + 13, w + 4, h - 17, 0x000000, 150);
				for(int l1 = 0; l1 < client.menuPos; l1++) {
					final int i2 = y + 31 + (client.menuPos - 1 - l1) * 15;
					int j2 = 0xc6b895;
					if(mx > x && mx < x + w && my > i2 - 13 && my < i2 + 3) {
						Rasterizer2D.fillRectangle(x + 3, i2 - 14, w - 6, 15, 0xCB8804, 150);
						j2 = 0xeee5c6;
					}
					client.plainFont.drawLeftAlignedEffectString(client.menuItemName[l1], x + 6, i2 - 2, j2, true);
				}
				if(demo) {
					Rasterizer2D.fillRectangle(x + 3, y + 32, w - 6, 15, 0xCB8804, 150);
					client.plainFont.drawLeftAlignedEffectString(client.menuItemName[0], x + 6, y + 44, 0xeee5c6, true);
				}
				break;
		}
	}


}
