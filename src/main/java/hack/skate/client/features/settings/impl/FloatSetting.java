package hack.skate.client.features.settings.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

import hack.skate.client.features.settings.Setting;

@Getter
@Setter
public class FloatSetting extends Setting {
    private float value;
    private float minValue;
    private float maxValue;
    private float increment;
    private String suffix = "";

    public FloatSetting(String name, float defaultValue, float minValue, float maxValue, float increment) {
        super(name);
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
    }
    
    public FloatSetting(String name, float defaultValue, float minValue, float maxValue, float increment, String suffix) {
        super(name);
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
        this.suffix = suffix;
    }
    
    public FloatSetting(String name, float defaultValue, float minValue, float maxValue, float increment, Predicate<Object> dependency) {
        super(name);
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
        this.setDependency(dependency);
    }
    
    public FloatSetting(String name, float defaultValue, float minValue, float maxValue, float increment, String suffix, Predicate<Object> dependency) {
        super(name);
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
        this.suffix = suffix;
        this.setDependency(dependency);
    }
    
    public void increment() {
        setValue(Math.min(value + increment, maxValue));
    }
    
    public void decrement() {
        setValue(Math.max(value - increment, minValue));
    }
    
    public void setValue(float value) {
        this.value = Math.max(minValue, Math.min(maxValue, value));
    }
} 