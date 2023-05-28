package com.execute.protocol.app.models;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.dto.ThingDto;
import com.execute.protocol.core.entities.Event;
import com.execute.protocol.core.entities.Target;
import com.execute.protocol.core.entities.Thing;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameProducer {
    private Target target;
    private EventDto event;
    private Set<ThingDto> things;
    private Byte deadShans;
}
