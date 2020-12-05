// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.hidden;

import com.veteran.hack.util.GuiFrameUtil;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "Hidden:FixGui", category = Category.HIDDEN, showOnArray = ShowOnArray.OFF, description = "Moves GUI elements back on screen")
public class FixGui extends Module
{
    public Setting<Boolean> shouldAutoEnable;
    
    public FixGui() {
        this.shouldAutoEnable = this.register(Settings.b("Enable", true));
    }
    
    @Override
    public void onUpdate() {
        GuiFrameUtil.fixFrames(FixGui.mc);
    }
}
