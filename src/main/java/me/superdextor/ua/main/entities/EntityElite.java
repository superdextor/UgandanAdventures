package me.superdextor.ua.main.entities;

import me.superdextor.ua.main.config.ConfigUA;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityElite extends EntityWarrior
{
	public EntityElite(World worldIn)
	{
		super(worldIn);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }
	
	@Override
	protected boolean shouldFight()
	{
		return true;
	}
	
	@Override
	protected int getAmountDropped()
	{
		return 1+this.rand.nextInt(3);
	}
	
	@Override
    protected short nextLeap()
    {
    	return (short) (20+this.rand.nextInt(50));
    }
    
	@Override
    protected short nextSpit()
    {
    	return (short) (100 + this.rand.nextInt(40));
    }
	
	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
    	return ConfigUA.tameElite?super.processInteract(player, hand):false;
    }
}
