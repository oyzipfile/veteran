// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import com.veteran.hack.module.Module;

@Info(name = "AutoJump", category = Category.HIDDEN, description = "Automatically jumps if possible")
public class AutoJump extends Module
{
    @Override
    public void onUpdate() {
        if (AutoJump.mc.player.isInWater() || AutoJump.mc.player.isInLava()) {
            AutoJump.mc.player.motionY = 0.1;
        }
        else if (AutoJump.mc.player.onGround) {
            AutoJump.mc.player.jump();
        }
    }
}
