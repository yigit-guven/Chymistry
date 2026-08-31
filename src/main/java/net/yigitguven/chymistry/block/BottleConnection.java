package net.yigitguven.chymistry.block;

import net.minecraft.util.StringRepresentable;

public enum BottleConnection implements StringRepresentable {
    NONE("none"),
    NORTH("north"),
    EAST("east"),
    SOUTH("south"),
    WEST("west");

    private final String name;

    private BottleConnection(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
