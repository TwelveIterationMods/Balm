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
    @Deprecated(since = "1.18", forRemoval = true)
    List<GuiEventListener> getChildren();

    @Accessor("children")
    List<GuiEventListener> balm_getChildren();

    @Accessor
    @Deprecated(since = "1.18", forRemoval = true)
    List<NarratableEntry> getNarratables();

    @Accessor("narratables")
    List<NarratableEntry> balm_getNarratables();

    @Accessor
    @Deprecated(since = "1.18", forRemoval = true)
    List<Widget> getRenderables();

    @Accessor("renderables")
    List<Widget> balm_getRenderables();
}
