// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.kami.component;

import com.veteran.hack.gui.rgui.util.Docking;
import com.veteran.hack.gui.rgui.component.Component;
import com.veteran.hack.gui.rgui.util.ContainerHelper;
import com.veteran.hack.gui.rgui.component.container.use.Frame;
import com.veteran.hack.gui.rgui.component.listen.RenderListener;
import com.veteran.hack.gui.rgui.component.use.Label;

public class ActiveModules extends Label
{
    public boolean sort_up;
    
    public ActiveModules() {
        super("");
        this.sort_up = true;
        this.addRenderListener(new RenderListener() {
            @Override
            public void onPreRender() {
                final Frame parentFrame = ContainerHelper.getFirstParent((Class<? extends Frame>)Frame.class, (Component)ActiveModules.this);
                if (parentFrame == null) {
                    return;
                }
                final Docking docking = parentFrame.getDocking();
                if (docking.isTop()) {
                    ActiveModules.this.sort_up = true;
                }
                if (docking.isBottom()) {
                    ActiveModules.this.sort_up = false;
                }
            }
            
            @Override
            public void onPostRender() {
            }
        });
    }
}
