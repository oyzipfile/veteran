// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import net.minecraft.network.Packet;
import java.util.Iterator;
import java.util.function.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import com.veteran.hack.module.Module;

@Info(name = "NoBreakAnimation", category = Category.PLAYER, description = "Prevents block break animation server side")
public class NoBreakAnimation extends Module
{
    private boolean isMining;
    private BlockPos lastPos;
    private EnumFacing lastFacing;
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    
    public NoBreakAnimation() {
        this.isMining = false;
        this.lastPos = null;
        this.lastFacing = null;
        CPacketPlayerDigging cPacketPlayerDigging;
        final Iterator<Entity> iterator;
        Entity entity;
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayerDigging) {
                cPacketPlayerDigging = (CPacketPlayerDigging)event.getPacket();
                NoBreakAnimation.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(cPacketPlayerDigging.getPosition())).iterator();
                while (iterator.hasNext()) {
                    entity = iterator.next();
                    if (entity instanceof EntityEnderCrystal) {
                        this.resetMining();
                        return;
                    }
                    else if (entity instanceof EntityLivingBase) {
                        this.resetMining();
                        return;
                    }
                    else {
                        continue;
                    }
                }
                if (cPacketPlayerDigging.getAction().equals((Object)CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                    this.isMining = true;
                    this.setMiningInfo(cPacketPlayerDigging.getPosition(), cPacketPlayerDigging.getFacing());
                }
                if (cPacketPlayerDigging.getAction().equals((Object)CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                    this.resetMining();
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (!NoBreakAnimation.mc.gameSettings.keyBindAttack.isKeyDown()) {
            this.resetMining();
            return;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null) {
            NoBreakAnimation.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
    }
    
    private void setMiningInfo(final BlockPos lastPos, final EnumFacing lastFacing) {
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }
    
    public void resetMining() {
        this.isMining = false;
        this.lastPos = null;
        this.lastFacing = null;
    }
}
