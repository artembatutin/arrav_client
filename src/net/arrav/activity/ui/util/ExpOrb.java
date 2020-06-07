package net.arrav.activity.ui.util;

import net.arrav.Client;
import net.arrav.Constants;
import net.arrav.graphic.Rasterizer2D;
import net.arrav.graphic.img.BitmapImage;
import net.arrav.util.SecondsTimer;
import net.arrav.util.SkillConstants;
import net.arrav.util.string.ColorConstants;

import java.text.NumberFormat;

/**
 * Created by Daniel on 2017-10-24.
 */
public class ExpOrb {

	/**
	 * The skill this orb is intended for.
	 */
	private final int skill;

	/**
	 * The sprite icon for this skill orb.
	 */
	private final BitmapImage icon;

	/**
	 * The show timer. Resets when this orb receives experience.
	 */
	private SecondsTimer showTimer = new SecondsTimer();

	/**
	 * The orb's current currentAlpha (transparency)
	 */
	private int alpha;

	/**
	 * Constructs this skill orb
	 */
	public ExpOrb(int skill, BitmapImage icon) {
		this.skill = skill;
		this.icon = icon;
	}

	/**
	 * Called upon the player receiving experience.
	 * <p>
	 * Resets the attributes of the orb to make sure the orb is drawn properly.
	 */
	public void receivedExperience() {
		alpha = 255;
		showTimer.start(5);
	}

	/**
	 * Draws this skill orb
	 */
	public void draw(int x, int y) {
		int width = 57, height = 57;
		int radius = width / 2;
		Rasterizer2D.fillCircle(x+(width / 2), y+ (height / 2), radius, ColorConstants.BURGUNDY, alpha);
		int stroke = 5;
		Rasterizer2D.draw_arc(x-1, y-1, width - (stroke + 3), height - (stroke + 3), stroke, 90, -(percentage()), ColorConstants.WHITE, alpha, 0, false);
		icon.drawImage(x + (width / 2) - icon.imageWidth / 2, (height / 2)- icon.imageHeight / 2 + y, alpha);
	}
	
	


	/**
	 * Draws a tooltip containing information about this skill orb.
	 */	
	static int lastSkill = -1;

	public void drawTooltip() {
		try {
			NumberFormat nf = NumberFormat.getInstance();
			int mouseX = Client.instance.mouseX;
			int mouseY = Client.instance.mouseY;
			int experience = Client.instance.currentExp[skill];
			int level = Client.instance.currentStats[skill];
			int staticLevel = SkillConstants.getLevelForExperience(experience);
			int maxExperience = SkillConstants.getExperienceForLevel(level + 1);
			int completion = staticLevel == 99 ? 100 : experience * 100 / maxExperience;
			int startExp = SkillConstants.getExperienceForLevel(level);
			int endExp = SkillConstants.getExperienceForLevel(level + 1);
			int percentage = (int) (100D * (experience - startExp) / (endExp - startExp));


			Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, 122, 82, 0x7D5C51, 150);
			Rasterizer2D.drawRectangle(mouseX, mouseY + 5, 122, 82, 0x000000);

			Client.instance.smallFont.drawLeftAlignedEffectString(Constants.SKILL_NAMES_ORDERED[skill],
					mouseX + Client.instance.smallFont.getEffectStringWidth(Constants.SKILL_NAMES_ORDERED[skill]) - 25, mouseY + 20,
					16777215, 0);
			Client.instance.smallFont.drawLeftAlignedEffectString("Level: @gre@" + Client.instance.maxStats[skill], mouseX + 5,
					mouseY + 35, 16777215, 0);
			Client.instance.smallFont.drawLeftAlignedEffectString("Exp: @gre@" + nf.format(Client.instance.currentExp[skill]),
					mouseX + 5, mouseY + 50, 16777215, 0);
			Client.instance.smallFont.drawLeftAlignedEffectString("Exp Left: @gre@" + nf.format(remainderExp()), mouseX + 5,
					mouseY + 65, 16777215, 0);

			Rasterizer2D.drawRoundedRectangle(mouseX, mouseY + 70, 121, 15, 0xED4747, 100, true, true);

			if (percentage >= 100) {
				percentage = 100;
			}

			Rasterizer2D.fillRoundedRectangle(mouseX + 2, mouseY + 70, 119, 15, 5, 0xDB2323);
			Rasterizer2D.fillRoundedRectangle(mouseX + 1, mouseY + 70, percentage, 15, 5, 0x37A351);


			Client.instance.smallFont.drawCenteredString(completion + "% ", mouseX + 118 / 2 + 10, mouseY + 83, 0xFFFFFF,
					1);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int currentLevel() {
		return Client.instance.maxStats[skill];
	}

	private int startExp() {
		return Client.instance.getXPForLevel(currentLevel());
	}

	private int requiredExp() {
		return Client.instance.getXPForLevel(currentLevel() + 1);
	}

	private int obtainedExp() {
		return Client.instance.currentExp[skill] - startExp();
	}

	private int remainderExp() {
		return requiredExp() - (startExp() + obtainedExp());
	}

	private int percentage() {
		int percent = 0;
		try {
			percent = (int) (((double) obtainedExp() / (double) (requiredExp() - startExp())) * 100);
			if (percent > 100) {
				percent = 100;
			}
		} catch (ArithmeticException e) {
			e.printStackTrace();
		}
		return percent;
	}

	public SecondsTimer getShowTimer() {
		return showTimer;
	}

	public int getSkill() {
		return skill;
	}

	public int getAlpha() {
		return alpha;
	}

	public void decrementAlpha() {
		alpha -= 5;
	}
}
