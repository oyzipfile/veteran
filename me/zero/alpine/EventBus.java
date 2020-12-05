// 
// Decompiled by Procyon v0.5.36
// 

package me.zero.alpine;

public interface EventBus
{
    void subscribe(final Object p0);
    
    void subscribeAll(final Object... p0);
    
    void subscribeAll(final Iterable<Object> p0);
    
    void unsubscribe(final Object p0);
    
    void unsubscribeAll(final Object... p0);
    
    void unsubscribeAll(final Iterable<Object> p0);
    
    void post(final Object p0);
    
    void attach(final EventBus p0);
    
    void detach(final EventBus p0);
}
