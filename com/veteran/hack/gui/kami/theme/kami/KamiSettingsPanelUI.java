// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.kami.theme.kami;

import com.veteran.hack.gui.rgui.component.Component;
import com.veteran.hack.gui.kami.RenderHelper;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.modules.experimental.GUIColour;
import org.lwjgl.opengl.GL11;
import com.veteran.hack.gui.rgui.render.font.FontRenderer;
import com.veteran.hack.gui.kami.component.SettingsPanel;
import com.veteran.hack.gui.rgui.render.AbstractComponentUI;

public class KamiSettingsPanelUI extends AbstractComponentUI<SettingsPanel>
{
    @Override
    public void renderComponent(final SettingsPanel component, final FontRenderer fontRenderer) {
        super.renderComponent(component, fontRenderer);
        GL11.glLineWidth(2.0f);
        final float red = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).red.getValue() / 255.0f;
        final float green = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).green.getValue() / 255.0f;
        final float blue = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).blue.getValue() / 255.0f;
        final float alpha = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).alpha.getValue() / 255.0f;
        if (ModuleManager.getModuleByName("GUI Colour").isEnabled()) {
            GL11.glColor4f(red, green, blue, alpha);
        }
        else {
            GL11.glColor4f(0.17f, 0.17f, 0.18f, 0.9f);
        }
        RenderHelper.drawFilledRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
    }
}
