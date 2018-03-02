package me.superdextor.ua.main.items;

import java.util.UUID;

import me.superdextor.ua.main.worldgen.WorldGenStatue;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUASpawnEgg extends ItemMonsterPlacer
{
	private final String[] displayName;
	private final ResourceLocation[] entityIds;
	private final int solidColor[];
	private final int spotColor[];
	private final NBTTagCompound entityData[];
	
	public ItemUASpawnEgg(int capacity)
	{
		this.displayName = new String[capacity];
		this.entityIds = new ResourceLocation[capacity];
		this.solidColor = new int[capacity];
		this.spotColor = new int[capacity];
		this.entityData = new NBTTagCompound[capacity];
		if(capacity > 0)
		{
			this.setHasSubtypes(true);
		}
	}
	
	public final void insertEggInfo(final EggEntry... entries)
	{
		if(entries.length!=this.getEggSize())
		{
			throw new ArrayIndexOutOfBoundsException("Cannot setup Spawn egg! Capacity '" + this.getEggSize() + "' does not match entries '" + entries.length + "'. Please report this!");
		}
		int i = 0;
		for(EggEntry entry : entries)
		{
			this.displayName[i] = entry.displayName;
			this.entityIds[i] = entry.entityId;
			this.solidColor[i] = entry.solidColor;
			this.spotColor[i] = entry.spotColor;
			this.entityData[i] = entry.entityData;
			i++;
		}
	}
	
    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
    	int i = stack.getMetadata();
    	
    	if(i >= this.getEggSize())
    	{
    		return I18n.translateToLocal("item.monsterPlacer.name");
    	}
    	
        return I18n.translateToLocal("item.monsterPlacer.name") + " " + I18n.translateToLocal("entity." + this.displayName[stack.getMetadata()] + ".name");
    }
    
    public Entity spawnCreature(World worldIn, double x, double y, double z, int meta)
    {
        if (meta < getEggSize() && EntityList.isRegistered(this.entityIds[meta]))
        {
            Entity entity = null;

            for (int i = 0; i < 1; ++i)
            {
                entity = EntityList.createEntityByIDFromName(this.entityIds[meta], worldIn);

                if (entity instanceof EntityLivingBase)
                {
                    EntityLiving entityliving = (EntityLiving)entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
                    worldIn.spawnEntity(entity);
                    entityliving.playLivingSound();
                }
                
                if(this.entityData[meta] != null)
                {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    entity.writeToNBT(nbttagcompound1);
                    UUID uuid = entity.getUniqueID();
                    nbttagcompound1.merge(entityData[meta]);
                    entity.setUniqueId(uuid);
                    entity.readFromNBT(nbttagcompound1);
                	
                }
            }

            return entity;
        }
        
        return null;
    }
    
    public ResourceLocation getEntityIdFromItem(int meta)
    {
    	if(meta >= getEggSize())
    	{
    		return null;
    	}
    	
    	if(this.entityIds[meta] == null)
    	{
    		return null;
    	}
    	
    	return this.entityIds[meta];
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
    	for(int i = 0; i < entityIds.length; i++)
    	{
            subItems.add(new ItemStack(itemIn, 1, i));
    	}
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	ItemStack stack = playerIn.getHeldItem(hand);
        if(worldIn.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else if(!playerIn.canPlayerEdit(pos.offset(facing), facing, stack) || stack.getMetadata() >= this.getEggSize())
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);

            if (iblockstate.getBlock() == Blocks.MOB_SPAWNER)
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntityMobSpawner)
                {
                    MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
                    
                    if(this.entityData[stack.getMetadata()] != null) {
                        NBTTagCompound data = new NBTTagCompound();
                        data.setTag("SpawnData", this.entityData[stack.getMetadata()]);
                        mobspawnerbaselogic.readFromNBT(data);
                    }
                    
                    mobspawnerbaselogic.setEntityId(getEntityIdFromItem(stack.getMetadata()));
                    
                    tileentity.markDirty();
                    worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        stack.shrink(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            pos = pos.offset(facing);
            double d0 = 0.0D;

            if (facing == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence) //Forge: Fix Vanilla bug comparing state instead of block
            {
                d0 = 0.5D;
            }

            Entity entity = spawnCreature(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D, stack.getMetadata());

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                {
                    entity.setCustomNameTag(stack.getDisplayName());
                }

                applyItemEntityDataToEntity(worldIn, playerIn, stack, entity);

                if (!playerIn.capabilities.isCreativeMode)
                {
                    stack.shrink(1);
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
    	ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (worldIn.isRemote)
        {
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        }
        else
        {
            RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

            if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if (!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid))
                {
                    return new ActionResult(EnumActionResult.PASS, itemStackIn);
                }
                else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemStackIn))
                {
                    Entity entity = spawnCreature(worldIn, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, itemStackIn.getMetadata());

                    if (entity == null)
                    {
                        return new ActionResult(EnumActionResult.PASS, itemStackIn);
                    }
                    else
                    {
                        if (entity instanceof EntityLivingBase && itemStackIn.hasDisplayName())
                        {
                            entity.setCustomNameTag(itemStackIn.getDisplayName());
                        }

                        applyItemEntityDataToEntity(worldIn, playerIn, itemStackIn, entity);

                        if (!playerIn.capabilities.isCreativeMode)
                        {
                            itemStackIn.shrink(1);
                        }

                        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
                    }
                }
                else
                {
                    return new ActionResult(EnumActionResult.FAIL, itemStackIn);
                }
            }
            else
            {
                return new ActionResult(EnumActionResult.PASS, itemStackIn);
            }
        }
    }
    
    public int getSpotColor(ItemStack stack)
    {
    	int i = stack.getMetadata();
    	if(i >= this.getEggSize())
    	{
    		return -1;
    	}
    	
		return this.spotColor[i];
	}
    
    public int getSolidColor(ItemStack stack)
    {
    	int i = stack.getMetadata();
    	if(i >= this.getEggSize())
    	{
    		return -1;
    	}
    	
		return this.solidColor[i];
	}
    
    public int getEggSize()
    {
    	return this.entityIds.length;
    }
    
    public static class EggEntry
    {
    	private final String displayName;
    	private final ResourceLocation entityId;
    	private final int solidColor;
    	private final int spotColor;
    	private final NBTTagCompound entityData;
    	
    	public EggEntry(String displayName, Class<? extends Entity> c, int solidColor, int spotColor, NBTTagCompound data)
    	{
    		this.displayName = displayName;
    		this.entityId = EntityList.getKey(c);
    		this.solidColor = solidColor;
    		this.spotColor = spotColor;
    		this.entityData = data == null ? null : (NBTTagCompound) data.copy();
		}
    	
    	public EggEntry(String displayName, Class<? extends Entity> c, int solidColor, int spotColor)
    	{
    		this(displayName,c,solidColor,spotColor,null);
		}
    	
    	public EggEntry(Class<? extends Entity> c, int solidColor, int spotColor)
    	{
    		this(EntityList.getTranslationName(EntityList.getKey(c)),c,solidColor,spotColor,null);
		}
    }
}
