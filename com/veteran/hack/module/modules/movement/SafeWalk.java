// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import com.veteran.hack.module.Module;

@Info(name = "SafeWalk", category = Category.MOVEMENT, description = "Keeps you from walking off edges")
public class SafeWalk extends Module
{
    private static SafeWalk INSTANCE;
    
    public SafeWalk() {
        SafeWalk.INSTANCE = this;
    }
    
    public static boolean shouldSafewalk() {
        return SafeWalk.INSTANCE.isEnabled();
    }
}
