package net.blay09.mods.forbic.network;

import net.blay09.mods.forbic.config.Synced;
import net.minecraft.network.FriendlyByteBuf;

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
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            if (String.class.isAssignableFrom(type)) {
                if (field.getAnnotation(Synced.class) != null) {
                    syncedFields.add(field);
                }
            } else if (Enum.class.isAssignableFrom(type)) {
                if (field.getAnnotation(Synced.class) != null) {
                    syncedFields.add(field);
                }
            } else if (List.class.isAssignableFrom(type)) {
                // No syncing of lists for now
            } else if (type.isPrimitive()) {
                if (field.getAnnotation(Synced.class) != null) {
                    syncedFields.add(field);
                }
            } else {
                syncedFields.addAll(getSyncedFields(field.getType()));
            }
        }
        return syncedFields;
    }

    public static Object deepCopy(Object from, Object to) {
        Field[] fields = from.getClass().getDeclaredFields();
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

    public static <T> Supplier<T> createDeepCopyFactory(Object from, Supplier<T> factory) {
        return () -> {
            T to = factory.get();
            deepCopy(from, to);
            return to;
        };
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> Function<FriendlyByteBuf, TMessage> createDecoder(Class<?> clazz, Function<TData, TMessage> messageFactory, Supplier<TData> dataFactory) {
        List<Field> syncedFields = getSyncedFields(clazz);
        syncedFields.sort(Comparator.comparing(Field::getName));
        return buf -> {
            TData data = dataFactory.get();
            for (Field field : syncedFields) {
                Class<?> type = field.getType();
                Object value;
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
                    continue;
                }
                try {
                    field.set(data, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return messageFactory.apply(data);
        };
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> BiConsumer<TMessage, FriendlyByteBuf> createEncoder(Class<TData> clazz) {
        List<Field> syncedFields = getSyncedFields(clazz);
        syncedFields.sort(Comparator.comparing(Field::getName));
        return (message, buf) -> {
            TData data = message.getData();
            for (Field field : syncedFields) {
                Class<?> type = field.getType();
                final Object value;
                try {
                    value = field.get(data);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
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
                }
            }
        };
    }

}
