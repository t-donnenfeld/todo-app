package ch.cern.todo.domain;

import ch.cern.todo.model.RoleEnum;
import ch.cern.todo.model.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
public class AuthenticatedUser implements UserDetails {

    private transient UserModel user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(createAuthority(RoleEnum.USER));
        if (user.getRole() == RoleEnum.ADMIN) {
            authorities.add(createAuthority(RoleEnum.ADMIN));
        }

        return authorities;
    }

    private GrantedAuthority createAuthority(RoleEnum role) {
        final String ROLE_PREFIX = "ROLE_";
        return new SimpleGrantedAuthority(ROLE_PREFIX + role.name());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
