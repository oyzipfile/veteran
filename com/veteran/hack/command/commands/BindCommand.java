// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.command.commands;

import com.veteran.hack.setting.builder.SettingBuilder;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.module.Module;
import com.veteran.hack.util.Wrapper;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.command.syntax.SyntaxParser;
import com.veteran.hack.command.syntax.parsers.ModuleParser;
import com.veteran.hack.command.syntax.ChunkBuilder;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.command.Command;

public class BindCommand extends Command
{
    public static Setting<Boolean> modifiersEnabled;
    
    public BindCommand() {
        super("bind", new ChunkBuilder().append("[module]|modifiers", true, new ModuleParser()).append("[key]|[on|off]", true).build(), new String[0]);
        this.setDescription("Binds a module to a key, or allows you to change modifier options");
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a module.");
            return;
        }
        final String module = args[0];
        final String rkey = args[1];
        if (module.equalsIgnoreCase("modifiers")) {
            if (rkey == null) {
                Command.sendChatMessage("Expected: on or off");
                return;
            }
            if (rkey.equalsIgnoreCase("on")) {
                BindCommand.modifiersEnabled.setValue(true);
                Command.sendChatMessage("Turned modifiers on.");
            }
            else if (rkey.equalsIgnoreCase("off")) {
                BindCommand.modifiersEnabled.setValue(false);
                Command.sendChatMessage("Turned modifiers off.");
            }
            else {
                Command.sendChatMessage("Expected: on or off");
            }
        }
        else {
            final Module m = ModuleManager.getModuleByName(module);
            if (m == null) {
                Command.sendChatMessage("Unknown module '" + module + "'!");
                return;
            }
            if (rkey == null) {
                Command.sendChatMessage(m.getName() + " is bound to &b" + m.getBindName());
                return;
            }
            int key = Wrapper.getKey(rkey);
            if (rkey.equalsIgnoreCase("none")) {
                key = -1;
            }
            if (key == 0) {
                Command.sendChatMessage("Unknown key '" + rkey + "'!");
                return;
            }
            m.getBind().setKey(key);
            Command.sendChatMessage("Bind for &b" + m.getName() + "&r set to &b" + rkey.toUpperCase());
        }
    }
    
    static {
        BindCommand.modifiersEnabled = SettingBuilder.register(Settings.b("modifiersEnabled", false), "binds");
    }
}
