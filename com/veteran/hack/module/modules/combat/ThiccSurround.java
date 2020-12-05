// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.block.Block;
import com.veteran.hack.module.modules.player.NoBreakAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import com.veteran.hack.util.BlockInteractionHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import com.veteran.hack.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.math.Vec3d;
import com.veteran.hack.module.ModuleManager;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "ThiccSurround", category = Category.COMBAT, description = "surround but thicker")
public class ThiccSurround extends Module
{
    private Setting<Mode> mode;
    private Setting<Boolean> triggerable;
    private Setting<Boolean> disableNone;
    private Setting<Integer> timeoutTicks;
    private Setting<Integer> blocksPerTick;
    private Setting<Integer> tickDelay;
    private Setting<Boolean> rotate;
    private Setting<Boolean> infoMessage;
    private int offsetStep;
    private int delayStep;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int totalTicksRunning;
    private boolean firstRun;
    private boolean missingObiDisable;
    
    public ThiccSurround() {
        this.mode = this.register(Settings.e("Mode", Mode.THICC));
        this.triggerable = this.register(Settings.b("Triggerable", true));
        this.disableNone = this.register(Settings.b("DisableNoObby", true));
        this.timeoutTicks = this.register(Settings.integerBuilder("TimeoutTicks").withMinimum(1).withValue(40).withMaximum(100).withVisibility(b -> this.triggerable.getValue()).build());
        this.blocksPerTick = this.register((Setting<Integer>)Settings.integerBuilder("BlocksPerTick").withMinimum(1).withValue(4).withMaximum(9).build());
        this.tickDelay = this.register((Setting<Integer>)Settings.integerBuilder("TickDelay").withMinimum(0).withValue(0).withMaximum(10).build());
        this.rotate = this.register(Settings.b("Rotate", true));
        this.infoMessage = this.register(Settings.b("InfoMessage", false));
        this.offsetStep = 0;
        this.delayStep = 0;
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.isSneaking = false;
        this.totalTicksRunning = 0;
        this.missingObiDisable = false;
    }
    
    private static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            if (ThiccSurround.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(ThiccSurround.mc.world.getBlockState(neighbour), false)) {
                final IBlockState blockState = ThiccSurround.mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    return side;
                }
            }
        }
        return null;
    }
    
    @Override
    protected void onEnable() {
        if (ThiccSurround.mc.player == null) {
            this.disable();
            return;
        }
        this.firstRun = true;
        this.playerHotbarSlot = ThiccSurround.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    protected void onDisable() {
        if (ThiccSurround.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            ThiccSurround.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            ThiccSurround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ThiccSurround.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.missingObiDisable = false;
    }
    
    @Override
    public void onUpdate() {
        if (ThiccSurround.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (this.triggerable.getValue() && this.totalTicksRunning >= this.timeoutTicks.getValue()) {
            this.totalTicksRunning = 0;
            this.disable();
            return;
        }
        if (!this.firstRun) {
            if (this.delayStep < this.tickDelay.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        if (this.firstRun) {
            this.firstRun = false;
            if (this.findObiInHotbar() == -1) {
                this.missingObiDisable = true;
            }
        }
        Vec3d[] offsetPattern = new Vec3d[0];
        int maxSteps = 0;
        if (this.mode.getValue().equals(Mode.THICC)) {
            offsetPattern = Offsets.THICC;
            maxSteps = Offsets.THICC.length;
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= maxSteps) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos(offsetPattern[this.offsetStep]);
            final BlockPos targetPos = new BlockPos(ThiccSurround.mc.player.getPositionVector()).add(offsetPos.x, offsetPos.y, offsetPos.z);
            if (this.placeBlock(targetPos)) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                ThiccSurround.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                ThiccSurround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ThiccSurround.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
        ++this.totalTicksRunning;
        if (this.missingObiDisable && this.disableNone.getValue()) {
            this.missingObiDisable = false;
            if (this.infoMessage.getValue()) {
                Command.sendChatMessage(this.getChatName() + " " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + ", Obsidian missing!");
            }
            this.disable();
        }
    }
    
    private boolean placeBlock(final BlockPos pos) {
        final Block block = ThiccSurround.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : ThiccSurround.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }
        final EnumFacing side = getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!BlockInteractionHelper.canBeClicked(neighbour)) {
            return false;
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = ThiccSurround.mc.world.getBlockState(neighbour).getBlock();
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.missingObiDisable = true;
            return false;
        }
        if (this.lastHotbarSlot != obiSlot) {
            ThiccSurround.mc.player.inventory.currentItem = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock)) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            ThiccSurround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ThiccSurround.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        ThiccSurround.mc.playerController.processRightClickBlock(ThiccSurround.mc.player, ThiccSurround.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        ThiccSurround.mc.player.swingArm(EnumHand.MAIN_HAND);
        ThiccSurround.mc.rightClickDelayTimer = 4;
        if (ModuleManager.getModuleByName("NoBreakAnimation").isEnabled()) {
            ((NoBreakAnimation)ModuleManager.getModuleByName("NoBreakAnimation")).resetMining();
        }
        return true;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = ThiccSurround.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block instanceof BlockObsidian) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }
    
    private enum Mode
    {
        THICC;
    }
    
    private static class Offsets
    {
        private static final Vec3d[] THICC;
        
        static {
            THICC = new Vec3d[] { new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 0.0, 2.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, -2.0), new Vec3d(0.0, -1.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(2.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(-2.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 1.0) };
        }
    }
}
