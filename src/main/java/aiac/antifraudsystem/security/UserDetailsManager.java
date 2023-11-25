package aiac.antifraudsystem.security;

import aiac.antifraudsystem.model.AppUser;
import aiac.antifraudsystem.model.AppUserDetails;
import aiac.antifraudsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsManager implements UserDetailsService {

    private final UserRepository repository;
    private final static String ROLE_PREFIX = "ROLE_";

    @Autowired
    public UserDetailsManager(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> opUser = repository.findByUserDetailsUsernameIgnoreCase(username);
        if (opUser.isPresent()) {
            AppUserDetails userDetails = opUser.get().getUserDetails();
            return new User(userDetails.getUsername(), userDetails.getPassword(),
                    true, true, true, userDetails.isNonLocked(),
                    List.of(new SimpleGrantedAuthority(ROLE_PREFIX + userDetails.getRole())));
        } else {
            throw new UsernameNotFoundException("Not found");
        }
    }
}
