package hack.skate.client.features.settings.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

import hack.skate.client.features.settings.Setting;

@Getter
@Setter
public class IntSetting extends Setting {
    private int value;
    private int minValue;
    private int maxValue;
    private String suffix = "";

    public IntSetting(String name, int defaultValue, int minValue, int maxValue) {
        super(name);
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public IntSetting(String name, int defaultValue, int minValue, int maxValue, String suffix) {
        super(name);
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.suffix = suffix;
    }

    public IntSetting(String name, int defaultValue, int minValue, int maxValue, String suffix, Predicate<Object> dependency) {
        super(name);
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.suffix = suffix;
        this.setDependency(dependency);
    }
    
    public IntSetting(String name, int defaultValue, int minValue, int maxValue, Predicate<Object> dependency) {
        super(name);
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.setDependency(dependency);
    }
}