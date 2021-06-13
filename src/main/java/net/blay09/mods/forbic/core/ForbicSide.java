package net.blay09.mods.forbic.core;

public enum ForbicSide {
    CLIENT,
    SERVEr;

    public boolean isClient() {
        return this == CLIENT;
    }
}
