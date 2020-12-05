// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import net.minecraft.util.text.TextFormatting;
import com.veteran.hack.util.Friends;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Random;
import net.minecraft.entity.Entity;
import com.veteran.hack.BaseMod;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import java.util.function.Predicate;
import com.veteran.hack.command.Command;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.EntityUseTotem;
import me.zero.alpine.listener.Listener;
import java.util.HashMap;
import com.veteran.hack.util.ColourTextFormatting;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "TotemPopCounter", description = "Counts how many times players pop", category = Category.COMBAT)
public class TotemPopCounter extends Module
{
    private Setting<Boolean> countFriends;
    private Setting<Boolean> countSelf;
    private Setting<Boolean> resetDeaths;
    private Setting<Boolean> resetSelfDeaths;
    private Setting<Announce> announceSetting;
    private Setting<Boolean> thanksTo;
    private Setting<Boolean> announceNear;
    private Setting<ColourTextFormatting.ColourCode> colourCode;
    private Setting<ColourTextFormatting.ColourCode> colourCode1;
    private HashMap<String, Integer> playerList;
    private boolean isDead;
    String[] insults;
    int rand;
    int timer;
    @EventHandler
    public Listener<EntityUseTotem> listListener;
    @EventHandler
    public Listener<PacketEvent.Receive> popListener;
    
    public TotemPopCounter() {
        this.countFriends = this.register(Settings.b("Count Friends", true));
        this.countSelf = this.register(Settings.b("Count Self", false));
        this.resetDeaths = this.register(Settings.b("Reset On Death", true));
        this.resetSelfDeaths = this.register(Settings.b("Reset Self Death", true));
        this.announceSetting = this.register(Settings.e("Announce", Announce.CLIENT));
        this.thanksTo = this.register(Settings.b("Thanks to", false));
        this.announceNear = this.register(Settings.b("Insult Nearby Pops", false));
        this.colourCode = this.register(Settings.e("Color Name", ColourTextFormatting.ColourCode.DARK_PURPLE));
        this.colourCode1 = this.register(Settings.e("Color Number", ColourTextFormatting.ColourCode.LIGHT_PURPLE));
        this.playerList = new HashMap<String, Integer>();
        this.isDead = false;
        this.insults = new String[] { new String("ezzz pop, youre so bad "), new String("Just stop trying "), new String("Keep popping, "), new String("Pop more "), new String("watch out, youre popping "), new String("Crying wont un-rape you "), new String("You must be a 2020 player "), new String("why are you popping "), new String("you're a literal pinata ") };
        this.rand = 0;
        this.timer = 50;
        int popCounter;
        this.listListener = new Listener<EntityUseTotem>(event -> {
            if (this.playerList == null) {
                this.playerList = new HashMap<String, Integer>();
            }
            if (this.playerList.get(event.getEntity().getName()) == null) {
                this.playerList.put(event.getEntity().getName(), 1);
                this.sendMessage(this.formatName(event.getEntity().getName()) + "&4 popped " + this.formatNumber(1) + "&4 totem" + this.ending());
            }
            else if (this.playerList.get(event.getEntity().getName()) != null) {
                popCounter = this.playerList.get(event.getEntity().getName());
                ++popCounter;
                this.playerList.put(event.getEntity().getName(), popCounter);
                this.sendMessage(this.formatName(event.getEntity().getName()) + "&4 popped " + this.formatNumber(popCounter) + "&4 totems" + this.ending());
            }
            if (this.announceNear.getValue() && Math.sqrt(TotemPopCounter.mc.player.getDistanceSq(event.getEntity())) <= 10.0 && event.getEntity() != TotemPopCounter.mc.player && this.timer <= 0) {
                Command.sendServerMessage(this.insults[this.rand] + event.getEntity().getName());
                this.timer = 50;
            }
            return;
        }, (Predicate<EntityUseTotem>[])new Predicate[0]);
        SPacketEntityStatus packet;
        Entity entity;
        this.popListener = new Listener<PacketEvent.Receive>(event -> {
            if (TotemPopCounter.mc.player != null) {
                if (event.getPacket() instanceof SPacketEntityStatus) {
                    packet = (SPacketEntityStatus)event.getPacket();
                    if (packet.getOpCode() == 35) {
                        entity = packet.getEntity((World)TotemPopCounter.mc.world);
                        if (this.friendCheck(entity.getName()) || this.selfCheck(entity.getName())) {
                            BaseMod.EVENT_BUS.post(new EntityUseTotem(entity));
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        --this.timer;
        final Random r = new Random();
        this.rand = r.nextInt(this.insults.length);
        if (!this.isDead && this.resetSelfDeaths.getValue() && 0.0f >= TotemPopCounter.mc.player.getHealth()) {
            this.sendMessage(this.formatName(TotemPopCounter.mc.player.getName()) + " died and " + this.grammar(TotemPopCounter.mc.player.getName()) + " pop list was reset!");
            this.isDead = true;
            this.playerList.clear();
            return;
        }
        if (this.isDead && 0.0f < TotemPopCounter.mc.player.getHealth()) {
            this.isDead = false;
        }
        for (final EntityPlayer player : TotemPopCounter.mc.world.playerEntities) {
            if (this.resetDeaths.getValue() && 0.0f >= player.getHealth() && this.friendCheck(player.getName()) && this.selfCheck(player.getName()) && this.playerList.containsKey(player.getName())) {
                this.sendMessage(this.formatName(player.getName()) + "&4 died after popping " + this.formatNumber(this.playerList.get(player.getName())) + "&4 totems" + this.ending());
                this.playerList.remove(player.getName(), this.playerList.get(player.getName()));
            }
        }
    }
    
    private boolean friendCheck(final String name) {
        if (this.isDead) {
            return false;
        }
        for (final Friends.Friend names : Friends.friends.getValue()) {
            if (names.getUsername().equalsIgnoreCase(name)) {
                return this.countFriends.getValue();
            }
        }
        return true;
    }
    
    private boolean selfCheck(final String name) {
        return !this.isDead && ((this.countSelf.getValue() && name.equalsIgnoreCase(TotemPopCounter.mc.player.getName())) || this.countSelf.getValue() || !name.equalsIgnoreCase(TotemPopCounter.mc.player.getName()));
    }
    
    private boolean isSelf(final String name) {
        return name.equalsIgnoreCase(TotemPopCounter.mc.player.getName());
    }
    
    private boolean isFriend(final String name) {
        for (final Friends.Friend names : Friends.friends.getValue()) {
            if (names.getUsername().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    private String formatName(String name) {
        String extraText = "";
        if (this.isFriend(name) && !this.isPublic()) {
            extraText = "Your friend, ";
        }
        else if (this.isFriend(name) && this.isPublic()) {
            extraText = "My friend, ";
        }
        if (this.isSelf(name)) {
            extraText = "";
            name = "I";
        }
        if (this.announceSetting.getValue().equals(Announce.EVERYONE)) {
            return extraText + name;
        }
        return extraText + this.setToText(this.colourCode.getValue()) + name + TextFormatting.RESET;
    }
    
    private String grammar(final String name) {
        if (this.isSelf(name)) {
            return "my";
        }
        return "their";
    }
    
    private String ending() {
        if (this.thanksTo.getValue()) {
            return " thanks to Veteran Hack!";
        }
        return "!";
    }
    
    private boolean isPublic() {
        return this.announceSetting.getValue().equals(Announce.EVERYONE);
    }
    
    private String formatNumber(final int message) {
        if (this.announceSetting.getValue().equals(Announce.EVERYONE)) {
            return "" + message;
        }
        return this.setToText(this.colourCode1.getValue()) + "" + message + TextFormatting.RESET;
    }
    
    private void sendMessage(final String message) {
        switch (this.announceSetting.getValue()) {
            case CLIENT: {
                Command.sendChatMessage(message);
            }
            case EVERYONE: {
                Command.sendServerMessage(message);
            }
            default: {}
        }
    }
    
    private TextFormatting setToText(final ColourTextFormatting.ColourCode colourCode) {
        return ColourTextFormatting.toTextMap.get(colourCode);
    }
    
    private enum Announce
    {
        CLIENT, 
        EVERYONE;
    }
}
