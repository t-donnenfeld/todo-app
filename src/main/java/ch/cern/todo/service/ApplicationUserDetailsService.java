package ch.cern.todo.service;

import ch.cern.todo.domain.AuthenticatedUser;
import ch.cern.todo.error.IncorrectUsernameOrPasswordException;
import ch.cern.todo.model.UserModel;
import ch.cern.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IncorrectUsernameOrPasswordException();
        }
        return new AuthenticatedUser(user);
    }
}
