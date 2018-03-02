package me.superdextor.ua.main;

import org.apache.logging.log4j.LogManager;

import me.superdextor.ua.main.blocks.UABlocks;
import me.superdextor.ua.main.config.ConfigUA;
import me.superdextor.ua.main.entities.EntityCommander;
import me.superdextor.ua.main.entities.EntityDeBomb;
import me.superdextor.ua.main.entities.EntityElite;
import me.superdextor.ua.main.entities.EntityGiantKnuckles;
import me.superdextor.ua.main.entities.EntityKnucklesSpit;
import me.superdextor.ua.main.entities.EntityQueen;
import me.superdextor.ua.main.entities.EntitySpear;
import me.superdextor.ua.main.entities.EntityWarrior;
import me.superdextor.ua.main.items.UAItems;
import me.superdextor.ua.main.items.ItemUASpawnEgg.EggEntry;
import me.superdextor.ua.main.worldgen.WorldGeneratorUA;
import me.superdextor.ua.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = ReferenceUA.MOD_ID, name = ReferenceUA.MOD_NAME, version = ReferenceUA.VERSION, acceptedMinecraftVersions = ReferenceUA.MC_VERSION, guiFactory = ReferenceUA.GUI_FACTORY, canBeDeactivated = false)
public class ModUgandanAdventures
{
	@Instance(ReferenceUA.MOD_ID)
	public static ModUgandanAdventures modInstance;
	
	@SidedProxy(clientSide = ReferenceUA.CLIENT_PROXY_CLASS, serverSide = ReferenceUA.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static ConfigUA config;
	
	@EventHandler
	public void PreInit(net.minecraftforge.fml.common.event.FMLPreInitializationEvent event)
	{
		long start = System.currentTimeMillis();
		config = new ConfigUA(event.getModConfigurationDirectory().toString());
		new ModAssets();
		CreativeTabs tab = null;
		if(ConfigUA.uniqueCreativeTab)
		{
			tab = new CreativeTabs("ua")
			{
		final ItemStack stack = new ItemStack(UAItems.WHEY);
		@Override
		public ItemStack getTabIconItem()
		{
			return this.stack;
		}
			};
		}
		new UABlocks(tab);
		new UAItems(tab);
		addRecipes();
		addEntities();
		GameRegistry.registerWorldGenerator(new WorldGeneratorUA(), 4);
		proxy.preInit();
		LogManager.getLogger("UA").info("PreInitialization completed in " + (System.currentTimeMillis() - start) + "ms");
	}
	
	@EventHandler
	public void Init(net.minecraftforge.fml.common.event.FMLInitializationEvent event)
	{
		long start = System.currentTimeMillis();
		proxy.init();
		LogManager.getLogger("UA").info("Initialization completed in " + (System.currentTimeMillis() - start) + "ms");
	}
	
	@EventHandler
	public void postInit(net.minecraftforge.fml.common.event.FMLPostInitializationEvent event)
	{
		long start = System.currentTimeMillis();
		proxy.postInit();
		LogManager.getLogger("UA").info("PostInitialization completed in " + (System.currentTimeMillis() - start) + "ms");
	}
	
	private void addRecipes()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(UAItems.DE, 9), UABlocks.DE_BLOCK);
		GameRegistry.addRecipe(new ItemStack(UABlocks.DE_BLOCK), "XXX","XXX","XXX",'X', UAItems.DE);
		GameRegistry.addSmelting(UABlocks.DE_ORE, new ItemStack(UAItems.DE), 0.2F);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_SWORD), "X", "X","Y", 'X', UAItems.DE, 'Y', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_AXE), "XX", "XY", " Y", 'X', UAItems.DE, 'Y', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_PICKAXE), "XXX", " Y ", " Y ", 'X', UAItems.DE, 'Y', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_SHOVEL), "X","Y","Y",'X',UAItems.DE,'Y',Items.STICK);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_HOE), "XX"," Y", " Y", 'X', UAItems.DE, 'Y',Items.STICK);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_HELMET), "XXX", "X X", 'X', UAItems.DE);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_CHESTPLATE), "X X", "XXX", "XXX", 'X', UAItems.DE);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_LEGGINGS), "XXX", "X X", "X X", 'X', UAItems.DE);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_BOOTS), "X X", "X X", 'X', UAItems.DE);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_APPLE), "XXX","XYX","XXX",'X',UAItems.DE,'Y',Items.APPLE);
		GameRegistry.addRecipe(new ItemStack(UAItems.DE_APPLE,1,1), "XXX","XYX","XXX",'X',UABlocks.DE_BLOCK,'Y',Items.APPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(UAItems.WHEY), UAItems.DE, UABlocks.WAY);
		GameRegistry.addShapelessRecipe(new ItemStack(UAItems.DE_BOMB), UAItems.DE, Items.GUNPOWDER, Items.GUNPOWDER);
		GameRegistry.addRecipe(new ItemStack(UAItems.SPEAR,3), "X","Y","Y",'X',Items.FLINT,'Y',Items.STICK);
	}
	
	private void addEntities()
	{
		byte id = 0;
		this.addEntity(EntityWarrior.class, "warrior", id++);
		this.addEntity(EntityElite.class, "elite", id++);
		this.addEntity(EntityCommander.class, "commander", id++);
		this.addEntity(EntityQueen.class, "queen", id++);
		EntityRegistry.registerModEntity(new ResourceLocation("ua","spit"), EntityKnucklesSpit.class, "spit", id++, this, 60, 2, true);
		this.addEntity(EntityGiantKnuckles.class, "giant", id++);
		EntityRegistry.registerModEntity(new ResourceLocation("ua","de_bomb"), EntityDeBomb.class, "de_bomb", id++, this, 60, 2, true);
		EntityRegistry.registerModEntity(new ResourceLocation("ua","spear"), EntitySpear.class, "spear", id++, this, 60, 2, true);
		if(ConfigUA.naturalSpawns)
		{
			Biome[] biomes = this.getBiomes();
			this.addSpawn(EntityWarrior.class, 10, 1, 3, biomes);
			this.addSpawn(EntityElite.class, 6, 1, 2, biomes);
			this.addSpawn(EntityCommander.class, 4, 1, 1, biomes);
			this.addSpawn(EntityQueen.class, 3, 1, 1, biomes);
		}
		UAItems.SPAWN_EGG.insertEggInfo(new EggEntry(EntityWarrior.class, 0xE81A21, 0xFAC08F), new EggEntry(EntityElite.class, 0x0F1C7A, 0xFAC08F), new EggEntry(EntityCommander.class, 0xFFCC00, 0xFAC08F), new EggEntry(EntityQueen.class, 0xFF77EF, 0xFAC08F));
	}
	
	private void addEntity(Class<? extends Entity> clazz, String entityName, int id)
	{
		EntityRegistry.registerModEntity(new ResourceLocation("ua",entityName), clazz, entityName, id, this, 60, 1, false);
	}
	
	private void addSpawn(Class<? extends EntityLiving> clazz, int weight, int min, int max, Biome[] biomes)
	{
		EntityRegistry.addSpawn(clazz, weight, min, max, EnumCreatureType.CREATURE, biomes);
	}
	
	private Biome[] getBiomes()
	{
		return new Biome[] {Biomes.BEACH, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.COLD_BEACH, Biomes.COLD_TAIGA, Biomes.COLD_TAIGA_HILLS, Biomes.DESERT, Biomes.DESERT_HILLS
	    		, Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_EDGE, Biomes.EXTREME_HILLS_WITH_TREES, Biomes.FOREST, Biomes.FOREST_HILLS, Biomes.FROZEN_RIVER, Biomes.ICE_MOUNTAINS, Biomes.ICE_PLAINS, Biomes.JUNGLE
	    		, Biomes.JUNGLE_EDGE, Biomes.JUNGLE_HILLS, Biomes.MESA, Biomes.MESA_CLEAR_ROCK, Biomes.MESA_ROCK, Biomes.MUTATED_BIRCH_FOREST, Biomes.MUTATED_BIRCH_FOREST_HILLS, Biomes.MUTATED_DESERT,
	    		Biomes.MUTATED_EXTREME_HILLS, Biomes.MUTATED_EXTREME_HILLS, Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, Biomes.MUTATED_FOREST, Biomes.MUTATED_ICE_FLATS, Biomes.MUTATED_JUNGLE, Biomes.MUTATED_JUNGLE, 
	    		Biomes.MUTATED_JUNGLE_EDGE, Biomes.MUTATED_MESA, Biomes.MUTATED_MESA_CLEAR_ROCK, Biomes.MUTATED_MESA_ROCK, Biomes.MUTATED_PLAINS, Biomes.MUTATED_REDWOOD_TAIGA, Biomes.MUTATED_REDWOOD_TAIGA_HILLS, Biomes.MUTATED_ROOFED_FOREST, 
	    		Biomes.MUTATED_ROOFED_FOREST, Biomes.MUTATED_SAVANNA, Biomes.MUTATED_SAVANNA_ROCK, Biomes.MUTATED_TAIGA, Biomes.MUTATED_TAIGA_COLD, Biomes.PLAINS, Biomes.REDWOOD_TAIGA, Biomes.REDWOOD_TAIGA_HILLS, Biomes.RIVER, 
	    		Biomes.ROOFED_FOREST, Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.FROZEN_OCEAN, Biomes.STONE_BEACH, Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.SWAMPLAND};
	}
}
