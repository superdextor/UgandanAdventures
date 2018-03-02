package me.superdextor.ua.proxy;

import me.superdextor.ua.main.blocks.UABlocks;
import me.superdextor.ua.main.entities.EntityCommander;
import me.superdextor.ua.main.entities.EntityDeBomb;
import me.superdextor.ua.main.entities.EntityElite;
import me.superdextor.ua.main.entities.EntityGiantKnuckles;
import me.superdextor.ua.main.entities.EntityKnuckles;
import me.superdextor.ua.main.entities.EntityKnucklesSpit;
import me.superdextor.ua.main.entities.EntityQueen;
import me.superdextor.ua.main.entities.EntitySpear;
import me.superdextor.ua.main.entities.EntityWarrior;
import me.superdextor.ua.main.items.UAItems;
import me.superdextor.ua.render.RenderKnuckles;
import me.superdextor.ua.render.RenderKnucklesSpit;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityWarrior.class, new RenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderKnuckles(manager)
				{
					@Override
					protected float[] getColor()
					{
						return new float[] {0.91F, 0.1F, 0.13F};
					}
				};
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityElite.class, new RenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderKnuckles(manager)
				{
					@Override
					protected float[] getColor()
					{
						return new float[] {0.06F, 0.11F, 0.48F};
					}
				};
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityCommander.class, new RenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderKnuckles(manager)
				{
					@Override
					protected float[] getColor()
					{
						return new float[] {1.0F, 0.8F, 0.0F};
					}
				};
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityQueen.class, new RenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				RenderBiped render = new RenderBiped<EntityQueen>(manager, new ModelPlayer(0.0F,true), 0.4F)
				{
				    private final ResourceLocation texture = new ResourceLocation("ua","textures/entity/queen.png");
					@Override
				    protected ResourceLocation getEntityTexture(EntityQueen entity)
				    {
				        return this.texture;
				    }
				};
		        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(render)
		        {
		            protected void initArmor()
		            {
		                this.modelLeggings = new ModelPlayer(0.5F, true);
		                this.modelArmor = new ModelPlayer(1.0F, true);
		            }
		        };
		        render.addLayer(layerbipedarmor);
				return render;
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantKnuckles.class, new RenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderKnuckles(manager)
				{
					@Override
					protected float[] getColor()
					{
						return new float[] {0.91F, 0.1F, 0.13F};
					}
					
					@Override
				    protected void preRenderCallback(EntityKnuckles entitylivingbaseIn, float partialTickTime)
				    {
				        GlStateManager.scale(6.0F, 6.0F, 6.0F);
				    }
				};
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityKnucklesSpit.class, new RenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderKnucklesSpit(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDeBomb.class, new RenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderSnowball(manager,UAItems.DE_BOMB,Minecraft.getMinecraft().getRenderItem());
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntitySpear.class, new RenderFactory()
		{
			@Override
			public Render createRenderFor(RenderManager manager)
			{
				return new RenderArrow<EntitySpear>(manager)
				{
				    private final ResourceLocation texture = new ResourceLocation("ua","textures/entity/spear.png");
					
				    @Override
				    protected ResourceLocation getEntityTexture(EntitySpear entity)
				    {
				        return this.texture;
				    }
				};
			}
		});
	}
	
	@Override
	public void init()
	{
		this.renderBlock(UABlocks.DE_ORE, 0);
		this.renderBlock(UABlocks.DE_BLOCK, 0);
		this.renderBlock(UABlocks.WAY, 0);
		this.renderItem(UAItems.DE, 0);
		this.renderItem(UAItems.DE_SWORD, 0);
		this.renderItem(UAItems.DE_AXE, 0);
		this.renderItem(UAItems.DE_PICKAXE, 0);
		this.renderItem(UAItems.DE_SHOVEL, 0);
		this.renderItem(UAItems.DE_HOE, 0);
		this.renderItem(UAItems.SPEAR, 0);
		this.renderItem(UAItems.DE_BOMB, 0);
		this.renderItem(UAItems.DE_HELMET, 0);
		this.renderItem(UAItems.DE_CHESTPLATE, 0);
		this.renderItem(UAItems.DE_LEGGINGS, 0);
		this.renderItem(UAItems.DE_BOOTS, 0);
		this.renderItem(UAItems.DE_APPLE, 0);
		this.renderItem(UAItems.DE_APPLE, 1);
		this.renderItem(UAItems.WHEY, 0);
		this.renderItem(UAItems.RECORD_1, 0);
		this.renderItem(UAItems.RECORD_2, 0);
		this.renderItem(UAItems.RECORD_3, 0);
		this.renderItem(UAItems.RECORD_4, 0);
		for(int i = UAItems.SPAWN_EGG.getEggSize(); i >= 0 ; i--)
		{
			this.renderItem(UAItems.SPAWN_EGG, i);
		}
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor()
        {
            public int getColorFromItemstack(ItemStack stack, int tintIndex)
            {
                return (tintIndex == 0 ? UAItems.SPAWN_EGG.getSolidColor(stack) : UAItems.SPAWN_EGG.getSpotColor(stack));
            }
        }, new Item[] {UAItems.SPAWN_EGG});
	}
	
	@Override
	public void postInit()
	{
		
	}
	
	private abstract class RenderFactory implements IRenderFactory
	{
		public abstract Render createRenderFor(RenderManager manager);
	}
	
	private void renderBlock(Block blockIn, int meta)
	{
		this.renderItem(Item.getItemFromBlock(blockIn), meta);
	}
	
	private void renderItem(Item itemIn, int meta)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemIn, meta, new ModelResourceLocation("ua:"+itemIn.getUnlocalizedName().substring(5), "inventory"));
	}
}
