// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.render;

import net.minecraft.util.math.RayTraceResult;
import java.awt.Color;
import com.veteran.hack.util.ColourConverter;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.modules.gui.ActiveModules;
import net.minecraft.util.EnumFacing;
import com.veteran.hack.util.VetHackTessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import com.veteran.hack.event.events.RenderEvent;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "BlockHighlight", description = "renders better blovk highlight for some ppl ", category = Category.RENDER)
public class BlockHighlight extends Module
{
    private Setting<RenderMode> renderModeSetting;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> a;
    private Setting<Integer> fRed;
    private Setting<Integer> fGreen;
    private Setting<Integer> fBlue;
    private Setting<Integer> fA;
    private Setting<Boolean> peftMode;
    private Setting<Boolean> fill;
    
    public BlockHighlight() {
        this.renderModeSetting = this.register(Settings.e("Render Mode", RenderMode.BLOCK));
        this.red = this.register((Setting<Integer>)Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(0).build());
        this.green = this.register((Setting<Integer>)Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(0).build());
        this.blue = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(200).build());
        this.a = this.register((Setting<Integer>)Settings.integerBuilder("Alpha").withMinimum(0).withMaximum(255).withValue(0).build());
        this.fRed = this.register((Setting<Integer>)Settings.integerBuilder("Fill Red").withMinimum(0).withMaximum(255).withValue(0).build());
        this.fGreen = this.register((Setting<Integer>)Settings.integerBuilder("Fill Green").withMinimum(0).withMaximum(255).withValue(0).build());
        this.fBlue = this.register((Setting<Integer>)Settings.integerBuilder("Fill Blue").withMinimum(0).withMaximum(255).withValue(200).build());
        this.fA = this.register((Setting<Integer>)Settings.integerBuilder("Fill Alpha").withMinimum(0).withMaximum(255).withValue(0).build());
        this.peftMode = this.register(Settings.b("Chroma", false));
        this.fill = this.register(Settings.b("Fill", false));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final RayTraceResult result = BlockHighlight.mc.player.rayTrace(6.0, Minecraft.getMinecraft().getRenderPartialTicks());
        if (result == null) {
            return;
        }
        final BlockPos lookingAt = new BlockPos((Vec3i)result.getBlockPos());
        if (BlockHighlight.mc.world.getBlockState(lookingAt).getBlock() == Blocks.AIR) {
            return;
        }
        if (this.renderModeSetting.getValue().equals(RenderMode.FACE)) {
            if (!this.peftMode.getValue()) {
                VetHackTessellator.prepare(7);
                if (result.sideHit == EnumFacing.NORTH) {
                    VetHackTessellator.drawBox(lookingAt, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.a.getValue(), 4);
                }
                if (result.sideHit == EnumFacing.SOUTH) {
                    VetHackTessellator.drawBox(lookingAt, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.a.getValue(), 8);
                }
                if (result.sideHit == EnumFacing.WEST) {
                    VetHackTessellator.drawBox(lookingAt, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.a.getValue(), 16);
                }
                if (result.sideHit == EnumFacing.EAST) {
                    VetHackTessellator.drawBox(lookingAt, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.a.getValue(), 32);
                }
                if (result.sideHit == EnumFacing.UP) {
                    VetHackTessellator.drawBox(lookingAt, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.a.getValue(), 2);
                }
                if (result.sideHit == EnumFacing.DOWN) {
                    VetHackTessellator.drawBox(lookingAt, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.a.getValue(), 1);
                }
                VetHackTessellator.release();
            }
            else {
                final ActiveModules activeMods = (ActiveModules)ModuleManager.getModuleByName("ActiveModules");
                final float[] hue = { System.currentTimeMillis() % (360 * activeMods.getRainbowSpeed()) / (360.0f * activeMods.getRainbowSpeed()) };
                final int rgb = Color.HSBtoRGB(hue[0], ColourConverter.toF(activeMods.saturationR.getValue()), ColourConverter.toF(activeMods.brightnessR.getValue()));
                final int r = rgb >> 16 & 0xFF;
                final int g = rgb >> 8 & 0xFF;
                final int b = rgb & 0xFF;
                VetHackTessellator.prepare(7);
                if (result.sideHit == EnumFacing.NORTH) {
                    VetHackTessellator.drawBox(lookingAt, r, g, b, this.a.getValue(), 4);
                }
                if (result.sideHit == EnumFacing.SOUTH) {
                    VetHackTessellator.drawBox(lookingAt, r, g, b, this.a.getValue(), 8);
                }
                if (result.sideHit == EnumFacing.WEST) {
                    VetHackTessellator.drawBox(lookingAt, r, g, b, this.a.getValue(), 16);
                }
                if (result.sideHit == EnumFacing.EAST) {
                    VetHackTessellator.drawBox(lookingAt, r, g, b, this.a.getValue(), 32);
                }
                if (result.sideHit == EnumFacing.UP) {
                    VetHackTessellator.drawBox(lookingAt, r, g, b, this.a.getValue(), 2);
                }
                if (result.sideHit == EnumFacing.DOWN) {
                    VetHackTessellator.drawBox(lookingAt, r, g, b, this.a.getValue(), 1);
                }
                VetHackTessellator.release();
            }
        }
        else if (!this.peftMode.getValue()) {
            VetHackTessellator.prepare(7);
            VetHackTessellator.drawBoundingBoxBlockPos(lookingAt, 1.0f, this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255);
            VetHackTessellator.release();
            if (this.fill.getValue()) {
                VetHackTessellator.prepare(7);
                VetHackTessellator.drawBox(lookingAt, this.fRed.getValue(), this.fGreen.getValue(), this.fBlue.getValue(), this.fA.getValue(), 63);
                VetHackTessellator.release();
            }
        }
        else {
            final ActiveModules activeMods = (ActiveModules)ModuleManager.getModuleByName("ActiveModules");
            final float[] hue = { System.currentTimeMillis() % (360 * activeMods.getRainbowSpeed()) / (360.0f * activeMods.getRainbowSpeed()) };
            final int rgb = Color.HSBtoRGB(hue[0], ColourConverter.toF(activeMods.saturationR.getValue()), ColourConverter.toF(activeMods.brightnessR.getValue()));
            final int r = rgb >> 16 & 0xFF;
            final int g = rgb >> 8 & 0xFF;
            final int b = rgb & 0xFF;
            VetHackTessellator.prepare(7);
            VetHackTessellator.drawBoundingBoxBlockPos(lookingAt, 1.0f, r, g, b, this.a.getValue());
            VetHackTessellator.release();
            if (!this.fill.getValue()) {
                return;
            }
            VetHackTessellator.prepare(7);
            VetHackTessellator.drawBox(lookingAt, r, g, b, this.fA.getValue(), 63);
            VetHackTessellator.release();
        }
    }
    
    private enum RenderMode
    {
        BLOCK, 
        FACE;
    }
}
