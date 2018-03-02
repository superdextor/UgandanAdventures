package me.superdextor.ua.main.entities;

import me.superdextor.ua.main.items.UAItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySpear extends EntityArrow
{
	public ItemStack spear = ItemStack.EMPTY;
	
    public EntitySpear(World worldIn)
    {
        super(worldIn);
    }

    public EntitySpear(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
    }

    public EntitySpear(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @Override
    protected ItemStack getArrowStack()
    {
        return this.spear;
    }
    
    @Override
    protected void arrowHit(EntityLivingBase living)
    {
    	if(!this.world.isRemote)
    	{
    		this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            this.world.setEntityState(this, (byte)3);
    	}
    }
    
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) 
    {
    	super.onHit(raytraceResultIn);
    	if(!this.world.isRemote&&this.inGround&&this.isEntityAlive()&&this.spear.isEmpty()&&this.pickupStatus!=PickupStatus.CREATIVE_ONLY)
    	{
    		this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            this.world.setEntityState(this, (byte)3);
            this.setDead();
    	}
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            for (int i = 0; i < 5; ++i)
            {
                Vec3d vec3d = new Vec3d(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
                vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
                double d0 = (double)(-this.rand.nextFloat()) * 0.6D - 0.3D;
                Vec3d vec3d1 = new Vec3d(((double)this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
                vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * 0.017453292F);
                vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * 0.017453292F);
                vec3d1 = vec3d1.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
                if (this.world instanceof WorldServer) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
                    ((WorldServer)this.world).spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, 0,  vec3d.xCoord, vec3d.yCoord + 0.05D, vec3d.zCoord, 0.0D, Item.getIdFromItem(UAItems.SPEAR), 0);
                else //Fix the fact that spawning ItemCrack uses TWO arguments.
                    this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.05D, vec3d.zCoord, Item.getIdFromItem(UAItems.SPEAR), 0);
            }
        }
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if(compound.hasKey("ItemStack"))
        {
        	this.spear = new ItemStack(compound.getCompoundTag("ItemStack"));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        if(!this.spear.isEmpty())
        {
            compound.setTag("ItemStack", this.spear.writeToNBT(new NBTTagCompound()));
        }
    }
}