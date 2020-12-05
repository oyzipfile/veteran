// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import com.veteran.hack.module.modules.render.Pathfind;
import net.minecraft.pathfinding.PathPoint;
import com.veteran.hack.command.syntax.ChunkBuilder;
import com.veteran.hack.command.Command;

public class PathCommand extends Command
{
    int x;
    int y;
    int z;
    
    public PathCommand() {
        super("path", new ChunkBuilder().append("x").append("y").append("z").build(), new String[0]);
        this.x = Integer.MIN_VALUE;
        this.y = Integer.MIN_VALUE;
        this.z = Integer.MIN_VALUE;
        this.setDescription("Pathfinding for AutoWalk");
    }
    
    @Override
    public void call(final String[] args) {
        if (args[0] != null && args[0].equalsIgnoreCase("retry")) {
            if (this.x != Integer.MIN_VALUE) {
                final PathPoint end = new PathPoint(this.x, this.y, this.z);
                Pathfind.createPath(end);
                if (!Pathfind.points.isEmpty()) {
                    Command.sendChatMessage("Path created!");
                }
                return;
            }
            Command.sendChatMessage("No location to retry pathfinding to.");
        }
        else {
            if (args.length <= 3) {
                Command.sendChatMessage("&cMissing arguments: x, y, z");
                return;
            }
            try {
                this.x = Integer.parseInt(args[0]);
                this.y = Integer.parseInt(args[1]);
                this.z = Integer.parseInt(args[2]);
                final PathPoint end = new PathPoint(this.x, this.y, this.z);
                Pathfind.createPath(end);
                if (!Pathfind.points.isEmpty()) {
                    Command.sendChatMessage("Path created!");
                }
            }
            catch (NumberFormatException e) {
                Command.sendChatMessage("Error: input must be numerical");
            }
        }
    }
}
