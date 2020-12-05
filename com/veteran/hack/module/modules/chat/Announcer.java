// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.chat;

import java.util.Iterator;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import java.util.Random;
import com.veteran.hack.command.Command;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import com.veteran.hack.setting.Settings;
import net.minecraft.init.Items;
import java.util.ArrayList;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemStack;
import java.util.List;
import com.veteran.hack.module.Module;

@Info(name = "Announcer", description = "annoys chat lmaoo", category = Category.CHAT)
public class Announcer extends Module
{
    List<ItemStack> currentHotbar;
    List<ItemStack> lastHotbar;
    private Vec3d lastPosition;
    private ItemStack destroyingBlock;
    int delay;
    int minedBlock;
    boolean shouldSendMessage;
    private Setting<Integer> delayChange;
    private Setting<Boolean> debug;
    private List<StackInfo> changedItems;
    @EventHandler
    public Listener<PacketEvent.Send> packetSendListener;
    
    public Announcer() {
        this.currentHotbar = new ArrayList<ItemStack>();
        this.lastHotbar = new ArrayList<ItemStack>();
        this.destroyingBlock = new ItemStack(Items.AIR);
        this.delay = -1;
        this.minedBlock = 0;
        this.shouldSendMessage = false;
        this.delayChange = this.register((Setting<Integer>)Settings.integerBuilder("Delay").withRange(1, 100).withValue(10).build());
        this.debug = this.register(Settings.b("Debug", false));
        this.changedItems = new ArrayList<StackInfo>();
        CPacketPlayerDigging p;
        this.packetSendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayerDigging) {
                p = (CPacketPlayerDigging)event.getPacket();
                if (p.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    this.destroyingBlock = this.getBlockMining();
                }
                else if (p.getAction() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                    ++this.minedBlock;
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onEnable() {
        this.delay = 400;
        this.minedBlock = 0;
        this.changedItems = new ArrayList<StackInfo>();
    }
    
    @Override
    public void onUpdate() {
        --this.delay;
        if (Announcer.mc.world == null) {
            return;
        }
        if (this.delay <= 0) {
            if (this.debug.getValue()) {
                Command.sendChatMessage("Updating currentHotbar arraylist.");
            }
            this.updateHotbar(this.currentHotbar);
            if (this.debug.getValue()) {
                Command.sendChatMessage("Updated currentHotbar to arrayList with " + this.currentHotbar.size() + " entries.");
            }
            if (this.lastHotbar.size() < 1) {
                if (this.debug.getValue()) {
                    Command.sendChatMessage("Updating lastHotbar arraylist as size is 0 or less than 0");
                }
                this.updateHotbar(this.lastHotbar);
                if (this.debug.getValue()) {
                    Command.sendChatMessage("Updated lastHotbar to arrayList with " + this.lastHotbar.size() + " entries.");
                }
            }
            if (this.debug.getValue()) {
                Command.sendChatMessage("comparing currentHotbar (" + this.currentHotbar.size() + ") and lastHotbar (" + this.lastHotbar.size() + ")");
            }
            this.changedItems = this.getChangedItems(this.currentHotbar, this.lastHotbar);
            if (this.debug.getValue()) {
                Command.sendChatMessage("changedItems is a list with " + this.changedItems.size() + " entries.");
            }
            if (this.lastPosition == null) {
                this.lastPosition = Announcer.mc.player.getPositionVector();
            }
            final int distance = (int)Math.sqrt(Announcer.mc.player.getPosition().distanceSq(this.lastPosition.x, this.lastPosition.y, this.lastPosition.z)) * 3;
            final Random rand = new Random();
            int random1 = 0;
            final int random2 = rand.nextInt(9);
            final String[] move = { new String("\u12a0\u1201\u1295 \u1270\u1295\u1240\u1233\u1240\u1235\u12a9 " + distance + " feet \u121d\u1235\u130b\u1293 \u1208  VeteranHack!"), new String("\u0986\u09ae\u09bf \u09b8\u09ac\u09c7 \u09b8\u09b0\u09c7 \u0997\u09c7\u099b\u09bf  " + distance + " feet \u09a7\u09a8\u09cd\u09af\u09ac\u09be\u09a6  VeteranHack!"), new String("VeteranHack\u306e\u304a\u304b\u3052\u3067 " + distance + " feet\u3092\u79fb\u52d5\u3057\u307e\u3057\u305f!"), new String("\u042f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u043f\u0435\u0440\u0435\u0435\u0445\u0430\u043b " + distance + " feet \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f VeteranHack!"), new String("\u05d0\u05d9\u05da \u05e4\u05bc\u05d5\u05e0\u05e7\u05d8 \u05d0\u05e8\u05d9\u05d1\u05e2\u05e8\u05d2\u05e2\u05e4\u05d0\u05e8\u05df " + distance + " feet \u05d3\u05d0\u05b7\u05e0\u05e7 \u05e6\u05d5 VeteranHack!"), new String("I just walked " + distance + " feet thanks to VeteranHack!"), new String("Ich bin gerade " + distance + " feet dank VeteranHack umgezogen"), new String("Je viens de d\u00e9m\u00e9nager " + distance + " feet gr\u00e2ce \u00e0 VeteranHack!"), new String("Me acabo de mudar " + distance + " feet gracias a VeteranHack!"), new String("\u16c1 \u16c3\u16a2\u16cb\u16cf \u16d7\u16df\u16a1\u16d6\u16de " + distance + " feet \u16cf\u16ba\u16a8\u16be\u16b4\u16cb \u16cf\u16df VeteranHack!") };
            if (this.changedItems.size() == 0) {
                if (distance != 0) {
                    Command.sendServerMessage(move[random2]);
                }
                this.lastPosition = Announcer.mc.player.getPositionVector();
                this.updateHotbar(this.lastHotbar);
                this.delay = this.delayChange.getValue() * 40;
                return;
            }
            if (this.changedItems.size() > 1) {
                random1 = rand.nextInt(this.changedItems.size());
            }
            final int used = this.changedItems.get(random1).getUsed();
            final ItemAction action = this.changedItems.get(random1).getAction();
            final String name = this.changedItems.get(random1).getName();
            if (this.debug.getValue()) {
                Command.sendChatMessage("ItemAction is " + String.valueOf(action) + ". name is " + name + ". used is " + used);
            }
            final String[] place = { new String("\u12a0\u1201\u1295 \u120b\u12ed \u12a0\u1235\u1240\u121d\u132b\u1208\u1201 " + used + " " + name + " \u1208 VeteranHack \u12a0\u1218\u1230\u130d\u1293\u1208\u1201!"), new String("\u0986\u09ae\u09bf \u09b8\u09ac\u09c7\u09ae\u09be\u09a4\u09cd\u09b0 " + used + " " + name + " \u0995\u09c7 \u09a7\u09a8\u09cd\u09af\u09ac\u09be\u09a6 \u099c\u09be\u09a8\u09bf\u09af\u09bc\u09c7\u099b\u09bf VeteranHack!"), new String("VeteranHack\u306e\u304a\u304b\u3052\u3067" + used + " " + name + "\u3092\u914d\u7f6e\u3057\u307e\u3057\u305f!"), new String("\u042f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u0440\u0430\u0437\u043c\u0435\u0441\u0442\u0438\u043b " + used + " " + name + "\u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f VeteranHack!"), new String("\u05e4\u05e9\u05d5\u05d8 \u05de\u05d9\u05e7\u05de\u05ea\u05d9 " + used + " " + name + " \u05d1\u05d6\u05db\u05d5\u05ea VeteranHack!"), new String("I just placed " + used + " " + name + " thanks to VeteranHack!"), new String("Ich habe gegessen " + used + " " + name + " gerade, dank VeteranHack!"), new String("Je viens de placer " + used + " " + name + " gr\u00e2ce \u00e0 VeteranHack!"), new String("Me acabo de comer " + used + " " + name + " gracias a VeteranHack!") };
            final String[] eat = { new String("\u12a5\u1294 \u1260\u120d\u127c\u12eb\u1208\u1201 " + used + " " + name + " \u12a0\u1218\u1230\u130d\u1293\u1208\u1201 VeteranHack!"), new String("\u0986\u09ae\u09bf \u09ae\u09be\u09a4\u09cd\u09b0 \u0996\u09c7\u09af\u09bc\u09c7\u099b\u09bf " + used + " " + name + " \u09a7\u09a8\u09cd\u09af\u09ac\u09be\u09a6 VeteranHack!"), new String("VeteranHack\u306e\u304a\u304b\u3052\u3067" + used + " " + name + "\u3092\u98df\u3079\u307e\u3057\u305f"), new String("\u042f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u0441\u044a\u0435\u043b " + used + " " + name + " \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f VeteranHack"), new String("\u05d0\u05d9\u05da \u05e4\u05bc\u05d5\u05e0\u05e7\u05d8 \u05d2\u05e2\u05d2\u05e2\u05e1\u05df " + used + " " + name + "\u05d3\u05d0\u05b7\u05e0\u05e7 \u05e6\u05d5 VeteranHack!"), new String("I just ate " + used + " " + name + " thanks to VeteranHack!"), new String("Je viens de manger " + used + " " + name + " gr\u00e2ce \u00e0 VeteranHack!"), new String("Ich habe platziert " + used + " " + name + " gerade, dank VeteranHack!"), new String("Me acabo de colocar " + used + " " + name + " gracias a VeteranHack!") };
            final String[] destroy = { new String("\u12a5\u1294 \u1260\u120d\u127c\u12eb\u1208\u1201 " + used + " " + name + " \u12a0\u1218\u1230\u130d\u1293\u1208\u1201 VeteranHack!"), new String("Acabo de minar " + used + " " + name + " gracias a VeteranHack"), new String("\u0986\u09ae\u09bf \u09ae\u09be\u09a4\u09cd\u09b0 \u0996\u09c7\u09af\u09bc\u09c7\u099b\u09bf " + used + " " + name + " \u09a7\u09a8\u09cd\u09af\u09ac\u09be\u09a6 VeteranHack!"), new String("VeteranHack\u306e\u304a\u304b\u3052\u3067" + used + " " + name + "\u3092\u98df\u3079\u307e\u3057\u305f"), new String("\u042f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u0441\u044a\u0435\u043b " + used + " " + name + " \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f VeteranHack"), new String("\u05d0\u05d9\u05da \u05e4\u05bc\u05d5\u05e0\u05e7\u05d8 \u05d2\u05e2\u05d2\u05e2\u05e1\u05df " + used + " " + name + "\u05d3\u05d0\u05b7\u05e0\u05e7 \u05e6\u05d5 VeteranHack!"), new String("I just mined " + used + " " + name + " thanks to VeteranHack!"), new String("Je viens de quarrier " + used + " " + name + " gr\u00e2ce \u00e0 VeteranHack!"), new String("Ich habe minen " + used + " " + name + " gerade, dank VeteranHack!") };
            if (action == ItemAction.EAT) {
                Command.sendServerMessage(eat[random2]);
            }
            else if (action == ItemAction.PLACE) {
                Command.sendServerMessage(place[random2]);
            }
            else if (action == ItemAction.BREAK) {
                Command.sendServerMessage(destroy[random2]);
            }
            this.delay = this.delayChange.getValue() * 40;
            this.updateHotbar(this.lastHotbar);
        }
    }
    
    public void updateHotbar(final List<ItemStack> hotbarList) {
        hotbarList.clear();
        for (int i = 0; i < 9; ++i) {
            if (Announcer.mc.player.inventory.getStackInSlot(i) != null) {
                hotbarList.add(Announcer.mc.player.inventory.getStackInSlot(i));
            }
        }
        if (Announcer.mc.player.inventory.getStackInSlot(45) != null) {
            hotbarList.add(Announcer.mc.player.inventory.getStackInSlot(45));
        }
    }
    
    public ItemStack getBlockMining() {
        final RayTraceResult r = Announcer.mc.player.rayTrace(6.0, Announcer.mc.getRenderPartialTicks());
        final BlockPos miningPos = r.getBlockPos();
        final Block miningBlock = Announcer.mc.world.getBlockState(miningPos).getBlock();
        return new ItemStack(miningBlock);
    }
    
    List<StackInfo> getChangedItems(final List<ItemStack> currentInv, final List<ItemStack> lastInv) {
        final List<StackInfo> changedItems = new ArrayList<StackInfo>();
        int i = 0;
        for (final ItemStack c : currentInv) {
            if (i < 10) {
                ++i;
            }
            if (this.debug.getValue()) {
                Command.sendChatMessage("testing index " + i + " of lastHotbar which has " + lastInv.size() + " entries.");
            }
            final ItemStack s = lastInv.get(i - 1);
            if (this.debug.getValue() && s.stackSize - c.stackSize > 0) {
                Command.sendChatMessage("Stacks size check passed with " + (s.stackSize - c.stackSize) + " items used");
            }
            if ((this.debug.getValue() && c.getItem() instanceof ItemBlock) || (this.debug.getValue() && c.getItem() instanceof ItemFood) || (this.debug.getValue() && c.getItem() == Items.END_CRYSTAL)) {
                Command.sendChatMessage("Item check passed, item name is " + c.getDisplayName());
            }
            if ((s.stackSize - c.stackSize > 0 && c.getItem() instanceof ItemBlock) || (s.stackSize - c.stackSize > 0 && c.getItem() == Items.END_CRYSTAL)) {
                changedItems.add(new StackInfo(s.stackSize - c.stackSize, ItemAction.PLACE, c.getDisplayName()));
            }
            else if (s.stackSize - c.stackSize > 0 && c.getItem() instanceof ItemFood) {
                changedItems.add(new StackInfo(s.stackSize - c.stackSize, ItemAction.EAT, c.getDisplayName()));
            }
            if (this.minedBlock > 0) {
                changedItems.add(new StackInfo(this.minedBlock, ItemAction.BREAK, this.destroyingBlock.getDisplayName()));
                this.minedBlock = 0;
            }
        }
        return changedItems;
    }
}
