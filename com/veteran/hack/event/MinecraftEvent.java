// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.event;

import com.veteran.hack.util.Wrapper;
import me.zero.alpine.type.Cancellable;

public class MinecraftEvent extends Cancellable
{
    private Era era;
    private final float partialTicks;
    
    public MinecraftEvent() {
        this.era = Era.PRE;
        this.partialTicks = Wrapper.getMinecraft().getRenderPartialTicks();
    }
    
    public MinecraftEvent(final Era p_Era) {
        this.era = Era.PRE;
        this.partialTicks = Wrapper.getMinecraft().getRenderPartialTicks();
    }
    
    public Era getEra() {
        return this.era;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public enum Era
    {
        PRE, 
        PERI, 
        POST;
    }
}
