// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.render;

import java.util.Collections;
import java.util.HashSet;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import java.util.Iterator;
import com.veteran.hack.setting.Settings;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import java.util.Set;
import com.veteran.hack.setting.Setting;
import net.minecraftforge.fml.common.Mod;
import com.veteran.hack.module.Module;

@Info(name = "XRay", category = Category.HIDDEN, description = "See through common blocks!")
@Mod.EventBusSubscriber(modid = "vethack")
public class XRay extends Module
{
    private static final String DEFAULT_XRAY_CONFIG = "minecraft:grass,minecraft:dirt,minecraft:netherrack,minecraft:gravel,minecraft:sand,minecraft:stone";
    private Setting<String> hiddenBlockNames;
    public Setting<Boolean> invert;
    private Setting<Boolean> outlines;
    private static Set<Block> hiddenBlocks;
    private static boolean invertStatic;
    private static boolean outlinesStatic;
    private static IBlockState transparentState;
    public static Block transparentBlock;
    
    public XRay() {
        this.hiddenBlockNames = this.register(Settings.stringBuilder("HiddenBlocks").withValue("minecraft:grass,minecraft:dirt,minecraft:netherrack,minecraft:gravel,minecraft:sand,minecraft:stone").withConsumer((old, value) -> {
            this.refreshHiddenBlocksSet(value);
            if (this.isEnabled()) {
                XRay.mc.renderGlobal.loadRenderers();
            }
            return;
        }).build());
        this.invert = this.register(Settings.booleanBuilder("Invert").withValue(false).withConsumer((old, value) -> {
            XRay.invertStatic = value;
            if (this.isEnabled()) {
                XRay.mc.renderGlobal.loadRenderers();
            }
            return;
        }).build());
        this.outlines = this.register(Settings.booleanBuilder("Outlines").withValue(true).withConsumer((old, value) -> {
            XRay.outlinesStatic = value;
            if (this.isEnabled()) {
                XRay.mc.renderGlobal.loadRenderers();
            }
            return;
        }).build());
        XRay.invertStatic = this.invert.getValue();
        XRay.outlinesStatic = this.outlines.getValue();
        this.refreshHiddenBlocksSet(this.hiddenBlockNames.getValue());
    }
    
    public String extGet() {
        return this.extGetInternal(null);
    }
    
    public void extAdd(final String s) {
        this.hiddenBlockNames.setValue(this.extGetInternal(null) + ", " + s);
    }
    
    public void extRemove(final String s) {
        this.hiddenBlockNames.setValue(this.extGetInternal(Block.getBlockFromName(s)));
    }
    
    public void extClear() {
        this.hiddenBlockNames.setValue("");
    }
    
    public void extDefaults() {
        this.extClear();
        this.extAdd("minecraft:grass,minecraft:dirt,minecraft:netherrack,minecraft:gravel,minecraft:sand,minecraft:stone");
    }
    
    public void extSet(final String s) {
        this.extClear();
        this.extAdd(s);
    }
    
    private String extGetInternal(final Block filter) {
        final StringBuilder sb = new StringBuilder();
        boolean notFirst = false;
        for (final Block b : XRay.hiddenBlocks) {
            if (b == filter) {
                continue;
            }
            if (notFirst) {
                sb.append(", ");
            }
            notFirst = true;
            sb.append(Block.REGISTRY.getNameForObject((Object)b));
        }
        return sb.toString();
    }
    
    private void refreshHiddenBlocksSet(final String v) {
        XRay.hiddenBlocks.clear();
        for (final String s : v.split(",")) {
            final String s2 = s.trim();
            final Block block = Block.getBlockFromName(s2);
            if (block != null) {
                XRay.hiddenBlocks.add(block);
            }
        }
    }
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        (XRay.transparentBlock = new Block(Material.GLASS) {
            public BlockRenderLayer getRenderLayer() {
                return BlockRenderLayer.CUTOUT;
            }
            
            public boolean isOpaqueCube(final IBlockState blah) {
                return false;
            }
            
            public boolean shouldSideBeRendered(final IBlockState blah, final IBlockAccess w, final BlockPos pos, final EnumFacing side) {
                final BlockPos adj = pos.offset(side);
                final IBlockState other = w.getBlockState(adj);
                return other.getBlock() != this && !other.isOpaqueCube();
            }
        }).setRegistryName("kami_xray_transparent");
        XRay.transparentState = XRay.transparentBlock.getDefaultState();
        event.getRegistry().registerAll((IForgeRegistryEntry[])new Block[] { XRay.transparentBlock });
    }
    
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll((IForgeRegistryEntry[])new Item[] { (Item)new ItemBlock(XRay.transparentBlock).setRegistryName(XRay.transparentBlock.getRegistryName()) });
    }
    
    public static IBlockState transform(final IBlockState input) {
        final Block b = input.getBlock();
        boolean hide = XRay.hiddenBlocks.contains(b);
        if (XRay.invertStatic) {
            hide = !hide;
        }
        if (hide) {
            IBlockState target = Blocks.AIR.getDefaultState();
            if (XRay.outlinesStatic && XRay.transparentState != null) {
                target = XRay.transparentState;
            }
            return target;
        }
        return input;
    }
    
    @Override
    protected void onEnable() {
        XRay.mc.renderGlobal.loadRenderers();
    }
    
    @Override
    protected void onDisable() {
        XRay.mc.renderGlobal.loadRenderers();
    }
    
    static {
        XRay.hiddenBlocks = Collections.synchronizedSet(new HashSet<Block>());
        XRay.outlinesStatic = true;
    }
}
