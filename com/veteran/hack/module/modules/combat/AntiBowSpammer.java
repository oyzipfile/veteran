// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import java.util.Iterator;
import java.util.List;
import com.veteran.hack.command.Command;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.veteran.hack.util.Friends;
import java.util.Collection;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import com.veteran.hack.module.Module;

@Info(name = "Anti Bowspammer", category = Category.CHAT, description = "insults bowspammers so they feel bad about themselves xd")
public class AntiBowSpammer extends Module
{
    int sentLastMessage;
    
    public AntiBowSpammer() {
        this.sentLastMessage = 0;
    }
    
    @Override
    public void onUpdate() {
        final List<Entity> entities = new ArrayList<Entity>();
        entities.addAll((Collection<? extends Entity>)AntiBowSpammer.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        for (final Entity e : entities) {
            if (((EntityPlayer)e).getHeldItemMainhand().getItem() == Items.BOW && this.sentLastMessage > 300) {
                this.sentLastMessage = 0;
                Command.sendServerMessage("/w " + e.getName() + " hey bowspamming larper faggot im going to rape your kids :cat_joy:");
            }
        }
        ++this.sentLastMessage;
    }
    
    public void onEnable() {
        Command.sendChatMessage("This module involves entities and /w. it may get you kicked, or lag your game. be cautious. this is a caveat");
    }
}
