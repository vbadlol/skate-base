package hack.skate.client.screens;

import hack.skate.client.screens.clickgui.ClickGuiScreen;
import hack.skate.client.utils.Imports;

public class ScreenManager implements Imports {
    private ClickGuiScreen clickGuiScreen;
    
    public void initialize() {
        clickGuiScreen = new ClickGuiScreen();
    }
    
    public void displayClickGUI() {
        mc.setScreen(clickGuiScreen);
    }
    
    public boolean isClickGUIOpen() {
        return mc.currentScreen == clickGuiScreen;
    }
    
    public void closeAllScreens() {
        mc.setScreen(null);
    }
}
