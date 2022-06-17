package net.blay09.mods.balm.fabric.client.rendering;

import com.mojang.math.Transformation;
import net.minecraft.client.resources.model.ModelState;

public class TransformationModelState implements ModelState {
    private final Transformation transformation;

    public TransformationModelState(Transformation transformation) {
        this.transformation = transformation;
    }

    @Override
    public Transformation getRotation() {
        return transformation;
    }
}
