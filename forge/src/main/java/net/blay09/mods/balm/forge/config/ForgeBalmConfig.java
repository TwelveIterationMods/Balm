package net.blay09.mods.balm.forge.config;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.AbstractBalmConfig;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.api.network.ConfigReflection;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgeBalmConfig extends AbstractBalmConfig {

    private final Map<Class<?>, ModConfig> configs = new HashMap<>();
    private final Map<Class<?>, BalmConfigData> configData = new HashMap<>();

    private <T extends BalmConfigData> IConfigSpec<?> createConfigSpec(Class<T> clazz) {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        try {
            buildConfigSpec("", builder, clazz);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Config spec generation unexpectedly failed.", e);
        }
        return builder.build();
    }

    private void buildConfigSpec(String parentPath, ForgeConfigSpec.Builder builder, Class<?> clazz) throws IllegalAccessException {
        List<Field> fields = ConfigReflection.getAllFields(clazz);
        Object defaults = createConfigDataInstance(clazz);
        for (Field field : fields) {
            Class<?> type = field.getType();
            Object defaultValue = field.get(defaults);
            String path = parentPath + field.getName();

            Comment comment = field.getAnnotation(Comment.class);
            if (comment != null) {
                builder.comment(comment.value());
            }

            if (String.class.isAssignableFrom(type)) {
                builder.define(path, (String) defaultValue);
            } else if (List.class.isAssignableFrom(type)) {
                builder.defineListAllowEmpty(Arrays.asList(path.split("\\.")), () -> ((List<?>) defaultValue), it -> true);
            } else if (Enum.class.isAssignableFrom(type)) {
                builder.defineEnum(path, (Enum) defaultValue);
            } else if (int.class.isAssignableFrom(type)) {
                builder.defineInRange(path, (int) defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else if (float.class.isAssignableFrom(type)) {
                builder.defineInRange(path, (float) defaultValue, -Float.MAX_VALUE, Float.MAX_VALUE);
            } else if (double.class.isAssignableFrom(type)) {
                builder.defineInRange(path, (double) defaultValue, -Double.MAX_VALUE, Double.MAX_VALUE);
            } else if (boolean.class.isAssignableFrom(type)) {
                builder.define(path, (boolean) defaultValue);
            } else if (long.class.isAssignableFrom(type)) {
                builder.defineInRange(path, (long) defaultValue, Long.MIN_VALUE, Long.MAX_VALUE);
            } else {
                buildConfigSpec(path + ".", builder, type);
            }
        }
    }

    private <T extends BalmConfigData> T readConfigValues(Class<T> clazz, ModConfig config) {
        T instance = createConfigDataInstance(clazz);
        try {
            readConfigValues("", instance, config);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

    private <T> void readConfigValues(String parentPath, T instance, ModConfig config) throws IllegalAccessException {
        List<Field> fields = ConfigReflection.getAllFields(instance.getClass());
        for (Field field : fields) {
            String path = parentPath + field.getName();
            Class<?> type = field.getType();
            if (Integer.TYPE.isAssignableFrom(type)) {
                field.set(instance, config.getConfigData().getInt(path));
            } else if (type.isPrimitive() || String.class.isAssignableFrom(type) || List.class.isAssignableFrom(type)) {
                field.set(instance, config.getConfigData().get(path));
            } else if (type.isEnum()) {
                Enum<?> value = config.getConfigData().getEnum(path, (Class<Enum>) type);
                field.set(instance, value);
            } else {
                readConfigValues(path + ".", field.get(instance), config);
            }
        }
    }

    private <T extends BalmConfigData> void writeConfigValues(ModConfig config, T configData) {
        try {
            writeConfigValues("", config, configData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private <T> void writeConfigValues(String parentPath, ModConfig config, T instance) throws IllegalAccessException {
        List<Field> fields = ConfigReflection.getAllFields(instance.getClass());
        for (Field field : fields) {
            String path = parentPath + field.getName();
            Class<?> type = field.getType();
            Object value = field.get(instance);
            if (type.isPrimitive() || Enum.class.isAssignableFrom(type) || String.class.isAssignableFrom(type) || List.class.isAssignableFrom(type)) {
                config.getConfigData().set(path, value);
            } else {
                writeConfigValues(path + ".", config, field.get(instance));
            }
        }
    }

    @Override
    public <T extends BalmConfigData> T initializeBackingConfig(Class<T> clazz) {
        IConfigSpec<?> configSpec = createConfigSpec(clazz);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configSpec);

        FMLJavaModLoadingContext.get().getModEventBus().addListener((ModConfigEvent.Loading event) -> {
            configs.put(clazz, event.getConfig());
            T newConfigData = readConfigValues(clazz, event.getConfig());
            configData.put(clazz, newConfigData);

            setActiveConfig(clazz, newConfigData);
        });

        FMLJavaModLoadingContext.get().getModEventBus().addListener((ModConfigEvent.Reloading event) -> {
            configs.put(clazz, event.getConfig());
            T newConfigData = readConfigValues(clazz, event.getConfig());
            configData.put(clazz, newConfigData);

            if (getConfigSyncMessageFactory(clazz) == null || ServerLifecycleHooks.getCurrentServer() != null) {
                setActiveConfig(clazz, newConfigData);
            }

            Balm.getEvents().fireEvent(new ConfigReloadedEvent());
        });

        T initialData = createConfigDataInstance(clazz);
        configData.put(clazz, initialData);
        return initialData;
    }

    @NotNull
    private <T> T createConfigDataInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Config class or sub-class missing a public no-arg constructor.", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BalmConfigData> T getBackingConfig(Class<T> clazz) {
        return (T) configData.get(clazz);
    }

    @Override
    public <T extends BalmConfigData> void saveBackingConfig(Class<T> clazz) {
        ModConfig modConfig = configs.get(clazz);
        if (modConfig != null) {
            writeConfigValues(modConfig, configData.get(clazz));
            modConfig.save();
        }
    }

    @Override
    public File getConfigDir() {
        return FMLPaths.CONFIGDIR.get().toFile();
    }
}
