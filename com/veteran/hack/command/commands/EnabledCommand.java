// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import java.util.List;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;
import java.util.Collection;
import com.veteran.hack.module.Module;
import java.util.ArrayList;
import com.veteran.hack.module.ModuleManager;
import java.util.concurrent.atomic.AtomicReference;
import com.veteran.hack.command.syntax.SyntaxChunk;
import com.veteran.hack.command.Command;

public class EnabledCommand extends Command
{
    public EnabledCommand() {
        super("enabled", null, new String[0]);
        this.setDescription("Prints enabled modules");
    }
    
    @Override
    public void call(final String[] args) {
        final AtomicReference<String> enabled = new AtomicReference<String>("");
        final List<Module> mods = new ArrayList<Module>(ModuleManager.getModules());
        final AtomicReference<String> obj;
        mods.forEach(module -> {
            if (module.isEnabled()) {
                obj.set(obj + module.getName() + ", ");
            }
            return;
        });
        enabled.set(StringUtils.chop(StringUtils.chop(String.valueOf(enabled))));
        Command.sendChatMessage("Enabled modules: \n" + TextFormatting.GRAY + enabled);
    }
}
