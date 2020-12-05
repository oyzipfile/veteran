// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.misc;

import com.veteran.hack.command.commands.FriendCommand;
import com.veteran.hack.command.Command;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import java.util.function.Predicate;
import com.veteran.hack.util.Friends;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.module.Module;

@Info(name = "MidClickFriends", category = Category.MISC, description = "Middle click players to friend or unfriend them", showOnArray = ShowOnArray.OFF)
public class MidClickFriends extends Module
{
    private int delay;
    @EventHandler
    public Listener<InputEvent.MouseInputEvent> mouseListener;
    
    public MidClickFriends() {
        this.delay = 0;
        Entity lookedAtEntity;
        this.mouseListener = new Listener<InputEvent.MouseInputEvent>(event -> {
            if (this.delay == 0 && Mouse.getEventButton() == 2 && Minecraft.getMinecraft().objectMouseOver.typeOfHit.equals((Object)RayTraceResult.Type.ENTITY)) {
                lookedAtEntity = Minecraft.getMinecraft().objectMouseOver.entityHit;
                if (!(!(lookedAtEntity instanceof EntityOtherPlayerMP))) {
                    if (Friends.isFriend(lookedAtEntity.getName())) {
                        this.remove(lookedAtEntity.getName());
                    }
                    else {
                        this.add(lookedAtEntity.getName());
                    }
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
    
    private void remove(final String name) {
        this.delay = 20;
        final Friends.Friend friend2 = Friends.friends.getValue().stream().filter(friend1 -> friend1.getUsername().equalsIgnoreCase(name)).findFirst().get();
        Friends.friends.getValue().remove(friend2);
        Command.sendChatMessage("&b" + friend2.getUsername() + "&r has been unfriended.");
    }
    
    private void add(final String name) {
        this.delay = 20;
        final Friends.Friend f;
        new Thread(() -> {
            f = new FriendCommand().getFriendByName(name);
            if (f == null) {
                Command.sendChatMessage("Failed to find UUID of " + name);
            }
            else {
                Friends.friends.getValue().add(f);
                Command.sendChatMessage("&b" + f.getUsername() + "&r has been friended.");
            }
        }).start();
    }
}
