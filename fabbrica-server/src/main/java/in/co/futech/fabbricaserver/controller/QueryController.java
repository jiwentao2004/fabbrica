package in.co.futech.fabbricaserver.controller;

import in.co.futech.fabbricaserver.model.Visualization;
import in.co.futech.fabbricaserver.repository.VisualizationRepository;
import in.co.futech.fabbricaserver.service.InfluxService;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
@BasePathAwareController
@RequestMapping(path = "/query")
public class QueryController {

    @Autowired
    private InfluxService influxService;

    @Autowired
    private VisualizationRepository visualizationRepository;


    @GetMapping()
    public ResponseEntity<QueryResult> getQuery(@RequestParam String visualizationId, @RequestParam String fromTime, @RequestParam String toTime){
        Optional<Visualization> optionalVisualization = visualizationRepository.findById(visualizationId);
        if(optionalVisualization.isPresent()){
             Visualization visualization = optionalVisualization.get();
             QueryResult queryResult = influxService.query(visualization.getQuery(), fromTime, toTime, visualization.getInterval(), visualization.getFill() );
             return new ResponseEntity<QueryResult>(queryResult, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<QueryResult>(HttpStatus.NOT_FOUND);
        }
    }
}
