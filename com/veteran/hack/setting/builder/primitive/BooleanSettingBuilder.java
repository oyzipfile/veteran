// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.setting.builder.primitive;

import com.veteran.hack.setting.Setting;
import com.veteran.hack.setting.impl.BooleanSetting;
import com.veteran.hack.setting.builder.SettingBuilder;

public class BooleanSettingBuilder extends SettingBuilder<Boolean>
{
    @Override
    public BooleanSetting build() {
        return new BooleanSetting((Boolean)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
    }
    
    @Override
    public BooleanSettingBuilder withName(final String name) {
        return (BooleanSettingBuilder)super.withName(name);
    }
}
