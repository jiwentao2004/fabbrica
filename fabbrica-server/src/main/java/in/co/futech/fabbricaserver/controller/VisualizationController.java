package in.co.futech.fabbricaserver.controller;

import in.co.futech.fabbricaserver.model.Factory;
import in.co.futech.fabbricaserver.model.Machine;
import in.co.futech.fabbricaserver.model.User;
import in.co.futech.fabbricaserver.model.Visualization;
import in.co.futech.fabbricaserver.repository.FactoryRepository;
import in.co.futech.fabbricaserver.repository.MachineRepository;
import in.co.futech.fabbricaserver.repository.VisualizationRepository;
import in.co.futech.fabbricaserver.service.UserDetail;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RepositoryRestController
@RequestMapping(path = "/visualizations")
public class VisualizationController {

    private VisualizationRepository visualizationRepository;
    private FactoryRepository factoryRepository;
    private MachineRepository machineRepository;
    private MongoTemplate mongoTemplate;

    public VisualizationController(VisualizationRepository visualizationRepository, FactoryRepository factoryRepository, MachineRepository machineRepository, MongoTemplate mongoTemplate) {
        this.visualizationRepository = visualizationRepository;
        this.factoryRepository = factoryRepository;
        this.machineRepository = machineRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Secured({ "ADMIN" })
    @PostMapping()
    public @ResponseBody Visualization addVisualization(@RequestBody Visualization visualization) {
        return visualizationRepository.save(visualization);
    }

    @Secured({ "ADMIN" })
    @PutMapping("/{id}")
    public ResponseEntity<Visualization> saveVisualization(@PathVariable String id, @RequestBody Visualization visualization) {
        if (visualizationRepository.existsById(id)) {
            visualizationRepository.save(visualization);
            return new ResponseEntity<>(visualization, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping()
    public ResponseEntity<List<Visualization>> getVisualizations(@RequestParam String filters, @RequestParam Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam String sort) {
        Pageable pageable = QueryBuilder.buildPageable(page, size, sort);
        Query query = QueryBuilder.buildQuery(filters, pageable);
        List<Visualization> visualizations = this.mongoTemplate.find(query, Visualization.class);
        Page<Visualization> pages = PageableExecutionUtils.getPage(visualizations, pageable,
                () -> mongoTemplate.count(query, Visualization.class));
        if (page > pages.getTotalPages()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("X-Total-Count", pages.getTotalElements() + "");
            return new ResponseEntity<List<Visualization>>(pages.getContent(), responseHeaders, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visualization> getVisualization(@PathVariable String id) {
        return visualizationRepository.findById(id).map(visualization -> new ResponseEntity<>(visualization, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Secured({ "USER" })
    @GetMapping("/machine")
    public ResponseEntity<Visualization> getMachineVisualization() {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetail.getUser();
        if(user.getTenants()!=null) {
            List<Factory> factories = factoryRepository.findByTenantIn(user.getTenants());
            List<Machine> machines = machineRepository.findByFactoryIn(factories);
            if(machines!=null) {

            }
        }
        return new ResponseEntity(Collections.EMPTY_LIST,HttpStatus.OK);
    }
}