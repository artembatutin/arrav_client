package net.arrav.world.model;

import net.arrav.util.string.ColorConstants;

import javax.rmi.CORBA.Tie;

/**
 * An enumerated type representing a tier and it's properties.
 * @author Tamatea <tamateea@gmail.com>
 */
public enum  TieredEntity {

    TIER1("[Tier 1]", ColorConstants.BURGUNDY),
    TIER2("[Tier 2]", ColorConstants.RED),
    TIER3("[Tier 3]", ColorConstants.BLUE),
    TIER4("[Tier 4]", ColorConstants.ORANGE),
    TIER5("[Tier 5]", ColorConstants.RED_TEXT),
    TIER6("[Tier 6]", ColorConstants.WHITE),
    TIER7("[Tier 7]", ColorConstants.GOLD),
    TIER8("[Tier 8]", ColorConstants.YELLOW),
    TIER9("[Tier 9]", ColorConstants.PALE_GREEN),
    TIER10("[Tier 10]",-1),

    SPECIAL("[Special]", ColorConstants.ORANGE)
        ;

    /**
     * The color the entity will be displayed as in context menu.
     */
    private int color;

    /**
     * The name of the tier (Could use name() but wanna save time with having to format 50 times per second.
     */
    private String name;


    TieredEntity(String name, int color) {
        this.color = color;
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public static TieredEntity ofOrdinal(int ordinal) {
        for(TieredEntity tier : VALUES)
            if(tier.ordinal() == ordinal)
                return tier;
            return null;
    }

    private static final TieredEntity[] VALUES = values();
}
