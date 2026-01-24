package hack.skate.client.features.impl.client;

import hack.skate.client.features.Category;
import hack.skate.client.features.FeatureInfo;
import org.lwjgl.glfw.GLFW;

import hack.skate.client.Skate;
import hack.skate.client.features.Feature;

@FeatureInfo(name = "ClickGUI", description = "Crazy gui", category = Category.CLIENT, key = GLFW.GLFW_KEY_RIGHT_SHIFT)
public class ClickGUI extends Feature {
    @Override
    public void onEnable() {
        Skate.screenManager.displayClickGUI();
        this.setEnabled(false);
    }

    @Override
    public boolean isEnabled() {
        return Skate.screenManager.isClickGUIOpen();
    }
}
