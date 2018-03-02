package me.superdextor.ua.render;

import javax.annotation.Nullable;

import me.superdextor.ua.main.entities.EntityKnuckles;
import me.superdextor.ua.model.ModelKnuckles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderKnuckles extends RenderLiving<EntityKnuckles>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("ua", "textures/entity/knuckles.png");

	public RenderKnuckles(RenderManager renderer)
	{
		super(renderer, new ModelKnuckles(), 0.34F);
        this.addLayer(new RenderKnuckles.LayerSkin(this));
        this.addLayer(new RenderKnuckles.LayerHeldItem(this));
	}

	@Override
    @Nullable
	protected ResourceLocation getEntityTexture(EntityKnuckles entity)
	{
		return TEXTURE;
	}
	
	@Override
    protected void preRenderCallback(EntityKnuckles entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(0.6F, 0.6F, 0.6F);
    }
	
	protected abstract float[] getColor();
	
	static class LayerSkin implements LayerRenderer<EntityKnuckles>
	{
	    private static final ResourceLocation TEXTURE = new ResourceLocation("ua", "textures/entity/knuckles_overlay.png");
	    private final RenderKnuckles knucklesRenderer;

	    public LayerSkin(RenderKnuckles sheepRendererIn)
	    {
	        this.knucklesRenderer = sheepRendererIn;
	    }

	    public void doRenderLayer(EntityKnuckles entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	    {
	        if (!entitylivingbaseIn.isInvisible())
	        {
	            this.knucklesRenderer.bindTexture(TEXTURE);

	            float[] afloat = this.knucklesRenderer.getColor();
	            GlStateManager.color(afloat[0], afloat[1], afloat[2]);
	            this.knucklesRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	        }
	    }

	    public boolean shouldCombineTextures()
	    {
	        return true;
	    }
	}
	
	static class LayerHeldItem implements LayerRenderer<EntityKnuckles>
	{
	    protected final RenderKnuckles renderKnuckles;

	    public LayerHeldItem(RenderKnuckles renderKnuckles)
	    {
	        this.renderKnuckles = renderKnuckles;
	    }

	    public void doRenderLayer(EntityKnuckles entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	    {
	        boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
	        ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
	        ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();

	        if (!itemstack.isEmpty() || !itemstack1.isEmpty())
	        {
	            GlStateManager.pushMatrix();

	            if (this.renderKnuckles.getMainModel().isChild)
	            {
	                float f = 0.5F;
	                GlStateManager.translate(0.0F, 0.75F, 0.0F);
	                GlStateManager.scale(0.5F, 0.5F, 0.5F);
	            }

	            this.renderHeldItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
	            this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
	            GlStateManager.popMatrix();
	        }
	    }

	    private void renderHeldItem(EntityKnuckles entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide)
	    {
	        if (!stack.isEmpty())
	        {
	            GlStateManager.pushMatrix();

	            boolean flag = handSide == EnumHandSide.LEFT;
	            GlStateManager.translate(flag ? -0.24F : 0.64F, entity.isSneaking() ? 0.2F : -0.2F, 0.2F);
	            this.translateToHand(handSide);
	            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
	            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
	            GlStateManager.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
	            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, transformType, flag);
	            GlStateManager.popMatrix();
	        }
	    }

	    protected void translateToHand(EnumHandSide e)
	    {
	        ((ModelKnuckles)this.renderKnuckles.getMainModel()).postRenderArm(0.0625F, e);
	    }

	    public boolean shouldCombineTextures()
	    {
	        return false;
	    }
	}
}