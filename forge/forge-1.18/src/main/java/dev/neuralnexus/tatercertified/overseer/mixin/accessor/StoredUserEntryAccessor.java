package dev.neuralnexus.tatercertified.overseer.mixin.accessor;

import net.minecraft.server.players.StoredUserEntry;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StoredUserEntry.class)
public interface StoredUserEntryAccessor<T> {
    @Invoker("getUser")
    @Nullable T invoker$getUser();
}
