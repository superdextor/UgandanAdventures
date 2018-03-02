package me.superdextor.ua.main.worldgen;

import java.util.Random;

import me.superdextor.ua.main.ModAssets;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class WorldGenStatue extends WorldGenerator
{
	private static final ResourceLocation STATUE_KNUCKLES = new ResourceLocation("ua","knuckles_statue");
	private static final ResourceLocation STATUE_COMMANDER = new ResourceLocation("ua","commander_statue");
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		pos = world.getHeight(pos.add(5, 0, 5));
		Rotation rotation = Rotation.values()[random.nextInt(4)];
		if(this.isValidArea(world, pos))
		{
			PlacementSettings settings = new PlacementSettings().setRotation(rotation);
			Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), random.nextInt(4)==0?STATUE_COMMANDER:STATUE_KNUCKLES);
			template.addBlocksToWorld(world, pos, settings);
			switch(rotation)
			{
				case NONE:
				{
					pos = pos.add(5, 11, 10);
					break;
				}
				
				case CLOCKWISE_90:
				{
					pos = pos.add(-10, 11, 5);
					break;
				}
				
				case CLOCKWISE_180:
				{
					pos = pos.add(-5, 11, -10);
					break;
				}
				
				case COUNTERCLOCKWISE_90:
				{
					pos = pos.add(10, 11, -5);
					break;
				}
			}
			world.setBlockState(pos, Blocks.CHEST.getDefaultState());
			if(world.getTileEntity(pos) instanceof TileEntityChest)
			{
				TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
				chest.setLootTable(ModAssets.STATUE_LOOT, random.nextLong());
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isValidArea(World world, BlockPos pos)
	{
		int x;
		int y;
		int z;
		BlockPos pos2;
		for(x = 0; x < 22; x++)
		{
			for(z = 0; z < 22; z++)
			{
				if(!world.isSideSolid(pos.add(x, -1, z), EnumFacing.UP)) return false;
				for(y = 0; y < 26; y++)
				{
					pos2 = pos.add(x, y, z);
					if(!world.getBlockState(pos2).getBlock().isReplaceable(world, pos2)) return false;
				}
			}
		}
		return true;
	}
}
