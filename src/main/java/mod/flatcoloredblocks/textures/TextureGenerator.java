package mod.flatcoloredblocks.textures;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mod.flatcoloredblocks.FlatColoredBlocks;
import mod.flatcoloredblocks.Log;
import mod.flatcoloredblocks.block.EnumFlatBlockType;
import mod.flatcoloredblocks.client.ClientSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TextureGenerator
{

	private final Map<Integer, TextureAtlasSprite> generatedTransparentTexture = new HashMap<Integer, TextureAtlasSprite>();
	private TextureAtlasSprite glowingTexture;

	@SubscribeEvent
	void registerTransparentTextures(
			final TextureStitchEvent.Pre ev )
	{
		try
		{
			final ResourceLocation sourceLoc = ClientSide.instance.getTextureResourceLocation( EnumFlatBlockType.TRANSPARENT );
			final IResource iresource = Minecraft.getInstance().getResourceManager().getResource( sourceLoc );
			final NativeImage bi = NativeImage.read( iresource.getInputStream() );

			for ( final int varient : FlatColoredBlocks.instance.transparent.shadeConvertVariant )
			{
				final ResourceLocation name = ClientSide.instance.getTextureName( EnumFlatBlockType.TRANSPARENT, varient );
				final TextureAtlasSprite out = AlphaModifiedTexture.generate( name, bi, varient / 255.0f, ev.getMap() );

				// register.
				generatedTransparentTexture.put( varient, out );
			}
		}
		catch ( final IOException e )
		{
			// just load the texture if for some reason the above fails.
			Log.logError( "Unable to load Base Texture", e );

			final TextureAtlasSprite out = ev.getMap().getSprite( new ResourceLocation( ClientSide.instance.getBaseTextureNameWithBlocks( EnumFlatBlockType.TRANSPARENT ) ) );

			for ( final int varient : FlatColoredBlocks.instance.transparent.shadeConvertVariant )
			{
				generatedTransparentTexture.put( varient, out );
			}
		}

		if ( !FlatColoredBlocks.instance.config.GLOWING_EMITS_LIGHT )
		{
			glowingTexture = ev.getMap().getSprite( new ResourceLocation( ClientSide.instance.getBaseTextureNameWithBlocks( EnumFlatBlockType.GLOWING ) ) );
		}
	}

	private TextureAtlasSprite orMissing(
			TextureAtlasSprite texture )
	{
		// getSprite(null) - missing sprite
		return texture != null ? texture : Minecraft.getInstance().getTextureMap().getSprite( null );
	}

	public TextureAtlasSprite getGlowingTexture(
			final int varient )
	{
		return orMissing( glowingTexture );
	}

	public TextureAtlasSprite getTransparentTexture(
			final int varient )
	{
		return orMissing( generatedTransparentTexture.get( varient ) );
	}

}
