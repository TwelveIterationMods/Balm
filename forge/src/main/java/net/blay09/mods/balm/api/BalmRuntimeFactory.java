package net.blay09.mods.balm.api;

import net.blay09.mods.balm.forge.ForgeBalmRuntime;

public class BalmRuntimeFactory {
    public static BalmRuntime create() {
        return new ForgeBalmRuntime();
    }
}
