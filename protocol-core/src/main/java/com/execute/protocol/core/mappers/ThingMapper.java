package com.execute.protocol.core.mappers;

import com.execute.protocol.core.dto.ThingDto;
import com.execute.protocol.core.entities.Thing;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface ThingMapper {
    ThingMapper INSTANCE = Mappers.getMapper(ThingMapper.class);
    Set<ThingDto> mapThingToDto(Set<Thing> event);
}
