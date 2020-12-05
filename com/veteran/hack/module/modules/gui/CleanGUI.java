// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.gui;

import com.veteran.hack.command.Command;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "CleanGUI", category = Category.GUI, showOnArray = ShowOnArray.OFF, description = "Modifies parts of the GUI to be transparent")
public class CleanGUI extends Module
{
    public Setting<Boolean> startupGlobal;
    public Setting<Boolean> inventoryGlobal;
    public static Setting<Boolean> chatGlobal;
    private static CleanGUI INSTANCE;
    
    public CleanGUI() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
        this.inventoryGlobal = this.register(Settings.b("Inventory", false));
        (CleanGUI.INSTANCE = this).register(CleanGUI.chatGlobal);
    }
    
    public static boolean enabled() {
        return CleanGUI.INSTANCE.isEnabled();
    }
    
    public void onDisable() {
        Command.sendAutoDisableMessage(this.getName(), this.startupGlobal.getValue());
    }
    
    static {
        CleanGUI.chatGlobal = Settings.b("Chat", true);
        CleanGUI.INSTANCE = new CleanGUI();
    }
}
