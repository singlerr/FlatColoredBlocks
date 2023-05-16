package mod.flatcoloredblocks.mixin;

import mod.chiselsandbits.helpers.DeprecationHelper;
import mod.flatcoloredblocks.ModUtil;
import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.ItemBlockFlatColored;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({DeprecationHelper.class})
public class DeprecationHelperMixin {
    @Inject(method = "getStateFromItem", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void mixinGetStateFromItem(ItemStack bitItemStack, CallbackInfoReturnable<BlockState> cir) {
        if (bitItemStack.getItem() instanceof ItemBlockFlatColored) {
            BlockFlatColored block = ((ItemBlockFlatColored) bitItemStack.getItem()).getColoredBlock();
            cir.setReturnValue(ModUtil.getFlatColoredBlockState(block, bitItemStack));
            cir.cancel();
        }
    }
}
