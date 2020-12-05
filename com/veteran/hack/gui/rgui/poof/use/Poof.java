// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.rgui.poof.use;

import java.lang.reflect.ParameterizedType;
import com.veteran.hack.gui.rgui.poof.IPoof;
import com.veteran.hack.gui.rgui.poof.PoofInfo;
import com.veteran.hack.gui.rgui.component.Component;

public abstract class Poof<T extends Component, S extends PoofInfo> implements IPoof<T, S>
{
    private Class<T> componentclass;
    private Class<S> infoclass;
    
    public Poof() {
        this.componentclass = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.infoclass = (Class<S>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
    
    @Override
    public Class getComponentClass() {
        return this.componentclass;
    }
    
    @Override
    public Class<S> getInfoClass() {
        return this.infoclass;
    }
}
