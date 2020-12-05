// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml;

public class LoaderOptions
{
    private boolean allowDuplicateKeys;
    private boolean wrappedToRootException;
    
    public LoaderOptions() {
        this.allowDuplicateKeys = true;
        this.wrappedToRootException = false;
    }
    
    public boolean isAllowDuplicateKeys() {
        return this.allowDuplicateKeys;
    }
    
    public void setAllowDuplicateKeys(final boolean allowDuplicateKeys) {
        this.allowDuplicateKeys = allowDuplicateKeys;
    }
    
    public boolean isWrappedToRootException() {
        return this.wrappedToRootException;
    }
    
    public void setWrappedToRootException(final boolean wrappedToRootException) {
        this.wrappedToRootException = wrappedToRootException;
    }
}
