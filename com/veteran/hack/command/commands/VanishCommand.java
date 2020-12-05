// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import com.veteran.hack.command.syntax.SyntaxChunk;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import com.veteran.hack.command.Command;

public class VanishCommand extends Command
{
    private static Entity vehicle;
    Minecraft mc;
    
    public VanishCommand() {
        super("vanish", null, new String[0]);
        this.mc = Minecraft.getMinecraft();
        this.setDescription("Allows you to vanish using an entity");
    }
    
    @Override
    public void call(final String[] args) {
        if (this.mc.player.getRidingEntity() != null && VanishCommand.vehicle == null) {
            VanishCommand.vehicle = this.mc.player.getRidingEntity();
            this.mc.player.dismountRidingEntity();
            this.mc.world.removeEntityFromWorld(VanishCommand.vehicle.getEntityId());
            Command.sendChatMessage("Vehicle " + VanishCommand.vehicle.getName() + " removed.");
        }
        else if (VanishCommand.vehicle != null) {
            VanishCommand.vehicle.isDead = false;
            this.mc.world.addEntityToWorld(VanishCommand.vehicle.getEntityId(), VanishCommand.vehicle);
            this.mc.player.startRiding(VanishCommand.vehicle, true);
            Command.sendChatMessage("Vehicle " + VanishCommand.vehicle.getName() + " created.");
            VanishCommand.vehicle = null;
        }
        else {
            Command.sendChatMessage("No Vehicle.");
        }
    }
}
