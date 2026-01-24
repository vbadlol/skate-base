package hack.skate.client.event.impl;

import hack.skate.client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventKey extends Event {
    private int key;
    private int action;
}
