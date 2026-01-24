package hack.skate.client.utils;

public class PlayerUtil implements Imports {
    public static boolean canCrit() {
        if (mc.player == null || mc.world == null) return false;
        return (mc.player.fallDistance > 0) &&
                !mc.player.isTouchingWater() &&
                !mc.player.isInLava() &&
                !mc.player.isClimbing() &&
                !mc.player.hasVehicle() &&
                !mc.player.hasStatusEffect(net.minecraft.entity.effect.StatusEffects.BLINDNESS);
    }
}
