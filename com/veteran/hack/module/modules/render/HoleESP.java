// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.render;

import com.veteran.hack.util.VetHackTessellator;
import java.awt.Color;
import com.veteran.hack.util.ColourConverter;
import com.veteran.hack.module.modules.gui.ActiveModules;
import com.veteran.hack.event.events.RenderEvent;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.Vec3i;
import net.minecraft.init.Blocks;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.modules.combat.Autocrystal;
import com.veteran.hack.setting.Settings;
import java.util.concurrent.ConcurrentHashMap;
import com.veteran.hack.setting.Setting;
import net.minecraft.util.math.BlockPos;
import com.veteran.hack.module.Module;

@Info(name = "HoleESP", category = Category.RENDER, description = "Show safe holes for crystal pvp")
public class HoleESP extends Module
{
    private final BlockPos[] surroundOffset;
    private Setting<Double> renderDistance;
    private Setting<Integer> a0;
    private Setting<Integer> r1;
    private Setting<Integer> g1;
    private Setting<Integer> b1;
    private Setting<Integer> r2;
    private Setting<Integer> g2;
    private Setting<Integer> b2;
    private Setting<RenderMode> renderModeSetting;
    private Setting<RenderBlocks> renderBlocksSetting;
    private Setting<Boolean> drawSkeleton;
    private Setting<Boolean> chroma;
    private Setting<Boolean> bedrockOnly;
    public ConcurrentHashMap<BlockPos, Boolean> safeHoles;
    
    public HoleESP() {
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
        this.renderDistance = this.register(Settings.d("Render Distance", 8.0));
        this.a0 = this.register((Setting<Integer>)Settings.integerBuilder("Transparency").withMinimum(0).withValue(32).withMaximum(255).build());
        this.r1 = this.register(Settings.integerBuilder("Red (Obby)").withMinimum(0).withValue(208).withMaximum(255).withVisibility(v -> this.obbySettings()).build());
        this.g1 = this.register(Settings.integerBuilder("Green (Obby)").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.obbySettings()).build());
        this.b1 = this.register(Settings.integerBuilder("Blue (Obby)").withMinimum(0).withValue(255).withMaximum(255).withVisibility(v -> this.obbySettings()).build());
        this.r2 = this.register(Settings.integerBuilder("Red (Bedrock)").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.bedrockSettings()).build());
        this.g2 = this.register(Settings.integerBuilder("Green (Bedrock)").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.bedrockSettings()).build());
        this.b2 = this.register(Settings.integerBuilder("Blue (Bedrock)").withMinimum(0).withValue(255).withMaximum(255).withVisibility(v -> this.bedrockSettings()).build());
        this.renderModeSetting = this.register(Settings.e("Render Mode", RenderMode.BLOCK));
        this.renderBlocksSetting = this.register(Settings.e("Render", RenderBlocks.BOTH));
        this.drawSkeleton = this.register(Settings.b("Draw Skeleton", false));
        this.chroma = this.register(Settings.b("Chroma Bedrock", false));
        this.bedrockOnly = this.register(Settings.b("Only Bedrock", false));
    }
    
    private boolean obbySettings() {
        return this.renderBlocksSetting.getValue().equals(RenderBlocks.BOTH);
    }
    
    private boolean bedrockSettings() {
        return this.renderBlocksSetting.getValue().equals(RenderBlocks.BOTH);
    }
    
    @Override
    public void onUpdate() {
        if (this.safeHoles == null) {
            this.safeHoles = new ConcurrentHashMap<BlockPos, Boolean>();
        }
        else {
            this.safeHoles.clear();
        }
        final int range = (int)Math.ceil(this.renderDistance.getValue());
        final Autocrystal crystalAura = (Autocrystal)ModuleManager.getModuleByName("Autocrystal");
        final List<BlockPos> blockPosList = crystalAura.getSphere(Autocrystal.getPlayerPos(), (float)range, range, false, true, 0);
        for (final BlockPos pos : blockPosList) {
            if (!HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            boolean isSafe = true;
            boolean isBedrock = true;
            for (final BlockPos offset : this.surroundOffset) {
                final Block block = HoleESP.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                if (block != Blocks.BEDROCK) {
                    isBedrock = false;
                }
                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    isSafe = false;
                    break;
                }
            }
            if (!isSafe) {
                continue;
            }
            this.safeHoles.put(pos, isBedrock);
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (HoleESP.mc.player == null || this.safeHoles == null) {
            return;
        }
        if (this.safeHoles.isEmpty()) {
            return;
        }
        final int red;
        final int green;
        final int blue;
        int red2;
        int green2;
        int blue2;
        ActiveModules activeMods;
        float[] hue;
        int rgb;
        this.safeHoles.forEach((blockPos, isBedrock) -> {
            switch (this.renderBlocksSetting.getValue()) {
                case BOTH: {
                    red = 0;
                    green = 0;
                    blue = 0;
                    if (!this.chroma.getValue()) {
                        red2 = this.r2.getValue();
                        green2 = this.g2.getValue();
                        blue2 = this.b2.getValue();
                    }
                    else {
                        activeMods = (ActiveModules)ModuleManager.getModuleByName("ActiveModules");
                        hue = new float[] { System.currentTimeMillis() % (360 * activeMods.getRainbowSpeed()) / (360.0f * activeMods.getRainbowSpeed()) };
                        rgb = Color.HSBtoRGB(hue[0], ColourConverter.toF(activeMods.saturationR.getValue()), ColourConverter.toF(activeMods.brightnessR.getValue()));
                        red2 = (rgb >> 16 & 0xFF);
                        green2 = (rgb >> 8 & 0xFF);
                        blue2 = (rgb & 0xFF);
                    }
                    if (isBedrock) {
                        VetHackTessellator.prepare(7);
                        this.drawBox(blockPos, red2, green2, blue2);
                        VetHackTessellator.release();
                        VetHackTessellator.prepare(7);
                        if (this.drawSkeleton.getValue()) {
                            VetHackTessellator.drawBoundingBoxBlockPos(blockPos, 1.0f, this.r2.getValue(), this.g2.getValue(), this.b2.getValue(), 255);
                        }
                        VetHackTessellator.release();
                        break;
                    }
                    else if (this.bedrockOnly.getValue()) {
                        return;
                    }
                    else {
                        VetHackTessellator.prepare(7);
                        this.drawBox(blockPos, this.r1.getValue(), this.g1.getValue(), this.b1.getValue());
                        VetHackTessellator.release();
                        VetHackTessellator.prepare(7);
                        if (this.drawSkeleton.getValue()) {
                            VetHackTessellator.drawBoundingBoxBlockPos(blockPos, 1.0f, this.r1.getValue(), this.g1.getValue(), this.b1.getValue(), 255);
                        }
                        VetHackTessellator.release();
                        break;
                    }
                    break;
                }
            }
        });
    }
    
    private void drawBox(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.a0.getValue());
        if (this.renderModeSetting.getValue().equals(RenderMode.DOWN)) {
            VetHackTessellator.drawBox(blockPos, color.getRGB(), 1);
        }
        else if (this.renderModeSetting.getValue().equals(RenderMode.BLOCK)) {
            VetHackTessellator.drawBox(blockPos, color.getRGB(), 63);
        }
        else if (this.renderModeSetting.getValue().equals(RenderMode.SQUARE)) {
            VetHackTessellator.drawBoundingSquareBlockPos(blockPos, 1.0f, color.getRed(), color.getGreen(), color.getBlue(), 255);
        }
        else if (this.renderModeSetting.getValue().equals(RenderMode.HALF)) {
            VetHackTessellator.drawHalfBoundingBoxBlockPos(blockPos, 1.0f, color.getRed(), color.getGreen(), color.getBlue(), 255);
        }
    }
    
    private enum RenderMode
    {
        DOWN, 
        BLOCK, 
        SQUARE, 
        HALF;
    }
    
    private enum RenderBlocks
    {
        BOTH;
    }
}
