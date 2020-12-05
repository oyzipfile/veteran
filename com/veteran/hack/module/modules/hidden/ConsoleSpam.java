// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.hidden;

import com.veteran.hack.command.Command;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import java.util.function.Predicate;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.network.play.client.CPacketUpdateSign;
import com.veteran.hack.util.Wrapper;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.module.Module;

@Info(name = "ConsoleSpam", description = "Spams Spigot consoles by sending invalid UpdateSign packets", category = Category.HIDDEN)
public class ConsoleSpam extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    
    public ConsoleSpam() {
        BlockPos location;
        NetHandlerPlayClient connection;
        final CPacketUpdateSign cPacketUpdateSign;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                location = ((CPacketPlayerTryUseItemOnBlock)event.getPacket()).getPos();
                connection = Wrapper.getPlayer().connection;
                new CPacketUpdateSign(location, new TileEntitySign().signText);
                connection.sendPacket((Packet)cPacketUpdateSign);
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onEnable() {
        Command.sendChatMessage(this.getChatName() + " Every time you right click a sign, a warning will appear in console.");
        Command.sendChatMessage(this.getChatName() + " Use an autoclicker to automate this process.");
    }
}
