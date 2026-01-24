package hack.skate.client.features.settings.impl;

import lombok.Setter;

import java.util.function.Predicate;

import hack.skate.client.features.settings.Setting;

@Setter
public class BoolSetting extends Setting {
    private boolean value;

    public boolean getValue() {
        return value;
    }

    public BoolSetting(String name, boolean defaultValue) {
        super(name);
        this.value = defaultValue;
    }
    
    public BoolSetting(String name, boolean defaultValue, Predicate<Object> dependency) {
        super(name);
        this.value = defaultValue;
        this.setDependency(dependency);
    }
}
