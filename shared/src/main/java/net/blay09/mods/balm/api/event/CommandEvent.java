package net.blay09.mods.balm.api.event;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;

public class CommandEvent extends BalmEvent {
    private final ParseResults<CommandSourceStack> parseResults;

    public CommandEvent(ParseResults<CommandSourceStack> parseResults) {
        this.parseResults = parseResults;
    }

    public ParseResults<CommandSourceStack> getParseResults() {
        return parseResults;
    }
}
