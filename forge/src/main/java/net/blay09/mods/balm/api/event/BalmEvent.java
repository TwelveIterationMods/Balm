package net.blay09.mods.balm.api.event;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

// TODO Cleanup: Not sure how to move this to shared without Fabric needing Forge's Event class.
@Cancelable
public class BalmEvent extends Event implements BalmEventContract {
}
