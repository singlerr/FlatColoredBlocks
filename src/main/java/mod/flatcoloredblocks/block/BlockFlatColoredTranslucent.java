package mod.flatcoloredblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockFlatColoredTranslucent extends BlockFlatColored
{
	public BlockFlatColoredTranslucent(
			final int i,
			final int j,
			final int varientNum )
	{
		super( i, j, varientNum );

		// Its still a full block.. even if its not a opaque cube
		// C&B requires this.
		fullBlock = true;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean doesSideBlockRendering(
			final IBlockState state,
			final IBlockAccess world,
			final BlockPos pos,
			final EnumFacing face )
	{
		final Block blk = state.getBlock();

		if ( blk instanceof BlockFlatColoredTranslucent )
		{
			return true;
		}

		return super.doesSideBlockRendering( state, world, pos, face );
	}

	@Override
	public boolean isFullCube(
			final IBlockState state )
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(
			final IBlockState state )
	{
		return false;
	}

}
