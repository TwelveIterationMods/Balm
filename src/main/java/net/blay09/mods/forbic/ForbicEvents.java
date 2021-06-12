package net.blay09.mods.forbic;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;
import java.util.function.Function;

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

    public static <T> void addCustomListener(Class<T> clazz, Consumer<T> handler) {
        // TODO
    }

    @FunctionalInterface
    public interface PlayerLoginHandler {
        void handle(ServerPlayer player);
    }

    @FunctionalInterface
    public interface FovUpdateHandler {
        Float handle(LivingEntity entity);
    }

    @FunctionalInterface
    public interface LivingDamageHandler {
        void handle(LivingEntity entity);
    }

    @FunctionalInterface
    public interface BiomeLoadingHandler {
        void handle();
    }
}
