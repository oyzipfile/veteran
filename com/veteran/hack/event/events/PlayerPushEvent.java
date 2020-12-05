// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.event.events;

import com.veteran.hack.event.MinecraftEvent;

public class PlayerPushEvent extends MinecraftEvent
{
    public double X;
    public double Y;
    public double Z;
    
    public PlayerPushEvent(final double p_X, final double p_Y, final double p_Z) {
        this.X = p_X;
        this.Y = p_Y;
        this.Z = p_Z;
    }
}
