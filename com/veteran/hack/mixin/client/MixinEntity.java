// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import com.veteran.hack.module.modules.player.Scaffold;
import com.veteran.hack.module.modules.movement.SafeWalk;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.veteran.hack.BaseMod;
import com.veteran.hack.event.events.EntityEvent;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public class MixinEntity
{
    @Redirect(method = { "applyEntityCollision" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocity(final Entity entity, final double x, final double y, final double z) {
        final EntityEvent.EntityCollision entityCollisionEvent = new EntityEvent.EntityCollision(entity, x, y, z);
        BaseMod.EVENT_BUS.post(entityCollisionEvent);
        if (entityCollisionEvent.isCancelled()) {
            return;
        }
        entity.motionX += x;
        entity.motionY += y;
        entity.motionZ += z;
        entity.isAirBorne = true;
    }
    
    @Redirect(method = { "move" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean isSneaking(final Entity entity) {
        return SafeWalk.shouldSafewalk() || Scaffold.shouldScaffold() || entity.isSneaking();
    }
}
