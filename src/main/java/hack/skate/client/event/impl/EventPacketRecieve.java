package hack.skate.client.event.impl;

import hack.skate.client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.packet.Packet;

@Getter
@AllArgsConstructor
public class EventPacketRecieve extends Event {
    private Packet<?> packet;
}
