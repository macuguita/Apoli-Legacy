package io.github.apace100.apoli.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.NightVisionPower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(LightmapRenderStateExtractor.class)
@Environment(EnvType.CLIENT)
public abstract class LightmapRenderStateExtractorMixin implements AutoCloseable {

    @Shadow @Final private Minecraft minecraft;

    @Definition(id = "max", method = "Ljava/lang/Math;max(FF)F")
    @Definition(id = "brightnessOption", local = @Local(type = float.class, name = "brightnessOption"))
    @Definition(id = "darknessEffectBrightnessModifier", local = @Local(type = float.class, name = "darknessEffectBrightnessModifier"))
    @Expression("max(?, brightnessOption - darknessEffectBrightnessModifier)")
    @ModifyExpressionValue(method = "extract", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float nightVisionPowerEffect(float value) {
        Optional<Float> nightVisionStrength = PowerHolderComponent.KEY.get(minecraft.player).getPowers(NightVisionPower.class).stream().filter(NightVisionPower::isActive).map(NightVisionPower::getStrength).max(Float::compareTo);
        return nightVisionStrength.map(aFloat -> Math.max(aFloat, value)).orElse(value);
    }
}
