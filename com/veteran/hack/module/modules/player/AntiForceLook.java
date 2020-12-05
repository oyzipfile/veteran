// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.module.Module;

@Info(name = "AntiForceLook", category = Category.HIDDEN, description = "Stops server packets from turning your head")
public class AntiForceLook extends Module
{
    @EventHandler
    Listener<PacketEvent.Receive> receiveListener;
    
    public AntiForceLook() {
        SPacketPlayerPosLook packet;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (AntiForceLook.mc.player != null) {
                if (event.getPacket() instanceof SPacketPlayerPosLook) {
                    packet = (SPacketPlayerPosLook)event.getPacket();
                    packet.yaw = AntiForceLook.mc.player.rotationYaw;
                    packet.pitch = AntiForceLook.mc.player.rotationPitch;
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
}
