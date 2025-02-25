package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.LichModel;
import twilightforest.entity.boss.Lich;

public class LichRenderer extends HumanoidMobRenderer<Lich, LichModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("twilightlich64.png");

	public LichRenderer(EntityRendererProvider.Context context, LichModel model, float shadowSize) {
		super(context, model, shadowSize);
		this.addLayer(new ShieldLayer<>(this));
	}

	@Nullable
	@Override
	protected RenderType getRenderType(Lich entity, boolean bodyVisible, boolean translucent, boolean glowing) {
		if (entity.isShadowClone()) return RenderType.entityTranslucent(this.getTextureLocation(entity));
		else return super.getRenderType(entity, bodyVisible, translucent, glowing);
	}

	@Override
	protected boolean isShaking(Lich entity) {
		return super.isShaking(entity) || (entity.isDeadOrDying() && entity.deathTime <= Lich.DEATH_ANIMATION_POINT_A);
	}

	@Override
	public void render(Lich entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
		if (entity.deathTime > 0) {
			stack.pushPose();
			if (entity.deathTime > Lich.DEATH_ANIMATION_POINT_A) {
				stack.translate(0.0D, -1.8D * Math.pow(Math.min(((float) (entity.deathTime - Lich.DEATH_ANIMATION_POINT_A) + partialTicks) * 0.05D, 1.0D), 3.0D), 0.0D);
			} else {
				float time = (float) entity.deathTime + partialTicks;
				stack.translate(Math.sin(time * time) * 0.01D, 0.0D, Math.cos(time * time) * 0.01D);
			}
			super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
			stack.popPose();
		} else super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
	}

	@Override
	protected float getFlipDegrees(Lich entity) { //Prevent the body from keeling over
		return entity.isDeadOrDying() ? 0.0F : super.getFlipDegrees(entity);
	}

	@Override
	public ResourceLocation getTextureLocation(Lich entity) {
		return TEXTURE;
	}
}
