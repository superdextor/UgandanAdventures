package me.superdextor.ua.main.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDeBomb extends EntityThrowable
{
    public EntityDeBomb(World worldIn)
    {
        super(worldIn);
    }

    public EntityDeBomb(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public EntityDeBomb(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }
    
    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeExplosionDamage(this.getThrower()), 4.0F);
        }

        if (!this.world.isRemote)
        {
        	this.world.newExplosion(this.getThrower(), this.posX, this.posY, this.posZ, 2.0F, false, true);
            this.setDead();
        }
    }
}
