// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import com.veteran.hack.module.modules.render.Chams;
import com.veteran.hack.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.entity.RenderLiving;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderLiving.class })
public class MixinRenderLiving
{
    @Inject(method = { "doRender" }, at = { @At("HEAD") })
    private void injectChamsPre(final EntityLiving entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (ModuleManager.isModuleEnabled("Chams") && Chams.renderChams((Entity)entity)) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1000000.0f);
        }
    }
    
    @Inject(method = { "doRender" }, at = { @At("RETURN") })
    private <S extends EntityLivingBase> void injectChamsPost(final EntityLiving entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (ModuleManager.isModuleEnabled("Chams") && Chams.renderChams((Entity)entity)) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(32823);
        }
    }
}
