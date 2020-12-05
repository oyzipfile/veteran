// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.Module;

@Info(name = "Sprint", description = "Automatically makes the player sprint", category = Category.MOVEMENT, showOnArray = ShowOnArray.OFF)
public class Sprint extends Module
{
    @Override
    public void onUpdate() {
        if (Sprint.mc.player == null) {
            return;
        }
        if (ModuleManager.getModuleByName("ElytraFlight").isEnabled() && (Sprint.mc.player.isElytraFlying() || Sprint.mc.player.capabilities.isFlying)) {
            return;
        }
        try {
            if (!Sprint.mc.player.collidedHorizontally && Sprint.mc.player.moveForward > 0.0f) {
                Sprint.mc.player.setSprinting(true);
            }
            else {
                Sprint.mc.player.setSprinting(false);
            }
        }
        catch (Exception ex) {}
    }
}
