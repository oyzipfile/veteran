// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import com.veteran.hack.module.Module;

@Info(name = "NoEntityTrace", category = Category.EXPLOIT, description = "Blocks entities from stopping you from mining")
public class NoEntityTrace extends Module
{
    public static boolean shouldBlock() {
        return false;
    }
}
