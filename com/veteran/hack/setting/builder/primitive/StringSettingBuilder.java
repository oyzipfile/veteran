// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.setting.builder.primitive;

import com.veteran.hack.setting.Setting;
import com.veteran.hack.setting.impl.StringSetting;
import com.veteran.hack.setting.builder.SettingBuilder;

public class StringSettingBuilder extends SettingBuilder<String>
{
    @Override
    public StringSetting build() {
        return new StringSetting((String)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
    }
}
