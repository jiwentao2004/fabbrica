package in.co.futech.fabbricaserver.controller;

import in.co.futech.fabbricaserver.util.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import in.co.futech.fabbricaserver.model.Tenant;
import in.co.futech.fabbricaserver.repository.TenantRepository;

import java.util.List;

@RepositoryRestController
@RequestMapping(path = "/tenants")
public class TenantController {

    private TenantRepository tenantRepository;
    private MongoTemplate mongoTemplate;

    public TenantController(TenantRepository tenantRepository, MongoTemplate mongoTemplate) {
        this.tenantRepository = tenantRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Secured({ "ADMIN" })
    @PostMapping()
    public @ResponseBody Tenant addTenant(@RequestBody Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    @Secured({ "ADMIN" })
    @PutMapping("/{id}")
    public ResponseEntity<Tenant> saveTenant(@PathVariable String id, @RequestBody Tenant tenant) {
        if (tenantRepository.existsById(id)) {
            tenantRepository.save(tenant);
            return new ResponseEntity<>(tenant, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping()
    public ResponseEntity<List<Tenant>> getTenants(@RequestParam String filters, @RequestParam Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam String sort) {
        Pageable pageable = QueryBuilder.buildPageable(page, size, sort);
        Query query = QueryBuilder.buildQuery(filters, pageable);
        List<Tenant> tenants = this.mongoTemplate.find(query, Tenant.class);
        Page<Tenant> pages = PageableExecutionUtils.getPage(tenants, pageable,
                () -> mongoTemplate.count(query, Tenant.class));
        if (page > pages.getTotalPages()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("X-Total-Count", pages.getTotalElements() + "");
            return new ResponseEntity<List<Tenant>>(pages.getContent(), responseHeaders, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenant(@PathVariable String id) {
        return tenantRepository.findById(id).map(tenant -> new ResponseEntity<>(tenant, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}