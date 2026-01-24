package hack.skate.client.render;

import hack.skate.client.Skate;
import hack.skate.client.utils.Imports;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.Uniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class Shaders implements SimpleSynchronousResourceReloadListener, Imports {
    public static ShaderProgram MSDF;
    public static Uniform msdfPxrange;
    
    private static boolean initialized = false;
    
    static {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new Shaders());
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(Skate.MOD_ID, "reload_shaders");
    }

    @Override
    public void reload(ResourceManager manager) {
        load();
    }

    public static void load() {
        try {
            MSDF = mc.getShaderLoader().getOrCreateProgram(
                    new ShaderProgramKey(
                            Identifier.of(Skate.MOD_ID, "core/msdf"),
                            VertexFormats.POSITION_TEXTURE_COLOR,
                            Defines.EMPTY
                    )
            );

            msdfPxrange = MSDF.getUniform("pxRange");
            
            initialized = true;
        } catch (Exception e) {
            System.err.println("Failed to load shaders: " + e.getMessage());
        }
    }
    
    public static void init() {
        load();
    }
    
    public static boolean isInitialized() {
        return initialized;
    }
}