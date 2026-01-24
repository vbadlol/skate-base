package hack.skate.client.features.settings.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import hack.skate.client.features.settings.Setting;

@Getter
@Setter
public class ModeSetting extends Setting {
    private String value;
    private List<String> modes;
    
    public ModeSetting(String name, String defaultValue, String... modes) {
        super(name);
        this.modes = Arrays.asList(modes);
        this.value = defaultValue;
    }
    
    public ModeSetting(String name, String defaultValue, Predicate<Object> dependency, String... modes) {
        super(name);
        this.modes = Arrays.asList(modes);
        this.value = defaultValue;
        this.setDependency(dependency);
    }
    
    public void cycle() {
        int index = modes.indexOf(value);
        index = (index + 1) % modes.size();
        value = modes.get(index);
    }
    
    public boolean is(String mode) {
        return value.equalsIgnoreCase(mode);
    }
} 