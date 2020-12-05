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

@Info(name = "Strafe", description = "Strafe", category = Category.HIDDEN)
public class Strafe extends Module
{
    private Setting<Boolean> jump;
    int waitCounter;
    int forward;
    private static final AxisAlignedBB WATER_WALK_AA;
    
    public Strafe() {
        this.jump = this.register(Settings.b("AutoJump", true));
        this.forward = 1;
    }
    
    @Override
    public void onUpdate() {
        final boolean boost = Math.abs(Strafe.mc.player.rotationYawHead - Strafe.mc.player.rotationYaw) < 90.0f;
        if (Strafe.mc.player.moveForward != 0.0f) {
            if (!Strafe.mc.player.isSprinting()) {
                Strafe.mc.player.setSprinting(true);
            }
            float yaw = Strafe.mc.player.rotationYaw;
            if (Strafe.mc.player.moveForward > 0.0f) {
                if (Strafe.mc.player.movementInput.moveStrafe != 0.0f) {
                    yaw += ((Strafe.mc.player.movementInput.moveStrafe > 0.0f) ? -45.0f : 45.0f);
                }
                this.forward = 1;
                Strafe.mc.player.moveForward = 1.0f;
                Strafe.mc.player.moveStrafing = 0.0f;
            }
            else if (Strafe.mc.player.moveForward < 0.0f) {
                if (Strafe.mc.player.movementInput.moveStrafe != 0.0f) {
                    yaw += ((Strafe.mc.player.movementInput.moveStrafe > 0.0f) ? 45.0f : -45.0f);
                }
                this.forward = -1;
                Strafe.mc.player.moveForward = -1.0f;
                Strafe.mc.player.moveStrafing = 0.0f;
            }
            if (Strafe.mc.player.onGround) {
                Strafe.mc.player.setJumping(false);
                if (this.waitCounter < 1) {
                    ++this.waitCounter;
                    return;
                }
                this.waitCounter = 0;
                final float f = (float)Math.toRadians(yaw);
                if (this.jump.getValue()) {
                    Strafe.mc.player.motionY = 0.405;
                    final EntityPlayerSP player = Strafe.mc.player;
                    player.motionX -= MathHelper.sin(f) * 0.1f * (double)this.forward;
                    final EntityPlayerSP player2 = Strafe.mc.player;
                    player2.motionZ += MathHelper.cos(f) * 0.1f * (double)this.forward;
                }
                else if (Strafe.mc.gameSettings.keyBindJump.isPressed()) {
                    Strafe.mc.player.motionY = 0.405;
                    final EntityPlayerSP player3 = Strafe.mc.player;
                    player3.motionX -= MathHelper.sin(f) * 0.1f * (double)this.forward;
                    final EntityPlayerSP player4 = Strafe.mc.player;
                    player4.motionZ += MathHelper.cos(f) * 0.1f * (double)this.forward;
                }
            }
            else {
                if (this.waitCounter < 1) {
                    ++this.waitCounter;
                    return;
                }
                this.waitCounter = 0;
                final double currentSpeed = Math.sqrt(Strafe.mc.player.motionX * Strafe.mc.player.motionX + Strafe.mc.player.motionZ * Strafe.mc.player.motionZ);
                double speed = boost ? 1.0064 : 1.001;
                if (Strafe.mc.player.motionY < 0.0) {
                    speed = 1.0;
                }
                final double direction = Math.toRadians(yaw);
                Strafe.mc.player.motionX = -Math.sin(direction) * speed * currentSpeed * this.forward;
                Strafe.mc.player.motionZ = Math.cos(direction) * speed * currentSpeed * this.forward;
            }
        }
    }
    
    static {
        WATER_WALK_AA = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.99, 1.0);
    }
}
