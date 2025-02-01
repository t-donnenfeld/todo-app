package ch.cern.todo.mapper;

import ch.cern.todo.model.UserModel;
import ch.cern.todo.openapi.model.CreateUserRequest;
import ch.cern.todo.openapi.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", uses = PasswordEncoder.class)
public abstract class UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(createUserRequest.getPassword()))")
    @Mapping(target = "role", constant = "USER")
    public abstract UserModel map(CreateUserRequest createUserRequest);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "id", source = "id")
    public abstract User map(UserModel userModel);

}
