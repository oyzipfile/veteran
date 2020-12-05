// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import net.minecraft.entity.player.EntityPlayer;
import com.veteran.hack.util.EntityUtil;
import java.util.function.Predicate;
import net.minecraft.pathfinding.PathPoint;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.modules.render.Pathfind;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "AutoWalk", category = Category.HIDDEN, description = "Automatically walks forward")
public class AutoWalk extends Module
{
    private Setting<AutoWalkMode> mode;
    @EventHandler
    private Listener<InputUpdateEvent> inputUpdateEventListener;
    
    public AutoWalk() {
        this.mode = this.register(Settings.e("Mode", AutoWalkMode.FORWARD));
        PathPoint next;
        this.inputUpdateEventListener = new Listener<InputUpdateEvent>(event -> {
            switch (this.mode.getValue()) {
                case FORWARD: {
                    event.getMovementInput().moveForward = 1.0f;
                    break;
                }
                case BACKWARDS: {
                    event.getMovementInput().moveForward = -1.0f;
                    break;
                }
                case PATH: {
                    if (Pathfind.points.isEmpty()) {
                        return;
                    }
                    else {
                        event.getMovementInput().moveForward = 1.0f;
                        if (AutoWalk.mc.player.isInWater() || AutoWalk.mc.player.isInLava()) {
                            AutoWalk.mc.player.movementInput.jump = true;
                        }
                        else if (AutoWalk.mc.player.collidedHorizontally && AutoWalk.mc.player.onGround) {
                            AutoWalk.mc.player.jump();
                        }
                        if (!ModuleManager.isModuleEnabled("Pathfind") || Pathfind.points.isEmpty()) {
                            return;
                        }
                        else {
                            next = Pathfind.points.get(0);
                            this.lookAt(next);
                            break;
                        }
                    }
                    break;
                }
            }
        }, (Predicate<InputUpdateEvent>[])new Predicate[0]);
    }
    
    private void lookAt(final PathPoint pathPoint) {
        final double[] v = EntityUtil.calculateLookAt(pathPoint.x + 0.5f, pathPoint.y, pathPoint.z + 0.5f, (EntityPlayer)AutoWalk.mc.player);
        AutoWalk.mc.player.rotationYaw = (float)v[0];
        AutoWalk.mc.player.rotationPitch = (float)v[1];
    }
    
    private enum AutoWalkMode
    {
        FORWARD, 
        BACKWARDS, 
        PATH;
    }
}
