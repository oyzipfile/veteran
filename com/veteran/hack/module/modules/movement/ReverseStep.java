// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import com.veteran.hack.module.Module;

@Info(name = "ReverseStep", category = Category.MOVEMENT, description = "allows you to get into holes more quickly by teleporting you down to the ground ")
public class ReverseStep extends Module
{
    @Override
    public void onUpdate() {
        if (ReverseStep.mc.world == null) {
            return;
        }
        if (!ReverseStep.mc.player.onGround && !ReverseStep.mc.world.getBlockState(ReverseStep.mc.player.getPosition().down(2)).getBlock().equals(Blocks.AIR) && ReverseStep.mc.player.motionX <= 0.07 && ReverseStep.mc.player.motionZ <= 0.07 && ReverseStep.mc.player.motionY < 0.0) {
            final EntityPlayerSP player = ReverseStep.mc.player;
            player.motionY *= 4.0;
        }
    }
}
