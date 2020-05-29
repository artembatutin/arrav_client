package net.arrav.world;

public enum CustomMaps {
    ALIEN(12918, 6828, 6829),
    DIABLO(13430, 6830, 6831),
    HELL(13942, 6832, 6833),
    SPIDER(14454, 6834, 6835),
    NEX_2(12916, 6836, 6837),
    SECURITY(13428, 6838, 6839),
    ENT(13940, 6840, 6841),
    BEELZEBUB(14452, 6842, 6843),
    SLAYER_TOWER(14964, 6844, 6845),
    TREASURE_ISLAND(15476, 6846, 6847),
    TRUMP(12402, 6848, 6849),
    TMNT_SWERS(12914, 6850, 6851),
    REAPER(13426, 6852, 6853),
    SLAYER_DUNGEON(13938, 6854, 6855),
    SLAYER_DUNGEON1(14194, 6856, 6857),
    RC(14706, 6858, 6859),
    AGILITY(12144, 6860, 6861),


    /**
     * home
     */
    HOME1(7486, 1706, 1707),
    HOME2(7487, 1708, 1709),
    HOME3(7488, 1710, 1711),
    HOME4(7742, 1712, 1713),
    HOME5(7743, 1714, 1715),//HOME
    HOME6(7744, 1716, 1717),
    HOME7(7998, 1718, 1719),
    HOME8(7999, 1720, 1721),
    HOME9(8000, 1722, 1723),



    GREAT_KOUREND(6457, 2300, 2301),


    ;


    private int region;
    private int landScape;
    private int objectMap;

    CustomMaps(int region, int landScape, int objectMap) {
        this.region = region;
        this.landScape = landScape;
        this.objectMap = objectMap;
    }

    public int getLandScape() {
        return landScape;
    }

    public int getObjectMap() {
        return objectMap;
    }

    public int getRegion() {
        return region;
    }

    public static final CustomMaps[] VALUES = values();

    public static CustomMaps ofOrdinal(int ordinal) {
        for(CustomMaps map : VALUES) {
            if(map.ordinal() == ordinal)
                return map;
        }
        return null;
    }

}
