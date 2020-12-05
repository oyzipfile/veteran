// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.kami.theme.kami;

import com.veteran.hack.gui.rgui.poof.IPoof;
import com.veteran.hack.gui.rgui.poof.PoofInfo;
import com.veteran.hack.gui.rgui.component.Component;
import com.veteran.hack.gui.kami.component.EnumButton.EnumbuttonIndexPoof;
import com.veteran.hack.gui.rgui.component.container.Container;
import com.veteran.hack.util.ColourConverter;
import com.veteran.hack.util.ColourSet;
import org.lwjgl.opengl.GL11;
import com.veteran.hack.gui.rgui.render.font.FontRenderer;
import java.awt.Color;
import com.veteran.hack.gui.kami.RootSmallFontRenderer;
import com.veteran.hack.gui.kami.component.EnumButton;
import com.veteran.hack.gui.rgui.render.AbstractComponentUI;

public class KamiEnumButtonUI extends AbstractComponentUI<EnumButton>
{
    RootSmallFontRenderer smallFontRenderer;
    protected Color idleColour;
    protected Color downColour;
    EnumButton modeComponent;
    long lastMS;
    
    public KamiEnumButtonUI() {
        this.smallFontRenderer = new RootSmallFontRenderer();
        this.idleColour = new Color(163, 163, 163);
        this.downColour = new Color(255, 255, 255);
        this.lastMS = System.currentTimeMillis();
    }
    
    @Override
    public void renderComponent(final EnumButton component, final FontRenderer aa) {
        if (System.currentTimeMillis() - this.lastMS > 3000L && this.modeComponent != null) {
            this.modeComponent = null;
        }
        int c = component.isPressed() ? 11184810 : 14540253;
        if (component.isHovered()) {
            c = (c & 0x7F7F7F) << 1;
        }
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        final int parts = component.getModes().length;
        final double step = component.getWidth() / (double)parts;
        final double startX = step * component.getIndex();
        final double endX = step * (component.getIndex() + 1);
        final int height = component.getHeight();
        final float downscale = 1.1f;
        GL11.glDisable(3553);
        GL11.glColor3f(ColourConverter.toF(ColourSet.enumColour.getRed()), ColourConverter.toF(ColourSet.enumColour.getGreen()), ColourConverter.toF(ColourSet.enumColour.getBlue()));
        GL11.glBegin(1);
        GL11.glVertex2d(startX, (double)(height / downscale));
        GL11.glVertex2d(endX, (double)(height / downscale));
        GL11.glEnd();
        if (this.modeComponent == null || !this.modeComponent.equals(component)) {
            this.smallFontRenderer.drawString(0, 0, c, component.getName());
            this.smallFontRenderer.drawString(component.getWidth() - this.smallFontRenderer.getStringWidth(component.getIndexMode()), 0, c, component.getIndexMode());
        }
        else {
            this.smallFontRenderer.drawString(component.getWidth() / 2 - this.smallFontRenderer.getStringWidth(component.getIndexMode()) / 2, 0, c, component.getIndexMode());
        }
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleSizeComponent(final EnumButton component) {
        int width = 0;
        for (final String s : component.getModes()) {
            width = Math.max(width, this.smallFontRenderer.getStringWidth(s));
        }
        component.setWidth(this.smallFontRenderer.getStringWidth(component.getName()) + width + 1);
        component.setHeight(this.smallFontRenderer.getFontHeight() + 2);
    }
    
    @Override
    public void handleAddComponent(final EnumButton component, final Container container) {
        component.addPoof(new EnumButton.EnumbuttonIndexPoof<EnumButton, EnumButton.EnumbuttonIndexPoof.EnumbuttonInfo>() {
            @Override
            public void execute(final EnumButton component, final EnumbuttonInfo info) {
                KamiEnumButtonUI.this.modeComponent = component;
                KamiEnumButtonUI.this.lastMS = System.currentTimeMillis();
            }
        });
    }
}
