// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.util;

import com.veteran.hack.module.modules.gui.InfoOverlay;
import net.minecraft.util.text.TextFormatting;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TimeUtil
{
    public static String time(final SimpleDateFormat format) {
        final Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }
    
    private static String formatTimeString(final TimeType timeType) {
        switch (timeType) {
            case HHMM: {
                return ":mm";
            }
            case HHMMSS: {
                return ":mm:ss";
            }
            default: {
                return "";
            }
        }
    }
    
    public static SimpleDateFormat dateFormatter(final TimeUnit timeUnit, final TimeType timeType) {
        SimpleDateFormat formatter = null;
        switch (timeUnit) {
            case H12: {
                formatter = new SimpleDateFormat("hh" + formatTimeString(timeType), Locale.UK);
                break;
            }
            case H24: {
                formatter = new SimpleDateFormat("HH" + formatTimeString(timeType), Locale.UK);
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected value: " + timeUnit);
            }
        }
        return formatter;
    }
    
    public static String getFinalTime(final TextFormatting colourCode2, final TextFormatting colourCode1, final TimeUnit timeUnit, final TimeType timeType, final Boolean doLocale) {
        String locale = "";
        final String time = time(dateFormatter(TimeUnit.H24, TimeType.HH));
        if (timeUnit == TimeUnit.H12 && doLocale) {
            if (Integer.parseInt(time) - 12 >= 0) {
                locale = "pm";
            }
            else {
                locale = "am";
            }
        }
        return InfoOverlay.getStringColour(colourCode1) + time(dateFormatter(timeUnit, timeType)) + InfoOverlay.getStringColour(colourCode2) + locale;
    }
    
    public enum TimeType
    {
        HHMM, 
        HHMMSS, 
        HH;
    }
    
    public enum TimeUnit
    {
        H24, 
        H12;
    }
}
