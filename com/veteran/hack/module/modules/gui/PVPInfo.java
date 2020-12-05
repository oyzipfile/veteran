// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.gui;

import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import java.util.Iterator;
import java.util.List;
import com.veteran.hack.gui.rgui.component.container.use.Frame;
import java.awt.Color;
import com.veteran.hack.util.ColourConverter;
import com.veteran.hack.module.ModuleManager;
import net.minecraft.init.Items;
import com.veteran.hack.util.InfoCalculator;
import java.util.ArrayList;
import com.veteran.hack.util.GuiFrameUtil;
import java.awt.Font;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.gui.font.CFontRenderer;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "PVP Info", description = "PVP INFOOOOOOO", category = Category.GUI)
public class PVPInfo extends Module
{
    private Setting<Boolean> gay;
    private Setting<Boolean> ca;
    private Setting<Boolean> at;
    private Setting<Boolean> hf;
    private Setting<Boolean> su;
    private Setting<Boolean> cr;
    private Setting<Boolean> to;
    private Setting<Boolean> xp;
    private Setting<Boolean> ga;
    private Setting<Boolean> ping;
    private Setting<Boolean> motion;
    CFontRenderer ff;
    
    public PVPInfo() {
        this.gay = this.register(Settings.b("Rainbow", true));
        this.ca = this.register(Settings.booleanBuilder("CA Status").withValue(false).build());
        this.at = this.register(Settings.booleanBuilder("AT Status").withValue(false).build());
        this.hf = this.register(Settings.booleanBuilder("HF Status").withValue(false).build());
        this.su = this.register(Settings.booleanBuilder("SU Status").withValue(false).build());
        this.cr = this.register(Settings.booleanBuilder("Crystals").withValue(false).build());
        this.to = this.register(Settings.booleanBuilder("Totems").withValue(false).build());
        this.xp = this.register(Settings.booleanBuilder("XP").withValue(false).build());
        this.ga = this.register(Settings.booleanBuilder("Gapples").withValue(false).build());
        this.ping = this.register(Settings.b("Ping", false));
        this.motion = this.register(Settings.b("XYZ Motion", false));
        this.ff = new CFontRenderer(new Font("Arial", 0, 18), true, false);
    }
    
    @Override
    public void onRender() {
        final Frame f = GuiFrameUtil.getFrameByName("PVP Info");
        final List<String> drawn = new ArrayList<String>();
        if (this.ca.getValue()) {
            drawn.add("CA: " + (this.isEnabled("Autocrystal") ? "TRUE" : "FALSE"));
        }
        if (this.su.getValue()) {
            drawn.add("SU: " + (this.isEnabled("Surround") ? "TRUE" : "FALSE"));
        }
        if (this.at.getValue()) {
            drawn.add("AT: " + (this.isEnabled("AutoTrap") ? "TRUE" : "FALSE"));
        }
        if (this.hf.getValue()) {
            drawn.add("HF: " + (this.isEnabled("HoleFiller") ? "TRUE" : "FALSE"));
        }
        if (this.ping.getValue()) {
            drawn.add("PING: " + String.valueOf(InfoCalculator.ping()) + "ms");
        }
        if (this.cr.getValue()) {
            drawn.add("CRY: " + String.valueOf(getItems(Items.END_CRYSTAL)));
        }
        if (this.to.getValue()) {
            drawn.add("TOT: " + String.valueOf(getItems(Items.TOTEM_OF_UNDYING)));
        }
        if (this.xp.getValue()) {
            drawn.add("XP: " + String.valueOf(getItems(Items.EXPERIENCE_BOTTLE)));
        }
        if (this.ga.getValue()) {
            drawn.add("GAP: " + String.valueOf(getItems(Items.GOLDEN_APPLE)));
        }
        if (this.motion.getValue()) {
            drawn.add("M-X: " + String.valueOf(PVPInfo.mc.player.motionX));
            drawn.add("M-Y: " + String.valueOf(PVPInfo.mc.player.motionY));
            drawn.add("M-Z: " + String.valueOf(PVPInfo.mc.player.motionZ));
        }
        final ActiveModules activeMods = (ActiveModules)ModuleManager.getModuleByName("ActiveModules");
        final float[] hue = { System.currentTimeMillis() % (360 * activeMods.getRainbowSpeed()) / (360.0f * activeMods.getRainbowSpeed()) };
        final int rgb = Color.HSBtoRGB(hue[0], ColourConverter.toF(activeMods.saturationR.getValue()), ColourConverter.toF(activeMods.brightnessR.getValue()));
        if (f.isPinned()) {
            int i = -1;
            for (final String s : drawn) {
                ++i;
                this.ff.drawStringWithShadow(s, f.getX() + 2, f.getY() + i * (this.ff.getHeight() + 1.0), ((boolean)this.gay.getValue()) ? rgb : 16777215);
            }
        }
    }
    
    boolean isEnabled(final String name) {
        return ModuleManager.getModuleByName(name).isEnabled();
    }
    
    public static int getItems(final Item i) {
        return PVPInfo.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::func_190916_E).sum() + PVPInfo.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::func_190916_E).sum();
    }
}
