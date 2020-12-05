// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.setting.impl;

import com.google.common.base.Converter;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import com.veteran.hack.setting.converter.EnumConverter;
import com.veteran.hack.setting.Setting;

public class EnumSetting<T extends Enum> extends Setting<T>
{
    private EnumConverter converter;
    public final Class<? extends Enum> clazz;
    
    public EnumSetting(final T value, final Predicate<T> restriction, final BiConsumer<T, T> consumer, final String name, final Predicate<T> visibilityPredicate, final Class<? extends Enum> clazz) {
        super(value, restriction, consumer, name, visibilityPredicate);
        this.converter = new EnumConverter(clazz, value);
        this.clazz = clazz;
    }
    
    @Override
    public Converter converter() {
        return this.converter;
    }
}
