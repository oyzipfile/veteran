// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import com.veteran.hack.event.MinecraftEvent;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.event.events.EntityEvent;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "Velocity", description = "Modify knockback impact", category = Category.MOVEMENT)
public class Velocity extends Module
{
    private Setting<Boolean> noPush;
    private Setting<Float> horizontal;
    private Setting<Float> vertical;
    @EventHandler
    private Listener<PacketEvent.Receive> packetEventListener;
    @EventHandler
    private Listener<EntityEvent.EntityCollision> entityCollisionListener;
    
    public Velocity() {
        this.noPush = this.register(Settings.b("NoPush", true));
        this.horizontal = this.register(Settings.f("Horizontal", 0.0f));
        this.vertical = this.register(Settings.f("Vertical", 0.0f));
        SPacketEntityVelocity velocity;
        final SPacketEntityVelocity sPacketEntityVelocity;
        final SPacketEntityVelocity sPacketEntityVelocity2;
        final SPacketEntityVelocity sPacketEntityVelocity3;
        SPacketExplosion sPacketExplosion;
        SPacketExplosion velocity2;
        final SPacketExplosion sPacketExplosion2;
        final SPacketExplosion sPacketExplosion3;
        this.packetEventListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getEra() == MinecraftEvent.Era.PRE) {
                if (event.getPacket() instanceof SPacketEntityVelocity) {
                    velocity = (SPacketEntityVelocity)event.getPacket();
                    if (velocity.getEntityID() == Velocity.mc.player.entityId) {
                        if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                            event.cancel();
                        }
                        sPacketEntityVelocity.motionX *= (int)(Object)this.horizontal.getValue();
                        sPacketEntityVelocity2.motionY *= (int)(Object)this.vertical.getValue();
                        sPacketEntityVelocity3.motionZ *= (int)(Object)this.horizontal.getValue();
                    }
                }
                else if (event.getPacket() instanceof SPacketExplosion) {
                    if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                        event.cancel();
                    }
                    velocity2 = (sPacketExplosion = (SPacketExplosion)event.getPacket());
                    sPacketExplosion.motionX *= this.horizontal.getValue();
                    sPacketExplosion2.motionY *= this.vertical.getValue();
                    sPacketExplosion3.motionZ *= this.horizontal.getValue();
                }
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.entityCollisionListener = new Listener<EntityEvent.EntityCollision>(event -> {
            if (event.getEntity() == Velocity.mc.player) {
                if ((this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) || this.noPush.getValue()) {
                    event.cancel();
                }
                else {
                    event.setX(-event.getX() * this.horizontal.getValue());
                    event.setY(0.0);
                    event.setZ(-event.getZ() * this.horizontal.getValue());
                }
            }
        }, (Predicate<EntityEvent.EntityCollision>[])new Predicate[0]);
    }
}
