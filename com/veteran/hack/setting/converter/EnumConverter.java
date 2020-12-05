// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.setting.converter;

import java.util.Arrays;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.google.common.base.Converter;

public class EnumConverter extends Converter<Enum, JsonElement>
{
    Class<? extends Enum> clazz;
    Enum value;
    
    public EnumConverter(final Class<? extends Enum> clazz, final Enum value) {
        this.clazz = clazz;
        this.value = value;
    }
    
    protected JsonElement doForward(final Enum anEnum) {
        return (JsonElement)new JsonPrimitive(anEnum.name());
    }
    
    protected Enum doBackward(final JsonElement jsonElement) {
        if (Arrays.toString((Object[])this.clazz.getEnumConstants()).contains(jsonElement.getAsString())) {
            try {
                return (Enum)Enum.valueOf(this.clazz, jsonElement.getAsString());
            }
            catch (IllegalArgumentException e) {
                for (final Enum enumConstant : (Enum[])this.clazz.getEnumConstants()) {
                    if (enumConstant.name().equalsIgnoreCase(jsonElement.getAsString())) {
                        return enumConstant;
                    }
                }
                return (Enum)Enum.valueOf(this.clazz, "null");
            }
        }
        try {
            return (Enum)Enum.valueOf(this.clazz, this.value.toString());
        }
        catch (IllegalArgumentException e) {
            for (final Enum enumConstant : (Enum[])this.clazz.getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(jsonElement.getAsString())) {
                    return enumConstant;
                }
            }
            return (Enum)Enum.valueOf(this.clazz, "null");
        }
    }
}
