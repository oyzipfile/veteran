// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.util;

import org.lwjgl.opengl.Display;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import java.util.List;
import com.veteran.hack.gui.kami.KamiGUI;
import com.veteran.hack.gui.rgui.component.container.Container;
import com.veteran.hack.gui.rgui.util.ContainerHelper;
import com.veteran.hack.BaseMod;
import com.veteran.hack.gui.rgui.component.container.use.Frame;

public class GuiFrameUtil
{
    public static Frame getFrameByName(final String name) {
        final KamiGUI kamiGUI = BaseMod.getInstance().getGuiManager();
        if (kamiGUI == null) {
            return null;
        }
        final List<Frame> frames = ContainerHelper.getAllChildren((Class<? extends Frame>)Frame.class, (Container)kamiGUI);
        for (final Frame frame : frames) {
            if (frame.getTitle().equalsIgnoreCase(name)) {
                return frame;
            }
        }
        return null;
    }
    
    public static Frame getFrameByName(final KamiGUI kamiGUI, final String name) {
        if (kamiGUI == null) {
            return null;
        }
        final List<Frame> frames = ContainerHelper.getAllChildren((Class<? extends Frame>)Frame.class, (Container)kamiGUI);
        for (final Frame frame : frames) {
            if (frame.getTitle().equalsIgnoreCase(name)) {
                return frame;
            }
        }
        return null;
    }
    
    public static void fixFrames(final Minecraft mc) {
        final KamiGUI kamiGUI = BaseMod.getInstance().getGuiManager();
        if (kamiGUI == null) {
            return;
        }
        final List<Frame> frames = ContainerHelper.getAllChildren((Class<? extends Frame>)Frame.class, (Container)kamiGUI);
        for (final Frame frame : frames) {
            int divider = mc.gameSettings.guiScale;
            if (divider == 0) {
                divider = 3;
            }
            if (frame.getX() > Display.getWidth() / divider) {
                frame.setX(Display.getWidth() / divider - frame.getWidth());
            }
            if (frame.getY() > Display.getHeight() / divider) {
                frame.setY(Display.getHeight() / divider - frame.getHeight());
            }
        }
    }
}
