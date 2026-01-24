package hack.skate.client.render.font;

import lombok.Getter;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;

import hack.skate.client.Skate;

public class Fonts implements SimpleSynchronousResourceReloadListener {
    @Getter
    private FontAtlas arial;

    public Fonts() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this);
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(Skate.MOD_ID, "reload_fonts");
    }

    @Override
    public void reload(final ResourceManager manager) {
        try {
            this.arial = new FontAtlas(manager, "arial");
        } catch (final IOException exception) {
            throw new RuntimeException("Couldn't load fonts", exception);
        }
    }
}