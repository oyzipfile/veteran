// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.veteran.hack.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Frustum.class })
public abstract class MixinFrustum
{
    @Inject(method = { "Lnet/minecraft/client/renderer/culling/Frustum;isBoundingBoxInFrustum(Lnet/minecraft/util/math/AxisAlignedBB;)Z" }, at = { @At("HEAD") }, cancellable = true)
    public void isBoundingBoxEtc(final AxisAlignedBB ignore, final CallbackInfoReturnable<Boolean> info) {
        if (ModuleManager.isModuleEnabled("Freecam")) {
            info.setReturnValue(true);
        }
    }
}
