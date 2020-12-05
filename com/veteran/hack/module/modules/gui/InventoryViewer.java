// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.gui;

import com.veteran.hack.command.Command;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.Gui;
import com.veteran.hack.util.ColourConverter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import com.veteran.hack.gui.rgui.component.container.use.Frame;
import com.veteran.hack.util.GuiFrameUtil;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "InventoryViewer", category = Category.GUI, description = "View your inventory on screen", showOnArray = ShowOnArray.OFF)
public class InventoryViewer extends Module
{
    private Setting<Boolean> mcTexture;
    private Setting<Boolean> showIcon;
    private Setting<Boolean> docking;
    private Setting<ViewSize> viewSizeSetting;
    private Setting<Boolean> coloredBackground;
    private Setting<Integer> a;
    private Setting<Integer> r;
    private Setting<Integer> g;
    private Setting<Integer> b;
    private boolean isLeft;
    private boolean isRight;
    private boolean isTop;
    private boolean isBottom;
    public boolean isRenderingInv;
    public static final String PENIS_NUKER = "SgVkYp2s5v8y/B?E(H+MbQeThWmZq4t6w9z$C&F)J@NcRfUjXn2r5u8x!A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C*F-J@NcRfUjXn2r5u8x/A?D(G+KbPdSgVkYp3s6v9y$B&E)H@McQfThWmZq4t7w!z%C*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w!z%C*F-JaNdRgUkXp2s5v8y/A?D(G+KbPeShVmYq3t6w9z$C&E)H@McQfTjWnZr4u7x!A%D*G-JaNdRgUkXp2s5v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A%D*G-KaPdSgVkXp2s5v8y/B?E(H+MbQeThWmZq3t6w9z$C&F)J@NcRfUjXn2r5u7x!A%D*G-KaPdSgVkYp3s6v9y/B?E(H+MbQeThWmZq4t7w!z%C&F)J@NcRfUjXn2r5u8x/A?D(G-KaP";
    
    public InventoryViewer() {
        this.mcTexture = this.register(Settings.b("Use ResourcePack", false));
        this.showIcon = this.register(Settings.booleanBuilder("Debug").withValue(false).withVisibility(v -> !this.mcTexture.getValue()).build());
        this.docking = this.register(Settings.booleanBuilder("Automatic Docking").withValue(true).withVisibility(v -> this.showIcon.getValue() && !this.mcTexture.getValue()).build());
        this.viewSizeSetting = this.register((Setting<ViewSize>)Settings.enumBuilder(ViewSize.class).withName("Icon Size").withValue(ViewSize.LARGE).withVisibility(v -> this.showIcon.getValue() && !this.mcTexture.getValue()).build());
        this.coloredBackground = this.register(Settings.booleanBuilder("Colored Background").withValue(true).withVisibility(v -> !this.mcTexture.getValue()).build());
        this.a = this.register(Settings.integerBuilder("Transparency").withMinimum(0).withValue(32).withMaximum(255).withVisibility(v -> this.coloredBackground.getValue() && !this.mcTexture.getValue()).build());
        this.r = this.register(Settings.integerBuilder("Red").withMinimum(0).withValue(155).withMaximum(255).withVisibility(v -> this.coloredBackground.getValue() && !this.mcTexture.getValue()).build());
        this.g = this.register(Settings.integerBuilder("Green").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.coloredBackground.getValue() && !this.mcTexture.getValue()).build());
        this.b = this.register(Settings.integerBuilder("Blue").withMinimum(0).withValue(255).withMaximum(255).withVisibility(v -> this.coloredBackground.getValue() && !this.mcTexture.getValue()).build());
        this.isLeft = false;
        this.isRight = false;
        this.isTop = false;
        this.isBottom = false;
    }
    
    private int invMoveHorizontal() {
        if (!this.docking.getValue() || this.mcTexture.getValue()) {
            return 0;
        }
        if (this.isLeft) {
            return 45;
        }
        if (this.isRight) {
            return -45;
        }
        return 0;
    }
    
    private int invMoveVertical() {
        if (!this.docking.getValue() || this.mcTexture.getValue()) {
            return 0;
        }
        if (this.isTop) {
            return 10;
        }
        if (this.isBottom) {
            return -10;
        }
        return 0;
    }
    
    private void updatePos() {
        final Frame frame = GuiFrameUtil.getFrameByName("inventory viewer");
        if (frame == null) {
            return;
        }
        this.isTop = frame.getDocking().isTop();
        this.isLeft = frame.getDocking().isLeft();
        this.isRight = frame.getDocking().isRight();
        this.isBottom = frame.getDocking().isBottom();
    }
    
    private ResourceLocation getBox() {
        if (this.mcTexture.getValue()) {
            return new ResourceLocation("textures/gui/container/generic_54.png");
        }
        if (!this.showIcon.getValue()) {
            return new ResourceLocation("kamiblue/clear.png");
        }
        if (!this.viewSizeSetting.getValue().equals(ViewSize.LARGE)) {
            if (!this.viewSizeSetting.getValue().equals(ViewSize.SMALL)) {
                if (this.viewSizeSetting.getValue().equals(ViewSize.MEDIUM)) {}
            }
        }
        return new ResourceLocation("null");
    }
    
    private void boxRender(final int x, final int y) {
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        if (this.coloredBackground.getValue()) {
            Gui.drawRect(x, y, x + 162, y + 54, ColourConverter.rgbToInt(this.r.getValue(), this.g.getValue(), this.b.getValue(), this.a.getValue()));
        }
        final ResourceLocation box = this.getBox();
        InventoryViewer.mc.renderEngine.bindTexture(box);
        this.updatePos();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        InventoryViewer.mc.ingameGUI.drawTexturedModalRect(x, y, this.invMoveHorizontal() + 7, this.invMoveVertical() + 17, 162, 54);
        GlStateManager.enableDepth();
    }
    
    @Override
    public void onRender() {
        final Frame frame = GuiFrameUtil.getFrameByName("inventory viewer");
        if (frame == null) {
            return;
        }
        if (frame.isPinned()) {
            final NonNullList<ItemStack> items = (NonNullList<ItemStack>)InventoryViewer.mc.player.inventory.mainInventory;
            this.boxRender(frame.getX(), frame.getY());
            this.itemRender(items, frame.getX(), frame.getY());
        }
        final Frame pframe = GuiFrameUtil.getFrameByName("Player View");
        if (pframe == null) {
            return;
        }
        if (pframe.isPinned()) {
            final float mouseXX = InventoryViewer.mc.getRenderViewEntity().rotationYaw * -1.0f;
            final float mouseYY = InventoryViewer.mc.getRenderViewEntity().rotationPitch * -1.0f;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            drawEntityOnScreen(pframe.getX() + 14, pframe.getY() + 75, 32, mouseXX, mouseYY, (EntityLivingBase)InventoryViewer.mc.player);
        }
    }
    
    public static void drawEntityOnScreen(final int posX, final int posY, final int scale, final float mouseX, final float mouseY, final EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0f);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float f = ent.renderYawOffset;
        final float f2 = ent.rotationYaw;
        final float f3 = ent.rotationPitch;
        final float f4 = ent.prevRotationYawHead;
        final float f5 = ent.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    private void itemRender(final NonNullList<ItemStack> items, final int x, final int y) {
        GlStateManager.clear(256);
        for (int size = items.size(), item = 9; item < size; ++item) {
            final int slotX = x + 1 + item % 9 * 18;
            final int slotY = y + 1 + (item / 9 - 1) * 18;
            preItemRender();
            InventoryViewer.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)items.get(item), slotX, slotY);
            InventoryViewer.mc.getRenderItem().renderItemOverlays(InventoryViewer.mc.fontRenderer, (ItemStack)items.get(item), slotX, slotY);
            postItemRender();
        }
        int slotX2 = x + 1 + 0;
        int slotY2 = y + 1 - 18;
        preItemRender();
        InventoryViewer.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)InventoryViewer.mc.player.inventoryContainer.getInventory().get(1), slotX2, slotY2);
        InventoryViewer.mc.getRenderItem().renderItemOverlays(InventoryViewer.mc.fontRenderer, (ItemStack)InventoryViewer.mc.player.inventoryContainer.getInventory().get(1), slotX2, slotY2);
        slotX2 = x + 1 + 18;
        slotY2 = y + 1 - 18;
        InventoryViewer.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)InventoryViewer.mc.player.inventoryContainer.getInventory().get(2), slotX2, slotY2);
        InventoryViewer.mc.getRenderItem().renderItemOverlays(InventoryViewer.mc.fontRenderer, (ItemStack)InventoryViewer.mc.player.inventoryContainer.getInventory().get(2), slotX2, slotY2);
        slotX2 = x + 1 + 36;
        slotY2 = y + 1 - 18;
        InventoryViewer.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)InventoryViewer.mc.player.inventoryContainer.getInventory().get(3), slotX2, slotY2);
        InventoryViewer.mc.getRenderItem().renderItemOverlays(InventoryViewer.mc.fontRenderer, (ItemStack)InventoryViewer.mc.player.inventoryContainer.getInventory().get(3), slotX2, slotY2);
        slotX2 = x + 1 + 54;
        slotY2 = y + 1 - 18;
        InventoryViewer.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)InventoryViewer.mc.player.inventoryContainer.getInventory().get(4), slotX2, slotY2);
        InventoryViewer.mc.getRenderItem().renderItemOverlays(InventoryViewer.mc.fontRenderer, (ItemStack)InventoryViewer.mc.player.inventoryContainer.getInventory().get(4), slotX2, slotY2);
        postItemRender();
    }
    
    private static void preItemRender() {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
    }
    
    private static void postItemRender() {
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
    boolean isInHole(final EntityPlayer entity) {
        return false;
    }
    
    public void onDisable() {
        Command.sendDisableMessage(this.getName());
    }
    
    private enum ViewSize
    {
        LARGE, 
        MEDIUM, 
        SMALL;
    }
}
