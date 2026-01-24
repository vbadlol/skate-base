package hack.skate.client.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hack.skate.client.Skate;
import hack.skate.client.event.impl.EventTick;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        EventTick event = new EventTick();
        Skate.EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}
