// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.event.events;

import javax.annotation.Nonnull;
import net.minecraft.entity.Entity;
import com.veteran.hack.event.MinecraftEvent;

public class ClientPlayerAttackEvent extends MinecraftEvent
{
    private Entity targetEntity;
    
    public ClientPlayerAttackEvent(@Nonnull final Entity targetEntity) {
        if (this.targetEntity == null) {
            throw new IllegalArgumentException("Target Entity cannot be null");
        }
        this.targetEntity = targetEntity;
    }
    
    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}
