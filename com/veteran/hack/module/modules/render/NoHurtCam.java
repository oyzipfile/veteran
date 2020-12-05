// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.render;

import com.veteran.hack.module.Module;

@Info(name = "NoHurtCam", category = Category.RENDER, description = "Disables the 'hurt' camera effect")
public class NoHurtCam extends Module
{
    private static NoHurtCam INSTANCE;
    
    public NoHurtCam() {
        NoHurtCam.INSTANCE = this;
    }
    
    public static boolean shouldDisable() {
        return NoHurtCam.INSTANCE != null && NoHurtCam.INSTANCE.isEnabled();
    }
}
