package in.co.futech.fabbricaserver;

import java.util.Collections;

import in.co.futech.fabbricaserver.model.Acl;
import in.co.futech.fabbricaserver.repository.AclRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import in.co.futech.fabbricaserver.model.Role;
import in.co.futech.fabbricaserver.model.User;
import in.co.futech.fabbricaserver.repository.UserRepository;

@SpringBootApplication
@ComponentScan("in.co.futech.fabbricaserver")
public class FabbricaServerApplication implements CommandLineRunner {

    public static final Logger logger = LoggerFactory.getLogger(FabbricaServerApplication.class);

    private final UserRepository userRepository;

    private final AclRepository aclRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FabbricaServerApplication(UserRepository userRepository, AclRepository aclRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.aclRepository = aclRepository;
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
            User user = new User("Administrator", "administrator", passwordEncoder.encode("Bbs199509"),
                    Collections.singletonList(Role.ADMIN));
            this.userRepository.save(user);
        }
        if (!this.userRepository.findByUsername("telegraf").isPresent()) {
            User user = new User("Telegraf", "telegraf", passwordEncoder.encode("telegraf"),
                    Collections.singletonList(Role.ADMIN));
            this.userRepository.save(user);
        }
        if (!this.aclRepository.existsByUsernameAndClientid("telegraf", "telegraf")){
            Acl acl = new Acl("telegraf", "telegraf", null, null, Collections.singletonList("#"));
            this.aclRepository.save(acl);
        }
    }

}
