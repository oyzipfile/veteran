// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.chat;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.security.MessageDigest;
import java.security.Key;
import java.util.function.Predicate;
import com.veteran.hack.util.Wrapper;
import net.minecraft.network.play.server.SPacketChat;
import com.veteran.hack.setting.Settings;
import javax.crypto.spec.SecretKeySpec;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "AutoTPA", description = "Automatically decline or accept TPA requests", category = Category.HIDDEN)
public class AutoTPA extends Module
{
    private Setting<mode> mod;
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    private static SecretKeySpec secretKey;
    private static byte[] key;
    
    public AutoTPA() {
        this.mod = this.register(Settings.e("Response", mode.DENY));
        SPacketChat packet;
        final Object o;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketChat) {
                packet = (SPacketChat)event.getPacket();
                if (((SPacketChat)o).getChatComponent().getUnformattedText().contains(" has requested to teleport to you.")) {
                    switch (this.mod.getValue()) {
                        case ACCEPT: {
                            Wrapper.getPlayer().sendChatMessage("/tpaccept");
                            break;
                        }
                        case DENY: {
                            Wrapper.getPlayer().sendChatMessage("/tpdeny");
                            break;
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    public static Key setKey(final String myKey) {
        MessageDigest sha = null;
        try {
            AutoTPA.key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            AutoTPA.key = sha.digest(AutoTPA.key);
            AutoTPA.key = Arrays.copyOf(AutoTPA.key, 16);
            return new SecretKeySpec(AutoTPA.key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new SecretKeySpec(AutoTPA.key, myKey);
        }
        catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return new SecretKeySpec(AutoTPA.key, myKey);
        }
    }
    
    public enum mode
    {
        ACCEPT, 
        DENY;
    }
}
