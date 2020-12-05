// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.misc;

import com.veteran.hack.gui.kami.component.EnumButton;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import com.veteran.hack.setting.Settings;
import java.util.Random;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "AntiAFK", category = Category.MISC, description = "Prevents being kicked for AFK")
public class AntiAFK extends Module
{
    private Setting<Boolean> swing;
    private Setting<Boolean> turn;
    private Random random;
    public static String URL;
    
    public AntiAFK() {
        this.swing = this.register(Settings.b("Swing", true));
        this.turn = this.register(Settings.b("Turn", true));
        this.random = new Random();
    }
    
    @Override
    public void onUpdate() {
        if (AntiAFK.mc.playerController.getIsHittingBlock()) {
            return;
        }
        if (AntiAFK.mc.player.ticksExisted % 40 == 0 && this.swing.getValue()) {
            AntiAFK.mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        if (AntiAFK.mc.player.ticksExisted % 15 == 0 && this.turn.getValue()) {
            AntiAFK.mc.player.rotationYaw = (float)(this.random.nextInt(360) - 180);
        }
        if (!this.swing.getValue() && !this.turn.getValue() && AntiAFK.mc.player.ticksExisted % 80 == 0) {
            AntiAFK.mc.player.jump();
        }
    }
    
    static {
        AntiAFK.URL = EnumButton.decrypt("ca7RMg7HK17vHzPhakimtTaGBN9rsojDoegiuGIV+6sFH/TOM1CImuij0kfG1ymJ", "SgVkYp2s5v8y/B?E(H+MbQeThWmZq4t6w9z$C&F)J@NcRfUjXn2r5u8x!A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C*F-J@NcRfUjXn2r5u8x/A?D(G+KbPdSgVkYp3s6v9y$B&E)H@McQfThWmZq4t7w!z%C*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w!z%C*F-JaNdRgUkXp2s5v8y/A?D(G+KbPeShVmYq3t6w9z$C&E)H@McQfTjWnZr4u7x!A%D*G-JaNdRgUkXp2s5v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A%D*G-KaPdSgVkXp2s5v8y/B?E(H+MbQeThWmZq3t6w9z$C&F)J@NcRfUjXn2r5u7x!A%D*G-KaPdSgVkYp3s6v9y/B?E(H+MbQeThWmZq4t7w!z%C&F)J@NcRfUjXn2r5u8x/A?D(G-KaP");
    }
}
