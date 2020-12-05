// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import java.util.function.Predicate;
import com.veteran.hack.command.Command;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.event.entity.living.PotionEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "Strength Detect", description = "tells you when ppl have strength smh 2ez", category = Category.HIDDEN)
public class StrengthDetector extends Module
{
    private Setting<Boolean> potpotpot;
    int delay;
    boolean hasSentMessage;
    @EventHandler
    public Listener<PotionEvent> potionListener;
    
    public StrengthDetector() {
        this.potpotpot = this.register(Settings.b("potpotpot", false));
        this.delay = 0;
        this.hasSentMessage = false;
        this.potionListener = new Listener<PotionEvent>(event -> {
            if (event.getPotionEffect().getPotion() == MobEffects.STRENGTH && event.getEntity() instanceof EntityPlayer) {
                if (this.potpotpot.getValue()) {
                    Command.sendServerMessage("potpotpot, " + event.getEntity().getName() + " just drank a Strength Potion!");
                }
                else {
                    Command.sendChatMessage(event.getEntity().getName() + " just drank a Strength Potion!");
                }
            }
        }, (Predicate<PotionEvent>[])new Predicate[0]);
    }
    
    public void onEnable() {
        this.delay = 400;
    }
}
