// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui;

import java.util.Iterator;
import com.veteran.hack.gui.kami.KamiGUI;
import com.veteran.hack.gui.rgui.component.listen.RenderListener;
import net.minecraft.client.renderer.GlStateManager;
import com.veteran.hack.gui.rgui.component.container.use.Frame;
import com.veteran.hack.gui.rgui.component.Component;
import org.lwjgl.opengl.GL11;
import com.veteran.hack.BaseMod;
import com.veteran.hack.util.Wrapper;
import com.veteran.hack.gui.kami.DisplayGuiScreen;

public class UIRenderer
{
    public static void renderAndUpdateFrames() {
        if (Wrapper.getMinecraft().currentScreen instanceof DisplayGuiScreen || Wrapper.getMinecraft().gameSettings.showDebugInfo) {
            return;
        }
        final KamiGUI gui = BaseMod.getInstance().getGuiManager();
        GL11.glDisable(3553);
        for (final Component c : gui.getChildren()) {
            if (c instanceof Frame) {
                GlStateManager.pushMatrix();
                final Frame child = (Frame)c;
                if (child.isPinned() && child.isVisible()) {
                    final boolean slide = child.getOpacity() != 0.0f;
                    GL11.glTranslated((double)child.getX(), (double)child.getY(), 0.0);
                    child.getRenderListeners().forEach(renderListener -> renderListener.onPreRender());
                    child.getTheme().getUIForComponent(child).renderComponent(child, child.getTheme().getFontRenderer());
                    int translateX = 0;
                    int translateY = 0;
                    if (slide) {
                        translateX += child.getOriginOffsetX();
                        translateY += child.getOriginOffsetY();
                    }
                    else {
                        if (child.getDocking().isBottom()) {
                            translateY += child.getOriginOffsetY();
                        }
                        if (child.getDocking().isRight()) {
                            translateX += child.getOriginOffsetX();
                            if (child.getChildren().size() > 0) {
                                translateX += (child.getWidth() - child.getChildren().get(0).getX() - child.getChildren().get(0).getWidth()) / DisplayGuiScreen.getScale();
                            }
                        }
                        if (child.getDocking().isLeft() && child.getChildren().size() > 0) {
                            translateX -= child.getChildren().get(0).getX();
                        }
                        if (child.getDocking().isTop() && child.getChildren().size() > 0) {
                            translateY -= child.getChildren().get(0).getY();
                        }
                    }
                    GL11.glTranslated((double)translateX, (double)translateY, 0.0);
                    child.getRenderListeners().forEach(RenderListener::onPostRender);
                    child.renderChildren();
                    GL11.glTranslated((double)(-translateX), (double)(-translateY), 0.0);
                    GL11.glTranslated((double)(-child.getX()), (double)(-child.getY()), 0.0);
                }
                GlStateManager.popMatrix();
            }
        }
        GL11.glEnable(3553);
        GL11.glEnable(3042);
        GlStateManager.enableBlend();
    }
}
