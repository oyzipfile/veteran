// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import com.veteran.hack.module.Module;

@Info(name = "Fastbreak", category = Category.HIDDEN, description = "Nullifies block hit delay")
public class Fastbreak extends Module
{
    @Override
    public void onUpdate() {
        Fastbreak.mc.playerController.blockHitDelay = 0;
    }
}
