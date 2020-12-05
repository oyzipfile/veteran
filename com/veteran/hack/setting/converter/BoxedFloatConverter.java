// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.setting.converter;

import com.google.gson.JsonElement;

public class BoxedFloatConverter extends AbstractBoxedNumberConverter<Float>
{
    protected Float doBackward(final JsonElement s) {
        return s.getAsFloat();
    }
}
