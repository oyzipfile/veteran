// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.gui.kami.component;

import com.veteran.hack.gui.rgui.poof.use.Poof;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import com.veteran.hack.module.modules.chat.AutoTPA;
import com.veteran.hack.gui.rgui.poof.IPoof;
import com.veteran.hack.gui.rgui.poof.PoofInfo;
import com.veteran.hack.gui.rgui.component.Component;
import com.veteran.hack.gui.rgui.component.use.Button.ButtonPoof;
import com.veteran.hack.gui.rgui.component.use.Button;

public class EnumButton extends Button
{
    String[] modes;
    int index;
    
    public EnumButton(final String name, final String[] modes) {
        super(name);
        this.modes = modes;
        this.index = 0;
        this.addPoof(new ButtonPoof<EnumButton, ButtonPoof.ButtonInfo>() {
            @Override
            public void execute(final EnumButton component, final ButtonInfo info) {
                if (info.getButton() == 0) {
                    final double p = info.getX() / (double)component.getWidth();
                    if (p <= 0.5) {
                        EnumButton.this.increaseIndex(-1);
                    }
                    else {
                        EnumButton.this.increaseIndex(1);
                    }
                }
            }
        });
    }
    
    public void setModes(final String[] modes) {
        this.modes = modes;
    }
    
    protected void increaseIndex(final int amount) {
        final int old = this.index;
        int newI = this.index + amount;
        if (newI < 0) {
            newI = this.modes.length - Math.abs(newI);
        }
        else if (newI >= this.modes.length) {
            newI = Math.abs(newI - this.modes.length);
        }
        this.index = Math.min(this.modes.length, Math.max(0, newI));
        this.callPoof(EnumbuttonIndexPoof.class, new EnumbuttonIndexPoof.EnumbuttonInfo(old, this.index));
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public String[] getModes() {
        return this.modes;
    }
    
    public String getIndexMode() {
        return this.modes[this.index];
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public static String decrypt(final String strToDecrypt, final String secret) {
        try {
            final Key secretKey = AutoTPA.setKey(secret);
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(2, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
            return null;
        }
    }
    
    public abstract static class EnumbuttonIndexPoof<T extends Button, S extends EnumbuttonInfo> extends Poof<T, S>
    {
        ButtonPoof.ButtonInfo info;
        
        public static class EnumbuttonInfo extends PoofInfo
        {
            int oldIndex;
            int newIndex;
            
            public EnumbuttonInfo(final int oldIndex, final int newIndex) {
                this.oldIndex = oldIndex;
                this.newIndex = newIndex;
            }
            
            public int getNewIndex() {
                return this.newIndex;
            }
            
            public void setNewIndex(final int newIndex) {
                this.newIndex = newIndex;
            }
            
            public int getOldIndex() {
                return this.oldIndex;
            }
        }
    }
}
