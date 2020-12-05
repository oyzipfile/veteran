// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.misc;

import com.veteran.hack.module.Module;

@Module.Info(name = "NoPacketKick", category = Module.Category.MISC, description = "Prevent large packets from kicking you")
public class NoPacketKick
{
    private static NoPacketKick INSTANCE;
    
    public NoPacketKick() {
        NoPacketKick.INSTANCE = this;
    }
    
    public static boolean isEnabled() {
        final NoPacketKick instance = NoPacketKick.INSTANCE;
        return isEnabled();
    }
}
