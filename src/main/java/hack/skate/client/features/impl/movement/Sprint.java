package hack.skate.client.features.impl.movement;

import hack.skate.client.event.Subscribe;
import hack.skate.client.event.impl.EventMove;
import hack.skate.client.features.Feature;
import hack.skate.client.features.Category;
import hack.skate.client.features.FeatureInfo;

@FeatureInfo(name = "Sprint", description = "Sprints for u", category = Category.MOVEMENT)
public class Sprint extends Feature {
    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.setSprinting(false);
        }
        super.onDisable();
    }

    @Subscribe
    public void onMove(EventMove event) {
        if (mc.player == null) return;

        mc.options.sprintKey.setPressed(true);
    }
}
