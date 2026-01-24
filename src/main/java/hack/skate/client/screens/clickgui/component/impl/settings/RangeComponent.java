package hack.skate.client.screens.clickgui.component.impl.settings;

import hack.skate.client.screens.clickgui.component.Component;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import hack.skate.client.Skate;
import hack.skate.client.config.ColorConfig;
import hack.skate.client.features.settings.impl.RangeSetting;
import hack.skate.client.utils.RenderUtil;

public class RangeComponent extends Component {
    private final RangeSetting setting;
    private boolean draggingMin;
    private boolean draggingMax;
    
    public RangeComponent(RangeSetting setting, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.setting = setting;
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (draggingMin) {
            float percent = (mouseX - x) / width;
            percent = Math.min(setting.getMaxPercentage(), Math.max(0, percent));
            float value = setting.getLowerBound() + (setting.getUpperBound() - setting.getLowerBound()) * percent;
            value = Math.round(value / setting.getIncrement()) * setting.getIncrement();
            setting.setMinValue(value);
        } else if (draggingMax) {
            float percent = (mouseX - x) / width;
            percent = Math.min(1, Math.max(setting.getMinPercentage(), percent));
            float value = setting.getLowerBound() + (setting.getUpperBound() - setting.getLowerBound()) * percent;
            value = Math.round(value / setting.getIncrement()) * setting.getIncrement();
            setting.setMaxValue(value);
        }
        
                Color bgColor = isHovered(mouseX, mouseY) ? ColorConfig.BACKGROUND_HOVER : ColorConfig.BACKGROUND_DARK;
        RenderUtil.drawRect(context.getMatrices(), x, y, x + width, y + height, bgColor);
        
        float minPos = x + (setting.getMinPercentage() * width);
        float maxPos = x + (setting.getMaxPercentage() * width);
        
        RenderUtil.drawRect(context.getMatrices(), minPos, y, maxPos, y + height, ColorConfig.PRIMARY_ACCENT_SEMI_TRANSPARENT);
        
        BigDecimal bdMin = BigDecimal.valueOf(setting.getMinValue()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bdMax = BigDecimal.valueOf(setting.getMaxValue()).setScale(2, RoundingMode.HALF_UP);
        String displayValue = setting.getName() + ": " + bdMin.floatValue() + " - " + bdMax.floatValue() + setting.getSuffix();
        Skate.fonts.getArial().renderWithShadow(context.getMatrices(), displayValue, x + 5, y + height / 2 - 5, 9, ColorConfig.TEXT_WHITE.getRGB());
    }
    
    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered((int) mouseX, (int) mouseY) && button == 0) {
            float percent = (float) ((mouseX - x) / width);
            float minPercent = setting.getMinPercentage();
            float maxPercent = setting.getMaxPercentage();
            
            if (Math.abs(percent - minPercent) < Math.abs(percent - maxPercent)) {
                draggingMin = true;
            } else {
                draggingMax = true;
            }
            
            if (draggingMin) {
                float value = setting.getLowerBound() + (setting.getUpperBound() - setting.getLowerBound()) * percent;
                value = Math.round(value / setting.getIncrement()) * setting.getIncrement();
                setting.setMinValue(value);
            } else {
                float value = setting.getLowerBound() + (setting.getUpperBound() - setting.getLowerBound()) * percent;
                value = Math.round(value / setting.getIncrement()) * setting.getIncrement();
                setting.setMaxValue(value);
            }
        }
    }
    
    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        draggingMin = false;
        draggingMax = false;
    }
} 