// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import com.veteran.hack.util.Friends;
import com.veteran.hack.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumHand;
import com.veteran.hack.util.BlockInteractionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import com.veteran.hack.module.ModuleManager;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import net.minecraft.util.math.BlockPos;
import com.veteran.hack.module.Module;

@Info(name = "BlockAura", category = Category.COMBAT)
public class WebAura extends Module
{
    private static BlockPos PlayerPos;
    private Setting<Double> range;
    private Setting<Boolean> webSelf;
    private Setting<Boolean> offhand;
    private Setting<AuraBlock> blockSetting;
    private int obsidianSlot;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public WebAura() {
        this.range = this.register(Settings.d("Range", 4.5));
        this.webSelf = this.register(Settings.b("Self", true));
        this.offhand = Settings.b("Offhand", false);
        this.blockSetting = this.register(Settings.e("Block Type", AuraBlock.WEB));
        final Packet packet;
        this.packetListener = new Listener<PacketEvent.Send>(event -> {
            packet = event.getPacket();
            if (packet instanceof CPacketPlayer && WebAura.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)WebAura.yaw;
                ((CPacketPlayer)packet).pitch = (float)WebAura.pitch;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onEnable() {
        this.obsidianSlot = ((WebAura.mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(this.AuraItem())) ? WebAura.mc.player.inventory.currentItem : -1);
        if (this.obsidianSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (WebAura.mc.player.inventory.getStackInSlot(l).getItem() == Item.getItemFromBlock(this.AuraItem())) {
                    this.obsidianSlot = l;
                    break;
                }
            }
        }
        if (this.obsidianSlot == -1) {
            return;
        }
        if (this.offhand.getValue()) {
            ModuleManager.getModuleByName("BetterOffhand").disable();
            ModuleManager.getModuleByName("OffhandGap").disable();
            WebAura.mc.playerController.windowClick(0, this.obsidianSlot + 36, 0, ClickType.PICKUP, (EntityPlayer)WebAura.mc.player);
            WebAura.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)WebAura.mc.player);
        }
    }
    
    public void onDisable() {
        if (this.offhand.getValue()) {
            ModuleManager.getModuleByName("BetterOffhand").enable();
            ModuleManager.getModuleByName("OffhandGap").enable();
            WebAura.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)WebAura.mc.player);
            WebAura.mc.playerController.windowClick(0, this.obsidianSlot + 36, 0, ClickType.PICKUP, (EntityPlayer)WebAura.mc.player);
        }
        resetRotation();
    }
    
    @Override
    public void onUpdate() {
        if (this.getClosestTarget() == null) {
            return;
        }
        this.obsidianSlot = ((WebAura.mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(this.AuraItem())) ? WebAura.mc.player.inventory.currentItem : -1);
        if (this.obsidianSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (WebAura.mc.player.inventory.getStackInSlot(l).getItem() == Item.getItemFromBlock(this.AuraItem())) {
                    this.obsidianSlot = l;
                    break;
                }
            }
        }
        if (this.obsidianSlot == -1) {
            return;
        }
        if (this.getClosestTarget() == null) {
            return;
        }
        final Vec3d targetVec = this.getClosestTarget().getPositionVector();
        final BlockPos target = new BlockPos(targetVec.x, targetVec.y, targetVec.z);
        if (WebAura.mc.world.getBlockState(target).getBlock() == this.AuraItem() && (!this.webSelf.getValue() || (this.webSelf.getValue() && WebAura.mc.world.getBlockState(WebAura.mc.player.getPosition()).getBlock() == this.AuraItem()))) {
            return;
        }
        final int oldSlot = WebAura.mc.player.inventory.currentItem;
        if (!this.offhand.getValue()) {
            WebAura.mc.player.inventory.currentItem = this.obsidianSlot;
        }
        this.lookAtPacket(target.x + 0.5, target.y - 0.5, target.z + 0.5, (EntityPlayer)WebAura.mc.player);
        if (!this.offhand.getValue()) {
            BlockInteractionHelper.placeBlockScaffold(target);
        }
        WebAura.mc.player.swingArm(((boolean)this.offhand.getValue()) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        if (this.webSelf.getValue()) {
            this.lookAtPacket(WebAura.mc.player.getPosition().x + 0.5, WebAura.mc.player.getPosition().y - 0.5, WebAura.mc.player.getPosition().z + 0.5, (EntityPlayer)WebAura.mc.player);
            if (!this.offhand.getValue()) {
                BlockInteractionHelper.placeBlockScaffold(WebAura.mc.player.getPosition());
            }
            WebAura.mc.player.swingArm(((boolean)this.offhand.getValue()) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        }
        WebAura.mc.player.inventory.currentItem = oldSlot;
        resetRotation();
    }
    
    private EntityPlayer getClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)WebAura.mc.world.playerEntities;
        for (final EntityPlayer target : playerList) {
            if (target == WebAura.mc.player) {
                continue;
            }
            if (WebAura.mc.player.getDistance((Entity)target) > this.range.getValue()) {
                continue;
            }
            if (!EntityUtil.isLiving((Entity)target)) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (Friends.isFriend(target.getName())) {
                continue;
            }
            return target;
        }
        return null;
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        WebAura.yaw = yaw1;
        WebAura.pitch = pitch1;
        WebAura.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (WebAura.isSpoofingAngles) {
            WebAura.yaw = WebAura.mc.player.rotationYaw;
            WebAura.pitch = WebAura.mc.player.rotationPitch;
            WebAura.isSpoofingAngles = false;
        }
    }
    
    private Block AuraItem() {
        switch (this.blockSetting.getValue()) {
            case WEB: {
                return Blocks.WEB;
            }
            case SIGN: {
                return Blocks.STANDING_SIGN;
            }
            case PRESSURE_PLATE: {
                return Blocks.WOODEN_PRESSURE_PLATE;
            }
            default: {
                return Blocks.WEB;
            }
        }
    }
    
    private enum AuraBlock
    {
        WEB, 
        SIGN, 
        PRESSURE_PLATE;
    }
}
