// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.gui;

import com.veteran.hack.command.Command;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "PrefixChat", category = Category.GUI, description = "Opens chat with prefix inside when prefix is pressed.", showOnArray = ShowOnArray.OFF)
public class PrefixChat extends Module
{
    public Setting<Boolean> startupGlobal;
    
    public PrefixChat() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
    }
    
    public void onDisable() {
        Command.sendAutoDisableMessage(this.getName(), this.startupGlobal.getValue());
    }
}
