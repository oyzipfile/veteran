// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.rgui.component.container;

import com.veteran.hack.gui.rgui.component.Component;
import com.veteran.hack.gui.rgui.render.theme.Theme;
import com.veteran.hack.gui.rgui.layout.Layout;

public class OrganisedContainer extends AbstractContainer
{
    Layout layout;
    
    public OrganisedContainer(final Theme theme, final Layout layout) {
        super(theme);
        this.layout = layout;
    }
    
    public Layout getLayout() {
        return this.layout;
    }
    
    public void setLayout(final Layout layout) {
        this.layout = layout;
    }
    
    @Override
    public Container addChild(final Component... component) {
        super.addChild(component);
        this.layout.organiseContainer(this);
        return this;
    }
    
    @Override
    public void setOriginOffsetX(final int originoffsetX) {
        super.setOriginOffsetX(originoffsetX);
        this.layout.organiseContainer(this);
    }
    
    @Override
    public void setOriginOffsetY(final int originoffsetY) {
        super.setOriginOffsetY(originoffsetY);
        this.layout.organiseContainer(this);
    }
}
