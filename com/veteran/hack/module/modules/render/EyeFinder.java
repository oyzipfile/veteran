// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import com.veteran.hack.util.VetHackTessellator;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import java.util.function.Consumer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import java.util.function.Predicate;
import com.veteran.hack.util.EntityUtil;
import com.veteran.hack.event.events.RenderEvent;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "EyeFinder", description = "Draw lines from entity's heads to where they are looking", category = Category.RENDER)
public class EyeFinder extends Module
{
    private Setting<Boolean> players;
    private Setting<Boolean> mobs;
    private Setting<Boolean> animals;
    
    public EyeFinder() {
        this.players = this.register(Settings.b("Players", true));
        this.mobs = this.register(Settings.b("Mobs", false));
        this.animals = this.register(Settings.b("Animals", false));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final boolean b;
        EyeFinder.mc.world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> EyeFinder.mc.player != entity).map(entity -> entity).filter(entityLivingBase -> !entityLivingBase.isDead).filter(entity -> {
            if (!this.players.getValue() || !(entity instanceof EntityPlayer)) {
                if (!(EntityUtil.isPassive(entity) ? this.animals.getValue() : this.mobs.getValue())) {
                    return b;
                }
            }
            return b;
        }).forEach(this::drawLine);
    }
    
    private void drawLine(final EntityLivingBase e) {
        final RayTraceResult result = e.rayTrace(6.0, Minecraft.getMinecraft().getRenderPartialTicks());
        if (result == null) {
            return;
        }
        final Vec3d eyes = e.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks());
        GlStateManager.enableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        final double posx = eyes.x - EyeFinder.mc.getRenderManager().renderPosX;
        final double posy = eyes.y - EyeFinder.mc.getRenderManager().renderPosY;
        final double posz = eyes.z - EyeFinder.mc.getRenderManager().renderPosZ;
        final double posx2 = result.hitVec.x - EyeFinder.mc.getRenderManager().renderPosX;
        final double posy2 = result.hitVec.y - EyeFinder.mc.getRenderManager().renderPosY;
        final double posz2 = result.hitVec.z - EyeFinder.mc.getRenderManager().renderPosZ;
        GL11.glColor4f(0.2f, 0.1f, 0.3f, 0.8f);
        GlStateManager.glLineWidth(1.5f);
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glEnd();
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            VetHackTessellator.prepare(7);
            GL11.glEnable(2929);
            final BlockPos b = result.getBlockPos();
            final float x = b.x - 0.01f;
            final float y = b.y - 0.01f;
            final float z = b.z - 0.01f;
            VetHackTessellator.drawBox(VetHackTessellator.getBufferBuilder(), x, y, z, 1.01f, 1.01f, 1.01f, 51, 25, 73, 200, 63);
            VetHackTessellator.release();
        }
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
    }
}
