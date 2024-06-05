package net.blay09.mods.balm.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.common.client.IconExport;
import net.blay09.mods.balm.common.config.ConfigJsonExport;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.io.File;

public class BalmCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("balm")
                .then(Commands.literal("export")
                        .then(Commands.literal("config").then(Commands.argument("class", StringArgumentType.greedyString()).executes(context -> {
                                    final var className = context.getArgument("class", String.class);
                                    try {
                                        final var configDataClass = Class.forName(className);
                                        ConfigJsonExport.exportToFile(configDataClass, new File("exports/config/" + configDataClass.getSimpleName() + ".json"));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                        throw new RuntimeException("Invalid config data class: " + className, e);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        throw new RuntimeException("Error exporting config data class: " + className, e);
                                    }

                                    context.getSource().sendSuccess(() -> Component.literal("Exported config for " + className), false);
                                    return 0;
                                })
                        )).then(Commands.literal("icons").then(Commands.argument("filter", StringArgumentType.greedyString()).executes(context -> {
                            final var filter = context.getArgument("filter", String.class);
                            if (Balm.getProxy().isClient()) {
                                try {
                                    IconExport.export(filter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    throw new RuntimeException("Error exporting icons for " + filter, e);
                                }
                                context.getSource().sendSuccess(() -> Component.literal("Exported icons for " + filter), false);
                                return 1;
                            }
                            return 0;
                        })))));
    }

}
