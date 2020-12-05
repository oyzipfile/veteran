// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import com.veteran.hack.module.Module;

@Info(name = "TpsSync", description = "Synchronizes some actions with the server TPS", category = Category.PLAYER)
public class TpsSync extends Module
{
    private static TpsSync INSTANCE;
    
    public TpsSync() {
        TpsSync.INSTANCE = this;
    }
    
    public static boolean isSync() {
        return TpsSync.INSTANCE.isEnabled();
    }
}
