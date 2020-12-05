// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import net.minecraft.util.math.Vec3d;
import com.veteran.hack.module.modules.hidden.Teleport;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.command.syntax.SyntaxParser;
import com.veteran.hack.command.syntax.parsers.ModuleParser;
import com.veteran.hack.command.syntax.ChunkBuilder;
import java.text.DecimalFormat;
import net.minecraft.client.Minecraft;
import com.veteran.hack.command.Command;

public class TeleportCommand extends Command
{
    Minecraft mc;
    DecimalFormat df;
    
    public TeleportCommand() {
        super("teleport", new ChunkBuilder().append("x/stop", true, new ModuleParser()).append("y", true).append("z", true).append("blocks per tp", false).build(), new String[] { "tp", "clip" });
        this.mc = Minecraft.getMinecraft();
        this.df = new DecimalFormat("#.###");
        this.setDescription("Potentia teleport exploit");
    }
    
    @Override
    public void call(final String[] args) {
        if (args[0].equalsIgnoreCase("stop")) {
            Command.sendChatMessage("Teleport Cancelled!");
            ModuleManager.getModuleByName("Teleport").disable();
            return;
        }
        if (args.length >= 4 && args[3] != null) {
            Teleport.blocksPerTeleport = Double.valueOf(args[3]);
        }
        else {
            Teleport.blocksPerTeleport = 10000.0;
        }
        if (args.length >= 3) {
            try {
                final double x = args[0].equals("~") ? this.mc.player.posX : ((args[0].charAt(0) == '~') ? (Double.parseDouble(args[0].substring(1)) + this.mc.player.posX) : Double.parseDouble(args[0]));
                final double y = args[1].equals("~") ? this.mc.player.posY : ((args[1].charAt(0) == '~') ? (Double.parseDouble(args[1].substring(1)) + this.mc.player.posY) : Double.parseDouble(args[1]));
                final double z = args[2].equals("~") ? this.mc.player.posZ : ((args[2].charAt(0) == '~') ? (Double.parseDouble(args[2].substring(1)) + this.mc.player.posZ) : Double.parseDouble(args[2]));
                Teleport.finalPos = new Vec3d(x, y, z);
                ModuleManager.getModuleByName("Teleport").enable();
                Command.sendChatMessage("\n&aTeleporting to \n&cX: &b" + this.df.format(x) + "&a, \n&cY: &b" + this.df.format(y) + "&a, \n&cZ: &b" + this.df.format(z) + "\n&aat &b" + this.df.format(Teleport.blocksPerTeleport) + "&c blocks per teleport.");
            }
            catch (NullPointerException e) {
                Command.sendErrorMessage("Null Pointer Exception Caught!");
            }
        }
    }
}
