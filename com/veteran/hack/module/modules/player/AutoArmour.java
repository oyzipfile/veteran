// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import com.veteran.hack.module.Module;

@Info(name = "AutoArmour", category = Category.PLAYER, description = "Automatically equips armour")
public class AutoArmour extends Module
{
    @Override
    public void onUpdate() {
        if (AutoArmour.mc.player.ticksExisted % 2 == 0) {
            return;
        }
        if (AutoArmour.mc.currentScreen instanceof GuiContainer && !(AutoArmour.mc.currentScreen instanceof InventoryEffectRenderer)) {
            return;
        }
        final int[] bestArmorSlots = new int[4];
        final int[] bestArmorValues = new int[4];
        for (int armorType = 0; armorType < 4; ++armorType) {
            final ItemStack oldArmor = AutoArmour.mc.player.inventory.armorItemInSlot(armorType);
            if (oldArmor != null && oldArmor.getItem() instanceof ItemArmor) {
                bestArmorValues[armorType] = ((ItemArmor)oldArmor.getItem()).damageReduceAmount;
            }
            bestArmorSlots[armorType] = -1;
        }
        for (int slot = 0; slot < 36; ++slot) {
            final ItemStack stack = AutoArmour.mc.player.inventory.getStackInSlot(slot);
            if (stack.getCount() <= 1) {
                if (stack != null) {
                    if (stack.getItem() instanceof ItemArmor) {
                        final ItemArmor armor = (ItemArmor)stack.getItem();
                        final int armorType2 = armor.armorType.ordinal() - 2;
                        if (armorType2 != 2 || !AutoArmour.mc.player.inventory.armorItemInSlot(armorType2).getItem().equals(Items.ELYTRA)) {
                            final int armorValue = armor.damageReduceAmount;
                            if (armorValue > bestArmorValues[armorType2]) {
                                bestArmorSlots[armorType2] = slot;
                                bestArmorValues[armorType2] = armorValue;
                            }
                        }
                    }
                }
            }
        }
        for (int armorType = 0; armorType < 4; ++armorType) {
            int slot2 = bestArmorSlots[armorType];
            if (slot2 != -1) {
                final ItemStack oldArmor2 = AutoArmour.mc.player.inventory.armorItemInSlot(armorType);
                if (oldArmor2 == null || oldArmor2 != ItemStack.EMPTY || AutoArmour.mc.player.inventory.getFirstEmptyStack() != -1) {
                    if (slot2 < 9) {
                        slot2 += 36;
                    }
                    AutoArmour.mc.playerController.windowClick(0, 8 - armorType, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmour.mc.player);
                    AutoArmour.mc.playerController.windowClick(0, slot2, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmour.mc.player);
                    break;
                }
            }
        }
    }
}
