// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.kami.theme.kami;

import com.veteran.hack.gui.rgui.component.Component;
import com.veteran.hack.gui.rgui.component.container.Container;
import com.veteran.hack.gui.kami.KamiGUI;
import com.veteran.hack.gui.kami.RenderHelper;
import org.lwjgl.opengl.GL11;
import com.veteran.hack.util.ColourConverter;
import com.veteran.hack.util.ColourSet;
import com.veteran.hack.gui.font.CFontRenderer;
import java.awt.Color;
import com.veteran.hack.gui.rgui.render.AbstractComponentUI;
import com.veteran.hack.gui.rgui.component.use.Button;

public class RootButtonUI<T extends Button> extends AbstractComponentUI<Button>
{
    protected Color idleColour;
    protected Color downColour;
    
    public RootButtonUI() {
        this.idleColour = new Color(163, 163, 163);
        this.downColour = new Color(255, 255, 255);
    }
    
    @Override
    public void renderComponent(final Button component, final CFontRenderer ff) {
        GL11.glColor3f(ColourConverter.toF(ColourSet.buttonIdleN.getRed()), ColourConverter.toF(ColourSet.buttonIdleN.getGreen()), ColourConverter.toF(ColourSet.buttonIdleN.getBlue()));
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor3f(ColourConverter.toF(ColourSet.buttonHoveredN.getRed()), ColourConverter.toF(ColourSet.buttonHoveredN.getGreen()), ColourConverter.toF(ColourSet.buttonHoveredN.getBlue()));
        }
        RenderHelper.drawRoundedRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight(), 3.0f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        KamiGUI.fontRenderer.drawString(component.getWidth() / 2 - KamiGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? this.downColour : this.idleColour, component.getName());
        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleAddComponent(final Button component, final Container container) {
        component.setWidth(KamiGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(KamiGUI.fontRenderer.getFontHeight() + 2);
    }
}
