// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.hidden;

import com.veteran.hack.command.Command;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "AutoEncourage", category = Category.HIDDEN, description = "Automatically Encourage player client side")
public class AutoEncourager extends Module
{
    private Setting<Double> delay;
    double delayTimer;
    
    public AutoEncourager() {
        this.delay = this.register(Settings.d("Delay", 240.0));
        this.delayTimer = (int)(this.delay.getValue() * 20.0 * 2.0);
    }
    
    @Override
    public void onUpdate() {
        --this.delayTimer;
        if (this.delayTimer == this.delay.getValue() / 2.0) {
            Command.sendChatMessage(AutoEncourager.mc.player.getName() + " remember to gap! Keep your head up king, go crystal those ping players");
        }
        if (this.delayTimer == 0.0) {
            Command.sendChatMessage(AutoEncourager.mc.player.getName() + " bro, you're literally the best at pvp.");
            this.delayTimer = this.delay.getValue() * 20.0 * 2.0;
        }
    }
}
