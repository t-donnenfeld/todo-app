package ch.cern.todo.service;

import ch.cern.todo.error.UserAlreadyExistsException;
import ch.cern.todo.mapper.UserMapper;
import ch.cern.todo.model.UserModel;
import ch.cern.todo.openapi.model.CreateUserRequest;
import ch.cern.todo.openapi.model.User;
import ch.cern.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User createUser(CreateUserRequest createUserRequest) {
        if (userRepository.findByUsername(createUserRequest.getUsername()) != null) {
            throw new UserAlreadyExistsException("User " + createUserRequest.getUsername() + " already exists");
        }
        UserModel createdUser = userRepository.save(userMapper.map(createUserRequest));
        return userMapper.map(createdUser);
    }

}
