package me.superdextor.ua.main.entities;

import java.util.ConcurrentModificationException;
import java.util.List;

import com.google.common.base.Predicate;

import me.superdextor.ua.main.config.ConfigUA;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityWarrior extends EntityKnuckles
{
	private int targetTimeStamp;
	private short checkDelay = 60;
	
	public EntityWarrior(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
    protected void initEntityAI()
    {
		super.initEntityAI();
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D,EntityQueen.class));
        this.tasks.addTask(5, new EntityWarrior.AIFollowCommander(this, 1.1D));
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if(--this.checkDelay <= 0)
		{
			this.checkDelay = (short) (140 + this.rand.nextInt(16) * 5);
			if(this.isTamed())
			{
				EntityLivingBase owner = this.getOwner();
				if(owner == null || !owner.isEntityAlive() && !(owner instanceof EntityPlayer))
				{
					this.setTamed(false);
					this.setOwnerId(null);
				}
				else if(this.getAttackTarget() == null && this.rand.nextBoolean() && owner instanceof EntityCommander)
				{
					owner = ((EntityCommander)owner).getOwner();
					if(owner instanceof EntityQueen && ((EntityQueen) owner).isInLove())
					{
						this.setInLove(null);
					}
				}
			}
			else
			{
				try
				{
					List<EntityCommander> list = this.world.getEntitiesWithinAABB(EntityCommander.class, this.getEntityBoundingBox().expandXyz(40.0D), new Predicate<EntityCommander>()
					{
						@Override
						public boolean apply(EntityCommander input)
						{
							return input.canEntityBeSeen(EntityWarrior.this);
						}
					});
					if(!list.isEmpty())
					{
						this.setTamed(true);
						this.setOwnerId(list.get(0).getUniqueID());
						this.playTameEffect(true);
					}
				}
				catch(ConcurrentModificationException e) {}
			}
		}
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn)
	{
		super.setAttackTarget(entitylivingbaseIn);
		if(this.isTamed())
		{
			EntityLivingBase owner = this.getOwner();
			if(owner != null && owner instanceof EntityCommander)
			{
				((EntityCommander) owner).defendWarriorFrom(entitylivingbaseIn);
			}
		}
	}
	
	@Override
	protected boolean shouldFight()
	{
		return super.shouldFight() && this.getHealth() > 5.0F;
	}
	
	@Override
	protected int getAmountDropped()
	{
		return this.rand.nextInt(2);
	}
	
	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
    	return ConfigUA.tameWarrior?super.processInteract(player, hand):false;
    }
	
	static class AIFollowCommander extends EntityAIBase
	{
	    final EntityWarrior knuckles;
	    EntityLivingBase commander;
	    double moveSpeed;
	    private int delayCounter;

	    public AIFollowCommander(EntityWarrior knuckles, double speed)
	    {
	        this.knuckles = knuckles;
	        this.moveSpeed = speed;
	    }

	    /**
	     * Returns whether the EntityAIBase should begin execution.
	     */
	    public boolean shouldExecute()
	    {
	    	if(this.knuckles.getAttackTarget() != null || !this.knuckles.isTamed()) return false;
	    	this.commander = this.knuckles.getOwner();
	    	return this.commander != null && this.commander.getDistanceSqToEntity(this.knuckles) > 70.0D;
	    }

	    /**
	     * Returns whether an in-progress EntityAIBase should continue executing
	     */
	    public boolean continueExecuting()
	    {
	    	if (!this.commander.isEntityAlive())
	        {
	            return false;
	        }
	        else
	        {
	            double d0 = this.knuckles.getDistanceSqToEntity(this.commander);
	            return d0 >= 40.0D;
	        }
	    }

	    /**
	     * Execute a one shot task or start executing a continuous task
	     */
	    public void startExecuting()
	    {
	        this.delayCounter = 0;
	    }

	    /**
	     * Resets the task
	     */
	    public void resetTask()
	    {
	        this.commander = null;
	    }

	    /**
	     * Updates the task
	     */
	    public void updateTask()
	    {
	        this.knuckles.getLookHelper().setLookPositionWithEntity(this.commander, 10.0F, (float)this.knuckles.getVerticalFaceSpeed());
	        
	        if (--this.delayCounter <= 0)
	        {
	            this.delayCounter = 10;
	            if(!this.knuckles.getNavigator().tryMoveToEntityLiving(this.commander, this.moveSpeed))
	            {
                    if (!this.knuckles.getLeashed())
                    {
                        if (this.knuckles.getDistanceSqToEntity(this.commander) >= 144.0D)
                        {
                            int i = MathHelper.floor(this.commander.posX) - 2;
                            int j = MathHelper.floor(this.commander.posZ) - 2;
                            int k = MathHelper.floor(this.commander.getEntityBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l)
                            {
                                for (int i1 = 0; i1 <= 4; ++i1)
                                {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.knuckles.world.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isFullyOpaque() && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1)))
                                    {
                                        this.knuckles.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.knuckles.rotationYaw, this.knuckles.rotationPitch);
                                        this.knuckles.getNavigator().clearPathEntity();
                                        return;
                                    }
                                }
                            }
                        }
                    }
	            }
	        }
	    }
	    
	    private boolean isEmptyBlock(BlockPos pos)
	    {
	        IBlockState iblockstate = this.knuckles.world.getBlockState(pos);
	        return iblockstate.getMaterial() == Material.AIR ? true : !iblockstate.isFullCube();
	    }
	}
}
