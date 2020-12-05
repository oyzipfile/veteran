// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.chat;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketChatMessage;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "ChadChat", category = Category.CHAT, description = "Modifies your chat messages")
public class ChadChat extends Module
{
    private Setting<Boolean> commands;
    private final String KAMI_SUFFIX = " \u23d0 \u0561\u028a\u0280\u0586\u0236\u0262\u0585\u0256 \u23d0 \u3010\uff32\uff10\uff10\uff34\u3011 \u23d0 \u02b3\u1d58\u02e2\u02b0\u1d49\u02b3\u02b0\u1d43\u1d9c\u1d4f  \u23d0 \u1d0b\u1d07\u1d07\u1d0d\u028f.\u1d04\u1d04\u30c4 \u23d0 \u166d\uff4f\u1587\uff0d\u1455\u14aa\uff49\u4e47\u144e\u3112 \u23d0 \u0e23\u0e4f\u0e22\u05e7\u0452\u0e04\u03c2\u043a \u23d0 \u1575\u157c\u15bb.\u1462\u1462  \u23d0 \u1d1b\u0280\u026a\u1d18\u029f\ua731\u02e2\u026a\u02e3 \u23d0 \u0274\u1d1c\u1d1b\u0262\u1d0f\u1d05.\u1d04\u1d04 \u0fc9 \u23d0  ";
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    
    public ChadChat() {
        this.commands = this.register(Settings.b("Commands", false));
        String s;
        String s2;
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketChatMessage) {
                s = ((CPacketChatMessage)event.getPacket()).getMessage();
                if (!s.startsWith("/") || this.commands.getValue()) {
                    s2 = s + " \u23d0 \u0561\u028a\u0280\u0586\u0236\u0262\u0585\u0256 \u23d0 \u3010\uff32\uff10\uff10\uff34\u3011 \u23d0 \u02b3\u1d58\u02e2\u02b0\u1d49\u02b3\u02b0\u1d43\u1d9c\u1d4f  \u23d0 \u1d0b\u1d07\u1d07\u1d0d\u028f.\u1d04\u1d04\u30c4 \u23d0 \u166d\uff4f\u1587\uff0d\u1455\u14aa\uff49\u4e47\u144e\u3112 \u23d0 \u0e23\u0e4f\u0e22\u05e7\u0452\u0e04\u03c2\u043a \u23d0 \u1575\u157c\u15bb.\u1462\u1462  \u23d0 \u1d1b\u0280\u026a\u1d18\u029f\ua731\u02e2\u026a\u02e3 \u23d0 \u0274\u1d1c\u1d1b\u0262\u1d0f\u1d05.\u1d04\u1d04 \u0fc9 \u23d0  ";
                    if (s2.length() >= 256) {
                        s2 = s2.substring(0, 256);
                    }
                    ((CPacketChatMessage)event.getPacket()).message = s2;
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
