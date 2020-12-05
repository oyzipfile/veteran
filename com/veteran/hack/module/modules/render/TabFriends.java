// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.render;

import com.veteran.hack.command.Command;
import com.veteran.hack.util.Friends;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "TabFriends", description = "Highlights friends in the tab menu", category = Category.GUI, showOnArray = ShowOnArray.OFF)
public class TabFriends extends Module
{
    public Setting<Boolean> startupGlobal;
    public static TabFriends INSTANCE;
    
    public TabFriends() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
        TabFriends.INSTANCE = this;
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String dname = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (Friends.isFriend(dname)) {
            return String.format("%sa%s", '§', dname);
        }
        return dname;
    }
    
    public void onDisable() {
        Command.sendAutoDisableMessage(this.getName(), this.startupGlobal.getValue());
    }
}
