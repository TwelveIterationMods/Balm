package net.blay09.mods.forbic.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.function.Function;

public class ForbicEventDispatcher<TEvent, THandler extends ForbicEventHandler<TEvent>> {

    private final Event<THandler> event;

    public ForbicEventDispatcher(Class<THandler> clazz, Function<THandler[], THandler> factory) {
        event = EventFactory.createArrayBacked(clazz, factory);
    }

    public void register(THandler handler) {
        event.register(handler);
    }

    public void invoke(TEvent eventData) {
        event.invoker().handle(eventData);
    }
}
