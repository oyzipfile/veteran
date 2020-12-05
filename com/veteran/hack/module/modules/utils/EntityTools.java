// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.utils;

import java.util.function.Predicate;
import com.veteran.hack.command.Command;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "EntityTools", category = Category.UTILS, description = "Right click entities to perform actions on them")
public class EntityTools extends Module
{
    private Setting<Mode> mode;
    private int delay;
    @EventHandler
    public Listener<InputEvent.MouseInputEvent> mouseListener;
    
    public EntityTools() {
        this.mode = this.register(Settings.e("Mode", Mode.DELETE));
        this.delay = 0;
        NBTTagCompound tag;
        this.mouseListener = new Listener<InputEvent.MouseInputEvent>(event -> {
            if (Mouse.getEventButton() == 1 && this.delay == 0 && EntityTools.mc.objectMouseOver.typeOfHit.equals((Object)RayTraceResult.Type.ENTITY)) {
                if (this.mode.getValue().equals(Mode.DELETE)) {
                    EntityTools.mc.world.removeEntity(EntityTools.mc.objectMouseOver.entityHit);
                }
                if (this.mode.getValue().equals(Mode.INFO)) {
                    tag = new NBTTagCompound();
                    EntityTools.mc.objectMouseOver.entityHit.writeToNBT(tag);
                    Command.sendChatMessage(this.getChatName() + "&6Entity Tags:\n" + tag + "");
                }
            }
        }, (Predicate<InputEvent.MouseInputEvent>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
    }
    
    private enum Mode
    {
        DELETE, 
        INFO;
    }
}
