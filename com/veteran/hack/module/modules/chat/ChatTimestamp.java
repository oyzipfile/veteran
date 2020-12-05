// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.chat;

import com.veteran.hack.command.Command;
import net.minecraft.util.text.TextFormatting;
import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketChat;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.util.TimeUtil;
import com.veteran.hack.util.ColourTextFormatting;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "ChatTimestamp", category = Category.CHAT, description = "Shows the time a message was sent beside the message", showOnArray = ShowOnArray.OFF)
public class ChatTimestamp extends Module
{
    private Setting<ColourTextFormatting.ColourCode> firstColour;
    private Setting<ColourTextFormatting.ColourCode> secondColour;
    private Setting<TimeUtil.TimeType> timeTypeSetting;
    private Setting<TimeUtil.TimeUnit> timeUnitSetting;
    private Setting<Boolean> doLocale;
    @EventHandler
    public Listener<PacketEvent.Receive> listener;
    
    public ChatTimestamp() {
        this.firstColour = this.register(Settings.e("First Colour", ColourTextFormatting.ColourCode.GRAY));
        this.secondColour = this.register(Settings.e("Second Colour", ColourTextFormatting.ColourCode.WHITE));
        this.timeTypeSetting = this.register(Settings.e("Time Format", TimeUtil.TimeType.HHMM));
        this.timeUnitSetting = this.register(Settings.e("Time Unit", TimeUtil.TimeUnit.H12));
        this.doLocale = this.register(Settings.b("Show AMPM", true));
        SPacketChat sPacketChat;
        this.listener = new Listener<PacketEvent.Receive>(event -> {
            if (ChatTimestamp.mc.player != null && !this.isDisabled()) {
                if (!(!(event.getPacket() instanceof SPacketChat))) {
                    sPacketChat = (SPacketChat)event.getPacket();
                    if (this.addTime(sPacketChat.getChatComponent().getUnformattedText())) {
                        event.cancel();
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    private boolean addTime(final String message) {
        Command.sendRawChatMessage("<" + TimeUtil.getFinalTime(this.setToText(this.secondColour.getValue()), this.setToText(this.firstColour.getValue()), this.timeUnitSetting.getValue(), this.timeTypeSetting.getValue(), this.doLocale.getValue()) + TextFormatting.RESET + "> " + message);
        return true;
    }
    
    private TextFormatting setToText(final ColourTextFormatting.ColourCode colourCode) {
        return ColourTextFormatting.toTextMap.get(colourCode);
    }
}
