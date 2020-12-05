// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.gui;

import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import java.util.Date;
import java.awt.Color;
import com.veteran.hack.util.ColourConverter;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.command.Command;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;
import com.veteran.hack.util.InfoCalculator;
import java.util.ArrayList;
import net.minecraft.util.text.TextFormatting;
import java.awt.Font;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.gui.font.CFontRenderer;
import com.veteran.hack.util.ColourTextFormatting;
import com.veteran.hack.util.TimeUtil;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "InfoOverlay", category = Category.GUI, description = "Configures the game information overlay", showOnArray = ShowOnArray.OFF)
public class InfoOverlay extends Module
{
    private Setting<Page> page;
    private Setting<Boolean> version;
    private Setting<Boolean> username;
    private Setting<Boolean> tps;
    private Setting<Boolean> fps;
    private Setting<Boolean> durability;
    private Setting<Boolean> memory;
    private Setting<Boolean> inHole;
    private Setting<Boolean> speed;
    private Setting<SpeedUnit> speedUnit;
    private Setting<Boolean> time;
    private Setting<TimeUtil.TimeType> timeTypeSetting;
    private Setting<TimeUtil.TimeUnit> timeUnitSetting;
    private Setting<Boolean> doLocale;
    public Setting<ColourTextFormatting.ColourCode> firstColour;
    public Setting<ColourTextFormatting.ColourCode> secondColour;
    int rgb;
    CFontRenderer ff1;
    CFontRenderer ff2;
    CFontRenderer ff3;
    
    public InfoOverlay() {
        this.page = this.register((Setting<Page>)Settings.enumBuilder(Page.class).withName("Page").withValue(Page.ONE).build());
        this.version = this.register(Settings.booleanBuilder("Version").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.username = this.register(Settings.booleanBuilder("Username").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.tps = this.register(Settings.booleanBuilder("TPS").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.fps = this.register(Settings.booleanBuilder("FPS").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.durability = this.register(Settings.booleanBuilder("Item Damage").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.memory = this.register(Settings.booleanBuilder("RAM Used").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.inHole = this.register(Settings.booleanBuilder("HOLE Status").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.TWO)).build());
        this.speed = this.register(Settings.booleanBuilder("Speed").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
        this.speedUnit = this.register((Setting<SpeedUnit>)Settings.enumBuilder(SpeedUnit.class).withName("Speed Unit").withValue(SpeedUnit.KMH).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.speed.getValue()).build());
        this.time = this.register(Settings.booleanBuilder("Time").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
        this.timeTypeSetting = this.register((Setting<TimeUtil.TimeType>)Settings.enumBuilder(TimeUtil.TimeType.class).withName("Time Format").withValue(TimeUtil.TimeType.HHMMSS).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.time.getValue()).build());
        this.timeUnitSetting = this.register((Setting<TimeUtil.TimeUnit>)Settings.enumBuilder(TimeUtil.TimeUnit.class).withName("Time Unit").withValue(TimeUtil.TimeUnit.H12).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.time.getValue()).build());
        this.doLocale = this.register(Settings.booleanBuilder("Time Show AMPM").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.time.getValue()).build());
        this.firstColour = this.register((Setting<ColourTextFormatting.ColourCode>)Settings.enumBuilder(ColourTextFormatting.ColourCode.class).withName("First Colour").withValue(ColourTextFormatting.ColourCode.WHITE).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
        this.secondColour = this.register((Setting<ColourTextFormatting.ColourCode>)Settings.enumBuilder(ColourTextFormatting.ColourCode.class).withName("Second Colour").withValue(ColourTextFormatting.ColourCode.BLUE).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
        this.ff1 = new CFontRenderer(new Font("Arial", 0, 24), true, false);
        this.ff2 = new CFontRenderer(new Font("Arial", 0, 14), true, false);
        this.ff3 = new CFontRenderer(new Font("Arial", 0, 18), true, false);
    }
    
    public static String getStringColour(final TextFormatting c) {
        return c.toString();
    }
    
    private TextFormatting setToText(final ColourTextFormatting.ColourCode colourCode) {
        return ColourTextFormatting.toTextMap.get(colourCode);
    }
    
    public ArrayList<String> infoContents() {
        final ArrayList<String> infoContents = new ArrayList<String>();
        if (this.time.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + TimeUtil.getFinalTime(this.setToText(this.secondColour.getValue()), this.setToText(this.firstColour.getValue()), this.timeUnitSetting.getValue(), this.timeTypeSetting.getValue(), this.doLocale.getValue()));
        }
        if (this.tps.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.tps() + getStringColour(this.setToText(this.secondColour.getValue())) + " tps");
        }
        if (this.fps.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + Minecraft.debugFPS + getStringColour(this.setToText(this.secondColour.getValue())) + " fps");
        }
        if (this.speed.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.speed(this.useUnitKmH()) + getStringColour(this.setToText(this.secondColour.getValue())) + " " + this.unitType(this.speedUnit.getValue()));
        }
        if (this.durability.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.dura() + getStringColour(this.setToText(this.secondColour.getValue())) + " dura");
        }
        if (this.inHole.getValue() && InfoOverlay.mc.world != null) {
            final Vec3d[] holeOffset = { InfoOverlay.mc.player.getPositionVector().add(1.0, 0.0, 0.0), InfoOverlay.mc.player.getPositionVector().add(-1.0, 0.0, 0.0), InfoOverlay.mc.player.getPositionVector().add(0.0, 0.0, 1.0), InfoOverlay.mc.player.getPositionVector().add(0.0, 0.0, -1.0), InfoOverlay.mc.player.getPositionVector().add(0.0, -1.0, 0.0) };
            int holeBlocks = 0;
            int safeHoleBlocks = 0;
            for (final Vec3d o : holeOffset) {
                if (InfoOverlay.mc.world.getBlockState(new BlockPos(o.x, o.y, o.z)).getBlock() == Blocks.BEDROCK) {
                    ++safeHoleBlocks;
                }
                if (InfoOverlay.mc.world.getBlockState(new BlockPos(o.x, o.y, o.z)).getBlock() == Blocks.OBSIDIAN) {
                    ++holeBlocks;
                }
            }
            if (holeBlocks + safeHoleBlocks != 5) {
                infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + "HOLE: " + getStringColour(this.setToText(this.secondColour.getValue())) + "UNSAFE");
            }
            if (holeBlocks + safeHoleBlocks == 5 && safeHoleBlocks != 5) {
                infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + "HOLE: " + getStringColour(this.setToText(this.secondColour.getValue())) + "OBBY");
            }
            if (safeHoleBlocks == 5) {
                infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + "HOLE: " + getStringColour(this.setToText(this.secondColour.getValue())) + "BEDROCK");
            }
        }
        return infoContents;
    }
    
    public void onDisable() {
        Command.sendDisableMessage(this.getName());
    }
    
    @Override
    public void onRender() {
        final ActiveModules activeMods = (ActiveModules)ModuleManager.getModuleByName("ActiveModules");
        final float[] hue = { System.currentTimeMillis() % (360 * activeMods.getRainbowSpeed()) / (360.0f * activeMods.getRainbowSpeed()) };
        final int rgb = Color.HSBtoRGB(hue[0], ColourConverter.toF(activeMods.saturationR.getValue()), ColourConverter.toF(activeMods.brightnessR.getValue()));
        if (this.version.getValue()) {
            this.ff1.drawStringWithShadow("Veteran Hack", 2.0, 2.0, rgb);
            this.ff2.drawStringWithShadow("b3.8", this.ff1.getStringWidth("Veteran Hack ") + 2, this.ff1.getStringHeight("Veteran Hack") - 2, rgb);
        }
        if (this.username.getValue()) {
            final Date date = new Date(System.currentTimeMillis());
            final int hour = date.getHours();
            String greeting = new String();
            if (0 <= hour && hour < 12) {
                greeting = "this morning";
            }
            if (12 <= hour && hour < 18) {
                greeting = "this afternoon";
            }
            if (18 <= hour && hour < 20) {
                greeting = "this evening";
            }
            if (20 <= hour) {
                greeting = "tonight";
            }
            this.ff3.drawStringWithShadow("Looking cute " + greeting + ", " + InfoOverlay.mc.player.getName() + " :^)", 2.0, 16.0, rgb);
        }
    }
    
    public boolean useUnitKmH() {
        return this.speedUnit.getValue().equals(SpeedUnit.KMH);
    }
    
    public static int getItems(final Item i) {
        return InfoOverlay.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::func_190916_E).sum() + InfoOverlay.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::func_190916_E).sum();
    }
    
    private String unitType(final SpeedUnit s) {
        switch (s) {
            case MPS: {
                return "m/s";
            }
            case KMH: {
                return "km/h";
            }
            default: {
                return "Invalid unit type (mps or kmh)";
            }
        }
    }
    
    private enum SpeedUnit
    {
        MPS, 
        KMH;
    }
    
    private enum Page
    {
        ONE, 
        TWO, 
        THREE;
    }
}
