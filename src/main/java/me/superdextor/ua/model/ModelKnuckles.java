package me.superdextor.ua.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelKnuckles extends ModelBase
{
    public ModelRenderer left_shoe;
    public ModelRenderer right_shoe;
    public ModelRenderer left_leg;
    public ModelRenderer right_leg;
    public ModelRenderer body;
    public ModelRenderer shoulder_left;
    public ModelRenderer shoulder_right;
    public ModelRenderer arm_left;
    public ModelRenderer arm_right;
    public ModelRenderer head;
    public ModelRenderer nose;
    public ModelRenderer mouth;
    public ModelRenderer jaw;
    public ModelRenderer left_eye;
    public ModelRenderer right_eye;
    public ModelRenderer tail;
    
    public ModelKnuckles.ArmPose leftArmPose;
    public ModelKnuckles.ArmPose rightArmPose;
    public boolean isSneak = false;

    public ModelKnuckles()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.leftArmPose = ModelKnuckles.ArmPose.EMPTY;
        this.rightArmPose = ModelKnuckles.ArmPose.EMPTY;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(-5.0F, 0.0F, -3.0F);
        this.head.addBox(0.0F, 0.0F, 0.0F, 10, 8, 8, 0.0F);
        this.mouth = new ModelRenderer(this, 28, 0);
        this.mouth.setRotationPoint(1.0F, 4.0F, -1.0F);
        this.mouth.addBox(0.0F, 0.0F, 0.0F, 8, 2, 1, 0.0F);
        this.nose = new ModelRenderer(this, 46, 0);
        this.nose.setRotationPoint(3.5F, 4.0F, -3.0F);
        this.nose.addBox(0.0F, 0.0F, 0.0F, 3, 2, 2, 0.0F);
        this.jaw = new ModelRenderer(this, 28, 4);
        this.jaw.setRotationPoint(2.0F, 6.0F, -1.0F);
        this.jaw.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1, 0.0F);
        this.left_eye = new ModelRenderer(this, 0, 0);
        this.left_eye.setRotationPoint(2.0F, 2.0F, -0.75F);
        this.left_eye.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
        this.right_eye = new ModelRenderer(this, 0, 3);
        this.right_eye.setRotationPoint(6.0F, 2.0F, -0.75F);
        this.right_eye.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
        this.body = new ModelRenderer(this, 0, 16);
        this.body.setRotationPoint(-7.0F, 5.0F, -2.0F);
        this.body.addBox(0.0F, 0.0F, 0.0F, 14, 13, 6, 0.0F);
        this.shoulder_left = new ModelRenderer(this, 40, 18);
        this.shoulder_left.setRotationPoint(-2.0F, 0.0F, 0.0F);
        this.shoulder_left.addBox(0.0F, 0.0F, 0.0F, 2, 10, 6, 0.0F);
        this.shoulder_right = new ModelRenderer(this, 40, 34);
        this.shoulder_right.setRotationPoint(14.0F, 0.0F, 0.0F);
        this.shoulder_right.addBox(0.0F, 0.0F, 0.0F, 2, 10, 6, 0.0F);
        this.tail = new ModelRenderer(this, 30, 35);
        this.tail.setRotationPoint(6.5F, 11.0F, 6.0F);
        this.tail.addBox(0.0F, 0.0F, 0.0F, 1, 1, 4, 0.0F);
        this.tail.rotateAngleX = -0.54F;
        this.arm_left = new ModelRenderer(this, 36, 6);
        this.arm_left.setRotationPoint(-15.0F, 9.0F, -1.0F);
        this.arm_left.addBox(0.0F, 0.0F, 0.0F, 7, 3, 3, 0.0F);
        this.arm_left.rotateAngleZ = -0.59F;
        this.arm_right = new ModelRenderer(this, 36, 12);
        this.arm_right.setRotationPoint(9.0F, 5.0F, -1.0F);
        this.arm_right.addBox(0.0F, 0.0F, 0.0F, 7, 3, 3, 0.0F);
        this.arm_right.rotateAngleZ = 0.54F;
        this.left_leg = new ModelRenderer(this, 0, 35);
        this.left_leg.setRotationPoint(-6.0F, 18.0F, -1.0F);
        this.left_leg.addBox(0.0F, 0.0F, 0.0F, 3, 4, 3, 0.0F);
        this.right_leg = new ModelRenderer(this, 0, 35);
        this.right_leg.setRotationPoint(3.0F, 18.0F, -1.0F);
        this.right_leg.addBox(0.0F, 0.0F, 0.0F, 3, 4, 3, 0.0F);
        this.left_shoe = new ModelRenderer(this, 12, 35);
        this.left_shoe.setRotationPoint(0.0F, 4.0F, -3.0F);
        this.left_shoe.addBox(0.0F, 0.0F, 0.0F, 3, 2, 6, 0.0F);
        this.right_shoe = new ModelRenderer(this, 12, 43);
        this.right_shoe.setRotationPoint(0.0F, 4.0F, -3.0F);
        this.right_shoe.addBox(0.0F, 0.0F, 0.0F, 3, 2, 6, 0.0F);
        this.head.addChild(this.mouth);
        this.head.addChild(this.jaw);
        this.head.addChild(this.nose);
        this.head.addChild(this.left_eye);
        this.head.addChild(this.right_eye);
        this.body.addChild(this.tail);
        this.body.addChild(this.shoulder_left);
        this.body.addChild(this.shoulder_right);
        this.left_leg.addChild(this.left_shoe);
        this.right_leg.addChild(this.right_shoe);
    }

    @Override
   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    { 
        GlStateManager.pushMatrix();
        if(this.isChild)
        {
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.head.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.right_leg.render(scale);
            this.body.render(scale);
            this.arm_left.render(scale);
            this.left_leg.render(scale);
            this.arm_right.render(scale);
        }
        else
        {
            this.head.render(scale);
            this.right_leg.render(scale);
            this.body.render(scale);
            this.arm_left.render(scale);
            this.left_leg.render(scale);
            this.arm_right.render(scale);
        }
        GlStateManager.popMatrix();
    }
    
    @SuppressWarnings("incomplete-switch")
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getTicksElytraFlying() > 4;
        
        if (flag)
        {
            this.head.rotateAngleX = -((float)Math.PI / 4F);
        }
        else
        {
            this.head.rotateAngleX = headPitch * 0.017453292F;
        }
        
        this.body.rotateAngleY = 0.0F;
        float f = 1.0F;

        if (flag)
        {
            f = (float)(entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
            f = f / 0.2F;
            f = f * f * f;
        }

        if (f < 1.0F)
        {
            f = 1.0F;
        }

        this.arm_right.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
        this.arm_left.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
        this.right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f;
        this.right_leg.rotateAngleY = 0.0F;
        this.left_leg.rotateAngleY = 0.0F;
        this.right_leg.rotateAngleZ = 0.0F;
        this.left_leg.rotateAngleZ = 0.0F;

        if (this.isRiding)
        {
            this.arm_right.rotateAngleX += -((float)Math.PI / 5F);
            this.arm_left.rotateAngleX += -((float)Math.PI / 5F);
            this.right_leg.rotateAngleX = -1.4137167F;
            this.right_leg.rotateAngleY = ((float)Math.PI / 10F);
            this.right_leg.rotateAngleZ = 0.07853982F;
            this.left_leg.rotateAngleX = -1.4137167F;
            this.left_leg.rotateAngleY = -((float)Math.PI / 10F);
            this.left_leg.rotateAngleZ = -0.07853982F;
        }

        this.arm_right.rotateAngleY = 0.0F;

        switch (this.leftArmPose)
        {
            case EMPTY:
                this.arm_left.rotateAngleY = 0.0F;
                break;
            case BLOCK:
                this.arm_left.rotateAngleX = this.arm_left.rotateAngleX * 0.5F - 0.9424779F;
                this.arm_left.rotateAngleY = 0.5235988F;
                break;
            case ITEM:
                this.arm_left.rotateAngleX = this.arm_left.rotateAngleX * 0.5F - ((float)Math.PI / 10F);
                this.arm_left.rotateAngleY = 0.0F;
        }

        switch (this.rightArmPose)
        {
            case EMPTY:
                this.arm_right.rotateAngleY = 0.0F;
                break;
            case BLOCK:
                this.arm_right.rotateAngleX = this.arm_right.rotateAngleX * 0.5F - 0.9424779F;
                this.arm_right.rotateAngleY = -0.5235988F;
                break;
            case ITEM:
                this.arm_right.rotateAngleX = this.arm_right.rotateAngleX * 0.5F - ((float)Math.PI / 10F);
                this.arm_right.rotateAngleY = 0.0F;
        }

        if (this.swingProgress > 0.0F)
        {
            EnumHandSide enumhandside = this.getMainHand(entityIn);
            ModelRenderer modelrenderer = this.getArmForSide(enumhandside);
            float f1 = this.swingProgress;
            this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * ((float)Math.PI * 2F)) * 0.2F;

            if (enumhandside == EnumHandSide.LEFT)
            {
                this.body.rotateAngleY *= -1.0F;
            }

            this.arm_right.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 5.0F;
            this.arm_right.rotationPointX = -MathHelper.cos(this.body.rotateAngleY) * 5.0F;
            this.arm_left.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 5.0F;
            this.arm_left.rotationPointX = MathHelper.cos(this.body.rotateAngleY) * 5.0F;
            this.arm_right.rotateAngleY += this.body.rotateAngleY;
            this.arm_left.rotateAngleY += this.body.rotateAngleY;
            this.arm_left.rotateAngleX += this.body.rotateAngleY;
            f1 = 1.0F - this.swingProgress;
            f1 = f1 * f1;
            f1 = f1 * f1;
            f1 = 1.0F - f1;
            float f2 = MathHelper.sin(f1 * (float)Math.PI);
            float f3 = MathHelper.sin(this.swingProgress * (float)Math.PI) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
            modelrenderer.rotateAngleX = (float)((double)modelrenderer.rotateAngleX - ((double)f2 * 1.2D + (double)f3));
            modelrenderer.rotateAngleY += this.body.rotateAngleY * 2.0F;
            modelrenderer.rotateAngleZ += MathHelper.sin(this.swingProgress * (float)Math.PI) * -0.4F;
        }

        if (this.isSneak)
        {
            this.arm_right.rotateAngleX += 0.4F;
            this.arm_left.rotateAngleX += 0.4F;
            this.body.rotateAngleX = 0.5F;
            this.left_leg.rotationPointZ = 4.0F;
            this.right_leg.rotationPointZ = 4.0F;
            this.head.rotationPointY = 1.0F;
        }
        else
        {
            this.body.rotateAngleX = 0.0F;
            this.left_leg.rotationPointZ = 0.1F;
            this.right_leg.rotationPointZ = 0.1F;
            this.head.rotationPointY = 0.0F;
        }
        
        this.arm_right.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.arm_left.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

        if (this.rightArmPose == ModelKnuckles.ArmPose.BOW_AND_ARROW)
        {
            this.arm_right.rotateAngleY = -0.1F + this.head.rotateAngleY;
            this.arm_left.rotateAngleY = 0.1F + this.head.rotateAngleY + 0.4F;
            this.arm_right.rotateAngleX = -((float)Math.PI / 2F) + this.head.rotateAngleX;
            this.arm_left.rotateAngleX = -((float)Math.PI / 2F) + this.head.rotateAngleX;
        }
        else if (this.leftArmPose == ModelKnuckles.ArmPose.BOW_AND_ARROW)
        {
            this.arm_right.rotateAngleY = -0.1F + this.head.rotateAngleY - 0.4F;
            this.arm_left.rotateAngleY = 0.1F + this.head.rotateAngleY;
            this.arm_right.rotateAngleX = -((float)Math.PI / 2F) + this.head.rotateAngleX;
            this.arm_left.rotateAngleX = -((float)Math.PI / 2F) + this.head.rotateAngleX;
        }
        this.arm_left.rotationPointZ = 0.5F;
        this.arm_right.rotationPointZ = 0.5F;
    }
    
    public void setModelAttributes(ModelBase model)
    {
        super.setModelAttributes(model);

        if (model instanceof ModelKnuckles)
        {
        	ModelKnuckles modelbiped = (ModelKnuckles)model;
            this.leftArmPose = modelbiped.leftArmPose;
            this.rightArmPose = modelbiped.rightArmPose;
            this.isSneak = modelbiped.isSneak;
        }
    }
    
    public void postRenderArm(float scale, EnumHandSide side)
    {
        this.getArmForSide(side).postRender(scale);
    }

    protected ModelRenderer getArmForSide(EnumHandSide side)
    {
        return side == EnumHandSide.LEFT ? this.arm_left : this.arm_right;
    }

    protected EnumHandSide getMainHand(Entity entityIn)
    {
        if (entityIn instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            EnumHandSide enumhandside = entitylivingbase.getPrimaryHand();
            return entitylivingbase.swingingHand == EnumHand.MAIN_HAND ? enumhandside : enumhandside.opposite();
        }
        else
        {
            return EnumHandSide.RIGHT;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static enum ArmPose
    {
        EMPTY,
        ITEM,
        BLOCK,
        BOW_AND_ARROW;
    }
}
