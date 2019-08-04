package in.co.futech.fabbricaserver.controller;

import in.co.futech.fabbricaserver.model.User;
import in.co.futech.fabbricaserver.repository.UserRepository;
import in.co.futech.fabbricaserver.util.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RepositoryRestController
@RequestMapping(path = "/users")
public class UserController {

    private UserRepository userRepository;
    private MongoTemplate mongoTemplate;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Secured({ "ADMIN" })
    @PostMapping()
    public @ResponseBody User addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Secured({ "ADMIN" })
    @PutMapping("/{id}")
    public ResponseEntity<User> saveUser(@PathVariable String id, @RequestBody User user) {
        if (userRepository.existsById(id)) {
            if(user.getPassword()!= null && !user.getPassword().equals("")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            else {
                User oldUser = userRepository.findById(user.getId()).get();
                user.setPassword(oldUser.getPassword());
            }
            userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers(@RequestParam String filters, @RequestParam Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam String sort) {
        Pageable pageable = QueryBuilder.buildPageable(page, size, sort);
        Query query = QueryBuilder.buildQuery(filters, pageable);
        List<User> users = this.mongoTemplate.find(query, User.class);
        Page<User> pages = PageableExecutionUtils.getPage(users, pageable,
                () -> mongoTemplate.count(query, User.class));
        if (page > pages.getTotalPages()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("X-Total-Count", pages.getTotalElements() + "");
            return new ResponseEntity<List<User>>(pages.getContent(), responseHeaders, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return userRepository.findById(id).map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}