// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.hidden;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketCloseWindow;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.module.Module;

@Info(name = "XCarry", category = Category.HIDDEN, description = "Store items in crafting slots", showOnArray = ShowOnArray.OFF)
public class XCarry extends Module
{
    @EventHandler
    private Listener<PacketEvent.Send> l;
    
    public XCarry() {
        this.l = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketCloseWindow) {
                event.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
