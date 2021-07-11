package net.blay09.mods.balm.core;

public enum BalmEnvironment {
    CLIENT,
    SERVEr;

    public boolean isClient() {
        return this == CLIENT;
    }
}
