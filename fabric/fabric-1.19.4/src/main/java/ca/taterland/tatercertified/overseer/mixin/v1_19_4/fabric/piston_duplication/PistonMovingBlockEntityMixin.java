/**
 * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.mixin.v1_19_4.fabric.piston_duplication;

import dev.neuralnexus.taterapi.meta.Mappings;
import dev.neuralnexus.taterapi.meta.enums.MinecraftVersion;
import dev.neuralnexus.taterapi.muxins.annotations.ReqMCVersion;
import dev.neuralnexus.taterapi.muxins.annotations.ReqMappings;

import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Based on the following PaperMC Patch:
 * https://github.com/PaperMC/Paper/blob/ver/1.19.4/patches/server/0417-Fix-piston-physics-inconsistency-MC-188840.patch
 */
@ReqMappings(Mappings.YARN_INTERMEDIARY)
@ReqMCVersion(
        min = MinecraftVersion.V14_4,
        max = MinecraftVersion.V19_4) // TODO Actually test to see if this works in 1.14.4; Check
// if/when this got patched in modern versions
@Mixin(PistonMovingBlockEntity.class)
public class PistonMovingBlockEntityMixin {
    @ModifyArg(
            method = "tick",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
                            ordinal = 0),
            index = 2)
    private static int overseer$modifySetBlock(int x) {
        return 84 | 2;
    }
}
