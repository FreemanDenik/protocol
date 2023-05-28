package com.execute.protocol.core.services;

import com.execute.protocol.core.dto.ThingDto;
import com.execute.protocol.core.entities.Thing;

import java.util.Set;

public interface ThingService {
    Set<Thing> getThings(Set<Integer> idNumbers);

}
