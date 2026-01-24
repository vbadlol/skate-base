package hack.skate.client.screens.clickgui.component.impl.settings;

import hack.skate.client.screens.clickgui.component.Component;
import hack.skate.client.utils.Imports;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.awt.*;

import hack.skate.client.Skate;
import hack.skate.client.config.ColorConfig;
import hack.skate.client.features.settings.impl.BoolSetting;
import hack.skate.client.utils.RenderUtil;

public class BooleanComponent extends Component implements Imports {
    private final BoolSetting setting;
    
    public BooleanComponent(BoolSetting setting, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.setting = setting;
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Color bgColor = isHovered(mouseX, mouseY) ? ColorConfig.BACKGROUND_HOVER : ColorConfig.BACKGROUND_DARK;
        RenderUtil.drawRect(context.getMatrices(), x, y, x + width, y + height, bgColor);
        
        Color boxColor = setting.getValue() ? ColorConfig.PRIMARY_ACCENT : ColorConfig.BACKGROUND_DISABLED;
        RenderUtil.drawRect(context.getMatrices(), x + width - 15, y + 4, x + width - 5, y + height - 4, boxColor);
        
        Skate.fonts.getArial().renderWithShadow(context.getMatrices(), setting.getName(), x + 5, y + height / 2 - 5, 9, ColorConfig.TEXT_WHITE.getRGB());
    }
    
    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered((int) mouseX, (int) mouseY) && button == 0) {
            setting.setValue(!setting.getValue());
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }
} 