package me.superdextor.ua.main.entities;

import java.util.List;

import me.superdextor.ua.main.config.ConfigUA;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGiantKnuckles extends EntityKnuckles
{
	public EntityGiantKnuckles(World worldIn)
	{
		super(worldIn);
        this.setSize(6.0F, 9.3F);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(19.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.9D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
    }
	
	@Override
	protected float getSoundPitch()
	{
		return super.getSoundPitch() - 0.5F;
	}
	
	@Override
	protected float getSoundVolume()
	{
		return 5.0F;
	}

	@Override
	protected int getAmountDropped()
	{
		return 16+this.rand.nextInt(25);
	}
	
	@Override
	protected boolean shouldFight()
	{
		return true;
	}
	
	@Override
    protected short nextLeap()
    {
    	return Short.MAX_VALUE;
    }
    
	@Override
    protected short nextSpit()
    {
    	return Short.MAX_VALUE;
    }
	
	@Override
    protected void collideWithNearbyEntities()
    {
		if(this.getAttackTarget()==null)
		{
			super.collideWithNearbyEntities();
			return;
		}
		
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expandXyz(1.5D), EntitySelectors.<Entity>getTeamCollisionPredicate(this));

        if (!list.isEmpty())
        {
            int i = this.world.getGameRules().getInt("maxEntityCramming");

            if (i > 0 && list.size() > i - 1 && this.rand.nextInt(4) == 0)
            {
                int j = 0;

                for (int k = 0; k < list.size(); ++k)
                {
                    if (!((Entity)list.get(k)).isRiding())
                    {
                        ++j;
                    }
                }

                if (j > i - 1)
                {
                    this.attackEntityFrom(DamageSource.CRAMMING, 6.0F);
                }
            }

            for (int l = 0; l < list.size(); ++l)
            {
                Entity entity = (Entity)list.get(l);
                this.collideWithEntity(entity);
            }
        }
    }
	
	@Override
	protected void collideWithEntity(Entity entityIn)
	{
		super.collideWithEntity(entityIn);
		if(this.getAttackTarget() == entityIn) this.attackEntityAsMob(entityIn);
		else if(entityIn instanceof EntityLivingBase)
		{
            ((EntityLivingBase)entityIn).knockBack(this, 1.0F, -(double)MathHelper.sin(entityIn.rotationYaw * 0.017453292F), (double)(MathHelper.cos(entityIn.rotationYaw * 0.017453292F)));
		}
	}
	
	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
    	return ConfigUA.tameWarrior?super.processInteract(player, hand):false;
    }
}
