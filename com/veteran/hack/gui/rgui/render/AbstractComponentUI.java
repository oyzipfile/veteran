// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.rgui.render;

import com.veteran.hack.gui.font.CFontRenderer;
import com.veteran.hack.gui.rgui.component.container.Container;
import com.veteran.hack.gui.rgui.render.font.FontRenderer;
import java.lang.reflect.ParameterizedType;
import com.veteran.hack.gui.rgui.component.Component;

public abstract class AbstractComponentUI<T extends Component> implements ComponentUI<T>
{
    private Class<T> persistentClass;
    
    public AbstractComponentUI() {
        this.persistentClass = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    @Override
    public void renderComponent(final T component, final FontRenderer fontRenderer) {
    }
    
    @Override
    public void handleMouseDown(final T component, final int x, final int y, final int button) {
    }
    
    @Override
    public void handleMouseRelease(final T component, final int x, final int y, final int button) {
    }
    
    @Override
    public void handleMouseDrag(final T component, final int x, final int y, final int button) {
    }
    
    @Override
    public void handleScroll(final T component, final int x, final int y, final int amount, final boolean up) {
    }
    
    @Override
    public void handleAddComponent(final T component, final Container container) {
    }
    
    @Override
    public void handleKeyDown(final T component, final int key) {
    }
    
    @Override
    public void handleKeyUp(final T component, final int key) {
    }
    
    @Override
    public void handleSizeComponent(final T component) {
    }
    
    @Override
    public Class<? extends Component> getHandledClass() {
        return this.persistentClass;
    }
    
    @Override
    public void renderComponent(final T component, final CFontRenderer cFontRenderer) {
    }
}
