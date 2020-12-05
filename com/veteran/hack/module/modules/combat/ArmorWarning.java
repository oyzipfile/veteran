// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import com.veteran.hack.util.Wrapper;
import net.minecraft.item.ItemStack;
import java.awt.Font;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.gui.font.CFontRenderer;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "Durability Warning", description = "displays a warning when your armor is below a threshold", category = Category.COMBAT)
public class ArmorWarning extends Module
{
    private Setting<Integer> threshold;
    CFontRenderer ff;
    
    public ArmorWarning() {
        this.threshold = this.register((Setting<Integer>)Settings.integerBuilder("Warning Threshold").withMinimum(5).withValue(25).withMaximum(100).build());
        this.ff = new CFontRenderer(new Font("Arial", 0, 18), true, false);
    }
    
    @Override
    public void onRender() {
        if (this.shouldMend(0) || this.shouldMend(1) || this.shouldMend(2) || this.shouldMend(3)) {
            final String text = "Armor below " + String.valueOf(this.threshold.getValue() + "% !");
            final int divider = getScale();
            this.ff.drawStringWithShadow(text, ArmorWarning.mc.displayWidth / (float)divider / 2.0f - this.ff.getStringWidth(text) / 2, ArmorWarning.mc.displayHeight / (float)divider / 2.0f - 16.0f, 15748422);
        }
    }
    
    private boolean shouldMend(final int i) {
        return ((ItemStack)ArmorWarning.mc.player.inventory.armorInventory.get(i)).getMaxDamage() != 0 && 100 * ((ItemStack)ArmorWarning.mc.player.inventory.armorInventory.get(i)).getItemDamage() / ((ItemStack)ArmorWarning.mc.player.inventory.armorInventory.get(i)).getMaxDamage() > reverseNumber(this.threshold.getValue(), 1, 100);
    }
    
    public static int reverseNumber(final int num, final int min, final int max) {
        return max + min - num;
    }
    
    public static int getScale() {
        int scaleFactor = 0;
        int scale = Wrapper.getMinecraft().gameSettings.guiScale;
        if (scale == 0) {
            scale = 1000;
        }
        while (scaleFactor < scale && Wrapper.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Wrapper.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        if (scaleFactor == 0) {
            scaleFactor = 1;
        }
        return scaleFactor;
    }
}
