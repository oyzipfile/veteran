// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import com.veteran.hack.command.syntax.ChunkBuilder;
import com.veteran.hack.command.Command;

public class SayCommand extends Command
{
    public SayCommand() {
        super("say", new ChunkBuilder().append("message").build(), new String[0]);
        this.setDescription("Allows you to send any message, even with a prefix in it");
    }
    
    @Override
    public void call(final String[] args) {
        final StringBuilder message = new StringBuilder();
        for (final String arg : args) {
            if (arg != null) {
                message.append(" ").append(arg);
            }
        }
        Command.sendServerMessage(message.toString());
    }
}
