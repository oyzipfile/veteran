// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.movement;

import com.veteran.hack.command.Command;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import com.veteran.hack.module.ModuleManager;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import com.veteran.hack.setting.Settings;
import com.veteran.hack.event.events.PlayerTravelEvent;
import me.zero.alpine.listener.EventHandler;
import com.veteran.hack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "ElytraFlight", description = "Modifies elytras to fly at custom velocities and fall speeds", category = Category.MOVEMENT)
public class ElytraFlight extends Module
{
    private Setting<ElytraFlightMode> mode;
    private Setting<Boolean> defaultSetting;
    private Setting<Boolean> easyTakeOff;
    private Setting<Boolean> hoverControl;
    private Setting<Boolean> easyTakeOffControl;
    private Setting<Boolean> timerControl;
    private Setting<TakeoffMode> takeOffMode;
    private Setting<Boolean> overrideMaxSpeed;
    private Setting<Float> speedHighway;
    private Setting<Float> speedHighwayOverride;
    private Setting<Float> speedControl;
    private Setting<Float> fallSpeedHighway;
    private Setting<Float> fallSpeedControl;
    private Setting<Float> fallSpeed;
    private Setting<Float> upSpeedBoost;
    private Setting<Float> downSpeedBoost;
    private Setting<Double> downSpeedControl;
    private ElytraFlightMode enabledMode;
    private boolean hasDoneWarning;
    private double hoverTarget;
    public float packetYaw;
    private boolean hoverState;
    @EventHandler
    private Listener<PacketEvent.Send> sendListener;
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener;
    @EventHandler
    private Listener<PlayerTravelEvent> playerTravelListener;
    
    public ElytraFlight() {
        this.mode = this.register(Settings.e("Mode", ElytraFlightMode.HIGHWAY));
        this.defaultSetting = this.register(Settings.b("Defaults", false));
        this.easyTakeOff = this.register(Settings.booleanBuilder("Easy Takeoff H").withValue(true).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.hoverControl = this.register(Settings.booleanBuilder("Hover").withValue(true).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.CONTROL)).build());
        this.easyTakeOffControl = this.register(Settings.booleanBuilder("Easy Takeoff C").withValue(false).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.CONTROL)).build());
        this.timerControl = this.register(Settings.booleanBuilder("Takeoff Timer").withValue(true).withVisibility(v -> this.easyTakeOffControl.getValue() && this.mode.getValue().equals(ElytraFlightMode.CONTROL)).build());
        this.takeOffMode = this.register((Setting<TakeoffMode>)Settings.enumBuilder(TakeoffMode.class).withName("Takeoff Mode").withValue(TakeoffMode.PACKET).withVisibility(v -> this.easyTakeOff.getValue() && this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.overrideMaxSpeed = this.register(Settings.booleanBuilder("Over Max Speed").withValue(false).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.speedHighway = this.register(Settings.floatBuilder("Speed H").withValue(1.8f).withMaximum(1.8f).withVisibility(v -> !this.overrideMaxSpeed.getValue() && this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.speedHighwayOverride = this.register(Settings.floatBuilder("Speed H O").withValue(1.8f).withVisibility(v -> this.overrideMaxSpeed.getValue() && this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.speedControl = this.register(Settings.floatBuilder("Speed C").withValue(1.8f).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.CONTROL)).build());
        this.fallSpeedHighway = this.register(Settings.floatBuilder("Fall Speed H").withValue(5.0000002E-5f).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.fallSpeedControl = this.register(Settings.floatBuilder("Fall Speed C").withValue(5.0000002E-5f).withMaximum(0.3f).withMinimum(0.0f).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.CONTROL)).build());
        this.fallSpeed = this.register(Settings.floatBuilder("Fall Speed").withValue(-0.003f).withVisibility(v -> !this.mode.getValue().equals(ElytraFlightMode.CONTROL) && !this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.upSpeedBoost = this.register(Settings.floatBuilder("Up Speed B").withValue(0.08f).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.BOOST)).build());
        this.downSpeedBoost = this.register(Settings.floatBuilder("Down Speed B").withValue(0.04f).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.BOOST)).build());
        this.downSpeedControl = this.register(Settings.doubleBuilder("Down Speed C").withMaximum(10.0).withMinimum(0.0).withValue(2.0).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.CONTROL)).build());
        this.hoverTarget = -1.0;
        this.packetYaw = 0.0f;
        this.hoverState = false;
        CPacketPlayer packet;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (!this.mode.getValue().equals(ElytraFlightMode.CONTROL) || ElytraFlight.mc.player == null) {
                return;
            }
            else {
                if (event.getPacket() instanceof CPacketPlayer) {
                    if (!ElytraFlight.mc.player.isElytraFlying()) {
                        return;
                    }
                    else {
                        packet = (CPacketPlayer)event.getPacket();
                        packet.pitch = 0.0f;
                        packet.yaw = this.packetYaw;
                    }
                }
                if (event.getPacket() instanceof CPacketEntityAction && ((CPacketEntityAction)event.getPacket()).getAction() == CPacketEntityAction.Action.START_FALL_FLYING) {
                    this.hoverTarget = ElytraFlight.mc.player.posY + 0.35;
                }
                return;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        SPacketPlayerPosLook packet2;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (!this.mode.getValue().equals(ElytraFlightMode.CONTROL) || ElytraFlight.mc.player == null || !ElytraFlight.mc.player.isElytraFlying()) {
                return;
            }
            else {
                if (event.getPacket() instanceof SPacketPlayerPosLook) {
                    packet2 = (SPacketPlayerPosLook)event.getPacket();
                    packet2.pitch = ElytraFlight.mc.player.rotationPitch;
                }
                return;
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        boolean moveForward;
        boolean moveBackward;
        boolean moveLeft;
        boolean moveRight;
        boolean moveUp;
        boolean moveDown;
        float n;
        float moveForwardFactor;
        float yawDeg;
        float yaw;
        double motionAmount;
        boolean doHover;
        double calcMotionDiff;
        EntityPlayerSP player;
        EntityPlayerSP player2;
        EntityPlayerSP player3;
        EntityPlayerSP player4;
        EntityPlayerSP player5;
        EntityPlayerSP player6;
        this.playerTravelListener = new Listener<PlayerTravelEvent>(event -> {
            if (this.mode.getValue().equals(ElytraFlightMode.CONTROL) && ElytraFlight.mc.player != null) {
                if (!ElytraFlight.mc.player.isElytraFlying()) {
                    if (this.easyTakeOffControl.getValue() && !ElytraFlight.mc.player.onGround && ElytraFlight.mc.player.motionY < -0.04) {
                        Objects.requireNonNull(ElytraFlight.mc.getConnection()).sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                        if (this.timerControl.getValue()) {
                            ElytraFlight.mc.timer.tickLength = 200.0f;
                        }
                        event.cancel();
                    }
                }
                else {
                    ElytraFlight.mc.timer.tickLength = 50.0f;
                    if (this.hoverTarget < 0.0) {
                        this.hoverTarget = ElytraFlight.mc.player.posY;
                    }
                    moveForward = ElytraFlight.mc.gameSettings.keyBindForward.isKeyDown();
                    moveBackward = ElytraFlight.mc.gameSettings.keyBindBack.isKeyDown();
                    moveLeft = ElytraFlight.mc.gameSettings.keyBindLeft.isKeyDown();
                    moveRight = ElytraFlight.mc.gameSettings.keyBindRight.isKeyDown();
                    moveUp = ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown();
                    moveDown = ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown();
                    if (moveForward) {
                        n = 1.0f;
                    }
                    else {
                        n = (float)(moveBackward ? -1 : 0);
                    }
                    moveForwardFactor = n;
                    yawDeg = ElytraFlight.mc.player.rotationYaw;
                    if (moveLeft && (moveForward || moveBackward)) {
                        yawDeg -= 40.0f * moveForwardFactor;
                    }
                    else if (moveRight && (moveForward || moveBackward)) {
                        yawDeg += 40.0f * moveForwardFactor;
                    }
                    else if (moveLeft) {
                        yawDeg -= 90.0f;
                    }
                    else if (moveRight) {
                        yawDeg += 90.0f;
                    }
                    if (moveBackward) {
                        yawDeg -= 180.0f;
                    }
                    this.packetYaw = yawDeg;
                    yaw = (float)Math.toRadians(yawDeg);
                    motionAmount = Math.sqrt(ElytraFlight.mc.player.motionX * ElytraFlight.mc.player.motionX + ElytraFlight.mc.player.motionZ * ElytraFlight.mc.player.motionZ);
                    this.hoverState = (this.hoverState ? (ElytraFlight.mc.player.posY < this.hoverTarget + 0.1) : (ElytraFlight.mc.player.posY < this.hoverTarget + 0.0));
                    doHover = (this.hoverState && this.hoverControl.getValue());
                    if (moveUp || moveForward || moveBackward || moveLeft || moveRight || ModuleManager.isModuleEnabled("AutoWalk")) {
                        if ((moveUp || doHover) && motionAmount > 1.0) {
                            if (ElytraFlight.mc.player.motionX == 0.0 && ElytraFlight.mc.player.motionZ == 0.0) {
                                ElytraFlight.mc.player.motionY = this.downSpeedControl.getValue();
                            }
                            else {
                                calcMotionDiff = motionAmount * 0.008;
                                player = ElytraFlight.mc.player;
                                player.motionY += calcMotionDiff * 3.2;
                                player2 = ElytraFlight.mc.player;
                                player2.motionX -= -MathHelper.sin(yaw) * calcMotionDiff / 1.0;
                                player3 = ElytraFlight.mc.player;
                                player3.motionZ -= MathHelper.cos(yaw) * calcMotionDiff / 1.0;
                                player4 = ElytraFlight.mc.player;
                                player4.motionX *= 0.9900000095367432;
                                player5 = ElytraFlight.mc.player;
                                player5.motionY *= 0.9800000190734863;
                                player6 = ElytraFlight.mc.player;
                                player6.motionZ *= 0.9900000095367432;
                            }
                        }
                        else {
                            ElytraFlight.mc.player.motionX = -MathHelper.sin(yaw) * (double)this.speedControl.getValue();
                            ElytraFlight.mc.player.motionY = -this.fallSpeedControl.getValue();
                            ElytraFlight.mc.player.motionZ = MathHelper.cos(yaw) * (double)this.speedControl.getValue();
                        }
                    }
                    else {
                        ElytraFlight.mc.player.motionX = 0.0;
                        ElytraFlight.mc.player.motionY = 0.0;
                        ElytraFlight.mc.player.motionZ = 0.0;
                    }
                    if (moveDown) {
                        ElytraFlight.mc.player.motionY = -this.downSpeedControl.getValue();
                    }
                    if (moveUp || moveDown) {
                        this.hoverTarget = ElytraFlight.mc.player.posY;
                    }
                    event.cancel();
                }
            }
        }, (Predicate<PlayerTravelEvent>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (ElytraFlight.mc.player == null) {
            return;
        }
        if (this.defaultSetting.getValue()) {
            this.defaults();
        }
        if (this.enabledMode != this.mode.getValue() && !this.hasDoneWarning) {
            Command.sendChatMessage("&l&cWARNING:&r Changing the mode while you're flying is not recommended. If you weren't flying you can ignore this message.");
            this.hasDoneWarning = true;
        }
        if (this.mode.getValue().equals(ElytraFlightMode.CONTROL)) {
            return;
        }
        this.takeOff();
        this.setFlySpeed();
        if (ElytraFlight.mc.player.onGround) {
            ElytraFlight.mc.player.capabilities.allowFlying = false;
        }
        if (!ElytraFlight.mc.player.isElytraFlying()) {
            return;
        }
        if (this.mode.getValue() == ElytraFlightMode.BOOST) {
            if (ElytraFlight.mc.player.isInWater()) {
                Objects.requireNonNull(ElytraFlight.mc.getConnection()).sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                return;
            }
            if (ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown()) {
                final EntityPlayerSP player = ElytraFlight.mc.player;
                player.motionY += this.upSpeedBoost.getValue();
            }
            else if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP player2 = ElytraFlight.mc.player;
                player2.motionY -= this.downSpeedBoost.getValue();
            }
            if (ElytraFlight.mc.gameSettings.keyBindForward.isKeyDown()) {
                final float yaw = (float)Math.toRadians(ElytraFlight.mc.player.rotationYaw);
                final EntityPlayerSP player3 = ElytraFlight.mc.player;
                player3.motionX -= MathHelper.sin(yaw) * 0.05f;
                final EntityPlayerSP player4 = ElytraFlight.mc.player;
                player4.motionZ += MathHelper.cos(yaw) * 0.05f;
            }
            else if (ElytraFlight.mc.gameSettings.keyBindBack.isKeyDown()) {
                final float yaw = (float)Math.toRadians(ElytraFlight.mc.player.rotationYaw);
                final EntityPlayerSP player5 = ElytraFlight.mc.player;
                player5.motionX += MathHelper.sin(yaw) * 0.05f;
                final EntityPlayerSP player6 = ElytraFlight.mc.player;
                player6.motionZ -= MathHelper.cos(yaw) * 0.05f;
            }
        }
        else {
            ElytraFlight.mc.player.capabilities.setFlySpeed(0.915f);
            ElytraFlight.mc.player.capabilities.isFlying = true;
            if (ElytraFlight.mc.player.capabilities.isCreativeMode) {
                return;
            }
            ElytraFlight.mc.player.capabilities.allowFlying = true;
        }
    }
    
    private void setFlySpeed() {
        if (ElytraFlight.mc.player.capabilities.isFlying) {
            if (this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)) {
                ElytraFlight.mc.player.setSprinting(false);
                ElytraFlight.mc.player.setVelocity(0.0, 0.0, 0.0);
                ElytraFlight.mc.player.setPosition(ElytraFlight.mc.player.posX, ElytraFlight.mc.player.posY - this.fallSpeedHighway.getValue(), ElytraFlight.mc.player.posZ);
                ElytraFlight.mc.player.capabilities.setFlySpeed(this.getHighwaySpeed());
            }
            else {
                ElytraFlight.mc.player.setVelocity(0.0, 0.0, 0.0);
                ElytraFlight.mc.player.capabilities.setFlySpeed(0.915f);
                ElytraFlight.mc.player.setPosition(ElytraFlight.mc.player.posX, ElytraFlight.mc.player.posY - this.fallSpeed.getValue(), ElytraFlight.mc.player.posZ);
            }
        }
    }
    
    private void takeOff() {
        if (!this.mode.getValue().equals(ElytraFlightMode.HIGHWAY) || !this.easyTakeOff.getValue()) {
            return;
        }
        if (!ElytraFlight.mc.player.isElytraFlying() && !ElytraFlight.mc.player.onGround) {
            switch (this.takeOffMode.getValue()) {
                case CLIENT: {
                    ElytraFlight.mc.player.capabilities.isFlying = true;
                }
                case PACKET: {
                    Objects.requireNonNull(ElytraFlight.mc.getConnection()).sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    break;
                }
            }
        }
        if (ElytraFlight.mc.player.isElytraFlying()) {
            this.easyTakeOff.setValue(false);
            Command.sendChatMessage(this.getChatName() + "Disabled takeoff!");
        }
    }
    
    @Override
    protected void onDisable() {
        ElytraFlight.mc.timer.tickLength = 50.0f;
        ElytraFlight.mc.player.capabilities.isFlying = false;
        ElytraFlight.mc.player.capabilities.setFlySpeed(0.05f);
        if (ElytraFlight.mc.player.capabilities.isCreativeMode) {
            return;
        }
        ElytraFlight.mc.player.capabilities.allowFlying = false;
    }
    
    @Override
    protected void onEnable() {
        this.enabledMode = this.mode.getValue();
        this.hoverTarget = -1.0;
    }
    
    private void defaults() {
        this.easyTakeOff.setValue(true);
        this.hoverControl.setValue(true);
        this.easyTakeOffControl.setValue(false);
        this.timerControl.setValue(false);
        this.takeOffMode.setValue(TakeoffMode.PACKET);
        this.overrideMaxSpeed.setValue(false);
        this.speedHighway.setValue(1.8f);
        this.speedHighwayOverride.setValue(1.8f);
        this.speedControl.setValue(1.8f);
        this.fallSpeedHighway.setValue(5.0000002E-5f);
        this.fallSpeedControl.setValue(0.001f);
        this.fallSpeed.setValue(-0.003f);
        this.upSpeedBoost.setValue(0.08f);
        this.downSpeedBoost.setValue(0.04f);
        this.downSpeedControl.setValue(2.0);
        this.defaultSetting.setValue(false);
        Command.sendChatMessage(this.getChatName() + "Set to defaults!");
    }
    
    private float getHighwaySpeed() {
        if (this.overrideMaxSpeed.getValue()) {
            return this.speedHighwayOverride.getValue();
        }
        return this.speedHighway.getValue();
    }
    
    private enum ElytraFlightMode
    {
        BOOST, 
        FLY, 
        CONTROL, 
        HIGHWAY;
    }
    
    private enum TakeoffMode
    {
        CLIENT, 
        PACKET;
    }
}
