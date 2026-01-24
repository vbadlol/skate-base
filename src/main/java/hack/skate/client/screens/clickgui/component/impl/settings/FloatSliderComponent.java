package hack.skate.client.screens.clickgui.component.impl.settings;

import hack.skate.client.screens.clickgui.component.Component;
import hack.skate.client.utils.Imports;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import hack.skate.client.Skate;
import hack.skate.client.config.ColorConfig;
import hack.skate.client.features.settings.impl.FloatSetting;
import hack.skate.client.utils.RenderUtil;

public class FloatSliderComponent extends Component implements Imports {
    private final FloatSetting setting;
    private boolean dragging;
    
    public FloatSliderComponent(FloatSetting setting, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.setting = setting;
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (dragging) {
            float percent = (mouseX - x) / width;
            percent = Math.min(1, Math.max(0, percent));
            float value = setting.getMinValue() + (setting.getMaxValue() - setting.getMinValue()) * percent;
            setting.setValue(value);
        }
        
        Color bgColor = isHovered(mouseX, mouseY) 
                ? ColorConfig.BACKGROUND_HOVER 
                : ColorConfig.BACKGROUND_DARK;
        RenderUtil.drawRect(context.getMatrices(), x, y, x + width, y + height, bgColor);
        
        float sliderWidth = ((setting.getValue() - setting.getMinValue()) / (setting.getMaxValue() - setting.getMinValue())) * width;
        RenderUtil.drawRect(context.getMatrices(), x, y, x + sliderWidth, y + height, ColorConfig.PRIMARY_ACCENT_SEMI_TRANSPARENT);
        
        BigDecimal bd = BigDecimal.valueOf(setting.getValue()).setScale(2, RoundingMode.HALF_UP);
        String displayValue = setting.getName() + ": " + bd.floatValue() + setting.getSuffix();
        Skate.fonts.getArial().renderWithShadow(context.getMatrices(), displayValue, x + 5, y + height / 2 - 5, 9, ColorConfig.TEXT_WHITE.getRGB());
    }
    
    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered((int) mouseX, (int) mouseY) && button == 0) {
            dragging = true;
            float percent = (float) ((mouseX - x) / width);
            percent = Math.min(1, Math.max(0, percent));
            float value = setting.getMinValue() + (setting.getMaxValue() - setting.getMinValue()) * percent;
            setting.setValue(value);
        }
    }
    
    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }
} 