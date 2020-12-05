// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.hidden;

import com.veteran.hack.command.Command;
import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketSoundEffect;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import java.util.Random;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "AutoFish", category = Category.HIDDEN, description = "Automatically catch fish")
public class AutoFish extends Module
{
    private Setting<Boolean> defaultSetting;
    private Setting<Integer> baseDelay;
    private Setting<Integer> extraDelay;
    private Setting<Integer> variation;
    Random random;
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener;
    
    public AutoFish() {
        this.defaultSetting = this.register(Settings.b("Defaults", false));
        this.baseDelay = this.register((Setting<Integer>)Settings.integerBuilder("Throw Delay").withValue(450).withMinimum(50).withMaximum(1000).build());
        this.extraDelay = this.register((Setting<Integer>)Settings.integerBuilder("Catch Delay").withValue(300).withMinimum(0).withMaximum(1000).build());
        this.variation = this.register((Setting<Integer>)Settings.integerBuilder("Variation").withValue(50).withMinimum(0).withMaximum(1000).build());
        SPacketSoundEffect pck;
        int soundX;
        int soundZ;
        int fishX;
        int fishZ;
        this.receiveListener = new Listener<PacketEvent.Receive>(e -> {
            if (e.getPacket() instanceof SPacketSoundEffect) {
                pck = (SPacketSoundEffect)e.getPacket();
                if (pck.getSound().getSoundName().toString().toLowerCase().contains("entity.bobber.splash")) {
                    if (AutoFish.mc.player.fishEntity != null) {
                        soundX = (int)pck.getX();
                        soundZ = (int)pck.getZ();
                        fishX = (int)AutoFish.mc.player.fishEntity.posX;
                        fishZ = (int)AutoFish.mc.player.fishEntity.posZ;
                        if (this.kindaEquals(soundX, fishX) && this.kindaEquals(fishZ, soundZ)) {
                            new Thread(() -> {
                                this.random = new Random();
                                try {
                                    Thread.sleep(this.extraDelay.getValue() + this.random.ints(1L, -this.variation.getValue(), this.variation.getValue()).findFirst().getAsInt());
                                }
                                catch (InterruptedException ex) {}
                                AutoFish.mc.rightClickMouse();
                                this.random = new Random();
                                try {
                                    Thread.sleep(this.baseDelay.getValue() + this.random.ints(1L, -this.variation.getValue(), this.variation.getValue()).findFirst().getAsInt());
                                }
                                catch (InterruptedException e2) {
                                    e2.printStackTrace();
                                }
                                AutoFish.mc.rightClickMouse();
                            }).start();
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.defaultSetting.getValue()) {
            this.baseDelay.setValue(450);
            this.extraDelay.setValue(300);
            this.variation.setValue(50);
            this.defaultSetting.setValue(false);
            Command.sendChatMessage(this.getChatName() + " Set to defaults!");
            Command.sendChatMessage(this.getChatName() + " Close and reopen the " + this.getName() + " settings menu to see changes");
        }
    }
    
    public boolean kindaEquals(final int kara, final int ni) {
        return ni == kara || ni == kara - 1 || ni == kara + 1;
    }
}
