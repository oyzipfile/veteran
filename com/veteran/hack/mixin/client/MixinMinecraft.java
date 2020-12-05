// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import com.veteran.hack.BaseMod;
import com.veteran.hack.event.events.GuiScreenEvent;
import com.veteran.hack.util.Wrapper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Minecraft.class })
public class MixinMinecraft
{
    @Shadow
    WorldClient world;
    @Shadow
    EntityPlayerSP player;
    @Shadow
    GuiScreen currentScreen;
    @Shadow
    GameSettings gameSettings;
    @Shadow
    GuiIngame ingameGUI;
    @Shadow
    boolean skipRenderWorld;
    @Shadow
    SoundHandler soundHandler;
    
    @Inject(method = { "displayGuiScreen" }, at = { @At("HEAD") }, cancellable = true)
    public void displayGuiScreen(GuiScreen guiScreenIn, final CallbackInfo info) {
        final GuiScreenEvent.Closed screenEvent = new GuiScreenEvent.Closed(Wrapper.getMinecraft().currentScreen);
        BaseMod.EVENT_BUS.post(screenEvent);
        final GuiScreenEvent.Displayed screenEvent2 = new GuiScreenEvent.Displayed(guiScreenIn);
        BaseMod.EVENT_BUS.post(screenEvent2);
        guiScreenIn = screenEvent2.getScreen();
        if (guiScreenIn == null && this.world == null) {
            guiScreenIn = (GuiScreen)new GuiMainMenu();
        }
        else if (guiScreenIn == null && this.player.getHealth() <= 0.0f) {
            guiScreenIn = (GuiScreen)new GuiGameOver((ITextComponent)null);
        }
        final GuiScreen old = this.currentScreen;
        final GuiOpenEvent event = new GuiOpenEvent(guiScreenIn);
        if (MinecraftForge.EVENT_BUS.post((Event)event)) {
            return;
        }
        guiScreenIn = event.getGui();
        if (old != null && guiScreenIn != old) {
            old.onGuiClosed();
        }
        if (guiScreenIn instanceof GuiMainMenu || guiScreenIn instanceof GuiMultiplayer) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages(true);
        }
        if ((this.currentScreen = guiScreenIn) != null) {
            Minecraft.getMinecraft().setIngameNotInFocus();
            KeyBinding.unPressAllKeys();
            while (Mouse.next()) {}
            while (Keyboard.next()) {}
            final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            final int i = scaledresolution.getScaledWidth();
            final int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(Minecraft.getMinecraft(), i, j);
            this.skipRenderWorld = false;
        }
        else {
            this.soundHandler.resumeSounds();
            Minecraft.getMinecraft().setIngameFocus();
        }
        info.cancel();
    }
    
    @Redirect(method = { "run" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReport(final Minecraft minecraft, final CrashReport crashReport) {
        this.save();
    }
    
    @Inject(method = { "shutdown" }, at = { @At("HEAD") })
    public void shutdown(final CallbackInfo info) {
        this.save();
    }
    
    private void save() {
        System.out.println("Saving Veteran Config");
        BaseMod.saveConfiguration();
        System.out.println("Configuration saved.");
    }
}
