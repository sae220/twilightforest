package twilightforest.client.model.entity;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.Yeti;

public class YetiModel<T extends Yeti> extends HumanoidModel<T> {
	private final ModelPart leftEye;
	private final ModelPart rightEye;
	private final ModelPart angryLeftEye;
	private final ModelPart angryRightEye;

	public YetiModel(ModelPart root) {
		super(root);

		var body = root.getChild("body");
		this.rightEye = body.getChild("right_eye");
		this.leftEye = body.getChild("left_eye");
		this.angryRightEye = body.getChild("angry_right_eye");
		this.angryLeftEye = body.getChild("angry_left_eye");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 0.0F, 0.0F, 0.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 0.0F, 0.0F, 0.0F),
			PartPose.ZERO);

		var body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-10.0F, 0.0F, -6.0F, 20.0F, 26.0F, 12.0F),
			PartPose.offset(0.0F, -14.0F, 0.0F));

		body.addOrReplaceChild("mouth", CubeListBuilder.create()
				.texOffs(96, 6)
				.addBox(-7.0F, -5.0F, -0.5F, 14.0F, 10.0F, 1.0F),
			PartPose.offset(0.0F, 12.0F, -6.0F));

		body.addOrReplaceChild("right_eye", CubeListBuilder.create()
				.texOffs(96, 0)
				.addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F),
			PartPose.offset(-5.5F, 4.5F, -6.0F));

		body.addOrReplaceChild("left_eye", CubeListBuilder.create()
				.texOffs(96, 0)
				.addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F),
			PartPose.offset(5.5F, 4.5F, -6.0F));

		body.addOrReplaceChild("angry_right_eye", CubeListBuilder.create()
				.texOffs(109, 0)
				.addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F),
			PartPose.offset(5.5F, 4.5F, -6.0F));

		body.addOrReplaceChild("angry_left_eye", CubeListBuilder.create()
				.texOffs(109, 0)
				.addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F),
			PartPose.offset(-5.5F, 4.5F, -6.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-5.0F, -2.0F, -3.0F, 6.0F, 16.0F, 6.0F),
			PartPose.offset(-11.0F, -4.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.mirror()
				.texOffs(0, 0)
				.addBox(-1.0F, -2.0F, -3.0F, 6.0F, 16.0F, 6.0F),
			PartPose.offset(11.0F, -4.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().mirror()
				.texOffs(0, 22)
				.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 12.0F, 8.0F),
			PartPose.offset(-6.0F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(0, 22)
				.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 12.0F, 8.0F),
			PartPose.offset(6.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.head.xRot = headPitch * Mth.DEG_TO_RAD;
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;
		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		if (entity.isVehicle()) {
			// arms up!
			this.rightArm.xRot += Mth.PI;
			this.leftArm.xRot += Mth.PI;
		}

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;

		AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);

		// if yeti is angry, hold arms forwards like a zombie
		if (entity.isAngry()) {
			float f6 = Mth.sin(this.attackTime * Mth.PI);
			float f7 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * Mth.PI);
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightArm.yRot = -(0.1F - f6 * 0.6F);
			this.leftArm.yRot = 0.1F - f6 * 0.6F;
			this.rightArm.xRot = -(Mth.HALF_PI);
			this.leftArm.xRot = -(Mth.HALF_PI);
			this.rightArm.xRot -= f6 * 1.2F - f7 * 0.4F;
			this.leftArm.xRot -= f6 * 1.2F - f7 * 0.4F;
			if (entity.isVehicle()) {
				// arms up!
				this.rightArm.xRot -= Mth.HALF_PI;
				this.leftArm.xRot -= Mth.HALF_PI;
			}
		}
	}

	/**
	 * Change eye color if yeti is angry
	 */
	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		if (entity.isAngry()) {
			this.rightEye.visible = false;
			this.leftEye.visible = false;
			this.angryRightEye.visible = true;
			this.angryLeftEye.visible = true;
		} else {
			this.rightEye.visible = true;
			this.leftEye.visible = true;
			this.angryRightEye.visible = false;
			this.angryLeftEye.visible = false;
		}
	}
}
