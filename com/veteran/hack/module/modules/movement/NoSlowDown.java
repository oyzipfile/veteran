// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovementInput;
import java.util.function.Predicate;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "NoSlowDown", category = Category.HIDDEN, description = "Prevents being slowed down when using an item or going through cobwebs")
public class NoSlowDown extends Module
{
    public Setting<Boolean> soulSand;
    public Setting<Boolean> cobweb;
    private Setting<Boolean> slime;
    private Setting<Boolean> allItems;
    private Setting<Boolean> food;
    private Setting<Boolean> bow;
    private Setting<Boolean> potion;
    private Setting<Boolean> shield;
    @EventHandler
    private Listener<InputUpdateEvent> eventListener;
    
    public NoSlowDown() {
        this.soulSand = this.register(Settings.b("Soul Sand", true));
        this.cobweb = this.register(Settings.b("Cobweb", true));
        this.slime = this.register(Settings.b("Slime", true));
        this.allItems = this.register(Settings.b("All Items", false));
        this.food = this.register(Settings.booleanBuilder().withName("Food").withValue(true).withVisibility(v -> !this.allItems.getValue()).build());
        this.bow = this.register(Settings.booleanBuilder().withName("Bows").withValue(true).withVisibility(v -> !this.allItems.getValue()).build());
        this.potion = this.register(Settings.booleanBuilder().withName("Potions").withValue(true).withVisibility(v -> !this.allItems.getValue()).build());
        this.shield = this.register(Settings.booleanBuilder().withName("Shield").withValue(true).withVisibility(v -> !this.allItems.getValue()).build());
        final MovementInput movementInput;
        final MovementInput movementInput2;
        this.eventListener = new Listener<InputUpdateEvent>(event -> {
            if (this.passItemCheck(NoSlowDown.mc.player.getActiveItemStack().getItem()) && NoSlowDown.mc.player.isHandActive() && !NoSlowDown.mc.player.isRiding()) {
                event.getMovementInput();
                movementInput.moveStrafe *= 5.0f;
                event.getMovementInput();
                movementInput2.moveForward *= 5.0f;
            }
        }, (Predicate<InputUpdateEvent>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.slime.getValue()) {
            Blocks.SLIME_BLOCK.slipperiness = 0.4945f;
        }
        else {
            Blocks.SLIME_BLOCK.slipperiness = 0.8f;
        }
    }
    
    public void onDisable() {
        Blocks.SLIME_BLOCK.slipperiness = 0.8f;
    }
    
    private boolean passItemCheck(final Item item) {
        return this.allItems.getValue() || (this.food.getValue() && item instanceof ItemFood) || (this.bow.getValue() && item instanceof ItemBow) || (this.potion.getValue() && item instanceof ItemPotion) || (this.shield.getValue() && item instanceof ItemShield);
    }
}
