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
}
