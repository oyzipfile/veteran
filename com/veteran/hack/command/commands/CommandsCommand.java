// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import java.util.Comparator;
import com.veteran.hack.BaseMod;
import com.veteran.hack.command.syntax.SyntaxChunk;
import com.veteran.hack.command.Command;

public class CommandsCommand extends Command
{
    public CommandsCommand() {
        super("commands", SyntaxChunk.EMPTY, new String[] { "cmds" });
        this.setDescription("Gives you this list of commands");
    }
    
    @Override
    public void call(final String[] args) {
        BaseMod.getInstance().getCommandManager().getCommands().stream().sorted(Comparator.comparing(command -> command.getLabel())).forEach(command -> Command.sendChatMessage("&f" + Command.getCommandPrefix() + command.getLabel() + "&r ~ &7" + command.getDescription()));
    }
}
