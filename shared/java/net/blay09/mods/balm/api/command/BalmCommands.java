package net.blay09.mods.balm.api.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Consumer;

public interface BalmCommands {
    void register(Consumer<CommandDispatcher<CommandSourceStack>> initializer);
}
