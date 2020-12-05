// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.module.Module;

@Info(name = "AntiHunger", category = Category.HIDDEN, description = "Reduces hunger lost when moving around")
public class AntiHunger extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> packetListener;
    
    public AntiHunger() {
        this.packetListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayer) {
                ((CPacketPlayer)event.getPacket()).onGround = false;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
