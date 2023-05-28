package com.execute.protocol.core.mappers;

import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Event;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = EventTranslator.class)
public interface EventMapper {
    //EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    /**
     * @return мэппинг User в DTO
     */

   // @Mapping(target = "age", source = "birthday")
//    @Mapping(target = "image", ignore = true)
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EventDto mapEventToDto(Event event);
//    /**
//     * @return мэппинг UserDto в User
//     */
    //@Mapping(target = "birthday", source = "age")
    //Event mapEventFromDto(EventDto eventDto);
}
