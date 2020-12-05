// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.kami;

import net.minecraft.client.Minecraft;
import com.veteran.hack.module.ModuleManager;
import org.lwjgl.input.Mouse;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.util.Iterator;
import com.veteran.hack.util.Wrapper;
import com.veteran.hack.gui.rgui.component.container.use.Frame;
import com.veteran.hack.gui.rgui.component.Component;
import com.veteran.hack.BaseMod;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.gui.GuiScreen;

public class DisplayGuiScreen extends GuiScreen
{
    KamiGUI gui;
    public final GuiScreen lastScreen;
    public static int mouseX;
    public static int mouseY;
    Framebuffer framebuffer;
    
    public DisplayGuiScreen(final GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
        final KamiGUI gui = BaseMod.getInstance().getGuiManager();
        for (final Component c : gui.getChildren()) {
            if (c instanceof Frame) {
                final Frame child = (Frame)c;
                if (!child.isPinnable() || !child.isVisible()) {
                    continue;
                }
                child.setOpacity(0.5f);
            }
        }
        this.framebuffer = new Framebuffer(Wrapper.getMinecraft().displayWidth, Wrapper.getMinecraft().displayHeight, false);
    }
    
    public void onGuiClosed() {
        final KamiGUI gui = BaseMod.getInstance().getGuiManager();
        gui.getChildren().stream().filter(component -> component instanceof Frame && component.isPinnable() && component.isVisible()).forEach(component -> component.setOpacity(0.0f));
    }
    
    public void initGui() {
        this.gui = BaseMod.getInstance().getGuiManager();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.calculateMouse();
        this.gui.drawGUI();
        GL11.glEnable(3553);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.gui.handleMouseDown(DisplayGuiScreen.mouseX, DisplayGuiScreen.mouseY);
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.gui.handleMouseRelease(DisplayGuiScreen.mouseX, DisplayGuiScreen.mouseY);
    }
    
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.gui.handleMouseDrag(DisplayGuiScreen.mouseX, DisplayGuiScreen.mouseY);
    }
    
    public void updateScreen() {
        if (Mouse.hasWheel()) {
            final int a = Mouse.getDWheel();
            if (a != 0) {
                this.gui.handleWheel(DisplayGuiScreen.mouseX, DisplayGuiScreen.mouseY, a);
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
        if (ModuleManager.getModuleByName("clickGUI").getBind().isDown(keyCode) || keyCode == 1) {
            this.mc.displayGuiScreen(this.lastScreen);
        }
        else {
            this.gui.handleKeyDown(keyCode);
            this.gui.handleKeyUp(keyCode);
        }
    }
    
    public static int getScale() {
        int scaleFactor = 0;
        int scale = Wrapper.getMinecraft().gameSettings.guiScale;
        if (scale == 0) {
            scale = 1000;
        }
        while (scaleFactor < scale && Wrapper.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Wrapper.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        if (scaleFactor == 0) {
            scaleFactor = 1;
        }
        return scaleFactor;
    }
    
    private void calculateMouse() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final int scaleFactor = getScale();
        DisplayGuiScreen.mouseX = Mouse.getX() / scaleFactor;
        DisplayGuiScreen.mouseY = minecraft.displayHeight / scaleFactor - Mouse.getY() / scaleFactor - 1;
    }
}
