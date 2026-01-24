package hack.skate.client.handlers.impl;

import org.lwjgl.glfw.GLFW;

import hack.skate.client.Skate;
import hack.skate.client.event.Subscribe;
import hack.skate.client.event.impl.EventKey;
import hack.skate.client.features.Feature;
import hack.skate.client.handlers.Handler;

public class KeyHandler extends Handler {
    @Subscribe
    public void onKey(EventKey event) {
        if (mc.currentScreen != null)
            return;

        if (event.getAction() != GLFW.GLFW_PRESS)
            return;

        for (Feature feature : Skate.featureManager.getFeatures()) {
            if (feature.getKey() == event.getKey()) {
                feature.toggle();
            }
        }
    }
}
