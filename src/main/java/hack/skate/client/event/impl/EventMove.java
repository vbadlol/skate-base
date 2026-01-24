package hack.skate.client.event.impl;

import hack.skate.client.event.Event;
import hack.skate.client.event.EventPhase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EventMove extends Event {
    private EventPhase phase;
    private double x, y, z;
    private boolean onGround;
    private double yaw, pitch;
}
