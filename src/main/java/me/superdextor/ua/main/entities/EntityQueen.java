package me.superdextor.ua.main.entities;

import java.util.ConcurrentModificationException;
import java.util.List;

import com.google.common.base.Predicate;

import me.superdextor.ua.main.config.ConfigUA;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityQueen extends EntityKnuckles
{
	public EntityQueen(World worldIn)
	{
		super(worldIn);
        this.setSize(0.6F, 1.95F);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if(!this.isChild() && this.ticksExisted % 2400 == 2000 && this.getAttackTarget() == null)
		{
			try
			{
				List<EntityKnuckles> list = this.world.getEntities(EntityKnuckles.class, new Predicate<EntityKnuckles>()
				{
					@Override
					public boolean apply(EntityKnuckles input)
					{
						return input.getClass() != EntityQueen.class;
					}
				});
				if(!list.isEmpty()) this.setInLove(null);
			}
			catch(ConcurrentModificationException e)
			{
				
			}
		}
	}

	@Override
	protected float getSoundVolume()
	{
		return 0.0F;
	}

	@Override
	protected boolean shouldFight()
	{
		return false;
	}
	
	@Override
	protected int getAmountDropped()
	{
		return 16+this.rand.nextInt(9);
	}
	
	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
    	return ConfigUA.tameQueen?super.processInteract(player, hand):false;
    }
}
