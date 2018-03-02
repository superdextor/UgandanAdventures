package me.superdextor.ua.main.entities;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import me.superdextor.ua.main.ModAssets;
import me.superdextor.ua.main.blocks.UABlocks;
import me.superdextor.ua.main.items.UAItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public abstract class EntityKnuckles extends EntityTameable implements IRangedAttackMob
{
	private short nextway;
	private short clackTimer = 0;
	private byte nextClack = 20;
    protected MoodState mood = MoodState.ALONE;
    
	public EntityKnuckles(World worldIn)
	{
		super(worldIn);
        this.setSize(0.6F, 0.93F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.nextway = (short) (5000+this.rand.nextInt(40)*20);
	}
	
	@Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityKnuckles.AIKnucklesAttack(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(9, new EntityKnuckles.AIWatchEntity(this));
        this.tasks.addTask(10, new EntityKnuckles.AIWatchEntity(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, false, true, new Predicate<EntityLiving>()
        {
            public boolean apply(@Nullable EntityLiving p_apply_1_)
            {
                return p_apply_1_ != null && IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper);
            }
        })
        		{
        	@Override
        	public boolean shouldExecute()
        	{
        		return EntityKnuckles.this.shouldFight() && super.shouldExecute();
        	}
        		});
    }
	
	@Override
    protected void entityInit()
    {
        super.entityInit();
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if(this.ticksExisted % 300 == 0)
		{
			this.heal(1.0F);
		}
		if(--this.nextway < 1)
		{
			this.nextway = (short) (2000 + this.rand.nextInt(20)*20);
			if(this.world.getGameRules().getBoolean("mobGriefing") && this.world.getBlockState(this.getPosition()).getBlock().isReplaceable(this.world, this.getPosition()) && !this.world.getBlockState(this.getPosition()).getMaterial().isLiquid() && this.world.getBlockState(this.getPosition().down()).isSideSolid(this.world, this.getPosition().down(), EnumFacing.UP))
			{
				this.world.setBlockState(this.getPosition(), UABlocks.WAY.getStateForPlacement(this.world, this.getPosition(), this.getHorizontalFacing(), 0, 0, 0, 0, this));
			}
		}
		if(this.clackTimer > 0 && this.clackTimer-- < 160 && this.clackTimer % this.nextClack == 0)
		{
			this.nextClack = (byte) ((byte) 5+this.rand.nextInt(11));
			this.playSound(ModAssets.ENTITY_KNUCKLES_CLACK, 1.5F, this.getSoundPitch());
		}
	}
	
	@Override
	protected float getSoundVolume()
	{
		return this.clackTimer>0?0.0F:super.getSoundVolume();
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }
	
	@Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;
        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD)
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
        EntityKnucklesSpit entityllamaspit = new EntityKnucklesSpit(this.world, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityllamaspit.posY;
        double d2 = target.posZ - this.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
        entityllamaspit.setThrowableHeading(d0, d1 + (double)f, d2, 1.5F, 10.0F);
        this.world.spawnEntity(entityllamaspit);
	}
	
	@Override
    protected boolean canDespawn()
    {
        return false;
    }
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		switch(this.mood) 
		{
			default: return null;
			case TALKINGTO: return ModAssets.ENTITY_KNUCKLES_TALKINGTO;
			case TRIBE: return ModAssets.ENTITY_KNUCKLES_TRIBE;
			case FIGHTING: return ModAssets.ENTITY_KNUCKLES_FIGHTING;
		}
	}
	
	@Override
	protected SoundEvent getHurtSound()
	{
		return ModAssets.ENTITY_KNUCKLES_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return ModAssets.ENTITY_KNUCKLES_DEATH;
	}
	
	@Override
    public boolean getCanSpawnHere()
    {
        return this.world.getLight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) > 8 && this.world.getBlockState((new BlockPos(this)).down()).canEntitySpawn(this);
    }
	
	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn)
	{
		super.setAttackTarget(entitylivingbaseIn);
		MoodState newstate = MoodState.getRecommendedState(this);
		if(this.mood == MoodState.FIGHTING && newstate == MoodState.TRIBE)
		{
			this.clackTimer = (short) (180+(10*this.rand.nextInt(5)));
		}
		else
		{
			this.clackTimer = 0;
		}
		this.mood = newstate;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		switch(this.rand.nextInt(30))
		{
			default:
			{
				Class cls = this.getClass();
				if(cls == EntityQueen.class) cls = ageable.getClass();
				return (EntityAgeable) EntityList.createEntityByIDFromName(EntityList.getKey(cls), this.world);
			}
			case 10: return new EntityWarrior(this.world);
			case 11: return new EntityWarrior(this.world);
			case 12: return new EntityWarrior(this.world);
			case 13: return new EntityWarrior(this.world);
			case 14: return new EntityWarrior(this.world);
			case 15: return new EntityWarrior(this.world);
			case 16: return new EntityWarrior(this.world);
			case 17: return new EntityElite(this.world);
			case 18: return new EntityElite(this.world);
			case 19: return new EntityElite(this.world);
			case 20: return new EntityCommander(this.world);
			case 21: return new EntityQueen(this.world);
		}
	}
	
	@Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() == UAItems.WHEY;
    }
	
	@Override
	public boolean canAttackClass(Class<? extends EntityLivingBase> cls)
	{
        return cls != EntityGhast.class && cls != EntityCreeper.class && cls.getSuperclass() != EntityKnuckles.class;
	}
	
	@Override
	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner)
	{
		return !(target instanceof EntityKnuckles && ((EntityKnuckles) target).getOwner()==owner);
	}
	
	@Override
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        return otherAnimal instanceof EntityKnuckles && this.isInLove() && otherAnimal.isInLove();
    }
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		IEntityLivingData d = super.onInitialSpawn(difficulty, livingdata);
		if(this.rand.nextInt(10)==0)
		{
			this.setGrowingAge(-24000);
		}
		return d;
	}
	
    @Nullable
    @Override
    public EntityLivingBase getOwner()
    {
        try
        {
            UUID uuid = this.getOwnerId();
            for (int i = 0; i < this.world.loadedEntityList.size(); ++i)
            {
                if (uuid.equals(this.world.loadedEntityList.get(i).getUniqueID()))
                {
                    return (EntityLivingBase)this.world.loadedEntityList.get(i);
                }
            }
            return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
        }
        catch (Exception var2)
        {
            return null;
        }
    }
    
    protected boolean shouldFight()
    {
    	return !this.isChild();
    }
    
    @Override
    protected final Item getDropItem()
    {
    	return UAItems.WHEY;
    }
    
    protected abstract int getAmountDropped();
    
    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        for(int j = this.getAmountDropped() + (lootingModifier > 0 ? this.rand.nextInt(lootingModifier+1) : 0); j > 0; --j)
        {
            this.dropItem(this.getDropItem(), 1);
        }
    }
    
    protected short nextLeap()
    {
    	return (short) (40+this.rand.nextInt(70));
    }
    
    protected short nextSpit()
    {
    	return (short) (130 + this.rand.nextInt(60));
    }
    
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if(this.isOwner(player))
        {
            if (this.isBreedingItem(itemstack))
            {
                if (itemstack.getItem() instanceof ItemFood && this.getHealth() < 20.0F)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        itemstack.shrink(1);
                    }
                    this.heal((float)((ItemFood) itemstack.getItem()).getHealAmount(itemstack));
                    return true;
                }
            }
            else if(itemstack.isEmpty() && !this.world.isRemote)
            {
                this.isJumping = false;
                this.navigator.clearPathEntity();
                this.setAttackTarget((EntityLivingBase)null);
            }
        }
        else if(this.isBreedingItem(itemstack))
        {
        	if(this.getAttackTarget() == null)
        	{
                if (!player.capabilities.isCreativeMode)
                {
                    itemstack.shrink(1);
                }

                if (!this.world.isRemote)
                {
                    if (this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player))
                    {
                        this.setTamed(true);
                        this.navigator.clearPathEntity();
                        this.setAttackTarget((EntityLivingBase)null);
                        this.setOwnerId(player.getUniqueID());
                        this.playTameEffect(true);
                        this.world.setEntityState(this, (byte)7);
                    }
                    else
                    {
                        this.playTameEffect(false);
                        this.world.setEntityState(this, (byte)6);
                    }
                }

                return true;
        	}
        }
        else
        {
        	return super.processInteract(player, hand);
        }

        return false;
    }
    
    static enum MoodState
    {
    	ALONE(0),
    	TALKINGTO(1),
    	TRIBE(0),
    	FIGHTING(2);
    	
    	final int priority;
    	
    	private MoodState(int priority)
    	{
    		this.priority = priority;
		}
    	
    	public MoodState compare(MoodState state2)
    	{
    		return state2.priority > this.priority ? state2 : this;
    	}
    	
    	public static MoodState getRecommendedState(EntityKnuckles entityKnuckles)
    	{
    		return entityKnuckles.getAttackTarget() != null ? FIGHTING : entityKnuckles.isTamed() ? TRIBE : ALONE;
    	}
    }
    
    static class AIKnucklesAttack extends EntityAIAttackMelee
    {
    	final EntityKnuckles knuckles;
    	short nextLeap;
    	short nextSpit;
    	
        public AIKnucklesAttack(EntityKnuckles entity)
        {
            super(entity, 1.2D, true);
            this.knuckles = entity;
            this.nextLeap = entity.nextLeap();
            this.nextSpit = entity.nextSpit();
        }
        
        public void updateTask()
        {
        	super.updateTask();
        	if(--this.nextLeap < 1)
        	{
        		this.leap();
        		this.nextLeap = this.knuckles.nextLeap();
        	}
        	
        	if(--this.nextSpit < 1)
        	{
        		this.knuckles.attackEntityWithRangedAttack(this.knuckles.getAttackTarget(), 0.0F);
        		this.nextSpit = this.knuckles.nextSpit();
        	}
        	else if(this.nextSpit == 70)
        	{
        		if(this.canSpit())
        		{
        			float p = this.knuckles.getSoundPitch();
        			this.nextSpit = (short) (32 / p);
        			this.knuckles.playSound(ModAssets.ENTITY_KNUCKLES_SPIT, 1.0F, p);
        		}
        		else
        		{
        			this.nextSpit = 150;
        		}
        	}
        }
        
        boolean canSpit()
        {
        	EntityLivingBase target = this.knuckles.getAttackTarget();
        	if(target instanceof EntityEnderman) return false;
        	return this.knuckles.getEntitySenses().canSee(target) && this.knuckles.getDistanceSqToEntity(target) < 16.0D;
        }
        
        void leap()
        {
            double d0 = this.knuckles.getDistanceSqToEntity(this.knuckles.getAttackTarget());
            if(d0 < 4.0D || !this.knuckles.onGround) return;
            
            d0 = this.knuckles.getAttackTarget().posX - this.knuckles.posX;
            double d1 = this.knuckles.getAttackTarget().posZ - this.knuckles.posZ;
            float f = MathHelper.sqrt(d0 * d0 + d1 * d1);

            if ((double)f >= 1.0E-4D)
            {
                this.knuckles.motionX += d0 / (double)f * 0.5D * 0.800000011920929D + this.knuckles.motionX * 0.20000000298023224D;
                this.knuckles.motionZ += d1 / (double)f * 0.5D * 0.800000011920929D + this.knuckles.motionZ * 0.20000000298023224D;
            }

            this.knuckles.motionY = 0.5F;
        }
    }
    
    static class AIWatchEntity extends EntityAIWatchClosest
    {
    	private final EntityKnuckles entityKnuckles;
    	
		public AIWatchEntity(EntityKnuckles entityKnuckles)
		{
			super(entityKnuckles, EntityPlayer.class, 3.0F, 1.0F);
			this.setMutexBits(3);
			this.entityKnuckles = entityKnuckles;
		}
		
		public AIWatchEntity(EntityKnuckles entityKnuckles, Class<? extends Entity> watchTargetClass, float maxDistance)
		{
			super(entityKnuckles, watchTargetClass, maxDistance);
			this.entityKnuckles = entityKnuckles;
		}
    	
        @Override
	    public void startExecuting()
	    {
	    	super.startExecuting();
	    	this.entityKnuckles.mood = this.entityKnuckles.mood.compare(MoodState.TALKINGTO);
	    }
	    
        @Override
	    public void resetTask()
	    {
	    	super.resetTask();
	    	this.entityKnuckles.mood = MoodState.getRecommendedState(this.entityKnuckles);
	    }
    }
}
