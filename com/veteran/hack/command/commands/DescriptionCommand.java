// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import com.veteran.hack.module.Module;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.command.syntax.ChunkBuilder;
import com.veteran.hack.command.Command;

public class DescriptionCommand extends Command
{
    public DescriptionCommand() {
        super("description", new ChunkBuilder().append("module").build(), new String[] { "tooltip" });
        this.setDescription("Prints a module's description into the chat");
    }
    
    @Override
    public void call(final String[] args) {
        for (final String s : args) {
            if (s != null) {
                final Module module = ModuleManager.getModuleByName(s);
                Command.sendChatMessage(module.getChatName() + "Description: &7" + module.getDescription());
            }
        }
    }
}
