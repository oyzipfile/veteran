// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import com.veteran.hack.command.Command;
import java.util.function.Predicate;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.network.play.server.SPacketDisconnect;
import java.util.Objects;
import com.veteran.hack.util.Wrapper;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketRespawn;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "EndTeleport", category = Category.PLAYER, description = "Allows for teleportation when going through end portals")
public class EndTeleport extends Module
{
    private Setting<Boolean> confirmed;
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    
    public EndTeleport() {
        this.confirmed = this.register(Settings.b("Confirm", true));
        NetHandlerPlayClient netHandlerPlayClient;
        final SPacketDisconnect sPacketDisconnect;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketRespawn && ((SPacketRespawn)event.getPacket()).getDimensionID() == 1 && this.confirmed.getValue()) {
                netHandlerPlayClient = Objects.requireNonNull(Wrapper.getMinecraft().getConnection());
                new SPacketDisconnect((ITextComponent)new TextComponentString("Attempting teleportation exploit"));
                netHandlerPlayClient.handleDisconnect(sPacketDisconnect);
                this.disable();
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    public void onEnable() {
        if (Wrapper.getMinecraft().getCurrentServerData() == null) {
            Command.sendWarningMessage(this.getChatName() + "This module does not work in singleplayer");
            this.disable();
        }
        else if (!this.confirmed.getValue()) {
            Command.sendWarningMessage(this.getChatName() + "This module will kick you from the server! It is part of the exploit and cannot be avoided");
        }
    }
}
