package in.co.futech.fabbricaserver.service;

import in.co.futech.fabbricaserver.model.Role;
import in.co.futech.fabbricaserver.model.User;
import in.co.futech.fabbricaserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("UserDetailService")
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> dbUser = this.userRepository.findByUsername(username);

        if (dbUser.isPresent()) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();

            for (Role role : dbUser.get().getRoles()) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role.name());
                grantedAuthorities.add(authority);
            }
            UserDetail userDetail = new UserDetail();
            User user = dbUser.get();
            user.setTenants(null);
            userDetail.setUser(dbUser.get());
            userDetail.setAuthorities(grantedAuthorities);
            return userDetail;
        } else {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
    }
}
