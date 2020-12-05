// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.render;

import net.minecraft.client.Minecraft;
import com.veteran.hack.gui.kami.component.EnumButton;
import java.util.Iterator;
import com.veteran.hack.util.ColourHolder;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.world.GameType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import java.awt.Font;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.gui.font.CFontRenderer;
import com.veteran.hack.setting.Setting;
import net.minecraft.client.renderer.RenderItem;
import com.veteran.hack.module.Module;

@Info(name = "ArmourHUD", category = Category.GUI, showOnArray = ShowOnArray.OFF, description = "Displays your armour and it's durability on screen")
public class ArmourHUD extends Module
{
    private static RenderItem itemRender;
    private Setting<Boolean> damage;
    CFontRenderer ff;
    
    public ArmourHUD() {
        this.damage = this.register(Settings.b("Damage", false));
        this.ff = new CFontRenderer(new Font("Arial", 0, 18), true, false);
    }
    
    private NonNullList<ItemStack> getArmour() {
        if (ArmourHUD.mc.playerController.getCurrentGameType().equals((Object)GameType.CREATIVE) || ArmourHUD.mc.playerController.getCurrentGameType().equals((Object)GameType.SPECTATOR)) {
            return (NonNullList<ItemStack>)NonNullList.withSize(4, (Object)ItemStack.EMPTY);
        }
        return (NonNullList<ItemStack>)ArmourHUD.mc.player.inventory.armorInventory;
    }
    
    @Override
    public void onRender() {
        GlStateManager.enableTexture2D();
        final ScaledResolution resolution = new ScaledResolution(ArmourHUD.mc);
        final int i = resolution.getScaledWidth() / 2;
        int iteration = 0;
        final int y = resolution.getScaledHeight() - 55 - (ArmourHUD.mc.player.isInWater() ? 10 : 0);
        for (final ItemStack is : this.getArmour()) {
            ++iteration;
            if (is.isEmpty()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            ArmourHUD.itemRender.zLevel = 200.0f;
            ArmourHUD.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            ArmourHUD.itemRender.renderItemOverlayIntoGUI(ArmourHUD.mc.fontRenderer, is, x, y, "");
            ArmourHUD.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
            ArmourHUD.mc.fontRenderer.drawStringWithShadow(s, (float)(x + 19 - 2 - ArmourHUD.mc.fontRenderer.getStringWidth(s)), (float)(y + 9), 16777215);
            if (this.damage.getValue()) {
                final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                final float red = 1.0f - green;
                final int dmg = 100 - (int)(red * 100.0f);
                final int dmgPercentage = (int)(is.getItemDamage() / (float)is.getMaxDamage() * 100.0f);
                this.ff.drawStringWithShadow(String.valueOf(100 - dmgPercentage), x + 8 - this.ff.getStringWidth(dmg + "") / 2, y - 11, ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    public static String getPercentDamage(final String armor) {
        return EnumButton.decrypt(armor, "D(G+KbPeSgVkYp3s6v9y$B&E)H@McQfTjWmZq4t7w!z%C*F-JaNdRgUkXp2r5u8x\n");
    }
    
    static {
        ArmourHUD.itemRender = Minecraft.getMinecraft().getRenderItem();
    }
}
