// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.util;

import org.lwjgl.input.Keyboard;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import com.veteran.hack.gui.kami.KamiGUI;
import com.veteran.hack.gui.rgui.render.font.FontRenderer;

public class Wrapper
{
    private static FontRenderer fontRenderer;
    
    public static void init() {
        Wrapper.fontRenderer = KamiGUI.fontRenderer;
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    
    public static World getWorld() {
        return (World)getMinecraft().world;
    }
    
    public static int getKey(final String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }
    
    public static FontRenderer getFontRenderer() {
        return Wrapper.fontRenderer;
    }
}
