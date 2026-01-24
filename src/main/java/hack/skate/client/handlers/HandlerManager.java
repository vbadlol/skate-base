package hack.skate.client.handlers;

import java.util.ArrayList;

import hack.skate.client.Skate;
import hack.skate.client.handlers.impl.KeyHandler;

public class HandlerManager {
    private final ArrayList<Handler> handlers = new ArrayList<>();

    public void initialize() {
        handlers.add(new KeyHandler());

        for (Handler handler : handlers) {
            Skate.EVENT_BUS.register(handler);
        }
    }
}