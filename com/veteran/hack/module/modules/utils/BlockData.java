// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import java.util.function.Predicate;
import com.veteran.hack.command.Command;
import java.util.Objects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.module.Module;

@Info(name = "BlockData", category = Category.UTILS, description = "Right click blocks to display their data")
public class BlockData extends Module
{
    private int delay;
    @EventHandler
    public Listener<InputEvent.MouseInputEvent> mouseListener;
    
    public BlockData() {
        this.delay = 0;
        BlockPos blockpos;
        IBlockState iblockstate;
        Block block;
        TileEntity t;
        NBTTagCompound tag;
        this.mouseListener = new Listener<InputEvent.MouseInputEvent>(event -> {
            if (Mouse.getEventButton() == 1 && this.delay == 0 && BlockData.mc.objectMouseOver.typeOfHit.equals((Object)RayTraceResult.Type.BLOCK)) {
                blockpos = BlockData.mc.objectMouseOver.getBlockPos();
                iblockstate = BlockData.mc.world.getBlockState(blockpos);
                block = iblockstate.getBlock();
                if (block.hasTileEntity()) {
                    t = BlockData.mc.world.getTileEntity(blockpos);
                    tag = new NBTTagCompound();
                    Objects.requireNonNull(t).writeToNBT(tag);
                    Command.sendChatMessage(this.getChatName() + "&6Block Tags:\n" + tag + "");
                }
            }
        }, (Predicate<InputEvent.MouseInputEvent>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
    }
}
