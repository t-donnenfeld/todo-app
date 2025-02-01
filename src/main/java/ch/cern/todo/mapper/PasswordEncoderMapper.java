package ch.cern.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", uses = PasswordEncoder.class)
public abstract class PasswordEncoderMapper {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Named("encodePassword")
    public String map(String password) {
        return passwordEncoder.encode(password);
    }
}
