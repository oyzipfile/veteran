// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import java.util.Random;
import com.veteran.hack.module.ModuleManager;
import net.minecraft.client.gui.GuiGameOver;
import java.util.function.Predicate;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.event.events.GuiScreenEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "AutoEZ", category = Category.CHAT, description = "Sends an insult in chat after killing someone")
public class AutoEZ extends Module
{
    private Setting<Mode> mode;
    EntityPlayer focus;
    int hasBeenCombat;
    @EventHandler
    public Listener<AttackEntityEvent> livingDeathEventListener;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener;
    
    public AutoEZ() {
        this.mode = this.register(Settings.e("Mode", Mode.ONTOP));
        this.livingDeathEventListener = new Listener<AttackEntityEvent>(event -> {
            if (event.getTarget() instanceof EntityPlayer) {
                this.focus = (EntityPlayer)event.getTarget();
                if (event.getEntityPlayer().getUniqueID() == AutoEZ.mc.player.getUniqueID()) {
                    if (this.focus.getHealth() <= 0.0 || this.focus.isDead || !AutoEZ.mc.world.playerEntities.contains(this.focus)) {
                        AutoEZ.mc.player.sendChatMessage(this.getText(this.mode.getValue()) + event.getTarget().getName());
                    }
                    else {
                        this.hasBeenCombat = 1000;
                    }
                }
            }
            return;
        }, (Predicate<AttackEntityEvent>[])new Predicate[0]);
        this.listener = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (!(!(event.getScreen() instanceof GuiGameOver))) {
                if (AutoEZ.mc.player.getHealth() > 0.0f) {
                    this.hasBeenCombat = 0;
                }
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
    
    private String getText(final Mode m) {
        return m.text;
    }
    
    @Override
    public void onUpdate() {
        if (this.hasBeenCombat > 0 && (this.focus.getHealth() <= 0.0f || this.focus.isDead || !AutoEZ.mc.world.playerEntities.contains(this.focus))) {
            if (ModuleManager.getModuleByName("AutoEZ").isEnabled()) {
                final Random rand = new Random();
                final int randomNum = rand.nextInt(6) + 1;
                if (randomNum == 1) {
                    AutoEZ.mc.player.sendChatMessage("You just got Killed by a rusher," + this.focus.getName());
                }
                if (randomNum == 2) {
                    AutoEZ.mc.player.sendChatMessage("Team vet ontop " + this.focus.getName());
                }
                if (randomNum == 4) {
                    AutoEZ.mc.player.sendChatMessage("I just removed " + this.focus.getName() + " with VeteranHack!");
                }
                if (randomNum == 3) {
                    AutoEZ.mc.player.sendChatMessage("Everybody whip and naenae; I just killed " + this.focus.getName() + " With VeteranHack!!!");
                }
                if (randomNum == 5) {
                    AutoEZ.mc.player.sendChatMessage(this.focus.getName() + " Is a little rusher baby.");
                }
                if (randomNum == 6) {
                    AutoEZ.mc.player.sendChatMessage("Clown Down " + this.focus.getName() + ".");
                }
            }
            this.hasBeenCombat = 0;
        }
        --this.hasBeenCombat;
    }
    
    public enum Mode
    {
        FUCKED("You just got Killed by a rusher, "), 
        ONTOP("DVDGOD ONTOP FOREVER AND ALWAYS!, EZ, "), 
        REMOVED("I just removed a hole camping faggot thanks to VeteranHack. Don't even bother coming back, "), 
        NAENAE("Everybody whip and naenae, Using VeteranHack I just killed "), 
        CLIPPED("VeteranHack forever ontop! Thanks for the clip, ");
        
        private String text;
        
        private Mode(final String text) {
            this.text = text;
        }
    }
}
