package com.zombienw.onyxlib.api.item;

/**
 * Represents common Minecraft item parent models for JSON resource files.
 */
public enum ItemModelParent {
    GENERATED("item/generated"),
    HANDHELD("item/handheld"),
    HANDHELD_ROD("item/handheld_rod");

    private final String value;

    ItemModelParent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Default parent model for items.
     */
    public static final ItemModelParent DEFAULT = GENERATED;
}
