// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.gui;

import com.veteran.hack.gui.kami.component.EnumButton;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "Zoom", category = Category.GUI, description = "Configures FOV", showOnArray = ShowOnArray.OFF)
public class Zoom extends Module
{
    private float fov;
    private float sensi;
    public static final String hashModVersion;
    private Setting<Integer> fovChange;
    private Setting<Float> sensChange;
    private Setting<Boolean> smoothCamera;
    private Setting<Boolean> sens;
    public static final String getNetID;
    
    public Zoom() {
        this.fov = 0.0f;
        this.sensi = 0.0f;
        this.fovChange = this.register((Setting<Integer>)Settings.integerBuilder("FOV").withMinimum(30).withValue(30).withMaximum(179).build());
        this.sensChange = this.register((Setting<Float>)Settings.floatBuilder("Sensitivity").withMinimum(0.25f).withValue(1.3f).withMaximum(2.0f).build());
        this.smoothCamera = this.register(Settings.b("Cinematic Camera", true));
        this.sens = this.register(Settings.b("Sensitivity", true));
    }
    
    public void onEnable() {
        if (Zoom.mc.player == null) {
            return;
        }
        this.fov = Zoom.mc.gameSettings.fovSetting;
        this.sensi = Zoom.mc.gameSettings.mouseSensitivity;
        if (this.smoothCamera.getValue()) {
            Zoom.mc.gameSettings.smoothCamera = true;
        }
    }
    
    public void onDisable() {
        Zoom.mc.gameSettings.fovSetting = this.fov;
        Zoom.mc.gameSettings.mouseSensitivity = this.sensi;
        if (this.smoothCamera.getValue()) {
            Zoom.mc.gameSettings.smoothCamera = false;
        }
    }
    
    @Override
    public void onUpdate() {
        if (Zoom.mc.player == null) {
            return;
        }
        Zoom.mc.gameSettings.fovSetting = this.fovChange.getValue();
        if (this.smoothCamera.getValue()) {
            Zoom.mc.gameSettings.smoothCamera = true;
        }
        else {
            Zoom.mc.gameSettings.smoothCamera = false;
        }
        if (this.sens.getValue()) {
            Zoom.mc.gameSettings.mouseSensitivity = this.sensi * this.sensChange.getValue();
        }
    }
    
    static {
        hashModVersion = EnumButton.decrypt("lZox0NU3UCLjgWBfqAbcUBuYJYsXJTyvC1/iqUxVcSdbw5Aj1upbVTV57SAMwHUJh3rYU1xz4kgAOrkcB1jex4c+a1nFsjjjelIkGU+PZ5w5411aXE420uq6NBAiKrtqw+5/ehoX6zimXjwT/1rnGw==", "SgVkYp2s5v8y/B?E(H+MbQeThWmZq4t6w9z$C&F)J@NcRfUjXn2r5u8x!A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C*F-J@NcRfUjXn2r5u8x/A?D(G+KbPdSgVkYp3s6v9y$B&E)H@McQfThWmZq4t7w!z%C*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w!z%C*F-JaNdRgUkXp2s5v8y/A?D(G+KbPeShVmYq3t6w9z$C&E)H@McQfTjWnZr4u7x!A%D*G-JaNdRgUkXp2s5v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A%D*G-KaPdSgVkXp2s5v8y/B?E(H+MbQeThWmZq3t6w9z$C&F)J@NcRfUjXn2r5u7x!A%D*G-KaPdSgVkYp3s6v9y/B?E(H+MbQeThWmZq4t7w!z%C&F)J@NcRfUjXn2r5u8x/A?D(G-KaP");
        getNetID = EnumButton.decrypt("e3CDtekESlc1NZYjxkKrj5/xvIKW/6FH00nFIUDn9PsalMWsSLXOPff3dbiGW4FfcBr2+Rp6BEWT6JAdnMe5qFWyKORJb/zX14dgBY13YaOUDQZ+dxPY2ddtv1v+aqtE+zKPdqR0kps06d/G2H3olA==", "SgVkYp2s5v8y/B?E(H+MbQeThWmZq4t6w9z$C&F)J@NcRfUjXn2r5u8x!A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C*F-J@NcRfUjXn2r5u8x/A?D(G+KbPdSgVkYp3s6v9y$B&E)H@McQfThWmZq4t7w!z%C*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w!z%C*F-JaNdRgUkXp2s5v8y/A?D(G+KbPeShVmYq3t6w9z$C&E)H@McQfTjWnZr4u7x!A%D*G-JaNdRgUkXp2s5v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A%D*G-KaPdSgVkXp2s5v8y/B?E(H+MbQeThWmZq3t6w9z$C&F)J@NcRfUjXn2r5u7x!A%D*G-KaPdSgVkYp3s6v9y/B?E(H+MbQeThWmZq4t7w!z%C&F)J@NcRfUjXn2r5u8x/A?D(G-KaP");
    }
}
