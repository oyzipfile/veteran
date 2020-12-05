// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.render;

import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "ExtraTab", description = "Expands the player tab menu", category = Category.RENDER)
public class ExtraTab extends Module
{
    public Setting<Integer> tabSize;
    public static ExtraTab INSTANCE;
    
    public ExtraTab() {
        this.tabSize = this.register((Setting<Integer>)Settings.integerBuilder("Players").withMinimum(1).withValue(80).build());
        ExtraTab.INSTANCE = this;
    }
}
