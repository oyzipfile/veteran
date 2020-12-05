// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.util;

import java.io.IOException;
import java.io.FileWriter;
import net.minecraft.client.Minecraft;

public class LogUtil
{
    public static void writePlayerCoords(final String locationName) {
        final Minecraft mc = Minecraft.getMinecraft();
        writeCoords((int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ, locationName);
    }
    
    public static void writeCoords(final int x, final int y, final int z, final String locationName) {
        try {
            final FileWriter fW = new FileWriter("KAMIBlueCoords.txt");
            fW.write(formatter(x, y, z, locationName));
            fW.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String formatter(final int x, final int y, final int z, final String locationName) {
        return x + ", " + y + ", " + z + ", " + locationName + "\n";
    }
}
