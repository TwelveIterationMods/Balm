package net.blay09.mods.forbic.event;


import java.util.function.Consumer;

public class ForbicEvents {
    public static void post(ForbicEvent event) {
        // TODO
    }

    public static void onPlayerLogin(PlayerLoginHandler handler) {
        // TODO
    }

    public static void onFovUpdate(FovUpdateHandler handler) {
        // TODO
    }

    public static void onLivingDamage(LivingDamageHandler handler) {
        // TODO
    }

    public static void onBiomeLoading(BiomeLoadingHandler handler) {
        // TODO
    }

    public static void onConfigReloaded(ConfigReloadedHandler handler) {
        // TODO
    }

    public static <T> void addCustomListener(Class<T> clazz, Consumer<T> handler) {
        // TODO
    }

}
