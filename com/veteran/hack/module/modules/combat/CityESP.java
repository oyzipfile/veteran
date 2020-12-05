// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import java.util.Iterator;
import java.util.List;
import com.veteran.hack.util.VetHackTessellator;
import net.minecraft.init.Blocks;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.veteran.hack.util.Friends;
import java.util.Collection;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import java.awt.Color;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.modules.render.HoleESP;
import com.veteran.hack.event.events.RenderEvent;
import com.veteran.hack.setting.Settings;
import net.minecraft.util.math.BlockPos;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "CityESP", category = Category.RENDER, description = "draws a square on the face of a block that can be mined to city-trap your opponent.")
public class CityESP extends Module
{
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    private final BlockPos[] surroundOffset;
    
    public CityESP() {
        this.red = this.register((Setting<Integer>)Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(119).build());
        this.green = this.register((Setting<Integer>)Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(189).build());
        this.blue = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(11).build());
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Alpha").withMinimum(0).withMaximum(255).withValue(100).build());
        this.surroundOffset = new BlockPos[] { new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (CityESP.mc.world == null) {
            return;
        }
        final HoleESP h = (HoleESP)ModuleManager.getModuleByName("HoleESP");
        final Autocrystal a = (Autocrystal)ModuleManager.getModuleByName("Autocrystal");
        final Color c = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
        final List<EntityPlayer> entities = new ArrayList<EntityPlayer>();
        entities.addAll((Collection<? extends EntityPlayer>)CityESP.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        for (final EntityPlayer e : entities) {
            int i = 0;
            for (final BlockPos add : this.surroundOffset) {
                ++i;
                final BlockPos o = new BlockPos(e.getPositionVector().x, e.getPositionVector().y, e.getPositionVector().z).add(add.x, add.y, add.z);
                if (CityESP.mc.world.getBlockState(o).getBlock() == Blocks.OBSIDIAN) {
                    VetHackTessellator.prepare(7);
                    if (i == 1 && a.canPlaceCrystal(o.north(1).down())) {
                        VetHackTessellator.drawBox(o.x, o.y, o.z, c.getRGB(), 4);
                    }
                    if (i == 2 && a.canPlaceCrystal(o.east(1).down())) {
                        VetHackTessellator.drawBox(o.x, o.y, o.z, c.getRGB(), 32);
                    }
                    if (i == 3 && a.canPlaceCrystal(o.south(1).down())) {
                        VetHackTessellator.drawBox(o.x, o.y, o.z, c.getRGB(), 8);
                    }
                    if (i == 4 && a.canPlaceCrystal(o.west(1).down())) {
                        VetHackTessellator.drawBox(o.x, o.y, o.z, c.getRGB(), 16);
                    }
                    VetHackTessellator.release();
                }
            }
        }
    }
}
