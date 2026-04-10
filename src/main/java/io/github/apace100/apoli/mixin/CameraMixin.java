package io.github.apace100.apoli.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyCameraSubmersionTypePower;
import io.github.apace100.apoli.power.PhasingPower;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public class CameraMixin {

    @WrapOperation(method = "modifyFovBasedOnDeathOrFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getFluidInCamera()Lnet/minecraft/world/level/material/FogType;"))
    private FogType modifySubmersionType(Camera camera, Operation<FogType> original) {
        FogType fogType = original.call(camera);
        if(camera.entity() instanceof LivingEntity) {
            for(ModifyCameraSubmersionTypePower p : PowerHolderComponent.getPowers(camera.entity(), ModifyCameraSubmersionTypePower.class)) {
                if(p.doesModify(fogType)) {
                    return p.getNewType();
                }
            }
        }
        return fogType;
    }

    // PHASING
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"), method = "alignWithEntity")
    private boolean preventThirdPerson(CameraType instance, Operation<Boolean> original) {
        Camera camera = (Camera) (Object) this;
        if (PowerHolderComponent.getPowers(camera.entity(), PhasingPower.class).stream().anyMatch(pp -> pp.getRenderType() == PhasingPower.RenderType.REMOVE_BLOCKS)) {
            return true;
        } else {
            return original.call(instance);
        }
    }
}
