package net.blay09.mods.balm.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.blay09.mods.balm.api.config.ConfigJsonExport;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.io.File;

public class BalmCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("balm")
                .then(Commands.literal("export")
                        .then(Commands.literal("config").then(Commands.argument("class", StringArgumentType.string()).executes(context -> {
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

                                            return 0;
                                        })
                        )).then(Commands.literal("icons")).then(Commands.argument("mod", StringArgumentType.string())).executes(context -> {
                            context.getSource().sendFailure(Component.literal("Not yet implemented"));
                            return 0;
                        })));
    }

}
