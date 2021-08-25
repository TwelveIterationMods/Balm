package net.blay09.mods.balm.api.config;

public interface BalmConfig {
    <T> T initialize(Class<T> clazz);
    <T> T getConfig(Class<T> clazz);
    <T> void saveConfig(Class<T> clazz);
}
