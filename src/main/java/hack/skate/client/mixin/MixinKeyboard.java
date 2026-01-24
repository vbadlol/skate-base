package hack.skate.client.mixin;

import net.minecraft.client.Keyboard;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hack.skate.client.Skate;
import hack.skate.client.event.impl.EventKey;

import org.spongepowered.asm.mixin.injection.At;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(method = "onKey", at = @At("HEAD"))
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (key != -1) {
            Skate.EVENT_BUS.post(new EventKey(key, action));
        }
    }
}