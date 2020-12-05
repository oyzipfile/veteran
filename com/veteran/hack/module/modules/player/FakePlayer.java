// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.module.modules.player;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.util.ArrayList;
import com.veteran.hack.setting.Settings;
import java.util.List;
import com.veteran.hack.setting.Setting;
import com.veteran.hack.module.Module;

@Info(name = "FakePlayerForDev", category = Category.PLAYER, description = "Spawns a fake Player")
public class FakePlayer extends Module
{
    private Setting<SpawnMode> spawnMode;
    private List<Integer> fakePlayerIdList;
    private static final String[][] fakePlayerInfo;
    
    public FakePlayer() {
        this.spawnMode = this.register(Settings.e("Spawn Mode", SpawnMode.SINGLE));
        this.fakePlayerIdList = null;
    }
    
    @Override
    protected void onEnable() {
        if (FakePlayer.mc.player == null || FakePlayer.mc.world == null) {
            this.disable();
            return;
        }
        this.fakePlayerIdList = new ArrayList<Integer>();
        int entityId = -101;
        for (final String[] data : FakePlayer.fakePlayerInfo) {
            if (this.spawnMode.getValue().equals(SpawnMode.SINGLE)) {
                this.addFakePlayer(data[0], data[1], entityId, 0, 0);
                break;
            }
            this.addFakePlayer(data[0], data[1], entityId, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
            --entityId;
        }
    }
    
    private void addFakePlayer(final String uuid, final String name, final int entityId, final int offsetX, final int offsetZ) {
        final EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString(uuid), name));
        fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
        final EntityOtherPlayerMP entityOtherPlayerMP = fakePlayer;
        entityOtherPlayerMP.posX += offsetX;
        final EntityOtherPlayerMP entityOtherPlayerMP2 = fakePlayer;
        entityOtherPlayerMP2.posZ += offsetZ;
        FakePlayer.mc.world.addEntityToWorld(entityId, (Entity)fakePlayer);
        this.fakePlayerIdList.add(entityId);
    }
    
    @Override
    public void onUpdate() {
        if (this.fakePlayerIdList == null || this.fakePlayerIdList.isEmpty()) {
            this.disable();
        }
    }
    
    @Override
    protected void onDisable() {
        if (FakePlayer.mc.player == null || FakePlayer.mc.world == null) {
            return;
        }
        if (this.fakePlayerIdList != null) {
            for (final int id : this.fakePlayerIdList) {
                FakePlayer.mc.world.removeEntityFromWorld(id);
            }
        }
    }
    
    static {
        fakePlayerInfo = new String[][] { { "66666666-6666-6666-6666-666666666600", "derp0", "-3", "0" }, { "66666666-6666-6666-6666-666666666601", "derp1", "0", "-3" }, { "66666666-6666-6666-6666-666666666602", "derp2", "3", "0" }, { "66666666-6666-6666-6666-666666666603", "derp3", "0", "3" }, { "66666666-6666-6666-6666-666666666604", "derp4", "-6", "0" }, { "66666666-6666-6666-6666-666666666605", "derp5", "0", "-6" }, { "66666666-6666-6666-6666-666666666606", "derp6", "6", "0" }, { "66666666-6666-6666-6666-666666666607", "derp7", "0", "6" }, { "66666666-6666-6666-6666-666666666608", "derp8", "-9", "0" }, { "66666666-6666-6666-6666-666666666609", "derp9", "0", "-9" }, { "66666666-6666-6666-6666-666666666610", "derp10", "9", "0" }, { "66666666-6666-6666-6666-666666666611", "derp11", "0", "9" } };
    }
    
    private enum SpawnMode
    {
        SINGLE, 
        MULTI;
    }
}
