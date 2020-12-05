// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.Gui;
import com.veteran.hack.module.modules.gui.CleanGUI;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectBackgroundClean(final int left, final int top, final int right, final int bottom, final int color) {
        if (!CleanGUI.enabled() || (CleanGUI.enabled() && !CleanGUI.chatGlobal.getValue())) {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadowClean(final FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        if (!CleanGUI.enabled() || (CleanGUI.enabled() && !CleanGUI.chatGlobal.getValue())) {
            return fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        return fontRenderer.drawString(text, (int)x, (int)y, color);
    }
}
