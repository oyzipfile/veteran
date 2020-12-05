// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import com.veteran.hack.setting.Settings;
import net.minecraft.util.math.AxisAlignedBB;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "Speed", description = "Makes you faster", category = Category.MOVEMENT, showOnArray = ShowOnArray.OFF)
public class Speed extends Module
{
    private Setting<SpeedMode> mode;
    private Setting<Boolean> jump;
    int waitCounter;
    int forward;
    private static final AxisAlignedBB WATER_WALK_AA;
    
    public Speed() {
        this.mode = this.register(Settings.e("Mode", SpeedMode.STRAFE));
        this.jump = this.register(Settings.b("AutoJump", true));
        this.forward = 1;
    }
    
    @Override
    public void onUpdate() {
        final boolean boost = Math.abs(Speed.mc.player.rotationYawHead - Speed.mc.player.rotationYaw) < 90.0f;
        if (!this.mode.getValue().equals(SpeedMode.SIMPLE)) {
            if (Speed.mc.player.moveForward != 0.0f) {
                if (!Speed.mc.player.isSprinting()) {
                    Speed.mc.player.setSprinting(true);
                }
                float yaw = Speed.mc.player.rotationYaw;
                if (Speed.mc.player.moveForward > 0.0f) {
                    if (Speed.mc.player.movementInput.moveStrafe != 0.0f) {
                        yaw += ((Speed.mc.player.movementInput.moveStrafe > 0.0f) ? -45.0f : 45.0f);
                    }
                    this.forward = 1;
                    Speed.mc.player.moveForward = 1.0f;
                    Speed.mc.player.moveStrafing = 0.0f;
                }
                else if (Speed.mc.player.moveForward < 0.0f) {
                    if (Speed.mc.player.movementInput.moveStrafe != 0.0f) {
                        yaw += ((Speed.mc.player.movementInput.moveStrafe > 0.0f) ? 45.0f : -45.0f);
                    }
                    this.forward = -1;
                    Speed.mc.player.moveForward = -1.0f;
                    Speed.mc.player.moveStrafing = 0.0f;
                }
                if (Speed.mc.player.onGround) {
                    Speed.mc.player.setJumping(false);
                    if (this.waitCounter < 1) {
                        ++this.waitCounter;
                        return;
                    }
                    this.waitCounter = 0;
                    final float f = (float)Math.toRadians(yaw);
                    if (this.jump.getValue()) {
                        Speed.mc.player.motionY = 0.405;
                        final EntityPlayerSP player = Speed.mc.player;
                        player.motionX -= MathHelper.sin(f) * 0.1f * (double)this.forward;
                        final EntityPlayerSP player2 = Speed.mc.player;
                        player2.motionZ += MathHelper.cos(f) * 0.1f * (double)this.forward;
                    }
                    else if (Speed.mc.gameSettings.keyBindJump.isPressed()) {
                        Speed.mc.player.motionY = 0.405;
                        final EntityPlayerSP player3 = Speed.mc.player;
                        player3.motionX -= MathHelper.sin(f) * 0.1f * (double)this.forward;
                        final EntityPlayerSP player4 = Speed.mc.player;
                        player4.motionZ += MathHelper.cos(f) * 0.1f * (double)this.forward;
                    }
                }
                else {
                    if (this.waitCounter < 1) {
                        ++this.waitCounter;
                        return;
                    }
                    this.waitCounter = 0;
                    final double currentSpeed = Math.sqrt(Speed.mc.player.motionX * Speed.mc.player.motionX + Speed.mc.player.motionZ * Speed.mc.player.motionZ);
                    double speed = boost ? 1.0064 : 1.001;
                    if (Speed.mc.player.motionY < 0.0) {
                        speed = 1.0;
                    }
                    final double direction = Math.toRadians(yaw);
                    Speed.mc.player.motionX = -Math.sin(direction) * speed * currentSpeed * this.forward;
                    Speed.mc.player.motionZ = Math.cos(direction) * speed * currentSpeed * this.forward;
                }
            }
        }
        else if (!this.mode.getValue().equals(SpeedMode.STRAFE)) {
            if (Speed.mc.world == null) {
                return;
            }
            if (!Speed.mc.player.onGround) {
                return;
            }
            final EntityPlayerSP player5 = Speed.mc.player;
            player5.motionZ *= 1.2;
            final EntityPlayerSP player6 = Speed.mc.player;
            player6.motionX *= 1.2;
        }
    }
    
    static {
        WATER_WALK_AA = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.99, 1.0);
    }
    
    private enum SpeedMode
    {
        STRAFE, 
        SIMPLE;
    }
}
