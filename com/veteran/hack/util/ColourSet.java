// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.util;

import java.awt.Color;

public class ColourSet
{
    public static Color bgColour;
    public static Color bgColourHover;
    public static double bgColourOther;
    public static Color buttonPressed;
    public static Color buttonIdleN;
    public static Color buttonHoveredN;
    public static Color buttonIdleT;
    public static Color buttonHoveredT;
    public static Color windowOutline;
    public static float windowOutlineWidth;
    public static Color pinnedWindow;
    public static double unpinnedWindow;
    public static double lineWindow;
    public static Color sliderColour;
    public static Color enumColour;
    public static Color chatOutline;
    public static Color scrollBar;
    
    static {
        ColourSet.bgColour = new Color(55, 63, 81);
        ColourSet.bgColourHover = new Color(55, 63, 81);
        ColourSet.bgColourOther = 229.5;
        ColourSet.buttonPressed = new Color(191, 71, 127).darker();
        ColourSet.buttonIdleN = new Color(120, 120, 120);
        ColourSet.buttonHoveredN = new Color(220, 220, 220);
        ColourSet.buttonIdleT = new Color(191, 71, 127);
        ColourSet.buttonHoveredT = new Color(220, 220, 220);
        ColourSet.windowOutline = new Color(60, 60, 60);
        ColourSet.windowOutlineWidth = 2.8f;
        ColourSet.pinnedWindow = new Color(90, 90, 90);
        ColourSet.unpinnedWindow = 168.3;
        ColourSet.lineWindow = 100.0;
        ColourSet.sliderColour = new Color(80, 80, 80);
        ColourSet.enumColour = new Color(80, 80, 80);
        ColourSet.chatOutline = new Color(80, 80, 80);
        ColourSet.scrollBar = new Color(80, 80, 80);
    }
}
