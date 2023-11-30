package io.github.mms0316.litematica_mms_ext.mixin;

import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WorldUtils.class, remap = false)
public abstract class MixinWorldUtils {

    @Inject(method = "easyPlaceBlockChecksCancel",
            at = @At("RETURN"), cancellable = true)
    private static void litematicammsext_breakExistingIfCreative(BlockState stateSchematic, BlockState stateClient, PlayerEntity player, HitResult trace, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if (EntityUtils.isCreativeMode(player)) {
                if (trace.getType() == HitResult.Type.BLOCK) {
                    // Break existing block

                    BlockHitResult hitResult = (BlockHitResult) trace;
                    var posVanilla = hitResult.getBlockPos();
                    var sideVanilla = hitResult.getSide();
                    MinecraftClient.getInstance().interactionManager.attackBlock(posVanilla, sideVanilla);

                    // Stop easyPlace from failing
                    cir.setReturnValue(false);
                }
            }
        }
    }

}