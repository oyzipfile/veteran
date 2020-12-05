// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import com.veteran.hack.module.Module;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.command.syntax.SyntaxParser;
import com.veteran.hack.command.syntax.parsers.ModuleParser;
import com.veteran.hack.command.syntax.ChunkBuilder;
import com.veteran.hack.command.Command;

public class ToggleCommand extends Command
{
    public ToggleCommand() {
        super("toggle", new ChunkBuilder().append("module", true, new ModuleParser()).build(), new String[] { "t" });
        this.setDescription("Quickly toggle a module on and off");
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }
        final Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Unknown module '" + args[0] + "'");
            return;
        }
        m.toggle();
        Command.sendChatMessage(m.getName() + (m.isEnabled() ? " &aenabled" : " &cdisabled"));
    }
}
