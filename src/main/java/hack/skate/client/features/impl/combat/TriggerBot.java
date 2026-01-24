package hack.skate.client.features.impl.combat;

import hack.skate.client.Skate;
import hack.skate.client.event.Subscribe;
import hack.skate.client.event.impl.EventTick;
import hack.skate.client.features.Feature;
import hack.skate.client.features.impl.client.Teams;
import hack.skate.client.features.settings.impl.BoolSetting;
import hack.skate.client.features.settings.impl.FloatSetting;
import hack.skate.client.features.settings.impl.IntSetting;
import hack.skate.client.features.settings.impl.ModeSetting;
import hack.skate.client.features.settings.impl.RangeSetting;
import hack.skate.client.utils.PlayerUtil;
import hack.skate.client.utils.Timer;
import hack.skate.client.features.Category;
import hack.skate.client.features.FeatureInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

@FeatureInfo(name = "TriggerBot", description = "Auto hits people.", category = Category.COMBAT)
public class TriggerBot extends Feature {
    public RangeSetting attackDelay = new RangeSetting("Delay", 100.0f, 150.0f, 0.0f, 1000.0f, 1.0f, " ms");
    public FloatSetting range = new FloatSetting("Range", 4.5f, 1f, 6f, 0.1f);
    public IntSetting chance = new IntSetting("Chance", 80, 0, 100, "%");
    public BoolSetting preferCrits = new BoolSetting("Prefer crits", true);
    public BoolSetting weapon = new BoolSetting("Need weapon", true);
    public BoolSetting mouse = new BoolSetting("Mouse", false);
    public ModeSetting mouseButton = new ModeSetting("Mouse button", "LMB", o -> mouse.getValue(),"LMB", "RMB", "MMB");
    public BoolSetting players = new BoolSetting("Players", true);
    public BoolSetting mobs = new BoolSetting("Mobs", false);
    public BoolSetting animals = new BoolSetting("Animals", false);
    
    private final Timer attackTimer = new Timer();
    private float currentDelay;
    private final Timer targetTimeout = new Timer();

    public TriggerBot() {
        this.currentDelay = randomizeDelay();
    }

    private float randomizeDelay() {
        return (float)(Math.random() * (attackDelay.getMaxValue() - attackDelay.getMinValue()) + attackDelay.getMinValue());
    }

    @Override
    public void onEnable() {
        currentDelay = randomizeDelay();
        super.onEnable();
    }

    @Subscribe
    public void onTick(EventTick event) {
        boolean isSilent = false;

        assert mc.player != null;
        Entity potentialTarget = null;
        if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            potentialTarget = mc.targetedEntity;
        }
        if (potentialTarget == null || mc.currentScreen != null) {
            return;
        }

        if (!shouldTarget(potentialTarget)) {
            return;
        }

        if (mc.player.distanceTo(potentialTarget) > range.getValue()) {
            return;
        }

        if(weapon.getValue()
                && mc.player.getMainHandStack() != null
                && !mc.player.getMainHandStack().isEmpty()) {
            if(!(mc.player.getMainHandStack().getItem() instanceof SwordItem ||
                    mc.player.getMainHandStack().getItem() instanceof AxeItem ||
                    mc.player.getMainHandStack().getItem() instanceof PickaxeItem)) {
                return;
            }
        }

        boolean isMouseClicked = true;

        if(!isMouseClicked) return;

        if (preferCrits.getValue() && !PlayerUtil.canCrit()) {
            if (!mc.player.isOnGround()) {
                return;
            }
        }

        if(mc.player.getAttackCooldownProgress(0.5f) >= 1.0f
                && potentialTarget.isAttackable()
                && potentialTarget.isAlive()
                && attackTimer.passedMs((long)currentDelay)
                && chance.getValue() >= (int)(Math.random() * 100)) {
            
            if (!isSilent) {
                mc.interactionManager.attackEntity(mc.player, ((EntityHitResult)mc.crosshairTarget).getEntity());
            }
            mc.player.swingHand(Hand.MAIN_HAND);

            targetTimeout.reset();

            attackTimer.reset();
            currentDelay = randomizeDelay();
        }
    }

    private boolean shouldTarget(Entity entity) {
        if (entity instanceof PlayerEntity && players.getValue()) {
            Teams teams = Skate.featureManager.getFeatureByClass(Teams.class);
            if (teams != null && teams.isEnabled() && teams.isOnSameTeam(entity)) {
                return false;
            }
            return true;
        }
        if (entity instanceof HostileEntity && mobs.getValue()) return true;
        if (entity instanceof PassiveEntity && animals.getValue()) return true;
        return false;
    }

    @Override
    public String getInfo() {
        return Math.round(attackDelay.getMinValue()) + "ms";
    }
}
