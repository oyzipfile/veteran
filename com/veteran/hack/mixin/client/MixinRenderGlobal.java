// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import net.minecraft.client.renderer.ChunkRenderContainer;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderGlobal.class })
public class MixinRenderGlobal
{
    @Shadow
    Minecraft mc;
    @Shadow
    public ChunkRenderContainer renderContainer;
}
