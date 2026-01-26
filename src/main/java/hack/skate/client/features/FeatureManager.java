package hack.skate.client.features;

import lombok.Getter;

import java.util.ArrayList;

import hack.skate.client.features.impl.client.ClickGUI;
import hack.skate.client.features.impl.client.Teams;
import hack.skate.client.features.impl.combat.TriggerBot;
import hack.skate.client.features.impl.movement.Sprint;
import hack.skate.client.features.impl.render.HUD;
import hack.skate.client.features.impl.render.Nametags;

@Getter
public class FeatureManager {
    private final ArrayList<Feature> features = new ArrayList<>();

    public void initialize() {
        features.add(new Sprint());
        features.add(new HUD());
        features.add(new ClickGUI());
        features.add(new TriggerBot());
        features.add(new Teams());
        features.add(new Nametags());

        features.forEach(Feature::initSettings);
        features.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    }

    public <T extends Feature> T getFeatureByClass(Class<T> featureClass) {
        return featureClass.cast(features.stream()
                .filter(feature -> feature.getClass().equals(featureClass))
                .findFirst()
                .orElse(null));
    }
}
