// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import com.veteran.hack.module.modules.misc.NoPacketKick;
import java.io.IOException;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.veteran.hack.BaseMod;
import com.veteran.hack.event.events.PacketEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetworkManager.class })
public class MixinNetworkManager
{
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onSendPacket(final Packet<?> packet, final CallbackInfo callbackInfo) {
        final PacketEvent event = new PacketEvent.Send(packet);
        BaseMod.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    private void onChannelRead(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callbackInfo) {
        final PacketEvent event = new PacketEvent.Receive(packet);
        BaseMod.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "exceptionCaught" }, at = { @At("HEAD") }, cancellable = true)
    private void exceptionCaught(final ChannelHandlerContext p_exceptionCaught_1_, final Throwable p_exceptionCaught_2_, final CallbackInfo info) {
        if (p_exceptionCaught_2_ instanceof IOException && NoPacketKick.isEnabled()) {
            info.cancel();
        }
    }
}
