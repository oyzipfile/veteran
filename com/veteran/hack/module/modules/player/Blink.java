// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import java.util.function.Predicate;
import java.util.LinkedList;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.Queue;
import com.veteran.hack.module.Module;

@Info(name = "Blink", category = Category.COMBAT, description = "Cancels server side packets")
public class Blink extends Module
{
    Queue<CPacketPlayer> packets;
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    private EntityOtherPlayerMP clonedPlayer;
    
    public Blink() {
        this.packets = new LinkedList<CPacketPlayer>();
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (this.isEnabled() && event.getPacket() instanceof CPacketPlayer) {
                event.cancel();
                this.packets.add((CPacketPlayer)event.getPacket());
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    protected void onEnable() {
        if (Blink.mc.player != null) {
            (this.clonedPlayer = new EntityOtherPlayerMP((World)Blink.mc.world, Blink.mc.getSession().getProfile())).copyLocationAndAnglesFrom((Entity)Blink.mc.player);
            this.clonedPlayer.rotationYawHead = Blink.mc.player.rotationYawHead;
            Blink.mc.world.addEntityToWorld(-100, (Entity)this.clonedPlayer);
        }
    }
    
    @Override
    protected void onDisable() {
        while (!this.packets.isEmpty()) {
            Blink.mc.player.connection.sendPacket((Packet)this.packets.poll());
        }
        final EntityPlayer localPlayer = (EntityPlayer)Blink.mc.player;
        if (localPlayer != null) {
            Blink.mc.world.removeEntityFromWorld(-100);
            this.clonedPlayer = null;
        }
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(this.packets.size());
    }
}
