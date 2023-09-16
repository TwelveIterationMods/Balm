package net.blay09.mods.balm.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.BalmClient;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(KeyMapping.class)
public class FabricKeyMappingMixin {

    @Final
    @Shadow
    private static Map<InputConstants.Key, KeyMapping> MAP;

    @ModifyArg(method = "<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 1), index = 1)
    private Object ctor(Object key, Object value) {
        if (value instanceof KeyMapping keyMapping) {
            if (key instanceof InputConstants.Key && BalmClient.getKeyMappings().shouldIgnoreConflicts(keyMapping)) {
                return MAP.get(key);
            }
        }
        return value;
    }

    @ModifyArg(method = "resetMapping()V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), index = 1)
    private static Object resetMappings(Object key, Object value) {
        if (value instanceof KeyMapping keyMapping) {
            if (key instanceof InputConstants.Key && BalmClient.getKeyMappings().shouldIgnoreConflicts(keyMapping)) {
                return MAP.get(key);
            }
        }
        return value;
    }
}
