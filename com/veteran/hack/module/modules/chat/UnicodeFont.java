// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.chat;

import net.minecraft.client.gui.FontRenderer;
import com.veteran.hack.module.Module;

@Info(name = "Unicode Font", category = Category.CHAT, description = "Forces the game to use the unicode font", showOnArray = ShowOnArray.OFF)
public class UnicodeFont extends Module
{
    public void onEnable() {
        final FontRenderer fontRenderer = UnicodeFont.mc.fontRenderer;
        final boolean unicodeFlag = true;
        fontRenderer.unicodeFlag = unicodeFlag;
        if (unicodeFlag) {
            return;
        }
        UnicodeFont.mc.fontRenderer.unicodeFlag = true;
        UnicodeFont.mc.gameSettings.forceUnicodeFont = true;
    }
    
    public void onDisable() {
        final FontRenderer fontRenderer = UnicodeFont.mc.fontRenderer;
        final boolean unicodeFlag = false;
        fontRenderer.unicodeFlag = unicodeFlag;
        if (unicodeFlag) {
            return;
        }
        UnicodeFont.mc.fontRenderer.unicodeFlag = false;
        UnicodeFont.mc.gameSettings.forceUnicodeFont = false;
    }
}
