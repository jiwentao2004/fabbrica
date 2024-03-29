package in.co.futech.fabbricaserver.controller;

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

import in.co.futech.fabbricaserver.model.Factory;
import in.co.futech.fabbricaserver.repository.FactoryRepository;

import java.util.List;

@RepositoryRestController
@RequestMapping(path = "/factories")
public class FactoryController {

    private FactoryRepository factoryRepository;
    private MongoTemplate mongoTemplate;

    public FactoryController(FactoryRepository factoryRepository, MongoTemplate mongoTemplate) {
        this.factoryRepository = factoryRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Secured({ "ADMIN" })
    @PostMapping()
    public @ResponseBody Factory addFactory(@RequestBody Factory factory) {
        return factoryRepository.save(factory);
    }

    @Secured({ "ADMIN" })
    @PutMapping("/{id}")
    public ResponseEntity<Factory> saveFactory(@PathVariable String id, @RequestBody Factory factory) {
        if (factoryRepository.existsById(id)) {
            factoryRepository.save(factory);
            return new ResponseEntity<>(factory, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping()
    public ResponseEntity<List<Factory>> getFactories(@RequestParam String filters, @RequestParam Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam String sort) {
        Pageable pageable = QueryBuilder.buildPageable(page, size, sort);
        Query query = QueryBuilder.buildQuery(filters, pageable);
        List<Factory> factories = this.mongoTemplate.find(query, Factory.class);
        Page<Factory> pages = PageableExecutionUtils.getPage(factories, pageable,
                () -> mongoTemplate.count(query, Factory.class));
        if (page > pages.getTotalPages()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("X-Total-Count", pages.getTotalElements() + "");
            return new ResponseEntity<List<Factory>>(pages.getContent(), responseHeaders, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factory> getFactory(@PathVariable String id) {
        return factoryRepository.findById(id).map(factory -> new ResponseEntity<>(factory, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}