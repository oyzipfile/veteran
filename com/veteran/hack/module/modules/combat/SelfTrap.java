// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.util.NonNullList;
import com.veteran.hack.util.EntityUtil;
import com.veteran.hack.util.Friends;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import com.veteran.hack.util.BlockInteractionHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.Block;
import java.util.List;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import com.veteran.hack.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.setting.Settings;
import net.minecraft.entity.player.EntityPlayer;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "SelfTrap", category = Category.COMBAT)
public class SelfTrap extends Module
{
    private Setting<Double> smartRange;
    private Setting<Integer> blocksPerTick;
    private Setting<Integer> tickDelay;
    private Setting<Cage> cage;
    private Setting<Boolean> rotate;
    private Setting<Boolean> announceUsage;
    private Setting<Boolean> smart;
    private Setting<Boolean> disableOnPlace;
    private Setting<Boolean> disableCAOnPlace;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private int delayStep;
    private boolean isSneaking;
    private int offsetStep;
    private boolean firstRun;
    private boolean caOn;
    private boolean isSpoofingAngles;
    private static double yaw;
    private EntityPlayer closestTarget;
    
    public SelfTrap() {
        this.smartRange = this.register((Setting<Double>)Settings.doubleBuilder("Range").withMinimum(0.0).withValue(4.5).withMaximum(32.0).build());
        this.blocksPerTick = this.register((Setting<Integer>)Settings.integerBuilder("BlocksPerTick").withMinimum(1).withValue(2).withMaximum(23).build());
        this.tickDelay = this.register((Setting<Integer>)Settings.integerBuilder("TickDelay").withMinimum(0).withValue(2).withMaximum(10).build());
        this.cage = this.register(Settings.e("Cage", Cage.BLOCKOVERHEAD));
        this.rotate = this.register(Settings.b("Rotate", true));
        this.announceUsage = this.register(Settings.b("AnnounceUsage", true));
        this.smart = this.register(Settings.b("Smart", false));
        this.disableOnPlace = this.register(Settings.b("Disable On Place", false));
        this.disableCAOnPlace = this.register(Settings.b("Disable CA On Place", false));
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.delayStep = 0;
        this.isSneaking = false;
        this.offsetStep = 0;
    }
    
    @Override
    protected void onEnable() {
        if (ModuleManager.getModuleByName("Autocrystal").isEnabled()) {
            this.caOn = true;
        }
        if (SelfTrap.mc.player == null) {
            this.disable();
            return;
        }
        this.firstRun = true;
        this.playerHotbarSlot = SelfTrap.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("[SelfTrap] " + ChatFormatting.GREEN.toString() + "Enabled" + ChatFormatting.RESET.toString() + "!");
        }
    }
    
    @Override
    protected void onDisable() {
        this.caOn = false;
        this.closestTarget = null;
        if (SelfTrap.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            SelfTrap.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            SelfTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("[SelfTrap] " + ChatFormatting.RED.toString() + "Disabled" + ChatFormatting.RESET.toString() + "!");
        }
    }
    
    @Override
    public void onUpdate() {
        int holeBlocks = 0;
        final Vec3d[] array;
        final Vec3d[] holeOffset = array = new Vec3d[] { SelfTrap.mc.player.getPositionVector().add(1.0, 0.0, 0.0), SelfTrap.mc.player.getPositionVector().add(-1.0, 0.0, 0.0), SelfTrap.mc.player.getPositionVector().add(0.0, 0.0, 1.0), SelfTrap.mc.player.getPositionVector().add(0.0, 0.0, -1.0) };
        for (final Vec3d vecOffset : array) {
            final BlockPos offset = new BlockPos(vecOffset.x, vecOffset.y, vecOffset.z);
            if (SelfTrap.mc.world.getBlockState(offset).getBlock() == Blocks.OBSIDIAN || SelfTrap.mc.world.getBlockState(offset).getBlock() == Blocks.BEDROCK) {
                ++holeBlocks;
            }
        }
        if (holeBlocks != 4) {
            return;
        }
        if (this.smart.getValue()) {
            this.findClosestTarget();
        }
        if (SelfTrap.mc.player == null) {
            return;
        }
        if (!this.firstRun) {
            if (this.delayStep < this.tickDelay.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        final List<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (this.cage.getValue().equals(Cage.TRAP)) {
            Collections.addAll(placeTargets, Offsets.TRAP);
        }
        if (this.cage.getValue().equals(Cage.BLOCKOVERHEAD)) {
            if (this.getViewYaw() <= 315 && this.getViewYaw() >= 225) {
                Collections.addAll(placeTargets, Offsets.BLOCKOVERHEADFACINGNEGX);
            }
            else if ((this.getViewYaw() < 45 && this.getViewYaw() > 0) || (this.getViewYaw() > 315 && this.getViewYaw() < 360)) {
                Collections.addAll(placeTargets, Offsets.BLOCKOVERHEADFACINGPOSZ);
            }
            else if (this.getViewYaw() <= 135 && this.getViewYaw() >= 45) {
                Collections.addAll(placeTargets, Offsets.BLOCKOVERHEADFACINGPOSX);
            }
            else if (this.getViewYaw() < 225 && this.getViewYaw() > 135) {
                Collections.addAll(placeTargets, Offsets.BLOCKOVERHEADFACINGNEGZ);
            }
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= placeTargets.size()) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos((Vec3d)placeTargets.get(this.offsetStep));
            final BlockPos targetPos = new BlockPos(SelfTrap.mc.player.getPositionVector()).down().add(offsetPos.x, offsetPos.y, offsetPos.z);
            if (this.closestTarget != null && this.smart.getValue()) {
                if (this.isInRange(this.getClosestTargetPos()) && this.placeBlockInRange(targetPos)) {
                    ++blocksPlaced;
                }
            }
            else if (!this.smart.getValue() && this.placeBlockInRange(targetPos)) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                SelfTrap.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                SelfTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
        final Vec3d overHead = new Vec3d(0.0, 3.0, 0.0);
        final BlockPos blockOverHead = new BlockPos(SelfTrap.mc.player.getPositionVector()).down().add(overHead.x, overHead.y, overHead.z);
        final Block block2 = SelfTrap.mc.world.getBlockState(blockOverHead).getBlock();
        if (!(block2 instanceof BlockAir) && !(block2 instanceof BlockLiquid) && this.disableCAOnPlace.getValue() && this.caOn) {
            ModuleManager.getModuleByName("NutgodCA").enable();
        }
        if (!(block2 instanceof BlockAir) && !(block2 instanceof BlockLiquid) && this.disableOnPlace.getValue()) {
            this.disable();
        }
    }
    
    private boolean placeBlockInRange(final BlockPos pos) {
        if (this.caOn && this.disableCAOnPlace.getValue()) {
            ModuleManager.getModuleByName("NutgodCA").disable();
        }
        final Block block = SelfTrap.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : SelfTrap.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }
        final EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!BlockInteractionHelper.canBeClicked(neighbour)) {
            return false;
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = SelfTrap.mc.world.getBlockState(neighbour).getBlock();
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != obiSlot) {
            SelfTrap.mc.player.inventory.currentItem = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock)) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            SelfTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfTrap.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        SelfTrap.mc.playerController.processRightClickBlock(SelfTrap.mc.player, SelfTrap.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        SelfTrap.mc.player.swingArm(EnumHand.MAIN_HAND);
        SelfTrap.mc.rightClickDelayTimer = 0;
        return true;
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(SelfTrap.mc.player.posX), Math.floor(SelfTrap.mc.player.posY), Math.floor(SelfTrap.mc.player.posZ));
    }
    
    public BlockPos getClosestTargetPos() {
        if (this.closestTarget != null) {
            return new BlockPos(Math.floor(this.closestTarget.posX), Math.floor(this.closestTarget.posY), Math.floor(this.closestTarget.posZ));
        }
        return null;
    }
    
    public int getViewYaw() {
        return (int)Math.abs(Math.floor(Minecraft.getMinecraft().player.rotationYaw * 8.0f / 360.0f));
    }
    
    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)SelfTrap.mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == SelfTrap.mc.player) {
                continue;
            }
            if (Friends.isFriend(target.getName())) {
                continue;
            }
            if (!EntityUtil.isLiving((Entity)target)) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (this.closestTarget == null) {
                this.closestTarget = target;
            }
            else {
                if (SelfTrap.mc.player.getDistance((Entity)target) >= SelfTrap.mc.player.getDistance((Entity)this.closestTarget)) {
                    continue;
                }
                this.closestTarget = target;
            }
        }
    }
    
    private boolean isInRange(final BlockPos blockPos) {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.smartRange.getValue().floatValue(), this.smartRange.getValue().intValue(), false, true, 0).stream().collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return positions.contains((Object)blockPos);
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = SelfTrap.mc.player.inventory.getStackInSlot(i);
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
    
    private enum Cage
    {
        TRAP, 
        BLOCKOVERHEAD;
    }
    
    private static class Offsets
    {
        private static final Vec3d[] TRAP;
        private static final Vec3d[] BLOCKOVERHEADFACINGPOSX;
        private static final Vec3d[] BLOCKOVERHEADFACINGPOSZ;
        private static final Vec3d[] BLOCKOVERHEADFACINGNEGX;
        private static final Vec3d[] BLOCKOVERHEADFACINGNEGZ;
        
        static {
            TRAP = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            BLOCKOVERHEADFACINGPOSX = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0) };
            BLOCKOVERHEADFACINGPOSZ = new Vec3d[] { new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(0.0, 3.0, 0.0) };
            BLOCKOVERHEADFACINGNEGX = new Vec3d[] { new Vec3d(-1.0, 0.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0) };
            BLOCKOVERHEADFACINGNEGZ = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
        }
    }
}
