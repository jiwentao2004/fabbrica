package in.co.futech.fabbricaserver;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import in.co.futech.fabbricaserver.model.Role;
import in.co.futech.fabbricaserver.model.User;
import in.co.futech.fabbricaserver.repository.UserRepository;

@SpringBootApplication
public class FabbricaServerApplication implements CommandLineRunner {

	public static final Logger logger = LoggerFactory.getLogger(FabbricaServerApplication.class);

    private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;
	
	@Autowired
    public FabbricaServerApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

	public static void main(String[] args) {
		SpringApplication.run(FabbricaServerApplication.class, args);
	}

	@Override
    public void run(String... args) throws Exception {
		logger.info("Inside Run");
        if (!this.userRepository.findByUsername("administrator").isPresent()) {
			logger.info("Inside Run Id");
            User user = new User("Administrator", "administrator", passwordEncoder.encode("Bbs199509"), Collections.singletonList(Role.ADMIN));
            this.userRepository.save(user);
        }
    }

}
