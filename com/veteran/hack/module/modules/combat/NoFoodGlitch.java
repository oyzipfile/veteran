// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import java.util.function.Predicate;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.module.Module;

@Info(name = "BetterGapple", description = "Attempts to stop gapple disease and makes eating easier in low tps", category = Category.HIDDEN)
public class NoFoodGlitch extends Module
{
    @EventHandler
    private Listener<PacketEvent.Send> sendListener;
    
    public NoFoodGlitch() {
        this.sendListener = new Listener<PacketEvent.Send>(e -> {
            if (!(e.getPacket() instanceof CPacketPlayer) || NoFoodGlitch.mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE) {}
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
