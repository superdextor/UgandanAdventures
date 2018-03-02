package me.superdextor.ua.main.entities;

import java.util.ConcurrentModificationException;
import java.util.List;

import com.google.common.base.Predicate;

import me.superdextor.ua.main.config.ConfigUA;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityCommander extends EntityKnuckles
{
	private int targetTimeStamp;
	private short checkDelay;
	
	public EntityCommander(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAIMate(this, 1.0D,EntityQueen.class));
        this.tasks.addTask(2, new EntityCommander.AIFollowQueen(this, 1.2D));
		super.initEntityAI();
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityCommander.class, 10, false, true, new Predicate<EntityCommander>()
        		{
					@Override
					public boolean apply(EntityCommander input)
					{
						return !input.isChild();
					}
        		})
        		{
        	@Override
        	public boolean shouldExecute()
        	{
        		return !EntityCommander.this.isChild() && super.shouldExecute();
        	}
        		});
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
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
				else if(this.getAttackTarget() == null && owner instanceof EntityQueen && ((EntityQueen) owner).isInLove())
				{
					this.setInLove(null);
				}
			}
			else
			{
				try
				{
					List<EntityQueen> list = this.world.getEntitiesWithinAABB(EntityQueen.class, this.getEntityBoundingBox().expandXyz(40.0D), new Predicate<EntityQueen>()
					{
						@Override
						public boolean apply(EntityQueen input)
						{
							return input.canEntityBeSeen(EntityCommander.this);
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
	protected boolean shouldFight()
	{
		return super.shouldFight() && this.getHealth() > 16.0F;
	}
	
	public void defendWarriorFrom(EntityLivingBase enemy)
	{
		if(this.shouldFight())
		{
			this.setRevengeTarget(enemy);
		}
	}
	
	@Override
	public boolean canAttackClass(Class<? extends EntityLivingBase> cls)
	{
        return cls != EntityGhast.class && cls != EntityCreeper.class;
	}
	
	@Override
	protected int getAmountDropped()
	{
		return 8+this.rand.nextInt(9);
	}
	
	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
    	return ConfigUA.tameCommander?super.processInteract(player, hand):false;
    }
	
	static class AIFollowQueen extends EntityAIBase
	{
	    final EntityCommander knuckles;
	    EntityLivingBase queen;
	    double moveSpeed;
	    private int delayCounter;

	    public AIFollowQueen(EntityCommander knuckles, double speed)
	    {
	        this.knuckles = knuckles;
	        this.moveSpeed = speed;
	    }

	    /**
	     * Returns whether the EntityAIBase should begin execution.
	     */
	    public boolean shouldExecute()
	    {
	    	if(!this.knuckles.isTamed()) return false;
	    	this.queen = this.knuckles.getOwner();
	    	return this.queen != null && this.queen.getDistanceToEntity(this.knuckles) > 12.0D;
	    }

	    /**
	     * Returns whether an in-progress EntityAIBase should continue executing
	     */
	    public boolean continueExecuting()
	    {
	    	if (!this.queen.isEntityAlive())
	        {
	            return false;
	        }
	        else
	        {
	            double d0 = this.knuckles.getDistanceSqToEntity(this.queen);
	            return d0 >= 30.0D;
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
	        this.queen = null;
	    }

	    /**
	     * Updates the task
	     */
	    public void updateTask()
	    {
	        this.knuckles.getLookHelper().setLookPositionWithEntity(this.queen, 10.0F, (float)this.knuckles.getVerticalFaceSpeed());
	        
	        if (--this.delayCounter <= 0)
	        {
	            this.delayCounter = 10;
	            if(!this.knuckles.getNavigator().tryMoveToEntityLiving(this.queen, this.moveSpeed))
	            {
                    if (!this.knuckles.getLeashed())
                    {
                        if (this.knuckles.getDistanceSqToEntity(this.queen) >= 144.0D)
                        {
                            int i = MathHelper.floor(this.queen.posX) - 2;
                            int j = MathHelper.floor(this.queen.posZ) - 2;
                            int k = MathHelper.floor(this.queen.getEntityBoundingBox().minY);

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
