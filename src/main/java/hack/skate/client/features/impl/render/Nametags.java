package hack.skate.client.features.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;

import hack.skate.client.Skate;
import hack.skate.client.config.ColorConfig;
import hack.skate.client.event.Subscribe;
import hack.skate.client.event.impl.EventRender2D;
import hack.skate.client.event.impl.EventRender3D;
import hack.skate.client.features.Feature;
import hack.skate.client.features.settings.impl.BoolSetting;
import hack.skate.client.features.settings.impl.IntSetting;
import hack.skate.client.utils.RenderUtil;
import hack.skate.client.features.FeatureInfo;
import hack.skate.client.features.Category;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

@FeatureInfo(name = "Nametags", description = "Shows nametags", category = Category.RENDER)
public class Nametags extends Feature {
    private final BoolSetting healthBar = new BoolSetting("Health Bar", true);
    private final IntSetting range = new IntSetting("Range", 64, 0, 256);
    private final IntSetting scale = new IntSetting("Scale", 100, 50, 200);
    private final BoolSetting dynamicScaling = new BoolSetting("Dynamic Scaling", true);

    private final Map<Entity, Pair<Rectangle, Boolean>> entityPositions = new HashMap<>();
    private final Color backgroundColor = ColorConfig.BACKGROUND_DARK;
    private final Color healthBarColor = ColorConfig.PRIMARY_ACCENT;

    @Subscribe
    public void onRender3D(EventRender3D event) {
        entityPositions.clear();

        if (mc.player == null || mc.world == null) return;

        RenderTickCounter renderTickCounter = mc.getRenderTickCounter();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == null) continue;

            Vec3d prevPos = new Vec3d(player.lastRenderX, player.lastRenderY, player.lastRenderZ);
            Vec3d interpolated = prevPos.add(
                    player.getPos().subtract(prevPos).multiply(renderTickCounter.getTickDelta(false))
            ).add(0, 0.05f, 0);

            Box boundingBox = new Box(
                    interpolated.x,
                    interpolated.y,
                    interpolated.z,
                    interpolated.x,
                    interpolated.y + player.getHeight() + (player.isSneaking() ? -0.2 : 0),
                    interpolated.z
            );

            Vec3d[] corners = {
                    new Vec3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ),
                    new Vec3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ),
                    new Vec3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ),
                    new Vec3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ),
                    new Vec3d(boundingBox.minX, boundingBox.maxY + 0.1, boundingBox.minZ),
                    new Vec3d(boundingBox.maxX, boundingBox.maxY + 0.1, boundingBox.minZ),
                    new Vec3d(boundingBox.maxX, boundingBox.maxY + 0.1, boundingBox.maxZ),
                    new Vec3d(boundingBox.minX, boundingBox.maxY + 0.1, boundingBox.maxZ)
            };

            Rectangle rectangle = null;
            boolean visible = false;

            for (Vec3d corner : corners) {
                Pair<Vec3d, Boolean> projection = RenderUtil.project(
                        event.getMatrices().peek().getPositionMatrix(),
                        event.getProjectionMatrix(),
                        corner
                );
                
                if (projection.getRight()) {
                    visible = true;
                }
                
                Vec3d projected = projection.getLeft();

                if (rectangle == null) {
                    rectangle = new Rectangle(
                            projected.getX(),
                            projected.getY(),
                            projected.getX(),
                            projected.getY()
                    );
                } else {
                    if (rectangle.x > projected.getX()) {
                        rectangle.x = projected.getX();
                    }
                    if (rectangle.y > projected.getY()) {
                        rectangle.y = projected.getY();
                    }
                    if (rectangle.z < projected.getX()) {
                        rectangle.z = projected.getX();
                    }
                    if (rectangle.w < projected.getY()) {
                        rectangle.w = projected.getY();
                    }
                }
            }

            entityPositions.put(player, new Pair<>(rectangle, visible));
        }
    }

    @Subscribe
    public void onRender2D(EventRender2D event) {
        if (mc.player == null || mc.world == null) return;
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        boolean hasVisibleEntities = entityPositions.entrySet().stream()
                .anyMatch(entry -> entry.getValue().getRight());
                
        if (entityPositions.isEmpty() || !hasVisibleEntities) return;

        for (Map.Entry<Entity, Pair<Rectangle, Boolean>> entry : entityPositions.entrySet()) {
            Pair<Rectangle, Boolean> pair = entry.getValue();
            if (!pair.getRight()) continue;
            
            PlayerEntity player = (PlayerEntity) entry.getKey();
            if (player == mc.player && mc.options.getPerspective() == Perspective.FIRST_PERSON) continue;
            if (!player.isPartOfGame() || player.isRemoved() || player.isSpectator()) continue;

            float distance = mc.player.distanceTo(player);
            if (distance > range.getValue()) continue;

            renderNameTag(event, player, pair.getLeft(), distance);
        }
    }
    
    private void renderNameTag(EventRender2D event, PlayerEntity player, Rectangle rect, float distance) {
        float currentScale = calculateScale(distance);
        
        float centerX = (float) ((rect.x + rect.z) / 2f);
        float y = (float) (rect.y - player.getHeight() - 10f * currentScale);

        float fontSize = 9 * currentScale;
        float width = Skate.fonts.getArial().getWidth(player.getName().getString(), fontSize);
        float height = 10 * currentScale;
        float padding = 2 * currentScale;
        
        event.getMatrixStack().push();
        event.getMatrixStack().translate(0, 0, -distance * 0.1);

        RenderUtil.drawRect(
                event.getMatrixStack(),
                centerX - width / 2 - padding,
                y - padding,
                centerX + width / 2 + padding,
                y + height,
                backgroundColor
        );
        
        if (healthBar.getValue()) {
            renderHealthBar(event, player, centerX, y, width, height, padding, currentScale);
        }
        
        Skate.fonts.getArial().renderWithShadow(
                event.getMatrixStack(),
                player.getName().getString(),
                centerX - Skate.fonts.getArial().getWidth(player.getName().getString(), fontSize) / 2,
                y - (healthBar.getValue() ? padding : currentScale),
                fontSize,
                ColorConfig.TEXT_WHITE.getRGB()
        );
        
        event.getMatrixStack().pop();
    }
    
    private void renderHealthBar(EventRender2D event, PlayerEntity player, float centerX, float y, 
                               float width, float height, float padding, float currentScale) {
        float healthPercent = player.getHealth() / player.getMaxHealth();
        float healthWidth = width * healthPercent;
        float healthBarHeight = 2 * currentScale;
        
        RenderUtil.drawRect(
                event.getMatrixStack(),
                centerX - width / 2 - padding,
                y + height - healthBarHeight,
                centerX + width / 2 + padding,
                y + height,
                backgroundColor
        );
        
        RenderUtil.drawRect(
                event.getMatrixStack(),
                centerX - width / 2 - padding,
                y + height - healthBarHeight,
                centerX - width / 2 - padding + healthWidth + (padding * 2),
                y + height,
                healthBarColor
        );
    }
    
    private float calculateScale(float distance) {
        if (dynamicScaling.getValue()) {
            float baseScale = scale.getValue() / 100f;
            float distanceFactor = 1.0f - (distance / range.getValue());
            return Math.max(baseScale * distanceFactor, 0.5f * baseScale);
        } else {
            return scale.getValue() / 100f;
        }
    }

    public static class Rectangle {
        public double x;
        public double y;
        public double z;
        public double w;

        public Rectangle(double x, double y, double z, double w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }
    }
}
