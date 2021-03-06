// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.veteran.hack.module.modules.movement.NoSlowDown;
import com.veteran.hack.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.BlockWeb;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockWeb.class })
public class MixinBlockWeb
{
    @Inject(method = { "onEntityCollision" }, at = { @At("HEAD") }, cancellable = true)
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn, final CallbackInfo info) {
        if (ModuleManager.isModuleEnabled("NoSlowDown") && ((NoSlowDown)ModuleManager.getModuleByName("NoSlowDown")).cobweb.getValue()) {
            info.cancel();
        }
    }
}
