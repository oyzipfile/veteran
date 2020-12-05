// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.rgui.render.theme;

import com.veteran.hack.gui.rgui.render.font.FontRenderer;
import com.veteran.hack.gui.rgui.render.ComponentUI;
import com.veteran.hack.gui.rgui.component.Component;

public interface Theme
{
    ComponentUI getUIForComponent(final Component p0);
    
    FontRenderer getFontRenderer();
}
