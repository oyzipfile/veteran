// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import net.minecraft.init.Blocks;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "IceSpeed", description = "Changes how slippery ice is", category = Category.HIDDEN)
public class IceSpeed extends Module
{
    private Setting<Float> slipperiness;
    
    public IceSpeed() {
        this.slipperiness = this.register((Setting<Float>)Settings.floatBuilder("Slipperiness").withMinimum(0.2f).withValue(0.4f).withMaximum(1.0f).build());
    }
    
    @Override
    public void onUpdate() {
        Blocks.ICE.slipperiness = this.slipperiness.getValue();
        Blocks.PACKED_ICE.slipperiness = this.slipperiness.getValue();
        Blocks.FROSTED_ICE.slipperiness = this.slipperiness.getValue();
    }
    
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
}
