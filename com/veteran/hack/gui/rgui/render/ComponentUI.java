// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.rgui.render;

import com.veteran.hack.gui.font.CFontRenderer;
import com.veteran.hack.gui.rgui.component.container.Container;
import com.veteran.hack.gui.rgui.render.font.FontRenderer;
import com.veteran.hack.gui.rgui.component.Component;

public interface ComponentUI<T extends Component>
{
    void renderComponent(final T p0, final FontRenderer p1);
    
    void handleMouseDown(final T p0, final int p1, final int p2, final int p3);
    
    void handleMouseRelease(final T p0, final int p1, final int p2, final int p3);
    
    void handleMouseDrag(final T p0, final int p1, final int p2, final int p3);
    
    void handleScroll(final T p0, final int p1, final int p2, final int p3, final boolean p4);
    
    void handleKeyDown(final T p0, final int p1);
    
    void handleKeyUp(final T p0, final int p1);
    
    void handleAddComponent(final T p0, final Container p1);
    
    void handleSizeComponent(final T p0);
    
    Class<? extends Component> getHandledClass();
    
    void renderComponent(final T p0, final CFontRenderer p1);
}
