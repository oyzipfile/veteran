// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.util.NonNullList;
import com.veteran.hack.util.Friends;
import com.veteran.hack.util.EntityUtil;
import com.veteran.hack.util.VetHackTessellator;
import com.veteran.hack.event.events.RenderEvent;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.EnumHand;
import com.veteran.hack.util.BlockInteractionHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import com.veteran.hack.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.veteran.hack.module.ModuleManager;
import net.minecraft.network.Packet;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import com.veteran.hack.setting.Setting;
import net.minecraft.util.math.BlockPos;
import com.veteran.hack.module.Module;

@Info(name = "WebHoleFiller", category = Category.COMBAT)
public class WebHoleFiller extends Module
{
    private static BlockPos PlayerPos;
    private Setting<Double> range;
    private Setting<Boolean> smart;
    private Setting<Integer> smartRange;
    private Setting<Boolean> announceUsage;
    private BlockPos render;
    private Entity renderEnt;
    private EntityPlayer closestTarget;
    private long systemTime;
    private static boolean togglePitch;
    private boolean switchCooldown;
    private boolean isAttacking;
    private boolean caOn;
    private int newSlot;
    double d;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public WebHoleFiller() {
        this.range = this.register(Settings.d("Range", 4.5));
        this.smart = this.register(Settings.b("Smart", false));
        this.smartRange = this.register(Settings.i("Smart Range", 4));
        this.announceUsage = this.register(Settings.b("Announce Usage", false));
        this.systemTime = -1L;
        this.switchCooldown = false;
        this.isAttacking = false;
        final Packet packet;
        this.packetListener = new Listener<PacketEvent.Send>(event -> {
            packet = event.getPacket();
            if (packet instanceof CPacketPlayer && WebHoleFiller.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)WebHoleFiller.yaw;
                ((CPacketPlayer)packet).pitch = (float)WebHoleFiller.pitch;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onEnable() {
        if (ModuleManager.getModuleByName("Autocrystal").isEnabled()) {
            this.caOn = true;
        }
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("[HoleFiller] " + ChatFormatting.GREEN.toString() + "Enabled" + ChatFormatting.RESET.toString() + "!");
        }
    }
    
    @Override
    public void onUpdate() {
        if (WebHoleFiller.mc.world == null) {
            return;
        }
        if (this.smart.getValue()) {
            this.findClosestTarget();
        }
        final List<BlockPos> blocks = this.findCrystalBlocks();
        BlockPos q = null;
        final double dist = 0.0;
        final double prevDist = 0.0;
        int obsidianSlot = (WebHoleFiller.mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.WEB)) ? WebHoleFiller.mc.player.inventory.currentItem : -1;
        if (obsidianSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (WebHoleFiller.mc.player.inventory.getStackInSlot(l).getItem() == Item.getItemFromBlock(Blocks.WEB)) {
                    obsidianSlot = l;
                    break;
                }
            }
        }
        if (obsidianSlot == -1) {
            return;
        }
        for (final BlockPos blockPos : blocks) {
            if (WebHoleFiller.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(blockPos)).isEmpty()) {
                if (this.smart.getValue() && this.isInRange(blockPos)) {
                    q = blockPos;
                }
                else {
                    q = blockPos;
                }
            }
        }
        this.render = q;
        if (q != null && WebHoleFiller.mc.player.onGround) {
            final int oldSlot = WebHoleFiller.mc.player.inventory.currentItem;
            if (WebHoleFiller.mc.player.inventory.currentItem != obsidianSlot) {
                WebHoleFiller.mc.player.inventory.currentItem = obsidianSlot;
            }
            this.lookAtPacket(q.x + 0.5, q.y - 0.5, q.z + 0.5, (EntityPlayer)WebHoleFiller.mc.player);
            BlockInteractionHelper.placeBlockScaffold(this.render);
            WebHoleFiller.mc.player.swingArm(EnumHand.MAIN_HAND);
            WebHoleFiller.mc.player.inventory.currentItem = oldSlot;
            resetRotation();
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            VetHackTessellator.prepare(7);
            VetHackTessellator.drawBox(this.render, 822018048, 63);
            VetHackTessellator.release();
            VetHackTessellator.prepare(7);
            VetHackTessellator.drawBoundingBoxBlockPos(this.render, 1.0f, 0, 255, 0, 170);
            VetHackTessellator.release();
        }
    }
    
    private double getDistanceToBlockPos(final BlockPos pos1, final BlockPos pos2) {
        final double x = pos1.getX() - pos2.getX();
        final double y = pos1.getY() - pos2.getY();
        final double z = pos1.getZ() - pos2.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean IsHole(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 0, 0);
        final BlockPos boost3 = blockPos.add(0, 0, -1);
        final BlockPos boost4 = blockPos.add(1, 0, 0);
        final BlockPos boost5 = blockPos.add(-1, 0, 0);
        final BlockPos boost6 = blockPos.add(0, 0, 1);
        final BlockPos boost7 = blockPos.add(0, 2, 0);
        final BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
        final BlockPos boost9 = blockPos.add(0, -1, 0);
        return WebHoleFiller.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && WebHoleFiller.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && WebHoleFiller.mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && (WebHoleFiller.mc.world.getBlockState(boost3).getBlock() == Blocks.OBSIDIAN || WebHoleFiller.mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK) && (WebHoleFiller.mc.world.getBlockState(boost4).getBlock() == Blocks.OBSIDIAN || WebHoleFiller.mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK) && (WebHoleFiller.mc.world.getBlockState(boost5).getBlock() == Blocks.OBSIDIAN || WebHoleFiller.mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK) && (WebHoleFiller.mc.world.getBlockState(boost6).getBlock() == Blocks.OBSIDIAN || WebHoleFiller.mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK) && WebHoleFiller.mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && (WebHoleFiller.mc.world.getBlockState(boost9).getBlock() == Blocks.OBSIDIAN || WebHoleFiller.mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK);
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(WebHoleFiller.mc.player.posX), Math.floor(WebHoleFiller.mc.player.posY), Math.floor(WebHoleFiller.mc.player.posZ));
    }
    
    public BlockPos getClosestTargetPos() {
        if (this.closestTarget != null) {
            return new BlockPos(Math.floor(this.closestTarget.posX), Math.floor(this.closestTarget.posY), Math.floor(this.closestTarget.posZ));
        }
        return null;
    }
    
    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)WebHoleFiller.mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == WebHoleFiller.mc.player) {
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
                if (WebHoleFiller.mc.player.getDistance((Entity)target) >= WebHoleFiller.mc.player.getDistance((Entity)this.closestTarget)) {
                    continue;
                }
                this.closestTarget = target;
            }
        }
    }
    
    private boolean isInRange(final BlockPos blockPos) {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::IsHole).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return positions.contains((Object)blockPos);
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        if (this.smart.getValue() && this.closestTarget != null) {
            positions.addAll((Collection)this.getSphere(this.getClosestTargetPos(), this.smartRange.getValue(), this.range.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::IsHole).filter((Predicate<? super Object>)this::isInRange).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        }
        else if (!this.smart.getValue()) {
            positions.addAll((Collection)this.getSphere(getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::IsHole).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        }
        return (List<BlockPos>)positions;
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
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        WebHoleFiller.yaw = yaw1;
        WebHoleFiller.pitch = pitch1;
        WebHoleFiller.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (WebHoleFiller.isSpoofingAngles) {
            WebHoleFiller.yaw = WebHoleFiller.mc.player.rotationYaw;
            WebHoleFiller.pitch = WebHoleFiller.mc.player.rotationPitch;
            WebHoleFiller.isSpoofingAngles = false;
        }
    }
    
    public void onDisable() {
        this.closestTarget = null;
        this.render = null;
        resetRotation();
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("[HoleFiller] " + ChatFormatting.RED.toString() + "Disabled" + ChatFormatting.RESET.toString() + "!");
        }
    }
    
    static {
        WebHoleFiller.togglePitch = false;
    }
}
