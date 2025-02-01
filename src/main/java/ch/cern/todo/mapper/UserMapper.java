package ch.cern.todo.mapper;

import ch.cern.todo.model.UserModel;
import ch.cern.todo.openapi.model.CreateUserRequest;
import ch.cern.todo.openapi.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public abstract class UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "role", constant = "USER")
    public abstract UserModel map(CreateUserRequest createUserRequest);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "id", source = "id")
    public abstract User map(UserModel userModel);

}
