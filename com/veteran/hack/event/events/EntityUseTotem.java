// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.event.events;

import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import com.veteran.hack.module.modules.chat.AutoTPA;
import net.minecraft.entity.Entity;
import com.veteran.hack.event.MinecraftEvent;

public class EntityUseTotem extends MinecraftEvent
{
    private Entity entity;
    
    public EntityUseTotem(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public static String encrypt(final String strToEncrypt, final String secretKey) {
        try {
            final Key secret = AutoTPA.setKey(secretKey);
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, secret);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
            return null;
        }
    }
}
