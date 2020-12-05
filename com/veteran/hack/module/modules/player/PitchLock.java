// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import net.minecraft.util.math.MathHelper;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "PitchLock", category = Category.HIDDEN, description = "Locks your camera pitch")
public class PitchLock extends Module
{
    private Setting<Boolean> auto;
    private Setting<Float> pitch;
    private Setting<Integer> slice;
    
    public PitchLock() {
        this.auto = this.register(Settings.b("Auto", true));
        this.pitch = this.register(Settings.f("Pitch", 180.0f));
        this.slice = this.register(Settings.i("Slice", 8));
    }
    
    @Override
    public void onUpdate() {
        if (this.slice.getValue() == 0) {
            return;
        }
        if (this.auto.getValue()) {
            final int angle = 360 / this.slice.getValue();
            float yaw = PitchLock.mc.player.rotationPitch;
            yaw = (float)(Math.round(yaw / angle) * angle);
            PitchLock.mc.player.rotationPitch = yaw;
            if (PitchLock.mc.player.isRiding()) {
                PitchLock.mc.player.getRidingEntity().rotationPitch = yaw;
            }
        }
        else {
            PitchLock.mc.player.rotationPitch = MathHelper.clamp(this.pitch.getValue() - 180.0f, -180.0f, 180.0f);
        }
    }
}
