package hack.skate.client.features;

import hack.skate.client.utils.Imports;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;

import hack.skate.client.Skate;
import hack.skate.client.event.Subscriber;
import hack.skate.client.features.settings.Setting;

public class Feature implements Subscriber, Imports {
    public ArrayList<Setting> settings = new ArrayList<>();
    private boolean enabled = false;
    @Getter @Setter
    private int key;

    private final FeatureInfo info;

    public Feature() {
        FeatureInfo info = this.getClass().getAnnotation(FeatureInfo.class);
        this.key = info != null ? info.key() : -1;
        this.info = this.getClass().getAnnotation(FeatureInfo.class);
    }

    public void initSettings() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value instanceof Setting) {
                    settings.add((Setting) value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return info != null ? info.name() : this.getClass().getSimpleName();
    }

    public String getDescription() {
        return info != null ? info.description() : "";
    }

    public Category getCategory() {
        return info != null ? info.category() : Category.CLIENT;
    }

    public void onEnable() {
        Skate.EVENT_BUS.register(this);
    }

    public void onDisable() {
        Skate.EVENT_BUS.unregister(this);
    }

    public void toggle() {
        setEnabled(!this.enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (this.enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public Boolean hasSettings() {
        return !settings.isEmpty();
    }

    public String getInfo() {
        return "";
    }

    public boolean isEnabled() {
        return enabled;
    }
}
