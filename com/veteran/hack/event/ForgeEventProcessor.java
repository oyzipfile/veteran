// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.event;

import com.veteran.hack.gui.rgui.component.Component;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraft.client.gui.GuiChat;
import com.veteran.hack.command.Command;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import com.veteran.hack.module.modules.render.BossStack;
import com.veteran.hack.util.VetHackTessellator;
import com.veteran.hack.gui.UIRenderer;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import com.veteran.hack.gui.rgui.component.container.Container;
import com.veteran.hack.module.ModuleManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.IInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import com.veteran.hack.util.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import com.veteran.hack.command.commands.PeekCommand;
import com.veteran.hack.gui.kami.KamiGUI;
import com.veteran.hack.gui.rgui.component.container.use.Frame;
import com.veteran.hack.event.events.DisplaySizeChangedEvent;
import com.veteran.hack.BaseMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ForgeEventProcessor
{
    private int displayWidth;
    private int displayHeight;
    
    @SubscribeEvent
    public void onUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (Minecraft.getMinecraft().displayWidth != this.displayWidth || Minecraft.getMinecraft().displayHeight != this.displayHeight) {
            BaseMod.EVENT_BUS.post(new DisplaySizeChangedEvent());
            this.displayWidth = Minecraft.getMinecraft().displayWidth;
            this.displayHeight = Minecraft.getMinecraft().displayHeight;
            BaseMod.getInstance().getGuiManager().getChildren().stream().filter(component -> component instanceof Frame).forEach(component -> KamiGUI.dock(component));
        }
        if (PeekCommand.sb != null) {
            final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            final int i = scaledresolution.getScaledWidth();
            final int j = scaledresolution.getScaledHeight();
            final GuiShulkerBox gui = new GuiShulkerBox(Wrapper.getPlayer().inventory, (IInventory)PeekCommand.sb);
            gui.setWorldAndResolution(Wrapper.getMinecraft(), i, j);
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)gui);
            PeekCommand.sb = null;
        }
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (Wrapper.getPlayer() == null) {
            return;
        }
        ModuleManager.onUpdate();
        BaseMod.getInstance().getGuiManager().callTick(BaseMod.getInstance().getGuiManager());
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        ModuleManager.onWorldRender(event);
    }
    
    @SubscribeEvent
    public void onRenderPre(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && ModuleManager.isModuleEnabled("BossStack")) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.isCanceled()) {
            return;
        }
        RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.EXPERIENCE;
        if (!Wrapper.getPlayer().isCreative() && Wrapper.getPlayer().getRidingEntity() instanceof AbstractHorse) {
            target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
        }
        if (event.getType() == target) {
            ModuleManager.onRender();
            GL11.glPushMatrix();
            UIRenderer.renderAndUpdateFrames();
            GL11.glPopMatrix();
            VetHackTessellator.releaseGL();
        }
        else if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && ModuleManager.isModuleEnabled("BossStack")) {
            BossStack.render(event);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) {
            return;
        }
        if (ModuleManager.isModuleEnabled("PrefixChat") && ("" + Keyboard.getEventCharacter()).equalsIgnoreCase(Command.getCommandPrefix()) && !Minecraft.getMinecraft().player.isSneaking()) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiChat(Command.getCommandPrefix()));
        }
        else {
            ModuleManager.onBind(Keyboard.getEventKey());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(final ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                Wrapper.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                if (event.getMessage().length() > 1) {
                    BaseMod.getInstance().commandManager.callCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                }
                else {
                    Command.sendChatMessage("Please enter a command.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Command.sendChatMessage("Error occured while running command! (" + e.getMessage() + ")");
            }
            event.setMessage("");
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDrawn(final RenderPlayerEvent.Pre event) {
        BaseMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDrawn(final RenderPlayerEvent.Post event) {
        BaseMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onChunkLoaded(final ChunkEvent.Load event) {
        BaseMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onEventMouse(final InputEvent.MouseInputEvent event) {
        BaseMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onChunkLoaded(final ChunkEvent.Unload event) {
        BaseMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onInputUpdate(final InputUpdateEvent event) {
        BaseMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onLivingEntityUseItemEventTick(final LivingEntityUseItemEvent.Start entityUseItemEvent) {
        BaseMod.EVENT_BUS.post(entityUseItemEvent);
    }
    
    @SubscribeEvent
    public void onLivingDamageEvent(final LivingDamageEvent event) {
        BaseMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onEntityJoinWorldEvent(final EntityJoinWorldEvent entityJoinWorldEvent) {
        BaseMod.EVENT_BUS.post(entityJoinWorldEvent);
    }
    
    @SubscribeEvent
    public void onPlayerPush(final PlayerSPPushOutOfBlocksEvent event) {
        BaseMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        BaseMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent entityEvent) {
        BaseMod.EVENT_BUS.post(entityEvent);
    }
    
    @SubscribeEvent
    public void onRenderBlockOverlay(final RenderBlockOverlayEvent event) {
        BaseMod.EVENT_BUS.post(event);
    }
}
