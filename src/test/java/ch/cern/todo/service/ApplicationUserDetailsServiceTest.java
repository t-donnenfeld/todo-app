package ch.cern.todo.service;

import ch.cern.todo.error.IncorrectUsernameOrPasswordException;
import ch.cern.todo.model.RoleEnum;
import ch.cern.todo.model.UserModel;
import ch.cern.todo.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class ApplicationUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @Test
    void loadUserByUsernameNonExistingUser() {
        Mockito.when(userRepository.findByUsername("non_existent_user")).thenReturn(null);
        ApplicationUserDetailsService service = new ApplicationUserDetailsService(userRepository);

        Assertions.assertThrows(IncorrectUsernameOrPasswordException.class, () -> service.loadUserByUsername("non_existent_user"));
    }

    @Test
    void loadUserByUsernameExistingUser() {
        Mockito.when(userRepository.findByUsername("existing_user")).thenReturn(getTestUserModel(RoleEnum.USER));
        ApplicationUserDetailsService service = new ApplicationUserDetailsService(userRepository);

        UserDetails undertest = service.loadUserByUsername("existing_user");

        Assertions.assertEquals("existing_user", undertest.getUsername());
        Assertions.assertEquals("password", undertest.getPassword());
        Assertions.assertEquals(1, undertest.getAuthorities().size());
        Assertions.assertEquals(1, undertest.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_USER")).count());
        Assertions.assertEquals(0, undertest.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).count());

    }

    @Test
    void loadUserByUsernameExistingAdminUser() {
        Mockito.when(userRepository.findByUsername("existing_user")).thenReturn(getTestUserModel(RoleEnum.ADMIN));
        ApplicationUserDetailsService service = new ApplicationUserDetailsService(userRepository);

        UserDetails undertest = service.loadUserByUsername("existing_user");

        Assertions.assertEquals("existing_user", undertest.getUsername());
        Assertions.assertEquals("password", undertest.getPassword());
        Assertions.assertEquals(2, undertest.getAuthorities().size());
        Assertions.assertEquals(1, undertest.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_USER")).count());
        Assertions.assertEquals(1, undertest.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).count());

    }

    private static UserModel getTestUserModel(RoleEnum roleEnum) {
        UserModel user = new UserModel();
        user.setUsername("existing_user");
        user.setPassword("password");
        user.setRole(roleEnum);
        user.setId(4L);
        return user;
    }

}