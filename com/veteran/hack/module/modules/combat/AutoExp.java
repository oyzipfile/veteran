// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import com.veteran.hack.command.Command;
import java.util.function.Predicate;
import net.minecraft.init.Items;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "AutoExp", category = Category.COMBAT, description = "Automatically mends armour")
public class AutoExp extends Module
{
    private Setting<Boolean> autoThrow;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> autoDisable;
    private int initHotbarSlot;
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener;
    boolean hasClicked;
    
    public AutoExp() {
        this.autoThrow = this.register(Settings.b("Auto Throw", true));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.autoDisable = this.register(Settings.booleanBuilder("Auto Disable").withValue(true).withVisibility(o -> this.autoSwitch.getValue()).build());
        this.initHotbarSlot = -1;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (AutoExp.mc.player != null && AutoExp.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
                AutoExp.mc.rightClickDelayTimer = 0;
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.hasClicked = false;
    }
    
    @Override
    protected void onEnable() {
        this.hasClicked = false;
        if (AutoExp.mc.player == null) {
            return;
        }
        if (this.autoSwitch.getValue()) {
            this.initHotbarSlot = AutoExp.mc.player.inventory.currentItem;
        }
    }
    
    @Override
    protected void onDisable() {
        if (AutoExp.mc.player == null) {
            return;
        }
        if (this.autoSwitch.getValue() && this.initHotbarSlot != -1 && this.initHotbarSlot != AutoExp.mc.player.inventory.currentItem) {
            AutoExp.mc.player.inventory.currentItem = this.initHotbarSlot;
        }
    }
    
    @Override
    public void onUpdate() {
        if (AutoExp.mc.player == null) {
            return;
        }
        if (this.autoSwitch.getValue() && AutoExp.mc.player.getHeldItemMainhand().getItem() != Items.EXPERIENCE_BOTTLE) {
            final int xpSlot = this.findXpPots();
            if (xpSlot == -1) {
                if (this.autoDisable.getValue()) {
                    Command.sendWarningMessage(this.getChatName() + " No XP in hotbar, disabling");
                    this.disable();
                }
                return;
            }
            AutoExp.mc.player.inventory.currentItem = xpSlot;
        }
        if (this.autoThrow.getValue() && AutoExp.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            AutoExp.mc.rightClickMouse();
        }
    }
    
    private int findXpPots() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (AutoExp.mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
}
