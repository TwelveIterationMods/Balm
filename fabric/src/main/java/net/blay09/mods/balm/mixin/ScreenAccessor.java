package net.blay09.mods.balm.mixin;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenAccessor {

    @Accessor
    List<GuiEventListener> getChildren();

    @Accessor
    List<NarratableEntry> getNarratables();

    @Accessor
    List<Widget> getRenderables();

}
