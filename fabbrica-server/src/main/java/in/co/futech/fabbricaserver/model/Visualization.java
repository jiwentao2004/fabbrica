package in.co.futech.fabbricaserver.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "visualizations")
public class Visualization extends AbstractDocument {
    @Indexed(unique = true)
    private String code;
    private String name;
    private DashboardType dashboardType;
    private VisualizationType type;
    private String query;
    private String interval;
    private String fill;
    private String options;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DashboardType getDashboardType() {
        return dashboardType;
    }

    public void setDashboardType(DashboardType dashboardType) {
        this.dashboardType = dashboardType;
    }

    public VisualizationType getType() {
        return type;
    }

    public void setType(VisualizationType type) {
        this.type = type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public enum DashboardType {
        MACHINE,
        FACTORY,
        TENANT
    }

    public enum  VisualizationType {
        LINE,
        AREA,
        COLUMN,
        BAR,
        MIXED,
        PIE,
        RADIALBAR,
        IMM
    }
}
