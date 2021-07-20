package net.blay09.mods.balm.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public interface UseBlockHandler {

    InteractionResult handle(Player player, Level level, InteractionHand hand, BlockHitResult hitResult);
}
