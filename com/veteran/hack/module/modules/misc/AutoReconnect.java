// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.misc;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiDisconnected;
import java.util.function.Predicate;
import net.minecraft.client.multiplayer.GuiConnecting;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.GuiScreenEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.multiplayer.ServerData;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "AutoReconnect", description = "Automatically reconnects after being disconnected", category = Category.MISC, alwaysListening = true, showOnArray = ShowOnArray.OFF)
public class AutoReconnect extends Module
{
    private Setting<Integer> seconds;
    private static ServerData cServer;
    @EventHandler
    public Listener<GuiScreenEvent.Closed> closedListener;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> displayedListener;
    
    public AutoReconnect() {
        this.seconds = this.register((Setting<Integer>)Settings.integerBuilder("Seconds").withValue(5).withMinimum(0).build());
        this.closedListener = new Listener<GuiScreenEvent.Closed>(event -> {
            if (event.getScreen() instanceof GuiConnecting) {
                AutoReconnect.cServer = AutoReconnect.mc.currentServerData;
            }
            return;
        }, (Predicate<GuiScreenEvent.Closed>[])new Predicate[0]);
        this.displayedListener = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (this.isEnabled() && event.getScreen() instanceof GuiDisconnected && (AutoReconnect.cServer != null || AutoReconnect.mc.currentServerData != null)) {
                event.setScreen((GuiScreen)new KamiGuiDisconnected((GuiDisconnected)event.getScreen()));
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
    
    private class KamiGuiDisconnected extends GuiDisconnected
    {
        int millis;
        long cTime;
        
        public KamiGuiDisconnected(final GuiDisconnected disconnected) {
            super(disconnected.parentScreen, disconnected.reason, disconnected.message);
            this.millis = AutoReconnect.this.seconds.getValue() * 1000;
            this.cTime = System.currentTimeMillis();
        }
        
        public void updateScreen() {
            if (this.millis <= 0) {
                this.mc.displayGuiScreen((GuiScreen)new GuiConnecting(this.parentScreen, this.mc, (AutoReconnect.cServer == null) ? this.mc.currentServerData : AutoReconnect.cServer));
            }
        }
        
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            final long a = System.currentTimeMillis();
            this.millis -= (int)(a - this.cTime);
            this.cTime = a;
            final String s = "Reconnecting in " + Math.max(0.0, Math.floor(this.millis / 100.0) / 10.0) + "s";
            this.fontRenderer.drawString(s, (float)(this.width / 2 - this.fontRenderer.getStringWidth(s) / 2), (float)(this.height - 16), 16777215, true);
        }
    }
}
