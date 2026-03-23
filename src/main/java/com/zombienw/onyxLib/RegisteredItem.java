package com.zombienw.onyxLib;

import java.util.function.Consumer;

public class RegisteredItem {

    private final String fullId;
    private final String namespace;
    private final CustomItem item;

    public RegisteredItem(String namespace, CustomItem item) {
        this.namespace = namespace;
        this.item = item;
        this.fullId = namespace + ":" + item.getId();
    }

    public String getFullId() { return fullId; }
    public String getNamespace() { return namespace; }
    public CustomItem getItem() { return item; }


    @Override
    public String toString() {
        return "RegisteredItem{" + fullId + "}";
    }
}
