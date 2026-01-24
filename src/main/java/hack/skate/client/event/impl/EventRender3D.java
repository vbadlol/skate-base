package hack.skate.client.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import hack.skate.client.event.Event;

@Getter
@AllArgsConstructor
public class EventRender3D extends Event {
    private final MatrixStack matrices;
    private final float tickDelta;
    private final Camera camera;
    private final GameRenderer gameRenderer;
    private final Matrix4f projectionMatrix;
    private final WorldRenderer worldRenderer;
}
