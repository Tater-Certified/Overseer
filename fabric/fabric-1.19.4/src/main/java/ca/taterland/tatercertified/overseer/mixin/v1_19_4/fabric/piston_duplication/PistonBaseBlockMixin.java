/**
 * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.mixin.v1_19_4.fabric.piston_duplication;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.neuralnexus.taterapi.meta.Mappings;
import dev.neuralnexus.taterapi.meta.enums.MinecraftVersion;
import dev.neuralnexus.taterapi.muxins.annotations.ReqMCVersion;
import dev.neuralnexus.taterapi.muxins.annotations.ReqMappings;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Map;

/**
 * Based on the following PaperMC Patch:
 * https://github.com/PaperMC/Paper/blob/ver/1.19.4/patches/server/0417-Fix-piston-physics-inconsistency-MC-188840.patch
 */
@ReqMappings(Mappings.YARN_INTERMEDIARY)
@ReqMCVersion(
        min = MinecraftVersion.V14_4,
        max = MinecraftVersion.V19_4) // TODO Actually test to see if this works in 1.14.4; Check
// if/when this got patched in modern versions
@Mixin(PistonBaseBlock.class)
public class PistonBaseBlockMixin {

    @Redirect(
            method = "moveBlocks",
            at =
                    @At(
                            value = "INVOKE",
                            target = "Ljava/util/List;get(I)Ljava/lang/Object;",
                            ordinal = 2))
    private Object overseer$redirectListGet(
            List<BlockPos> instance, int i, @Share("oldPos") LocalRef<BlockPos> posRef) {
        posRef.set(instance.get(i));
        return posRef.get();
    }

    @Redirect(
            method = "moveBlocks",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                            ordinal = 3))
    private BlockState overseer$redirectRemoveBlockState(Level instance, BlockPos param0) {
        return null;
    }

    @Redirect(
            method = "moveBlocks",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/level/Level;setBlockEntity(Lnet/minecraft/world/level/block/entity/BlockEntity;)V"))
    private void overseer$preventPistonDuplication(
            Level instance,
            BlockEntity blockEntity,
            @Local(ordinal = 0) LocalRef<BlockState> lv7,
            @Local Map<BlockPos, BlockState> map,
            @Local(ordinal = 1) BlockPos lv6,
            @Local(ordinal = 1) BlockState lv9,
            @Local(ordinal = 1) Direction arg3,
            @Local(ordinal = 0, argsOnly = true) boolean bl,
            @Share("oldPos") LocalRef<BlockPos> posRef) {
        lv7.set(instance.getBlockState(posRef.get()));
        map.replace(posRef.get(), lv7.get());
        instance.setBlockEntity(
                MovingPistonBlock.newMovingBlockEntity(lv6, lv9, lv7.get(), arg3, bl, false));
        instance.setBlock(posRef.get(), Blocks.AIR.defaultBlockState(), 2 | 4 | 16 | 1024);
    }
}
