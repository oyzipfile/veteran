// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.modules.chat.CustomChat;
import com.veteran.hack.command.syntax.ChunkBuilder;
import com.veteran.hack.command.Command;

public class CustomChatCommand extends Command
{
    public CustomChatCommand() {
        super("customchat", new ChunkBuilder().append("ending").build(), new String[] { "chat" });
        this.setDescription("Allows you to customize CustomChat's custom setting");
    }
    
    @Override
    public void call(final String[] args) {
        final CustomChat cC = (CustomChat)ModuleManager.getModuleByName("CustomChat");
        if (cC == null) {
            Command.sendErrorMessage("&cThe CustomChat module is not available for some reason. Make sure the name you're calling is correct and that you have the module installed!!");
            return;
        }
        if (!cC.isEnabled()) {
            Command.sendWarningMessage("&6Warning: The CustomChat module is not enabled!");
            Command.sendWarningMessage("The command will still work, but will not visibly do anything.");
        }
        if (!cC.textMode.getValue().equals(CustomChat.TextMode.CUSTOM)) {
            Command.sendWarningMessage("&6Warning: You don't have custom mode enabled in CustomChat!");
            Command.sendWarningMessage("The command will still work, but will not visibly do anything.");
        }
        for (final String s : args) {
            if (s != null) {
                cC.customText.setValue(s);
                Command.sendChatMessage("Set the Custom Text Mode to <" + s + ">");
            }
        }
    }
}
