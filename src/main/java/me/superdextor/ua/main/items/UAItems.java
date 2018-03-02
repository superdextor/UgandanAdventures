package me.superdextor.ua.main.items;

import java.util.List;

import me.superdextor.ua.main.ModAssets;
import me.superdextor.ua.main.entities.EntityDeBomb;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UAItems
{
	public static final Item DE;
	public static final ItemSword DE_SWORD;
	public static final ItemAxe DE_AXE;
	public static final ItemPickaxe DE_PICKAXE;
	public static final ItemSpade DE_SHOVEL;
	public static final ItemHoe DE_HOE;
	public static final ItemSpear SPEAR;
	public static final Item DE_BOMB;
	public static final ItemArmor DE_HELMET;
	public static final ItemArmor DE_CHESTPLATE;
	public static final ItemArmor DE_LEGGINGS;
	public static final ItemArmor DE_BOOTS;
	public static final ItemFood DE_APPLE;
	public static final ItemFood WHEY;
	public static final ItemUASpawnEgg SPAWN_EGG;
	public static final ItemRecord RECORD_1;
	public static final ItemRecord RECORD_2;
	public static final ItemRecord RECORD_3;
	public static final ItemRecord RECORD_4;
	
	public UAItems(CreativeTabs tab)
	{
		this.registerItem(DE.setCreativeTab(tab==null?CreativeTabs.MISC:tab), "de");
		this.registerItem(DE_SWORD.setCreativeTab(tab==null?CreativeTabs.COMBAT:tab), "de_sword");
		this.registerItem(DE_AXE.setCreativeTab(tab==null?CreativeTabs.TOOLS:tab), "de_axe");
		this.registerItem(DE_PICKAXE.setCreativeTab(tab==null?CreativeTabs.TOOLS:tab), "de_pickaxe");
		this.registerItem(DE_SHOVEL.setCreativeTab(tab==null?CreativeTabs.TOOLS:tab), "de_shovel");
		this.registerItem(DE_HOE.setCreativeTab(tab==null?CreativeTabs.TOOLS:tab), "de_hoe");
		this.registerItem(SPEAR.setCreativeTab(tab==null?CreativeTabs.COMBAT:tab), "spear");
		this.registerItem(DE_BOMB.setCreativeTab(tab==null?CreativeTabs.COMBAT:tab), "de_bomb");
		this.registerItem(DE_HELMET.setCreativeTab(tab==null?CreativeTabs.COMBAT:tab), "de_helmet");
		this.registerItem(DE_CHESTPLATE.setCreativeTab(tab==null?CreativeTabs.COMBAT:tab), "de_chestplate");
		this.registerItem(DE_LEGGINGS.setCreativeTab(tab==null?CreativeTabs.COMBAT:tab), "de_leggings");
		this.registerItem(DE_BOOTS.setCreativeTab(tab==null?CreativeTabs.COMBAT:tab), "de_boots");
		this.registerItem(DE_APPLE.setCreativeTab(tab==null?CreativeTabs.FOOD:tab), "de_apple");
		this.registerItem(WHEY.setAlwaysEdible().setCreativeTab(tab==null?CreativeTabs.FOOD:tab), "whey");
		this.registerItem(RECORD_1.setCreativeTab(tab==null?CreativeTabs.MISC:tab), "record_1");
		this.registerItem(RECORD_2.setCreativeTab(tab==null?CreativeTabs.MISC:tab), "record_2");
		this.registerItem(RECORD_3.setCreativeTab(tab==null?CreativeTabs.MISC:tab), "record_3");
		this.registerItem(RECORD_4.setCreativeTab(tab==null?CreativeTabs.MISC:tab), "record_4");
		this.registerItem(SPAWN_EGG.setCreativeTab(tab==null?CreativeTabs.MISC:tab), "spawn_egg");
	}
	
	private void registerItem(Item itemIn, String name)
	{
		GameRegistry.register(itemIn.setUnlocalizedName(name).setRegistryName(name));
	}
	
	static
	{
		DE = new Item();
		Item.ToolMaterial material = EnumHelper.addToolMaterial("UA_DE_TOOLMATERIAL", 1, 247, 5.0F, 1.0F, 22);
		material.setRepairItem(new ItemStack(UAItems.DE));
		DE_SWORD = new ItemSword(material);
		DE_AXE = new UAItemAxe(material, 6.0F, -3.1F);
		DE_PICKAXE = new UAItemPickaxe(material);
		DE_SHOVEL = new ItemSpade(material);
		DE_HOE = new ItemHoe(material);
		SPEAR = new ItemSpear();
		DE_BOMB = new Item()
		{
			@Override
		    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
		    {
		        ItemStack itemstack = playerIn.getHeldItem(handIn);

		        if (!playerIn.capabilities.isCreativeMode)
		        {
		            itemstack.shrink(1);
		        }

		        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.4F));

		        if (!worldIn.isRemote)
		        {
		            EntityDeBomb entitydebomb = new EntityDeBomb(worldIn, playerIn);
		            entitydebomb.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 0.75F, 1.0F);
		            worldIn.spawnEntity(entitydebomb);
		        }

		        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
		    }
		}.setMaxStackSize(16);
		ItemArmor.ArmorMaterial armorMaterial = EnumHelper.addArmorMaterial("UA_DE_ARMORMATERIAL", "de", 13, new int[]{1, 4, 5, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 1.0F);
		armorMaterial.setRepairItem(new ItemStack(DE));
		DE_HELMET = new UAItemArmor(armorMaterial, EntityEquipmentSlot.HEAD);
		DE_CHESTPLATE = new UAItemArmor(armorMaterial, EntityEquipmentSlot.CHEST);
		DE_LEGGINGS = new UAItemArmor(armorMaterial, EntityEquipmentSlot.LEGS);
		DE_BOOTS = new UAItemArmor(armorMaterial, EntityEquipmentSlot.FEET);
		DE_APPLE = new ItemAppleGold(4, 1.2F, false)
				{
			@Override
		    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
		    {
		        if (!worldIn.isRemote)
		        {
		            if (stack.getMetadata() > 0)
		            {
		                player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 400, 1));
		                player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 6000, 1));
		                player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 6000, 0));
		                player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 2400, 1));
		            }
		            else
		            {
		                player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 500, 0));
		                player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 2400, 0));
		                player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 2400, 1));
		            }
		        }
		    }
				}.setAlwaysEdible();
		WHEY = new ItemFood(7, 0.8F, false)
				{
			@SideOnly(Side.CLIENT)
			@Override
			public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
			{
				super.addInformation(stack, playerIn, tooltip, advanced);
				tooltip.add(TextFormatting.GOLD+"Gold standard");
				tooltip.add("100% WHEY");
			}
			
			@Override
		    public EnumAction getItemUseAction(ItemStack stack)
		    {
		        return EnumAction.DRINK;
		    }
				};
		RECORD_1 = addRecord("onfire", ModAssets.RECORD_ONFIRE, "ua");
		RECORD_2 = addRecord("knuckles", ModAssets.RECORD_KNUCKLES, "ua");
		RECORD_3 = addRecord("udontknow", ModAssets.RECORD_UDONTKNOW, "ua");
		RECORD_4 = addRecord("dewae", ModAssets.RECORD_DEWAE, "ua");
		SPAWN_EGG = new ItemUASpawnEgg(4);
	}
	
	private static ItemRecord addRecord(String tracklabel, SoundEvent track, final String modid)
	{
		return new ItemUARecord(tracklabel, track)
				{
		    public ResourceLocation getRecordResource(String name)
		    {
		        return new ResourceLocation(modid,name);
		    }
				};
	}
	
	private static class ItemUARecord extends ItemRecord
    {
		public ItemUARecord(String label, SoundEvent soundIn)
		{
			super(label, soundIn);
		}
    }
	
	private static class UAItemAxe extends ItemAxe
	{
		public UAItemAxe(ToolMaterial material, float damage, float speed)
		{
			super(material, damage, speed);
		}
	}
	
	private static class UAItemPickaxe extends ItemPickaxe
	{
		public UAItemPickaxe(ToolMaterial material)
		{
			super(material);
		}
	}
	
	private static class UAItemArmor extends ItemArmor
	{
		public UAItemArmor(ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn)
		{
			super(materialIn, 0, equipmentSlotIn);
		}
		
		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
		{
			return "ua:textures/models/armor/de" + (this.armorType == EntityEquipmentSlot.LEGS ? "_layer_2.png": "_layer_1.png");
		}
	}
}
