
package com.example.userserver.mappers;


import com.example.userserver.dtos.UserDto;
import com.example.userserver.models.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserDto dto);

    UserDto map(User entity);

    List<UserDto> map(List<User> users);

}
