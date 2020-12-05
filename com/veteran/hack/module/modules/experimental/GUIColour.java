// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.experimental;

import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "GUI Colour", description = "Change GUI Colours", category = Category.EXPERIMENTAL)
public class GUIColour extends Module
{
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    
    public GUIColour() {
        this.red = this.register((Setting<Integer>)Settings.integerBuilder("Red").withMinimum(0).withValue(13).withMaximum(255).build());
        this.green = this.register((Setting<Integer>)Settings.integerBuilder("Green").withMinimum(0).withValue(13).withMaximum(255).build());
        this.blue = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withMinimum(0).withValue(13).withMaximum(255).build());
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Alpha").withMinimum(0).withValue(117).withMaximum(255).build());
    }
}
