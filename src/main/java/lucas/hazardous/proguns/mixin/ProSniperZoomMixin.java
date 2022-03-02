package lucas.hazardous.proguns.mixin;

import lucas.hazardous.proguns.ProSniperGun;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class ProSniperZoomMixin {
    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
    public void zoomCamera(CallbackInfoReturnable<Double> callbackInfo) {
        if(ProSniperGun.isZooming()) {
            callbackInfo.setReturnValue(callbackInfo.getReturnValue() * 5);
        }
    }
}