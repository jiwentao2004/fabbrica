package in.co.futech.fabbricaserver.controller;

import in.co.futech.fabbricaserver.model.MachineModel;
import in.co.futech.fabbricaserver.repository.MachineModelRepository;
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

import java.util.List;

@RepositoryRestController
@RequestMapping(path = "/machineModels")
public class MachineModelController {

    private MachineModelRepository machineModelRepository;
    private MongoTemplate mongoTemplate;

    public MachineModelController(MachineModelRepository machineModelRepository, MongoTemplate mongoTemplate) {
        this.machineModelRepository = machineModelRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Secured({ "ADMIN" })
    @PostMapping()
    public @ResponseBody MachineModel addMachineModel(@RequestBody MachineModel machineModel) {
        return machineModelRepository.save(machineModel);
    }

    @Secured({ "ADMIN" })
    @PutMapping("/{id}")
    public ResponseEntity<MachineModel> saveMachineModel(@PathVariable String id, @RequestBody MachineModel machineModel) {
        if (machineModelRepository.existsById(id)) {
            machineModelRepository.save(machineModel);
            return new ResponseEntity<>(machineModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping()
    public ResponseEntity<List<MachineModel>> getMachineModels(@RequestParam String filters, @RequestParam Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam String sort) {
        Pageable pageable = QueryBuilder.buildPageable(page, size, sort);
        Query query = QueryBuilder.buildQuery(filters, pageable);
        List<MachineModel> machineModels = this.mongoTemplate.find(query, MachineModel.class);
        Page<MachineModel> pages = PageableExecutionUtils.getPage(machineModels, pageable,
                () -> mongoTemplate.count(query, MachineModel.class));
        if (page > pages.getTotalPages()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("X-Total-Count", pages.getTotalElements() + "");
            return new ResponseEntity<List<MachineModel>>(pages.getContent(), responseHeaders, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineModel> getMachineModel(@PathVariable String id) {
        return machineModelRepository.findById(id)
                .map(machineModel -> new ResponseEntity<>(machineModel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}