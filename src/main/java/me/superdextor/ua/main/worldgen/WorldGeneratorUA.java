package me.superdextor.ua.main.worldgen;

import java.util.Random;

import me.superdextor.ua.main.blocks.UABlocks;
import me.superdextor.ua.main.config.ConfigUA;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGeneratorUA implements IWorldGenerator
{
	private final WorldGenerator genstatue = new WorldGenStatue();
	private final WorldGenerator gendeores;
	
	public WorldGeneratorUA()
	{
		this.gendeores = new WorldGenMinable(UABlocks.DE_ORE.getDefaultState(), 8, BlockMatcher.forBlock(Blocks.STONE));
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.getWorldInfo().getTerrainType()==WorldType.FLAT)
		{
			return;
		}
		
		switch(world.provider.getDimension())
		{
			default: break;
			case 0:
			{
				BlockPos pos = new BlockPos(chunkX*16, 0, chunkZ*16);
				this.genstatue.generate(world,random,pos);
				
				for(int i = ConfigUA.deOreSpawnrate; i > 0; i--)
				{
					this.gendeores.generate(world, random, pos.add(random.nextInt(16), random.nextInt(64), random.nextInt(16)));
				}
			}
		}
	}
}
