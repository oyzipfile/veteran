// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.chat;

class StackInfo
{
    int i;
    ItemAction action;
    String name;
    
    public StackInfo(final int i, final ItemAction action, final String name) {
        this.i = i;
        this.action = action;
        this.name = name;
    }
    
    public int getUsed() {
        return this.i;
    }
    
    public ItemAction getAction() {
        return this.action;
    }
    
    public String getName() {
        return this.name;
    }
}
