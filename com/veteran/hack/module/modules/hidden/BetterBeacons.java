// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.hidden;

import java.util.function.Predicate;
import net.minecraft.network.Packet;
import com.veteran.hack.util.Wrapper;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.CPacketCustomPayload;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "BetterBeacons", category = Category.HIDDEN, description = "Choose any of the 5 beacon effects regardless of beacon base height")
public class BetterBeacons extends Module
{
    private Setting<Effects> effects;
    private boolean doCancelPacket;
    @EventHandler
    public Listener<PacketEvent.Send> packetListener;
    
    public BetterBeacons() {
        this.effects = this.register(Settings.e("Effect", Effects.SPEED));
        this.doCancelPacket = true;
        PacketBuffer data;
        int i1;
        int k1;
        PacketBuffer buf;
        this.packetListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketCustomPayload && ((CPacketCustomPayload)event.getPacket()).getChannelName().equals("MC|Beacon") && this.doCancelPacket) {
                this.doCancelPacket = false;
                data = ((CPacketCustomPayload)event.getPacket()).getBufferData();
                i1 = data.readInt();
                k1 = data.readInt();
                event.cancel();
                buf = new PacketBuffer(Unpooled.buffer());
                buf.writeInt(this.getPotionID());
                buf.writeInt(k1);
                Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketCustomPayload("MC|Beacon", buf));
                this.doCancelPacket = true;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    private int getPotionID() {
        switch (this.effects.getValue()) {
            case SPEED: {
                return 1;
            }
            case HASTE: {
                return 3;
            }
            case RESISTANCE: {
                return 11;
            }
            case JUMP_BOOST: {
                return 8;
            }
            case STRENGTH: {
                return 5;
            }
            default: {
                return -1;
            }
        }
    }
    
    private enum Effects
    {
        SPEED, 
        HASTE, 
        RESISTANCE, 
        JUMP_BOOST, 
        STRENGTH;
    }
}
