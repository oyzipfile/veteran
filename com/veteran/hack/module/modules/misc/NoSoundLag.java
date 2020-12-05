// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.misc;

import java.util.function.Predicate;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.module.Module;

@Info(name = "NoSoundLag", category = Category.MISC, description = "Prevents lag caused by sound machines")
public class NoSoundLag extends Module
{
    @EventHandler
    Listener<PacketEvent.Receive> receiveListener;
    
    public NoSoundLag() {
        SPacketSoundEffect soundPacket;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (NoSoundLag.mc.player != null) {
                if (event.getPacket() instanceof SPacketSoundEffect) {
                    soundPacket = (SPacketSoundEffect)event.getPacket();
                    if (soundPacket.getCategory() == SoundCategory.PLAYERS && soundPacket.getSound() == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC) {
                        event.cancel();
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
}
