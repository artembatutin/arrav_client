package net.arrav.world.model;

public enum DataType {


    NEW(6),
    NEWEST(0),
    OSRS(7),
    CUSTOM(255);

    private int index;

    DataType(int index) {
    this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static DataType ofOrdinal(int ordinal) {
        for(DataType type : VALUES)
            if(type.ordinal() == ordinal)
                return type;
            return null;
    }
    private static final DataType[] VALUES = values();
}
