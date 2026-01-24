package hack.skate.client;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hack.skate.client.event.EventBus;
import hack.skate.client.features.FeatureManager;
import hack.skate.client.handlers.HandlerManager;
import hack.skate.client.render.Shaders;
import hack.skate.client.render.font.Fonts;
import hack.skate.client.screens.ScreenManager;

public class Skate implements ModInitializer {
	public static final String MOD_ID = "skate";
	public static EventBus EVENT_BUS = new EventBus();
	public static final FeatureManager featureManager = new FeatureManager();
	public static final ScreenManager screenManager = new ScreenManager();
	public static final HandlerManager handlerManager = new HandlerManager();
	public static Fonts fonts;
;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Shaders.init();
		fonts = new Fonts();
		featureManager.initialize();
		screenManager.initialize();
		handlerManager.initialize();

		LOGGER.info("Initialized :3");
	}
}