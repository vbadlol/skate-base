
package hack.skate.client.features.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@AllArgsConstructor
public class Setting {
    private String name;
    @Setter
    private Predicate<Object> dependency;
    
    public Setting(String name) {
        this.name = name;
        this.dependency = null;
    }
    
    public boolean isVisible() {
        return dependency == null || dependency.test(null);
    }
}
