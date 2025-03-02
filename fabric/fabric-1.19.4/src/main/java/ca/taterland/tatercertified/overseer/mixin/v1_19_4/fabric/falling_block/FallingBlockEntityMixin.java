/**
 * Copyright (c) 2025 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.mixin.v1_19_4.fabric.falling_block;

import dev.neuralnexus.taterapi.meta.Mappings;
import dev.neuralnexus.taterapi.meta.enums.MinecraftVersion;
import dev.neuralnexus.taterapi.muxins.annotations.ReqMCVersion;
import dev.neuralnexus.taterapi.muxins.annotations.ReqMappings;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Based in the following PaperMC Patch:
 * https://github.com/PaperMC/Paper/blob/ver/1.19.4/patches/server/0418-Fix-sand-duping.patch
 */
@ReqMappings(Mappings.YARN_INTERMEDIARY)
@ReqMCVersion(min = MinecraftVersion.V14_4, max = MinecraftVersion.V21_1)
// TODO Actually test to see if this works in 1.14.4; It probably does
@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {
    public FallingBlockEntityMixin(EntityType<?> arg, Level arg2) {
        super(arg, arg2);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void overseer$injectAtHeadToRemoveDuping(CallbackInfo ci) {
        if (this.isRemoved()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "tick",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/entity/item/FallingBlockEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
                            shift = At.Shift.AFTER),
            cancellable = true)
    private void overseer$injectAtMoveToRemoveDuping(CallbackInfo ci) {
        if (this.isRemoved()) {
            ci.cancel();
        }
    }
}
