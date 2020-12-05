// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemShulkerBox;
import com.veteran.hack.util.Wrapper;
import com.veteran.hack.command.syntax.SyntaxChunk;
import net.minecraft.tileentity.TileEntityShulkerBox;
import com.veteran.hack.command.Command;

public class PeekCommand extends Command
{
    public static TileEntityShulkerBox sb;
    
    public PeekCommand() {
        super("peek", SyntaxChunk.EMPTY, new String[0]);
        this.setDescription("Look inside the contents of a shulker box without opening it");
    }
    
    @Override
    public void call(final String[] args) {
        final ItemStack is = Wrapper.getPlayer().inventory.getCurrentItem();
        if (is.getItem() instanceof ItemShulkerBox) {
            final TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            entityBox.blockType = ((ItemShulkerBox)is.getItem()).getBlock();
            entityBox.setWorld(Wrapper.getWorld());
            entityBox.readFromNBT(is.getTagCompound().getCompoundTag("BlockEntityTag"));
            PeekCommand.sb = entityBox;
        }
        else {
            Command.sendChatMessage("You aren't carrying a shulker box.");
        }
    }
}
