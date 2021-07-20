package net.blay09.mods.balm.event.core;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.function.Consumer;

public class BalmEventType<TEvent extends BalmEvent> {

    private final Event<Consumer<TEvent>> event;

    public BalmEventType() {
        event = EventFactory.createArrayBacked(Consumer.class, (listeners) -> (event) -> {
            for (Consumer<TEvent> listener : listeners) {
                listener.accept(event);
            }
        });
    }

    public void register(Consumer<TEvent> handler) {
        event.register(handler);
    }

    public boolean invoke(TEvent eventData) {
        event.invoker().accept(eventData);
        return !eventData.isCanceled();
    }
}
