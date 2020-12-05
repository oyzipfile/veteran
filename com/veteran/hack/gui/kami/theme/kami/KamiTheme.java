// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.kami.theme.kami;

import com.veteran.hack.gui.rgui.render.AbstractComponentUI;
import com.veteran.hack.gui.kami.KamiGUI;
import com.veteran.hack.gui.rgui.render.ComponentUI;
import com.veteran.hack.gui.rgui.render.font.FontRenderer;
import com.veteran.hack.gui.rgui.render.theme.AbstractTheme;

public class KamiTheme extends AbstractTheme
{
    FontRenderer fontRenderer;
    
    public KamiTheme() {
        this.installUI(new RootButtonUI<Object>());
        this.installUI(new GUIUI());
        this.installUI(new RootGroupboxUI());
        this.installUI(new KamiFrameUI<Object>());
        this.installUI(new RootScrollpaneUI());
        this.installUI(new RootInputFieldUI<Object>());
        this.installUI(new RootLabelUI<Object>());
        this.installUI(new RootChatUI());
        this.installUI(new RootCheckButtonUI<Object>());
        this.installUI(new KamiActiveModulesUI());
        this.installUI(new KamiSettingsPanelUI());
        this.installUI(new RootSliderUI());
        this.installUI(new KamiEnumButtonUI());
        this.installUI(new RootColorizedCheckButtonUI());
        this.installUI(new KamiUnboundSliderUI());
        this.fontRenderer = KamiGUI.fontRenderer;
    }
    
    @Override
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public class GUIUI extends AbstractComponentUI<KamiGUI>
    {
    }
}
