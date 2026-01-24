package hack.skate.client.screens.clickgui.component.impl.settings;

import hack.skate.client.screens.clickgui.component.Component;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import hack.skate.client.Skate;
import hack.skate.client.config.ColorConfig;
import hack.skate.client.features.settings.impl.IntSetting;
import hack.skate.client.utils.RenderUtil;

public class SliderComponent extends Component {
    private final IntSetting setting;
    private boolean dragging;
    
    public SliderComponent(IntSetting setting, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.setting = setting;
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (dragging) {
            float percent = (mouseX - x) / width;
            percent = Math.min(1, Math.max(0, percent));
            int value = (int) (setting.getMinValue() + (setting.getMaxValue() - setting.getMinValue()) * percent);
            setting.setValue(value);
        }
        
        Color bgColor = isHovered(mouseX, mouseY) ? ColorConfig.BACKGROUND_HOVER : ColorConfig.BACKGROUND_DARK;
        RenderUtil.drawRect(context.getMatrices(), x, y, x + width, y + height, bgColor);
        
        float sliderWidth = ((float) (setting.getValue() - setting.getMinValue()) / (setting.getMaxValue() - setting.getMinValue())) * width;
        RenderUtil.drawRect(context.getMatrices(), x, y, x + sliderWidth, y + height, ColorConfig.PRIMARY_ACCENT_SEMI_TRANSPARENT);
        
        String displayValue = setting.getName() + ": " + setting.getValue() + setting.getSuffix();
        Skate.fonts.getArial().renderWithShadow(context.getMatrices(), displayValue, x + 5, y + height / 2 - 5, 9, ColorConfig.TEXT_WHITE.getRGB());
    }
    
    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered((int) mouseX, (int) mouseY) && button == 0) {
            dragging = true;
            float percent = (float) ((mouseX - x) / width);
            percent = Math.min(1, Math.max(0, percent));
            int value = (int) (setting.getMinValue() + (setting.getMaxValue() - setting.getMinValue()) * percent);
            setting.setValue(value);
        }
    }
    
    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }
} 