// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.hidden;

import net.minecraft.item.ItemSword;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.block.state.IBlockState;
import java.util.function.Predicate;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.module.Module;

@Info(name = "AutoTool", description = "Automatically switch to the best tools when mining or attacking", category = Category.HIDDEN)
public class AutoTool extends Module
{
    @EventHandler
    private Listener<PlayerInteractEvent.LeftClickBlock> leftClickListener;
    @EventHandler
    private Listener<AttackEntityEvent> attackListener;
    
    public AutoTool() {
        this.leftClickListener = new Listener<PlayerInteractEvent.LeftClickBlock>(event -> this.equipBestTool(AutoTool.mc.world.getBlockState(event.getPos())), (Predicate<PlayerInteractEvent.LeftClickBlock>[])new Predicate[0]);
        this.attackListener = new Listener<AttackEntityEvent>(event -> equipBestWeapon(), (Predicate<AttackEntityEvent>[])new Predicate[0]);
    }
    
    private void equipBestTool(final IBlockState blockState) {
        int bestSlot = -1;
        double max = 0.0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoTool.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty) {
                float speed = stack.getDestroySpeed(blockState);
                if (speed > 1.0f) {
                    final int eff;
                    speed += (float)(((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0) ? (Math.pow(eff, 2.0) + 1.0) : 0.0);
                    if (speed > max) {
                        max = speed;
                        bestSlot = i;
                    }
                }
            }
        }
        if (bestSlot != -1) {
            equip(bestSlot);
        }
    }
    
    public static void equipBestWeapon() {
        int bestSlot = -1;
        double maxDamage = 0.0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoTool.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty) {
                if (stack.getItem() instanceof ItemTool) {
                    final double damage = ((ItemTool)stack.getItem()).attackDamage + (double)EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
                    if (damage > maxDamage) {
                        maxDamage = damage;
                        bestSlot = i;
                    }
                }
                else if (stack.getItem() instanceof ItemSword) {
                    final double damage = ((ItemSword)stack.getItem()).getAttackDamage() + (double)EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
                    if (damage > maxDamage) {
                        maxDamage = damage;
                        bestSlot = i;
                    }
                }
            }
        }
        if (bestSlot != -1) {
            equip(bestSlot);
        }
    }
    
    private static void equip(final int slot) {
        AutoTool.mc.player.inventory.currentItem = slot;
        AutoTool.mc.playerController.syncCurrentPlayItem();
    }
}
