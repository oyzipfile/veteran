// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import com.veteran.hack.module.modules.gui.InfoOverlay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemEnderEye;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemBow;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import java.util.function.Predicate;
import net.minecraft.init.Items;
import com.veteran.hack.module.ModuleManager;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.item.Item;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "OffhandGap", category = Category.COMBAT, description = "Holds a God apple when right clicking your sword!")
public class OffhandGap extends Module
{
    private Setting<Double> disableHealth;
    private Setting<Boolean> eatWhileAttacking;
    private Setting<Boolean> swordOrAxeOnly;
    private Setting<Boolean> preferBlocks;
    private Setting<Boolean> holeCheck;
    int gaps;
    boolean BetterOffhandWasEnabled;
    boolean cancelled;
    Item usedItem;
    Item toUseItem;
    int holeBlocks;
    Autocrystal crystalAura;
    @EventHandler
    private Listener<PacketEvent.Send> sendListener;
    
    public OffhandGap() {
        this.disableHealth = this.register((Setting<Double>)Settings.doubleBuilder("Disable Health").withMinimum(0.0).withValue(4.0).withMaximum(20.0).build());
        this.eatWhileAttacking = this.register(Settings.b("Eat While Attacking", false));
        this.swordOrAxeOnly = this.register(Settings.b("Sword or Axe Only", true));
        this.preferBlocks = this.register(Settings.booleanBuilder("Prefer Placing Blocks").withValue(false).withVisibility(v -> !this.swordOrAxeOnly.getValue()).build());
        this.holeCheck = this.register(Settings.b("Hole Check", false));
        this.gaps = -1;
        this.BetterOffhandWasEnabled = false;
        this.cancelled = false;
        this.sendListener = new Listener<PacketEvent.Send>(e -> {
            if (e.getPacket() instanceof CPacketPlayerTryUseItem) {
                if (this.cancelled || (this.holeCheck.getValue() && this.holeBlocks != 5.0f)) {
                    this.disableGaps();
                    return;
                }
                else if (OffhandGap.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || OffhandGap.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe || this.passItemCheck()) {
                    if (ModuleManager.isModuleEnabled("BetterOffhand")) {
                        this.BetterOffhandWasEnabled = true;
                        ModuleManager.getModuleByName("BetterOffhand").disable();
                    }
                    if (!this.eatWhileAttacking.getValue()) {
                        this.usedItem = OffhandGap.mc.player.getHeldItemMainhand().getItem();
                    }
                    this.enableGaps(this.gaps);
                }
            }
            try {
                if (!OffhandGap.mc.gameSettings.keyBindUseItem.isKeyDown() && OffhandGap.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
                    this.disableGaps();
                }
                else if (this.usedItem != OffhandGap.mc.player.getHeldItemMainhand().getItem() && OffhandGap.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
                    if (!this.eatWhileAttacking.getValue()) {
                        this.usedItem = OffhandGap.mc.player.getHeldItemMainhand().getItem();
                        this.disableGaps();
                    }
                }
                else if (OffhandGap.mc.player.getHealth() + OffhandGap.mc.player.getAbsorptionAmount() <= this.disableHealth.getValue()) {
                    this.disableGaps();
                }
            }
            catch (NullPointerException ex) {}
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        this.holeBlocks = 0;
        if (OffhandGap.mc.world == null) {
            return;
        }
        if (this.holeCheck.getValue()) {
            final Vec3d[] array;
            final Vec3d[] holeOffset = array = new Vec3d[] { OffhandGap.mc.player.getPositionVector().add(1.0, 0.0, 0.0), OffhandGap.mc.player.getPositionVector().add(-1.0, 0.0, 0.0), OffhandGap.mc.player.getPositionVector().add(0.0, 0.0, 1.0), OffhandGap.mc.player.getPositionVector().add(0.0, 0.0, -1.0), OffhandGap.mc.player.getPositionVector().add(0.0, -1.0, 0.0) };
            for (final Vec3d v : array) {
                final BlockPos offset = new BlockPos(v.x, v.y, v.z);
                if (OffhandGap.mc.world.getBlockState(offset).getBlock() == Blocks.OBSIDIAN || OffhandGap.mc.world.getBlockState(offset).getBlock() == Blocks.BEDROCK) {
                    ++this.holeBlocks;
                }
            }
        }
        if (OffhandGap.mc.player == null) {
            return;
        }
        this.cancelled = (OffhandGap.mc.player.getHealth() + OffhandGap.mc.player.getAbsorptionAmount() <= this.disableHealth.getValue());
        if (this.cancelled) {
            this.disableGaps();
            return;
        }
        this.toUseItem = Items.GOLDEN_APPLE;
        if (OffhandGap.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {
            for (int i = 0; i < 45; ++i) {
                if (OffhandGap.mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                    this.gaps = i;
                    break;
                }
            }
        }
    }
    
    private boolean passItemCheck() {
        if (this.swordOrAxeOnly.getValue()) {
            return false;
        }
        final Item item = OffhandGap.mc.player.getHeldItemMainhand().getItem();
        return !(item instanceof ItemBow) && !(item instanceof ItemSnowball) && !(item instanceof ItemEgg) && !(item instanceof ItemPotion) && !(item instanceof ItemEnderEye) && !(item instanceof ItemEnderPearl) && !(item instanceof ItemFood) && !(item instanceof ItemShield) && !(item instanceof ItemFlintAndSteel) && !(item instanceof ItemFishingRod) && !(item instanceof ItemArmor) && !(item instanceof ItemExpBottle) && (!this.preferBlocks.getValue() || !(item instanceof ItemBlock));
    }
    
    private void disableGaps() {
        if (this.BetterOffhandWasEnabled) {
            this.moveGapsToInventory(this.gaps);
            ModuleManager.getModuleByName("BetterOffhand").enable();
            this.BetterOffhandWasEnabled = false;
        }
    }
    
    private void enableGaps(final int slot) {
        if (OffhandGap.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {
            OffhandGap.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
            OffhandGap.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
        }
    }
    
    private void moveGapsToInventory(final int slot) {
        if (OffhandGap.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            OffhandGap.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
            OffhandGap.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
        }
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(InfoOverlay.getItems(Items.GOLDEN_APPLE));
    }
}
