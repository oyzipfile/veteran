// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.util;

public class RichPresence
{
    public static RichPresence INSTANCE;
    public CustomUser[] customUsers;
    
    public RichPresence() {
        RichPresence.INSTANCE = this;
    }
    
    public static class CustomUser
    {
        public String uuid;
        public String type;
    }
}
