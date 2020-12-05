// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.event.events;

import com.google.common.base.Predicate;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.client.multiplayer.WorldClient;
import com.veteran.hack.event.MinecraftEvent;

public class EventRenderGetEntitiesINAABBexcluding extends MinecraftEvent
{
    public EventRenderGetEntitiesINAABBexcluding(final WorldClient worldClient, final Entity entityIn, final AxisAlignedBB boundingBox, final Predicate predicate) {
    }
}
