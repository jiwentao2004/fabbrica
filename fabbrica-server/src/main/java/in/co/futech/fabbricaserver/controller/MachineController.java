package in.co.futech.fabbricaserver.controller;

import in.co.futech.fabbricaserver.model.Acl;
import in.co.futech.fabbricaserver.model.Machine;
import in.co.futech.fabbricaserver.model.User;
import in.co.futech.fabbricaserver.repository.AclRepository;
import in.co.futech.fabbricaserver.repository.MachineRepository;
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
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RepositoryRestController
@RequestMapping(path = "/machines")
public class MachineController {

    private MachineRepository machineRepository;
    private UserRepository userRepository;
    private AclRepository aclRepository;
    private MongoTemplate mongoTemplate;

    public MachineController(MachineRepository machineRepository, UserRepository userRepository, AclRepository aclRepository, MongoTemplate mongoTemplate) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.aclRepository = aclRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Secured({ "ADMIN" })
    @PostMapping()
    public @ResponseBody Machine addMachine(@RequestBody Machine machine) {
        machine.setClientid(UUID.randomUUID().toString());
        if(machine.getFactory() != null  && machine.getFactory().getTenant() != null ) {
            List<User> users = userRepository.findAllByTenants(machine.getFactory().getTenant());
            for (User user: users ){
                Acl acl = new Acl(user.getUsername(), machine.getClientid(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.singletonList(machine.getClientid()));
                aclRepository.save(acl);
            }
        }
        return machineRepository.save(machine);
    }

    @Secured({ "ADMIN" })
    @PutMapping("/{id}")
    public ResponseEntity<Machine> saveMachine(@PathVariable String id, @RequestBody Machine machine) {
        if (machineRepository.existsById(id)) {
            if(machine.getFactory() != null  && machine.getFactory().getTenant() != null ) {
                List<User> users = userRepository.findAllByTenants(machine.getFactory().getTenant());
                for (User user: users ){
                    if(!aclRepository.existsByUsernameAndClientid(user.getUsername(), machine.getClientid())) {
                        Acl acl = new Acl(user.getUsername(), machine.getClientid(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.singletonList(machine.getClientid()));
                        aclRepository.save(acl);
                    }
                }
            }
            machineRepository.save(machine);
            return new ResponseEntity<>(machine, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping()
    public ResponseEntity<List<Machine>> getMachines(@RequestParam String filters, @RequestParam Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam String sort) {
        Pageable pageable = QueryBuilder.buildPageable(page, size, sort);
        Query query = QueryBuilder.buildQuery(filters, pageable);
        List<Machine> machines = this.mongoTemplate.find(query, Machine.class);
        Page<Machine> pages = PageableExecutionUtils.getPage(machines, pageable,
                () -> mongoTemplate.count(query, Machine.class));
        if (page > pages.getTotalPages()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("X-Total-Count", pages.getTotalElements() + "");
            return new ResponseEntity<List<Machine>>(pages.getContent(), responseHeaders, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Machine> getMachine(@PathVariable String id) {
        return machineRepository.findById(id)
                .map(machine -> new ResponseEntity<>(machine, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMachine(@PathVariable String id) {
        if(machineRepository.existsById(id)) {
            machineRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}