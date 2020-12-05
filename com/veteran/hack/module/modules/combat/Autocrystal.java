// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.combat;

import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import com.veteran.hack.util.InfoCalculator;
import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import com.veteran.hack.module.modules.render.Tracers;
import com.veteran.hack.util.EntityUtil;
import java.awt.Color;
import com.veteran.hack.module.ModuleManager;
import com.veteran.hack.module.modules.gui.ActiveModules;
import com.veteran.hack.util.ColourConverter;
import com.veteran.hack.util.VetHackTessellator;
import com.veteran.hack.event.events.RenderEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.RayTraceResult;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.EntityLivingBase;
import com.veteran.hack.command.Command;
import java.util.ArrayList;
import com.veteran.hack.util.Friends;
import net.minecraft.init.Items;
import com.veteran.hack.util.Wrapper;
import java.util.Comparator;
import net.minecraft.network.Packet;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import java.awt.Font;
import net.minecraft.world.World;
import com.veteran.hack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.gui.font.CFontRenderer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.HashMap;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "Autocrystal", category = Category.COMBAT, description = "Places Crystals to kill enemies.")
public class Autocrystal extends Module
{
    private Setting<Page> p;
    private Setting<PlayType> style;
    private Setting<PlaceBehavior> placeBehavior;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> place;
    private Setting<Boolean> multiPlace;
    private Setting<Boolean> explode;
    private Setting<Boolean> noToolExplode;
    public Setting<Double> range;
    public Setting<Double> wallRange;
    private Setting<Boolean> pingSync;
    private Setting<Double> delay;
    private Setting<Double> minDmg;
    public Setting<Double> maxSelfDmg;
    private Setting<Boolean> facePlace;
    public Setting<Double> facePlaceHealth;
    private Setting<Boolean> noGapSwitch;
    private Setting<Boolean> statusMessages;
    private Setting<Boolean> multiTarget;
    private Setting<Boolean> pSilent;
    private Setting<Boolean> noActionSwitch;
    private Setting<Boolean> players;
    private Setting<Boolean> tracer;
    private Setting<Boolean> chroma;
    private Setting<Boolean> outline;
    private Setting<Boolean> customColours;
    private Setting<Integer> aBlock;
    private Setting<Integer> aTracer;
    private Setting<Integer> r;
    private Setting<Integer> g;
    private Setting<Integer> b;
    HashMap<EntityEnderCrystal, Boolean> crystalMap;
    private BlockPos render;
    private Entity renderEnt;
    private BlockPos lastPos;
    private long systemTime;
    double damage;
    double selfDamage;
    private static boolean togglePitch;
    private boolean switchCoolDown;
    private boolean isAttacking;
    private int oldSlot;
    int timer;
    int breakTries;
    private EntityPlayer lastTarget;
    EntityEnderCrystal last;
    Vec3d[] offset;
    CFontRenderer ff;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> cPacketListener;
    
    public Autocrystal() {
        this.p = this.register((Setting<Page>)Settings.enumBuilder(Page.class).withName("Page").withValue(Page.ONE).build());
        this.style = this.register((Setting<PlayType>)Settings.enumBuilder(PlayType.class).withName("Playstyle").withValue(PlayType.CUSTOM).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.placeBehavior = (Setting<PlaceBehavior>)Settings.enumBuilder(PlaceBehavior.class).withName("Place Behavior").withValue(PlaceBehavior.VETPLACE).withVisibility(v -> this.p.getValue().equals(Page.ONE) && this.style.getValue().equals(PlayType.CUSTOM)).build();
        this.autoSwitch = this.register(Settings.booleanBuilder("Auto Switch").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.place = this.register(Settings.booleanBuilder("Place").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.multiPlace = this.register(Settings.booleanBuilder("Multi Place").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE) && this.place.getValue()).build());
        this.explode = this.register(Settings.booleanBuilder("Explode").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.noToolExplode = this.register(Settings.booleanBuilder("No Tool Explode").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE) && this.explode.getValue() && this.style.getValue().equals(PlayType.CUSTOM)).build());
        this.range = this.register(Settings.doubleBuilder("Range").withMinimum(1.0).withValue(5.0).withMaximum(6.0).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.wallRange = this.register(Settings.doubleBuilder("Wall Range").withMinimum(1.0).withValue(0.0).withMaximum(5.0).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.pingSync = this.register(Settings.booleanBuilder().withName("Auto Hit Delay").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.delay = this.register(Settings.doubleBuilder("Hit Delay").withMinimum(0.5).withValue(5.0).withMaximum(10.0).withVisibility(v -> this.p.getValue().equals(Page.ONE) && this.explode.getValue()).build());
        this.minDmg = this.register(Settings.doubleBuilder("Minimum Damage").withMinimum(0.0).withValue(0.0).withMaximum(16.0).withVisibility(v -> this.p.getValue().equals(Page.ONE) && this.place.getValue() && this.style.getValue().equals(PlayType.CUSTOM)).build());
        this.maxSelfDmg = this.register(Settings.doubleBuilder("Max Self Damage").withMinimum(1.0).withValue(10.0).withMaximum(18.0).withVisibility(v -> this.p.getValue().equals(Page.ONE) && this.place.getValue() && this.style.getValue().equals(PlayType.CUSTOM)).build());
        this.facePlace = this.register(Settings.booleanBuilder("Face Place").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE) && this.place.getValue() && this.style.getValue().equals(PlayType.CUSTOM)).build());
        this.facePlaceHealth = this.register(Settings.doubleBuilder("Face Place Health").withMinimum(1.0).withValue(10.0).withMaximum(36.0).withVisibility(v -> this.p.getValue().equals(Page.ONE) && this.place.getValue() && this.facePlace.getValue() && this.style.getValue().equals(PlayType.CUSTOM)).build());
        this.noGapSwitch = this.register(Settings.booleanBuilder("No Gap Switch").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE) && this.autoSwitch.getValue()).build());
        this.statusMessages = this.register(Settings.booleanBuilder("Status Messages").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.multiTarget = this.register(Settings.booleanBuilder("MultiTarget").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.pSilent = this.register(Settings.booleanBuilder("PSilent").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.noActionSwitch = this.register(Settings.booleanBuilder("No Action Switch").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.players = this.register(Settings.booleanBuilder("Players").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.tracer = this.register(Settings.booleanBuilder("Tracer").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.chroma = this.register(Settings.booleanBuilder("Chroma").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.outline = this.register(Settings.booleanBuilder("Render Block Outline").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.customColours = this.register(Settings.booleanBuilder("Custom Colours").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO) && !this.chroma.getValue()).build());
        this.aBlock = this.register(Settings.integerBuilder("Block Transparency").withMinimum(0).withValue(44).withMaximum(205).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.aTracer = this.register(Settings.integerBuilder("Tracer Transparency").withMinimum(0).withValue(200).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.r = this.register(Settings.integerBuilder("Red").withMinimum(0).withValue(155).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.g = this.register(Settings.integerBuilder("Green").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.b = this.register(Settings.integerBuilder("Blue").withMinimum(0).withValue(255).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.crystalMap = new HashMap<EntityEnderCrystal, Boolean>();
        this.systemTime = 0L;
        this.damage = 0.0;
        this.selfDamage = 0.0;
        this.switchCoolDown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        this.timer = 20;
        this.breakTries = 0;
        this.lastTarget = null;
        this.last = new EntityEnderCrystal((World)Autocrystal.mc.world, 0.0, 0.0, 0.0);
        this.offset = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0) };
        this.ff = new CFontRenderer(new Font("Arial", 0, 20), true, false);
        final Packet packet;
        this.cPacketListener = new Listener<PacketEvent.Send>(event -> {
            packet = event.getPacket();
            if (packet instanceof CPacketPlayer && Autocrystal.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)Autocrystal.yaw;
                ((CPacketPlayer)packet).pitch = (float)Autocrystal.pitch;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.style.getValue() == PlayType.AGGRO) {
            this.minDmg.setValue(4.0);
            this.facePlace.setValue(true);
            this.facePlaceHealth.setValue(14.0);
            this.maxSelfDmg.setValue(8.0);
        }
        if (this.style.getValue() == PlayType.PASSIVE) {
            this.minDmg.setValue(14.0);
            this.facePlace.setValue(true);
            this.facePlaceHealth.setValue(10.0);
            this.maxSelfDmg.setValue(16.0);
        }
        if (Autocrystal.mc.world == null) {
            return;
        }
        if (this.lastPos == null) {
            this.lastPos = new BlockPos(0, 0, 0);
        }
        --this.timer;
        final EntityEnderCrystal crystal = (EntityEnderCrystal)Autocrystal.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> Autocrystal.mc.player.getDistance(c))).orElse(null);
        if (crystal == null) {
            return;
        }
        if (this.explode.getValue() && crystal != null && ((Autocrystal.mc.player.canEntityBeSeen((Entity)crystal) && Autocrystal.mc.player.getDistance((Entity)crystal) <= this.range.getValue()) || (!Autocrystal.mc.player.canEntityBeSeen((Entity)crystal) && Autocrystal.mc.player.getDistance((Entity)crystal) <= this.wallRange.getValue())) && this.passSwordCheck()) {
            if (System.nanoTime() / 1000000.0f - this.systemTime >= 25.0 * this.delay.getValue()) {
                this.isAttacking = true;
                if (calculateDamage(crystal, (Entity)Autocrystal.mc.player) <= this.minDmg.getValue()) {
                    this.explode(crystal);
                }
            }
        }
        else {
            resetRotation();
            if (this.oldSlot != -1) {
                Wrapper.getPlayer().inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
        }
        int crystalSlot = (Autocrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? Autocrystal.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (Autocrystal.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (Autocrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        final List<BlockPos> blocks = this.findCrystalBlocks();
        BlockPos q = null;
        this.damage = 0.5;
        final Entity entit = (Entity)Autocrystal.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer && !Friends.isFriend(e.getName()) && !e.equals((Object)Autocrystal.mc.player) && Autocrystal.mc.player.getDistance(e) <= 169.0f).map(e -> e).min(Comparator.comparing(c -> Autocrystal.mc.player.getDistance(c))).orElse(null);
        if (entit == null) {
            return;
        }
        final List<Entity> entities = new ArrayList<Entity>();
        entities.add(entit);
        if (this.multiTarget.getValue()) {
            final Entity entity3;
            final Entity entit2 = (Entity)Autocrystal.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer && !Friends.isFriend(e.getName()) && !e.equals((Object)Autocrystal.mc.player) && Autocrystal.mc.player.getDistance(e) <= 13.0f && e != entity3).map(e -> e).min(Comparator.comparing(c -> Autocrystal.mc.player.getDistance(c))).orElse(null);
            if (entit2 != null) {
                entities.add(entit2);
            }
        }
        EntityPlayer pt = (EntityPlayer)Autocrystal.mc.player;
        for (final Entity entity2 : entities) {
            if (this.place.getValue() && this.placeBehavior.getValue() == PlaceBehavior.VETPLACE) {
                for (final BlockPos blockPos : blocks) {
                    final double b = entity2.getDistanceSq(blockPos);
                    if (b > 169.0) {
                        continue;
                    }
                    final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)Autocrystal.mc.player);
                    if (this.timer <= 0 && entity2 instanceof EntityPlayer && this.statusMessages.getValue()) {
                        Command.sendChatMessage("Autocrystal - placing against target &c" + entity2.getName() + ".");
                        this.timer = 600;
                    }
                    if (self >= Autocrystal.mc.player.getHealth() + Autocrystal.mc.player.getAbsorptionAmount() - 1.0f || self > d) {
                        continue;
                    }
                    if (self >= this.maxSelfDmg.getValue()) {
                        continue;
                    }
                    if (d <= this.minDmg.getValue() && (!this.facePlace.getValue() || ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() > this.facePlaceHealth.getValue() || b > 1.2)) {
                        continue;
                    }
                    q = blockPos;
                    this.damage = d;
                    this.selfDamage = self;
                    this.renderEnt = entity2;
                    if (!(entity2 instanceof EntityPlayer)) {
                        continue;
                    }
                    pt = (EntityPlayer)entity2;
                }
            }
        }
        if (this.damage == 0.5) {
            this.render = null;
            this.renderEnt = null;
            resetRotation();
            return;
        }
        if (!this.multiPlace.getValue()) {
            final float lastPosDmg = calculateDamage(this.lastPos.x + 0.5, this.lastPos.y + 1, this.lastPos.z + 0.5, (Entity)pt);
            final float lastPosSelf = calculateDamage(this.lastPos.x + 0.5, this.lastPos.y + 1, this.lastPos.z + 0.5, (Entity)Autocrystal.mc.player);
            if (lastPosDmg >= this.minDmg.getValue() && lastPosDmg >= this.damage && lastPosSelf <= this.maxSelfDmg.getValue() && this.isEmpty(this.lastPos.up()) && this.isEmpty(this.lastPos.up(2))) {
                q = this.lastPos;
                this.damage = lastPosDmg;
            }
        }
        if (this.place.getValue()) {
            this.render = q;
            this.lastPos = q;
            if (!offhand && Autocrystal.mc.player.inventory.currentItem != crystalSlot) {
                if ((this.noActionSwitch.getValue() && this.isUsingBinds() && this.autoSwitch.getValue() && !this.noGapSwitch.getValue()) || (this.autoSwitch.getValue() && this.noGapSwitch.getValue() && (Autocrystal.mc.player.getHeldItemMainhand().getItem() != Items.GOLDEN_APPLE || !Autocrystal.mc.gameSettings.keyBindUseItem.isKeyDown()))) {
                    Autocrystal.mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCoolDown = true;
                }
                return;
            }
            this.lookAtPacket(q.x + 0.5, q.y - 0.5, q.z + 0.5, (EntityPlayer)Autocrystal.mc.player);
            final RayTraceResult result = Autocrystal.mc.world.rayTraceBlocks(new Vec3d(Autocrystal.mc.player.posX, Autocrystal.mc.player.posY + Autocrystal.mc.player.getEyeHeight(), Autocrystal.mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5, q.z + 0.5));
            EnumFacing f;
            if (result == null || result.sideHit == null) {
                f = EnumFacing.UP;
            }
            else {
                f = result.sideHit;
            }
            Autocrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            if (this.pSilent.getValue()) {
                resetRotation();
            }
        }
        if (Autocrystal.isSpoofingAngles) {
            if (Autocrystal.togglePitch) {
                final EntityPlayerSP player = Autocrystal.mc.player;
                player.rotationPitch += (float)4.0E-4;
                Autocrystal.togglePitch = false;
            }
            else {
                final EntityPlayerSP player2 = Autocrystal.mc.player;
                player2.rotationPitch -= (float)4.0E-4;
                Autocrystal.togglePitch = true;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            VetHackTessellator.prepare(7);
            int colour = 1157627903;
            if (this.customColours.getValue()) {
                colour = ColourConverter.rgbToInt(this.r.getValue(), this.g.getValue(), this.b.getValue(), this.aBlock.getValue());
            }
            if (this.chroma.getValue()) {
                final ActiveModules activeMods = (ActiveModules)ModuleManager.getModuleByName("ActiveModules");
                final float[] hue = { System.currentTimeMillis() % (360 * activeMods.getRainbowSpeed()) / (360.0f * activeMods.getRainbowSpeed()) };
                final int rgb = Color.HSBtoRGB(hue[0], ColourConverter.toF(activeMods.saturationR.getValue()), ColourConverter.toF(activeMods.brightnessR.getValue()));
                final int red = rgb >> 16 & 0xFF;
                final int green = rgb >> 8 & 0xFF;
                final int blue = rgb & 0xFF;
                colour = ColourConverter.rgbToInt(red, green, blue, this.aBlock.getValue());
            }
            VetHackTessellator.drawBox(this.render, colour, 63);
            VetHackTessellator.release();
            if (this.outline.getValue()) {
                VetHackTessellator.prepare(7);
                VetHackTessellator.drawBoundingBoxBlockPos(this.render, 4.0f, colour >> 16 & 0xFF, colour >> 8 & 0xFF, colour & 0xFF, this.aBlock.getValue() + 50);
                VetHackTessellator.release();
            }
            if (this.renderEnt != null && this.tracer.getValue()) {
                final Vec3d p = EntityUtil.getInterpolatedRenderPos(this.renderEnt, Autocrystal.mc.getRenderPartialTicks());
                float rL = 1.0f;
                float gL = 1.0f;
                float bL = 1.0f;
                float aL = 1.0f;
                if (this.customColours.getValue()) {
                    rL = ColourConverter.toF(this.r.getValue());
                    gL = ColourConverter.toF(this.g.getValue());
                    bL = ColourConverter.toF(this.b.getValue());
                    aL = ColourConverter.toF(this.aTracer.getValue());
                }
                Tracers.drawLineFromPosToPos(this.render.x - Autocrystal.mc.getRenderManager().renderPosX + 0.5, this.render.y - Autocrystal.mc.getRenderManager().renderPosY + 1.0, this.render.z - Autocrystal.mc.getRenderManager().renderPosZ + 0.5, p.x, p.y, p.z, this.renderEnt.getEyeHeight(), rL, gL, bL, aL);
            }
            GlStateManager.pushMatrix();
            glBillboardDistanceScaled(this.render.getX() + 0.5f, this.render.getY() + 0.5f, this.render.getZ() + 0.5f, (EntityPlayer)Autocrystal.mc.player, 1.0f);
            final String damageText = ((Math.floor(this.damage) == this.damage) ? Integer.valueOf((int)this.damage) : String.format("%.1f", this.damage)) + "";
            final String selfDamage = ((Math.floor(this.selfDamage) == this.selfDamage) ? Integer.valueOf((int)this.selfDamage) : String.format("%.1f", this.selfDamage)) + "";
            GlStateManager.disableDepth();
            GlStateManager.translate(-(this.ff.getStringWidth(damageText) / 2.0), 0.0, 0.0);
            this.ff.drawStringWithShadow(damageText, 0.0, 0.0, -5592406);
            this.ff.drawStringWithShadow(selfDamage, 0.0, this.ff.getHeight() + 2, 16579836);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public void onRender() {
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1] + 1.0f);
    }
    
    boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (Autocrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || Autocrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && Autocrystal.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && Autocrystal.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && Autocrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && Autocrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Autocrystal.mc.player.posX), Math.floor(Autocrystal.mc.player.posY), Math.floor(Autocrystal.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedSize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedSize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finalD = 1.0;
        if (entity instanceof EntityLivingBase) {
            finalD = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)Autocrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finalD;
    }
    
    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById(11)))) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = Autocrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    public boolean isUsingBinds() {
        return Autocrystal.mc.gameSettings.keyBindUseItem.isKeyDown() || Autocrystal.mc.gameSettings.keyBindAttack.isKeyDown();
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        Autocrystal.yaw = yaw1;
        Autocrystal.pitch = pitch1;
        Autocrystal.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (Autocrystal.isSpoofingAngles) {
            Autocrystal.yaw = Autocrystal.mc.player.rotationYaw;
            Autocrystal.pitch = Autocrystal.mc.player.rotationPitch;
            Autocrystal.isSpoofingAngles = false;
        }
    }
    
    public void onEnable() {
        if (this.statusMessages.getValue()) {
            Command.sendChatMessage(this.getChatName() + "&aEnabled&r");
        }
    }
    
    public void onDisable() {
        if (this.statusMessages.getValue()) {
            Command.sendChatMessage(this.getChatName() + "&cDisabled&r");
        }
        this.render = null;
        this.renderEnt = null;
        resetRotation();
    }
    
    public void explode(final EntityEnderCrystal crystal) {
        this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)Autocrystal.mc.player);
        Autocrystal.mc.playerController.attackEntity((EntityPlayer)Autocrystal.mc.player, (Entity)crystal);
        Autocrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
        this.systemTime = System.nanoTime() / 1000000L;
    }
    
    private float explodeRate() {
        if (!this.pingSync.getValue()) {
            return this.delay.getValue().floatValue();
        }
        final float tps = Autocrystal.mc.timer.tickLength / 1000.0f;
        final float ping = (float)InfoCalculator.ping();
        return ping * 20.0f / tps;
    }
    
    private boolean passSwordCheck() {
        return !(Autocrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemTool) || !this.noToolExplode.getValue();
    }
    
    private boolean isEmpty(final BlockPos pos) {
        final List<Entity> playersInAABB = (List<Entity>)Autocrystal.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos)).stream().filter(e -> e instanceof EntityPlayer).collect(Collectors.toList());
        return playersInAABB.isEmpty();
    }
    
    public static int getItems(final Item i) {
        return Autocrystal.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::func_190916_E).sum() + Autocrystal.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::func_190916_E).sum();
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(getItems(Items.END_CRYSTAL));
    }
    
    public static void glBillboard(final float x, final float y, final float z) {
        final float scale = 0.02666667f;
        GlStateManager.translate(x - Autocrystal.mc.getRenderManager().renderPosX, y - Autocrystal.mc.getRenderManager().renderPosY, z - Autocrystal.mc.getRenderManager().renderPosZ);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-Autocrystal.mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(Autocrystal.mc.player.rotationPitch, (Autocrystal.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }
    
    public static void glBillboardDistanceScaled(final float x, final float y, final float z, final EntityPlayer player, final float scale) {
        glBillboard(x, y, z);
        final int distance = (int)player.getDistance((double)x, (double)y, (double)z);
        float scaleDistance = distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }
    
    static {
        Autocrystal.togglePitch = false;
    }
    
    private enum PlaceBehavior
    {
        VETPLACE;
    }
    
    private enum PlayType
    {
        CUSTOM, 
        AGGRO, 
        PASSIVE;
    }
    
    private enum Page
    {
        ONE, 
        TWO;
    }
}
