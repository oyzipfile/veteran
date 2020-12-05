// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import org.spongepowered.asm.mixin.injection.Inject;
import com.veteran.hack.BaseMod;
import com.veteran.hack.event.events.ClientPlayerAttackEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.veteran.hack.util.LagCompensator;
import com.veteran.hack.module.modules.player.TpsSync;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ PlayerControllerMP.class })
public class MixinPlayerControllerMP
{
    @Redirect(method = { "onPlayerDamageBlock" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F"))
    float getPlayerRelativeBlockHardness(final IBlockState state, final EntityPlayer player, final World worldIn, final BlockPos pos) {
        return state.getPlayerRelativeBlockHardness(player, worldIn, pos) * (TpsSync.isSync() ? (LagCompensator.INSTANCE.getTickRate() / 20.0f) : 1.0f);
    }
    
    @Inject(method = { "attackEntity" }, at = { @At("HEAD") }, cancellable = true)
    public void attackEntity(final EntityPlayer playerIn, final Entity targetEntity, final CallbackInfo ci) {
        if (targetEntity == null) {
            return;
        }
        if (targetEntity instanceof EntityPlayerSP) {
            final ClientPlayerAttackEvent e = new ClientPlayerAttackEvent(targetEntity);
            BaseMod.EVENT_BUS.post(e);
            if (e.isCancelled()) {
                ci.cancel();
            }
        }
    }
}
