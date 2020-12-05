// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import com.veteran.hack.util.EntityUtil;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(category = Category.HIDDEN, description = "Prevents fall damage", name = "NoFall")
public class NoFall extends Module
{
    private Setting<FallMode> fallMode;
    private Setting<Boolean> pickup;
    private Setting<Integer> distance;
    private Setting<Integer> pickupDelay;
    private long last;
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    
    public NoFall() {
        this.fallMode = this.register(Settings.e("Mode", FallMode.PACKET));
        this.pickup = this.register(Settings.booleanBuilder("Pickup").withValue(true).withVisibility(v -> this.fallMode.getValue().equals(FallMode.BUCKET)).build());
        this.distance = this.register(Settings.integerBuilder("Distance").withValue(3).withMinimum(1).withMaximum(10).withVisibility(v -> this.fallMode.getValue().equals(FallMode.BUCKET)).build());
        this.pickupDelay = this.register(Settings.integerBuilder("Pickup Delay").withValue(300).withMinimum(100).withMaximum(1000).withVisibility(v -> this.fallMode.getValue().equals(FallMode.BUCKET) && this.pickup.getValue()).build());
        this.last = 0L;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (this.fallMode.getValue().equals(FallMode.PACKET) && event.getPacket() instanceof CPacketPlayer) {
                ((CPacketPlayer)event.getPacket()).onGround = true;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.fallMode.getValue().equals(FallMode.BUCKET) && NoFall.mc.player.fallDistance >= this.distance.getValue() && !EntityUtil.isAboveWater((Entity)NoFall.mc.player) && System.currentTimeMillis() - this.last > 100L) {
            final Vec3d posVec = NoFall.mc.player.getPositionVector();
            final RayTraceResult result = NoFall.mc.world.rayTraceBlocks(posVec, posVec.add(0.0, -5.329999923706055, 0.0), true, true, false);
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                EnumHand hand = EnumHand.MAIN_HAND;
                if (NoFall.mc.player.getHeldItemOffhand().getItem() == Items.WATER_BUCKET) {
                    hand = EnumHand.OFF_HAND;
                }
                else if (NoFall.mc.player.getHeldItemMainhand().getItem() != Items.WATER_BUCKET) {
                    for (int i = 0; i < 9; ++i) {
                        if (NoFall.mc.player.inventory.getStackInSlot(i).getItem() == Items.WATER_BUCKET) {
                            NoFall.mc.player.inventory.currentItem = i;
                            NoFall.mc.player.rotationPitch = 90.0f;
                            this.last = System.currentTimeMillis();
                            return;
                        }
                    }
                    return;
                }
                NoFall.mc.player.rotationPitch = 90.0f;
                NoFall.mc.playerController.processRightClick((EntityPlayer)NoFall.mc.player, (World)NoFall.mc.world, hand);
            }
            if (this.pickup.getValue()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(this.pickupDelay.getValue());
                    }
                    catch (InterruptedException ex) {}
                    NoFall.mc.player.rotationPitch = 90.0f;
                    NoFall.mc.rightClickMouse();
                }).start();
            }
        }
    }
    
    private enum FallMode
    {
        BUCKET, 
        PACKET;
    }
}
