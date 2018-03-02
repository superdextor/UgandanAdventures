package me.superdextor.ua.main.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigUA
{
	private final Configuration config;
	
	public static boolean uniqueCreativeTab, naturalSpawns;

	public static int deOreSpawnrate;
	
	public static boolean tameQueen, tameCommander, tameElite, tameWarrior;
	
	public ConfigUA(String dir)
	{
		this.config = new Configuration(new File(dir+"/UgandanAdventures.cfg"));
		try
		{
			this.config.load();
			updateConfigValues();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			this.config.save();
		}
	}
	
	public Configuration getConfig()
	{
		return this.config;
	}
	
	public void updateConfigValues()
	{
		final String General = this.config.CATEGORY_GENERAL;
		this.config.get(General, "1 Unique Creative Tab", true).setRequiresMcRestart(true);
		uniqueCreativeTab = this.config.getBoolean("1 Unique Creative Tab", General, true, "Gives Ugandan Adventures it's own Creative tab");
		this.config.get(General, "2 Natural Spawns", true).setRequiresMcRestart(true);
		naturalSpawns = this.config.getBoolean("2 Natural Spawns", General, true, "Allows Ugandan Knuckles to naturally spawn in your world");
		this.config.get(General, "3 \"De Ore\" Spawn Rate", 7).setRequiresWorldRestart(true);
		deOreSpawnrate = this.config.getInt("3 \"De Ore\" Spawn Rate", General, 7, 0, 100, "The amount of times \"De Ore\" will spawn in a chunk");
		tameWarrior = this.config.getBoolean("4 Tamable Warrior", General, true, "Toggle the ability to tame Red Knuckles");
		tameElite = this.config.getBoolean("5 Tamable Elite", General, true, "Toggle the ability to tame Blue Knuckles");
		tameCommander = this.config.getBoolean("6 Tamable Commander", General, false, "Toggle the ability to tame Yellow Knuckles");
		tameQueen = this.config.getBoolean("7 Tamable Queen", General, false, "Toggle the ability to tame Pink Knuckles");
	}
}
