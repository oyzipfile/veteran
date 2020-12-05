// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "Glide", category = Category.MOVEMENT, description = "Makes you glide like a bird")
public class Glide extends Module
{
    private Setting<Boolean> elytra;
    
    public Glide() {
        this.elytra = this.register(Settings.b("Elytra", true));
    }
    
    @Override
    public void onUpdate() {
        if (Glide.mc.world == null) {
            return;
        }
        if (Glide.mc.player.isSneaking()) {
            final EntityPlayerSP player = Glide.mc.player;
            player.motionY *= 0.8;
        }
        else {
            final EntityPlayerSP player2 = Glide.mc.player;
            player2.motionY *= 0.3;
        }
    }
}
