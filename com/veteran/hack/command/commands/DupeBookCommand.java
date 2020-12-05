// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import java.util.stream.IntStream;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.nbt.NBTTagList;
import java.util.Random;
import net.minecraft.item.ItemWritableBook;
import com.veteran.hack.util.Wrapper;
import com.veteran.hack.command.syntax.ChunkBuilder;
import com.veteran.hack.command.Command;

public class DupeBookCommand extends Command
{
    public DupeBookCommand() {
        super("dupebook", new ChunkBuilder().append("name").build(), new String[0]);
        this.setDescription("Generates books used for chunk savestate dupe.");
    }
    
    @Override
    public void call(final String[] args) {
        final ItemStack heldItem = Wrapper.getPlayer().inventory.getCurrentItem();
        if (heldItem.getItem() instanceof ItemWritableBook) {
            final IntStream characterGenerator = new Random().ints(128, 1112063).map(i -> (i < 55296) ? i : (i + 2048));
            final NBTTagList pages = new NBTTagList();
            final String joinedPages = characterGenerator.limit(10500L).mapToObj(i -> String.valueOf((char)i)).collect((Collector<? super Object, ?, String>)Collectors.joining());
            for (int page = 0; page < 50; ++page) {
                pages.appendTag((NBTBase)new NBTTagString(joinedPages.substring(page * 210, (page + 1) * 210)));
            }
            if (heldItem.hasTagCompound()) {
                assert heldItem.getTagCompound() != null;
                heldItem.getTagCompound().setTag("pages", (NBTBase)pages);
                heldItem.getTagCompound().setTag("title", (NBTBase)new NBTTagString(""));
                heldItem.getTagCompound().setTag("author", (NBTBase)new NBTTagString(Wrapper.getPlayer().getName()));
            }
            else {
                heldItem.setTagInfo("pages", (NBTBase)pages);
                heldItem.setTagInfo("title", (NBTBase)new NBTTagString(""));
                heldItem.setTagInfo("author", (NBTBase)new NBTTagString(Wrapper.getPlayer().getName()));
            }
            final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            buf.writeItemStack(heldItem);
            Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketCustomPayload("MC|BEdit", buf));
            Command.sendChatMessage("Dupe book generated.");
        }
        else {
            Command.sendErrorMessage("You must be holding a writable book.");
        }
    }
}
