// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.veteran.hack.event.events.ChunkEvent;
import com.veteran.hack.BaseMod;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetHandlerPlayClient.class })
public class MixinNetHandlerPlayClient
{
    @Inject(method = { "handleChunkData" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;read(Lnet/minecraft/network/PacketBuffer;IZ)V") }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void read(final SPacketChunkData data, final CallbackInfo info, final Chunk chunk) {
        BaseMod.EVENT_BUS.post(new ChunkEvent(chunk, data));
    }
}
