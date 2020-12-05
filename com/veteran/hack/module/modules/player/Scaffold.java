// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import com.veteran.hack.util.BlockInteractionHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import com.veteran.hack.util.Wrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import com.veteran.hack.util.EntityUtil;
import com.veteran.hack.module.ModuleManager;
import net.minecraft.util.MovementInput;
import java.util.function.Predicate;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "Scaffold", category = Category.MOVEMENT, description = "Places blocks under you")
public class Scaffold extends Module
{
    private Setting<Boolean> placeBlocks;
    private Setting<Mode> modeSetting;
    private Setting<Boolean> randomDelay;
    private Setting<Integer> delayRange;
    private Setting<Integer> ticks;
    public static boolean shouldSlow;
    private static Scaffold INSTANCE;
    @EventHandler
    private Listener<InputUpdateEvent> eventListener;
    
    public Scaffold() {
        this.placeBlocks = this.register(Settings.b("Place Blocks", true));
        this.modeSetting = this.register((Setting<Mode>)Settings.enumBuilder(Mode.class).withName("Mode").withValue(Mode.LEGIT).build());
        this.randomDelay = this.register(Settings.booleanBuilder("Random Delay").withValue(false).withVisibility(v -> this.modeSetting.getValue().equals(Mode.LEGIT)).build());
        this.delayRange = this.register(Settings.integerBuilder("Delay Range").withMinimum(0).withValue(6).withMaximum(10).withVisibility(v -> this.modeSetting.getValue().equals(Mode.LEGIT) && this.randomDelay.getValue()).build());
        this.ticks = this.register(Settings.integerBuilder("Ticks").withMinimum(0).withMaximum(60).withValue(2).withVisibility(v -> !this.modeSetting.getValue().equals(Mode.LEGIT)).build());
        final MovementInput movementInput;
        final MovementInput movementInput2;
        final MovementInput movementInput3;
        final MovementInput movementInput4;
        this.eventListener = new Listener<InputUpdateEvent>(event -> {
            if (this.modeSetting.getValue().equals(Mode.LEGIT) && Scaffold.shouldSlow) {
                if (this.randomDelay.getValue()) {
                    event.getMovementInput();
                    movementInput.moveStrafe *= 0.2f + this.getRandomInRange();
                    event.getMovementInput();
                    movementInput2.moveForward *= 0.2f + this.getRandomInRange();
                }
                else {
                    event.getMovementInput();
                    movementInput3.moveStrafe *= 0.2f;
                    event.getMovementInput();
                    movementInput4.moveForward *= 0.2f;
                }
            }
            return;
        }, (Predicate<InputUpdateEvent>[])new Predicate[0]);
        Scaffold.INSTANCE = this;
    }
    
    public static boolean shouldScaffold() {
        return Scaffold.INSTANCE.isEnabled();
    }
    
    @Override
    public void onUpdate() {
        if (Scaffold.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        Scaffold.shouldSlow = false;
        Vec3d vec3d = EntityUtil.getInterpolatedPos((Entity)Scaffold.mc.player, this.ticks.getValue());
        if (this.modeSetting.getValue().equals(Mode.LEGIT)) {
            vec3d = EntityUtil.getInterpolatedPos((Entity)Scaffold.mc.player, 0.0f);
        }
        final BlockPos blockPos = new BlockPos(vec3d).down();
        final BlockPos belowBlockPos = blockPos.down();
        final BlockPos legitPos = new BlockPos(EntityUtil.getInterpolatedPos((Entity)Scaffold.mc.player, 2.0f));
        if (this.modeSetting.getValue().equals(Mode.LEGIT) && Wrapper.getWorld().getBlockState(legitPos.down()).getMaterial().isReplaceable() && Scaffold.mc.player.onGround) {
            Scaffold.shouldSlow = true;
            Scaffold.mc.player.movementInput.sneak = true;
            Scaffold.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Scaffold.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
        if (!Wrapper.getWorld().getBlockState(blockPos).getMaterial().isReplaceable()) {
            return;
        }
        this.setSlotToBlocks(belowBlockPos);
        if (!BlockInteractionHelper.checkForNeighbours(blockPos)) {
            return;
        }
        if (this.placeBlocks.getValue()) {
            BlockInteractionHelper.placeBlockScaffold(blockPos);
        }
        Scaffold.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Scaffold.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        Scaffold.shouldSlow = false;
    }
    
    private float getRandomInRange() {
        return 0.11f + (float)Math.random() * (this.delayRange.getValue() / 10.0f - 0.11f);
    }
    
    private void setSlotToBlocks(final BlockPos belowBlockPos) {
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (!BlockInteractionHelper.blackList.contains(block)) {
                        if (!(block instanceof BlockContainer)) {
                            if (Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock()) {
                                if (!(((ItemBlock)stack.getItem()).getBlock() instanceof BlockFalling) || !Wrapper.getWorld().getBlockState(belowBlockPos).getMaterial().isReplaceable()) {
                                    newSlot = i;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        int oldSlot = 1;
        if (newSlot != -1) {
            oldSlot = Wrapper.getPlayer().inventory.currentItem;
            Wrapper.getPlayer().inventory.currentItem = newSlot;
        }
        Wrapper.getPlayer().inventory.currentItem = oldSlot;
    }
    
    static {
        Scaffold.shouldSlow = false;
    }
    
    private enum Mode
    {
        NEITHER, 
        LEGIT;
    }
}
