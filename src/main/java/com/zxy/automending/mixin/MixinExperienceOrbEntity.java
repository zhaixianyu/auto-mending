package com.zxy.automending.mixin;


import com.zxy.automending.Auto;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin (ExperienceOrbEntity.class)
public class MixinExperienceOrbEntity {
        @Inject(at = @At("HEAD"),method = "onPlayerCollision")
        public void onPlayerCollision(PlayerEntity player, CallbackInfo ci){
            Auto.getAuto().autoMenDing(player);
    }
}
