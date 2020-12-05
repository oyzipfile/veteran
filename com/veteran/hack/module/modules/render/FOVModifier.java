// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.render;

import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "FOV Modifier", description = "Change FOV easier", category = Category.RENDER)
public class FOVModifier extends Module
{
    private float fov;
    private Setting<Integer> fovChange;
    
    public FOVModifier() {
        this.fov = 90.0f;
        this.fovChange = this.register((Setting<Integer>)Settings.integerBuilder("FOV").withMinimum(30).withValue(90).withMaximum(179).build());
    }
    
    public void OnEnable() {
        if (FOVModifier.mc.player == null) {
            return;
        }
        this.fov = FOVModifier.mc.gameSettings.fovSetting;
    }
    
    public void onDisable() {
        FOVModifier.mc.gameSettings.fovSetting = this.fov;
    }
    
    @Override
    public void onUpdate() {
        if (FOVModifier.mc.player == null) {
            return;
        }
        FOVModifier.mc.gameSettings.fovSetting = this.fovChange.getValue();
    }
}
