// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.setting.impl.numerical;

import com.google.common.base.Converter;
import com.veteran.hack.setting.converter.AbstractBoxedNumberConverter;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import com.veteran.hack.setting.converter.BoxedFloatConverter;

public class FloatSetting extends NumberSetting<Float>
{
    private static final BoxedFloatConverter converter;
    
    public FloatSetting(final Float value, final Predicate<Float> restriction, final BiConsumer<Float, Float> consumer, final String name, final Predicate<Float> visibilityPredicate, final Float min, final Float max) {
        super(value, restriction, consumer, name, visibilityPredicate, min, max);
    }
    
    @Override
    public AbstractBoxedNumberConverter converter() {
        return FloatSetting.converter;
    }
    
    static {
        converter = new BoxedFloatConverter();
    }
}
