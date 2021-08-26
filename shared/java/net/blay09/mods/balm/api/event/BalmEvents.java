package net.blay09.mods.balm.api.event;

import java.util.function.Consumer;

public interface BalmEvents {
    default <T> void onEvent(Class<T> eventClass, Consumer<T> handler) {
        onEvent(eventClass, handler, EventPriority.Normal);
    }

    <T> void onEvent(Class<T> eventClass, Consumer<T> handler, EventPriority priority);

    <T> void fireEvent(T event);

    <T> void onTickEvent(TickType type, TickPhase phase, Consumer<T> handler);
}
