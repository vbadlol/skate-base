package hack.skate.client.screens.clickgui;

import hack.skate.client.features.Category;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import hack.skate.client.Skate;
import hack.skate.client.screens.clickgui.component.Panel;

public class ClickGuiScreen extends Screen {
    private final List<Panel> panels = new ArrayList<>();
    
    public ClickGuiScreen() {
        super(Text.literal(""));
        
        float panelX = 10;
        for (Category category : Category.values()) {
            Panel panel = new Panel(category, panelX, 10, 100, 18, Skate.featureManager.getFeatures());
            panels.add(panel);
            panelX += 110;
        }
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        for (Panel panel : panels) {
            panel.render(context, mouseX, mouseY, delta);
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Panel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Panel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Panel panel : panels) {
            panel.keyPressed(keyCode, scanCode, modifiers);
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
