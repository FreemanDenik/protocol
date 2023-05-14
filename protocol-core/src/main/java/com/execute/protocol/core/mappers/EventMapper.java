package com.execute.protocol.core.mappers;

import com.execute.protocol.core.dto.AnswerDto;
import com.execute.protocol.core.dto.EventDto;
import com.execute.protocol.core.entities.Answer;
import com.execute.protocol.core.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper/*(uses = AgeTranslator.class)*/
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    /**
     * @return мэппинг User в DTO
     */

   // @Mapping(target = "age", source = "birthday")
    EventDto mapEventToDto(Event event);
//    /**
//     * @return мэппинг UserDto в User
//     */
    //@Mapping(target = "birthday", source = "age")
    //Event mapEventFromDto(EventDto eventDto);
}
