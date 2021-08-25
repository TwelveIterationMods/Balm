package net.blay09.mods.balm.api.network;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.config.BalmConfigHolder;
import net.blay09.mods.balm.api.config.Synced;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SyncConfigMessage<TData> {

    private final TData data;

    public SyncConfigMessage(TData data) {
        this.data = data;
    }

    public TData getData() {
        return data;
    }

    private static List<Field> getSyncedFields(Class<?> clazz) {
        List<Field> syncedFields = new ArrayList<>();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.getAnnotation(Synced.class) != null) {
                syncedFields.add(field);
            }
        }
        return syncedFields;
    }

    public static Object deepCopy(Object from, Object to) {
        Field[] fields = from.getClass().getFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            try {
                if (String.class.isAssignableFrom(type) || Enum.class.isAssignableFrom(type) || type.isPrimitive()) {
                    field.set(to, field.get(from));
                } else if (List.class.isAssignableFrom(type)) {
                    field.set(to, new ArrayList((Collection) field.get(from)));
                } else {
                    field.set(to, deepCopy(field.get(from), field.get(to)));
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return to;
    }

    public static <TData> Supplier<TData> createDeepCopyFactory(Supplier<TData> from, Supplier<TData> factory) {
        return () -> {
            TData to = factory.get();
            deepCopy(from.get(), to);
            return to;
        };
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> Function<FriendlyByteBuf, TMessage> createDecoder(Class<?> clazz, Function<TData, TMessage> messageFactory, Supplier<TData> dataFactory) {
        return buf -> {
            TData data = dataFactory.get();
            readSyncedFields(buf, data);
            return messageFactory.apply(data);
        };
    }

    private static <TData> void readSyncedFields(FriendlyByteBuf buf, TData data) {
        List<Field> syncedFields = getSyncedFields(data.getClass());
        syncedFields.sort(Comparator.comparing(Field::getName));
        for (Field field : syncedFields) {
            Class<?> type = field.getType();
            Object value;
            try {
                if (String.class.isAssignableFrom(type)) {
                    value = buf.readUtf();
                } else if (Enum.class.isAssignableFrom(type)) {
                    value = type.getEnumConstants()[buf.readByte()];
                } else if (int.class.isAssignableFrom(type)) {
                    value = buf.readInt();
                } else if (float.class.isAssignableFrom(type)) {
                    value = buf.readFloat();
                } else if (double.class.isAssignableFrom(type)) {
                    value = buf.readDouble();
                } else if (boolean.class.isAssignableFrom(type)) {
                    value = buf.readBoolean();
                } else if (long.class.isAssignableFrom(type)) {
                    value = buf.readLong();
                } else {
                    value = field.get(data);
                    readSyncedFields(buf, value);
                }
                field.set(data, value);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> BiConsumer<TMessage, FriendlyByteBuf> createEncoder(Class<TData> clazz) {
        return (message, buf) -> {
            TData data = message.getData();
            writeSyncedFields(buf, data);
        };
    }

    private static <TData> void writeSyncedFields(FriendlyByteBuf buf, TData data) {
        List<Field> syncedFields = getSyncedFields(data.getClass());
        syncedFields.sort(Comparator.comparing(Field::getName));
        for (Field field : syncedFields) {
            Class<?> type = field.getType();
            final Object value;
            try {
                value = field.get(data);
                if (String.class.isAssignableFrom(type)) {
                    buf.writeUtf((String) value);
                } else if (Enum.class.isAssignableFrom(type)) {
                    buf.writeByte(((Enum<?>) value).ordinal());
                } else if (int.class.isAssignableFrom(type)) {
                    buf.writeInt((int) value);
                } else if (float.class.isAssignableFrom(type)) {
                    buf.writeFloat((float) value);
                } else if (double.class.isAssignableFrom(type)) {
                    buf.writeDouble((float) value);
                } else if (boolean.class.isAssignableFrom(type)) {
                    buf.writeBoolean((boolean) value);
                } else if (long.class.isAssignableFrom(type)) {
                    buf.writeLong((long) value);
                } else {
                    writeSyncedFields(buf, field.get(data));
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static <TMessage extends SyncConfigMessage<TData>, TData extends BalmConfig> void register(ResourceLocation resourceLocation,
                                                                                                      Class<TMessage> messageClass,
                                                                                                      Function<TData, TMessage> messageFactory,
                                                                                                      Class<TData> dataClass,
                                                                                                      Supplier<TData> dataFactory) {
        Supplier<TData> copyFactory = SyncConfigMessage.createDeepCopyFactory(() -> Balm.getConfig().getConfig(dataClass), dataFactory);
        Balm.getNetworking().registerServerboundPacket(resourceLocation, messageClass, (TMessage message, FriendlyByteBuf buf) -> {
            TData data = message.getData();
            writeSyncedFields(buf, data);
        }, buf -> {
            TData data = copyFactory.get();
            readSyncedFields(buf, data);
            return messageFactory.apply(data);
        }, BalmConfigHolder::handleSync);
    }

}
