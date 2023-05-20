package mod.flatcoloredblocks.mixin;

import mod.chiselsandbits.helpers.ModUtil;
import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.ItemBlockFlatColored;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ModUtil.class)
public class ModUtilMixin {

    @Inject(method = "getItemStackFromBlockState", at = @At(value = "RETURN", ordinal = 1),locals = LocalCapture.CAPTURE_FAILHARD, remap = false, cancellable = true)
    private static void fixReturnCorrectItemStack(BlockState blockState, CallbackInfoReturnable<ItemStack> cir, Item item){
        if(item instanceof ItemBlockFlatColored){
            BlockFlatColored block = ((ItemBlockFlatColored)item).getColoredBlock();
            cir.setReturnValue(block.getItem(null,null,blockState));
            cir.cancel();
        }

    }
}
