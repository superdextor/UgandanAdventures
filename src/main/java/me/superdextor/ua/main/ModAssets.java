package me.superdextor.ua.main;

import org.apache.logging.log4j.LogManager;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.loot.LootTableList;

public class ModAssets
{
	public final static SoundEvent ENTITY_KNUCKLES_TALKINGTO;
	public final static SoundEvent ENTITY_KNUCKLES_TRIBE;
	public final static SoundEvent ENTITY_KNUCKLES_FIGHTING;
	public final static SoundEvent ENTITY_KNUCKLES_HURT;
	public final static SoundEvent ENTITY_KNUCKLES_DEATH;
	public static final SoundEvent ENTITY_KNUCKLES_CLACK;
	public final static SoundEvent ENTITY_KNUCKLES_SPIT;
	public final static SoundEvent RECORD_ONFIRE;
	public final static SoundEvent RECORD_KNUCKLES;
	public final static SoundEvent RECORD_UDONTKNOW;
	public final static SoundEvent RECORD_DEWAE;
	
	public final static ResourceLocation STATUE_LOOT;
	
	static
	{
		int id = 850000;
		ENTITY_KNUCKLES_TALKINGTO = addSound("entity.knuckles.talkingto", id++);
		ENTITY_KNUCKLES_TRIBE = addSound("entity.knuckles.tribe", id++);
		ENTITY_KNUCKLES_FIGHTING = addSound("entity.knuckles.fighting", id++);
		ENTITY_KNUCKLES_HURT = addSound("entity.knuckles.hurt", id++);
		ENTITY_KNUCKLES_DEATH = addSound("entity.knuckles.death", id++);
		ENTITY_KNUCKLES_CLACK = addSound("entity.knuckles.clack", id++);
		ENTITY_KNUCKLES_SPIT = addSound("entity.knuckles.spit", id++);
		RECORD_ONFIRE = addSound("records.onfire", id++);
		RECORD_KNUCKLES = addSound("records.knuckles", id++);
		RECORD_UDONTKNOW = addSound("records.udontknow", id++);
		RECORD_DEWAE = addSound("records.dewae", id++);
		
		STATUE_LOOT = addLoottable("statue");
	}
	
	private static SoundEvent addSound(String s, int id)
	{
		ResourceLocation location = new ResourceLocation("ua",s);
		SoundEvent event = new SoundEvent(location);
		if(SoundEvent.REGISTRY.getObjectById(id) != null)
		{
			LogManager.getLogger("UA").warn("Skipping sound '"+s+"' Id is already taken by '"+SoundEvent.REGISTRY.getObjectById(id).getRegistryName());
		}
		else
		{
			SoundEvent.REGISTRY.register(id, location, event);
		}
		return event;
	}
	
	private static ResourceLocation addLoottable(String path)
	{
		ResourceLocation location = new ResourceLocation("ua",path);
		LootTableList.register(location);
		return location;
	}
}
