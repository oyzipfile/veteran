// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.kami.theme.kami;

import com.veteran.hack.gui.rgui.component.Component;
import com.veteran.hack.gui.rgui.component.container.Container;
import org.lwjgl.opengl.GL11;
import com.veteran.hack.gui.rgui.render.font.FontRenderer;
import com.veteran.hack.gui.rgui.component.container.use.Groupbox;
import com.veteran.hack.gui.rgui.render.AbstractComponentUI;

public class RootGroupboxUI extends AbstractComponentUI<Groupbox>
{
    @Override
    public void renderComponent(final Groupbox component, final FontRenderer fontRenderer) {
        GL11.glLineWidth(1.0f);
        fontRenderer.drawString(1, 1, component.getName());
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        GL11.glDisable(3553);
        GL11.glBegin(1);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glVertex2d((double)component.getWidth(), 0.0);
        GL11.glVertex2d((double)component.getWidth(), 0.0);
        GL11.glVertex2d((double)component.getWidth(), (double)component.getHeight());
        GL11.glVertex2d((double)component.getWidth(), (double)component.getHeight());
        GL11.glVertex2d(0.0, (double)component.getHeight());
        GL11.glVertex2d(0.0, (double)component.getHeight());
        GL11.glVertex2d(0.0, 0.0);
        GL11.glEnd();
    }
    
    @Override
    public void handleMouseDown(final Groupbox component, final int x, final int y, final int button) {
    }
    
    @Override
    public void handleAddComponent(final Groupbox component, final Container container) {
        component.setWidth(100);
        component.setHeight(100);
        component.setOriginOffsetY(component.getTheme().getFontRenderer().getFontHeight() + 3);
    }
}
