package me.superdextor.ua.main.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class UABlocks
{
	public static final Block DE_ORE;
	public static final Block DE_BLOCK;
	public static final Block WAY;
	
	public UABlocks(CreativeTabs tab)
	{
		this.registerBlock(DE_ORE.setCreativeTab(tab==null?CreativeTabs.BUILDING_BLOCKS:tab), "de_ore");
		this.registerBlock(DE_BLOCK.setCreativeTab(tab==null?CreativeTabs.BUILDING_BLOCKS:tab), "de_block");
		this.registerBlock(WAY.setCreativeTab(tab==null?CreativeTabs.MISC:tab), "way");
	}
	
	private void registerBlock(Block blockIn, String name)
	{
		GameRegistry.register(blockIn.setUnlocalizedName(name).setRegistryName(name));
		GameRegistry.register(new ItemBlock(blockIn).setRegistryName(name));
	}
	
	static
	{
		DE_ORE = new BlockOre(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);
		DE_ORE.setHarvestLevel("pickaxe", 1);
		DE_BLOCK = new UABlock(Material.IRON, MapColor.CYAN, SoundType.METAL).setHardness(5.0F).setResistance(10.0F);
		WAY = new BlockWay();
	}
	
	private static class UABlock extends Block
	{
		public UABlock(Material material, SoundType type)
		{
			super(material);
			this.setSoundType(type);
		}
		
		public UABlock(Material material, MapColor color, SoundType type)
		{
			super(material, color);
			this.setSoundType(type);
		}
	}
	
	private static class BlockOre extends net.minecraft.block.BlockOre
	{
		public BlockOre(SoundType type)
		{
			this.setSoundType(type);
		}
	}
}
