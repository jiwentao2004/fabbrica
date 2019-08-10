package in.co.futech.fabbricaserver.service;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service("InfluxService")
public class InfluxService {
    @Value("${influx.url}")
    private String url;

    @Value("${influx.database}")
    private String database;

    @Value("${influx.retention}")
    private String retention;

    @Value("${influx.username}")
    private String username;

    @Value("${influx.password}")
    private String password;

    private InfluxDB influxDB;

    public InfluxService() {
        System.out.println("This is the constructor");
    }

    @PostConstruct
    public void initDB() {
        this.influxDB = InfluxDBFactory.connect(this.url);
    }

    public QueryResult query(String query, String fromTime, String toTime, String interval, String fill) {
        String queryString = String.format("SELECT %s from \"%s\".\"%s\".\"%s\" WHERE time > \'%s\' GROUP BY time(%s) FILL(0)", query, database, retention, "stats", fromTime, interval, fill);
        System.out.println(queryString);
        return influxDB.query(new Query(queryString));
    }
}
