package net.blay09.mods.balm.event.core;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.function.Function;

@Deprecated
public class BalmEventDispatcher<TEvent, THandler extends BalmEventHandler<TEvent>> {

    private final Event<THandler> event;

    public BalmEventDispatcher(Class<THandler> clazz, Function<THandler[], THandler> factory) {
        event = EventFactory.createArrayBacked(clazz, factory);
    }

    public void register(THandler handler) {
        event.register(handler);
    }

    public void invoke(TEvent eventData) {
        event.invoker().handle(eventData);
    }
}
