// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.modules.hidden.FixGui;
import com.veteran.hack.command.syntax.ChunkBuilder;
import com.veteran.hack.command.Command;

public class FixGuiCommand extends Command
{
    public FixGuiCommand() {
        super("fixgui", new ChunkBuilder().build(), new String[0]);
        this.setDescription("Allows you to disable the automatic gui positioning");
    }
    
    @Override
    public void call(final String[] args) {
        final FixGui fixGui = (FixGui)ModuleManager.getModuleByName("Hidden:FixGui");
        if (fixGui.isEnabled() && fixGui.shouldAutoEnable.getValue()) {
            fixGui.shouldAutoEnable.setValue(false);
            fixGui.disable();
            Command.sendChatMessage("[" + this.getLabel() + "] Disabled");
        }
        else {
            fixGui.shouldAutoEnable.setValue(true);
            fixGui.enable();
            Command.sendChatMessage("[" + this.getLabel() + "] Enabled");
        }
    }
}
