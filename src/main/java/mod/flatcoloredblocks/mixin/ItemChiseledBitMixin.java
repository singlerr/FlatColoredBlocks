package mod.flatcoloredblocks.mixin;

import mod.chiselsandbits.chiseledblock.BlockBitInfo;
import mod.chiselsandbits.core.Log;
import mod.chiselsandbits.helpers.DeprecationHelper;
import mod.chiselsandbits.helpers.ModUtil;
import mod.chiselsandbits.items.ItemChiseledBit;
import mod.flatcoloredblocks.block.BlockFlatColored;
import mod.flatcoloredblocks.block.ItemBlockFlatColored;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.HashSet;
import java.util.Set;

@Mixin(ItemChiseledBit.class)
public class ItemChiseledBitMixin {
    /**
     * @author singlerr
     * @reason unable to inject code to this method, overwriting codes almost copied from github repository
     */
    @Overwrite(remap = false)
    public static ITextComponent getBitStateName(BlockState state) {
        ItemStack target = null;
        Block blk = null;

        if (state == null) {
            return new StringTextComponent("Null");
        }

        try {
            blk = state.getBlock();

            final Item item = Item.getItemFromBlock(blk);
            if (ModUtil.isEmpty(item)) {
                final Fluid f = BlockBitInfo.getFluidFromBlock(blk);
                if (f != null) {
                    return new TranslationTextComponent(f.getAttributes().getTranslationKey());
                }
            } else {
                if (item instanceof ItemBlockFlatColored) {
                    BlockFlatColored blockFlatColored = (BlockFlatColored) blk;
                    target = blockFlatColored.getItem(null, null, state);
                } else {
                    target = new ItemStack(() -> Item.getItemFromBlock(state.getBlock()), 1);
                }

            }
        } catch (final IllegalArgumentException e) {
            Log.logError("Unable to get Item Details for Bit.", e);
        }

        if (target == null || target.getItem() == null) {
            return null;
        }

        try {
            final ITextComponent myName = target.getDisplayName();
            if (!(myName instanceof IFormattableTextComponent))
                return myName;

            final IFormattableTextComponent formattableName = (IFormattableTextComponent) myName;

            final Set<String> extra = new HashSet<>();
            if (blk != null && state != null) {
                for (final Property<?> p : state.getProperties()) {
                    if (p.getName().equals("axis") || p.getName().equals("facing")) {
                        extra.add(DeprecationHelper.translateToLocal("mod.chiselsandbits.pretty." + p.getName() + "-" + state.get(p)));
                    }
                }
            }

            if (extra.isEmpty()) {
                return myName;
            }

            for (final String x : extra) {
                formattableName.appendString(" ").appendString(x);
            }

            return formattableName;
        } catch (final Exception e) {
            return new StringTextComponent("Error");
        }
    }
}
