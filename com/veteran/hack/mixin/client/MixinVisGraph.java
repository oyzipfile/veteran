// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import java.util.EnumSet;
import com.veteran.hack.module.ModuleManager;
import net.minecraft.util.EnumFacing;
import java.util.Set;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.renderer.chunk.VisGraph;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ VisGraph.class })
public class MixinVisGraph
{
    @Inject(method = { "getVisibleFacings" }, at = { @At("HEAD") }, cancellable = true)
    public void getVisibleFacings(final CallbackInfoReturnable<Set<EnumFacing>> callbackInfo) {
        if (ModuleManager.isModuleEnabled("Freecam")) {
            callbackInfo.setReturnValue(EnumSet.allOf(EnumFacing.class));
        }
    }
}
