package in.co.futech.fabbricaserver.util;

import in.co.futech.fabbricaserver.model.User;
import in.co.futech.fabbricaserver.repository.UserRepository;
import in.co.futech.fabbricaserver.service.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Auditor implements AuditorAware<User> {
    @Autowired
    private UserRepository userRepository;
    public Optional<User> getCurrentAuditor() {
        try {
            UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userRepository.findByUsername(userDetail.getUsername());
        }
        catch (NullPointerException ex) {
            return Optional.empty();
        }
    }
}
