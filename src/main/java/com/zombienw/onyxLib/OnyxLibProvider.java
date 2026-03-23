package com.zombienw.onyxLib;

public class OnyxLibProvider {
    private static OnyxLib api;

    private OnyxLibProvider() {}

    static void set(OnyxLib instance) { api = instance; }
    static void clear() { api = null; }

    public static OnyxLib get() {
        if (api == null) throw new IllegalStateException("OnyxLib has not been initialized yet.");
        return api;
    }
}
