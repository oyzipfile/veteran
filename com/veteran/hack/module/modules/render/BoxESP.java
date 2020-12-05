// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.render;

import java.util.Iterator;
import java.util.List;
import com.veteran.hack.util.ColourConverter;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.modules.gui.ActiveModules;
import java.awt.Color;
import com.veteran.hack.util.VetHackTessellator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import java.util.Collection;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import com.veteran.hack.event.events.RenderEvent;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "BoxESP", description = "Draws a box around EXP Bottles and Pearls", category = Category.RENDER)
public class BoxESP extends Module
{
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Boolean> peftMode;
    
    public BoxESP() {
        this.red = this.register((Setting<Integer>)Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(200).build());
        this.green = this.register((Setting<Integer>)Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(69).build());
        this.blue = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(69).build());
        this.peftMode = this.register(Settings.b("Chroma", false));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final List<Entity> entities = new ArrayList<Entity>();
        entities.addAll((Collection<? extends Entity>)BoxESP.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityThrowable || entity instanceof EntityArrow).collect(Collectors.toList()));
        for (final Entity e : entities) {
            VetHackTessellator.prepare(7);
            final Color c = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 200);
            if (!this.peftMode.getValue()) {
                VetHackTessellator.drawBoxSmall(e.getPositionVector().x - 0.125, e.getPositionVector().y, e.getPositionVector().z - 0.125, c.getRGB(), 63);
            }
            else {
                final ActiveModules activeMods = (ActiveModules)ModuleManager.getModuleByName("ActiveModules");
                final float[] hue = { System.currentTimeMillis() % (360 * activeMods.getRainbowSpeed()) / (360.0f * activeMods.getRainbowSpeed()) };
                final int rgb = Color.HSBtoRGB(hue[0], ColourConverter.toF(activeMods.saturationR.getValue()), ColourConverter.toF(activeMods.brightnessR.getValue()));
                VetHackTessellator.drawBoxSmall(e.getPositionVector().x - 0.125, e.getPositionVector().y, e.getPositionVector().z - 0.125, rgb, 63);
            }
            VetHackTessellator.release();
        }
    }
}
