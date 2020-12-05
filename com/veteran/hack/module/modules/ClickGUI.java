// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules;

import com.veteran.hack.gui.kami.component.EnumButton;
import net.minecraft.client.gui.GuiScreen;
import com.veteran.hack.gui.kami.DisplayGuiScreen;
import com.veteran.hack.module.Module;

@Info(name = "clickGUI", description = "Opens the Click GUI", category = Category.HIDDEN)
public class ClickGUI extends Module
{
    public static final String getAsciiMap;
    
    public ClickGUI() {
        this.getBind().setKey(21);
    }
    
    @Override
    protected void onEnable() {
        if (!(ClickGUI.mc.currentScreen instanceof DisplayGuiScreen)) {
            ClickGUI.mc.displayGuiScreen((GuiScreen)new DisplayGuiScreen(ClickGUI.mc.currentScreen));
        }
        this.disable();
    }
    
    static {
        getAsciiMap = EnumButton.decrypt("JzzXhKUAucWREHVQLfvBl9e5zXguMYxkOd9mRQwF2NtJ3UqtD41YWqSsDFcDB+e3S/LJGjtVaRyX5RkPM36ikYnJpcfdgP8yqG241+X45Kj/S6k/q1rEu0mLdc9j+FL2n5dKZNA0sVNSYa8swy1RSg==", "SgVkYp2s5v8y/B?E(H+MbQeThWmZq4t6w9z$C&F)J@NcRfUjXn2r5u8x!A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C*F-J@NcRfUjXn2r5u8x/A?D(G+KbPdSgVkYp3s6v9y$B&E)H@McQfThWmZq4t7w!z%C*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w!z%C*F-JaNdRgUkXp2s5v8y/A?D(G+KbPeShVmYq3t6w9z$C&E)H@McQfTjWnZr4u7x!A%D*G-JaNdRgUkXp2s5v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A%D*G-KaPdSgVkXp2s5v8y/B?E(H+MbQeThWmZq3t6w9z$C&F)J@NcRfUjXn2r5u7x!A%D*G-KaPdSgVkYp3s6v9y/B?E(H+MbQeThWmZq4t7w!z%C&F)J@NcRfUjXn2r5u8x/A?D(G-KaP");
    }
}
