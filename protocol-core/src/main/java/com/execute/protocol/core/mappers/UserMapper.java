package com.execute.protocol.core.mappers;

import com.example.protocol.dto.UserDto;
import com.execute.protocol.core.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = AgeTranslator.class)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * @return мэппинг User в DTO
     */

    @Mapping(target = "age", source = "birthday")
    UserDto mapUserToDto(User user);

    /**
     * @return мэппинг UserDto в User
     */
    @Mapping(target = "birthday", source = "age")
    User mapUserFromDto(UserDto userDto);
}
