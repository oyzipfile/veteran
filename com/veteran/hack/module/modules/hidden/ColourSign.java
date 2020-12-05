// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.hidden;

import net.minecraft.util.text.ITextComponent;
import java.io.IOException;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntitySign;
import java.util.function.Predicate;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.GuiScreenEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.module.Module;

@Info(name = "ColourSign", description = "Allows ingame colouring of text on signs", category = Category.HIDDEN)
public class ColourSign extends Module
{
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> eventListener;
    
    public ColourSign() {
        this.eventListener = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (event.getScreen() instanceof GuiEditSign && this.isEnabled()) {
                event.setScreen((GuiScreen)new KamiGuiEditSign(((GuiEditSign)event.getScreen()).tileSign));
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
    
    private class KamiGuiEditSign extends GuiEditSign
    {
        public KamiGuiEditSign(final TileEntitySign teSign) {
            super(teSign);
        }
        
        public void initGui() {
            super.initGui();
        }
        
        protected void actionPerformed(final GuiButton button) throws IOException {
            if (button.id == 0) {
                this.tileSign.signText[this.editLine] = (ITextComponent)new TextComponentString(this.tileSign.signText[this.editLine].getFormattedText().replaceAll("(§)(.)", "$1$1$2$2"));
            }
            super.actionPerformed(button);
        }
        
        protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
            super.keyTyped(typedChar, keyCode);
            String s = ((TextComponentString)this.tileSign.signText[this.editLine]).getText();
            s = s.replace("&", "§");
            this.tileSign.signText[this.editLine] = (ITextComponent)new TextComponentString(s);
        }
    }
}
