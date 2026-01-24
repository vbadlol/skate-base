package hack.skate.client.event.impl;

import hack.skate.client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

@AllArgsConstructor
@Getter
public class EventRender2D extends Event {
    private int scaledWidth, scaledHeight;
    private MatrixStack matrixStack;
    private float tickDelta;
    private DrawContext context;
}
