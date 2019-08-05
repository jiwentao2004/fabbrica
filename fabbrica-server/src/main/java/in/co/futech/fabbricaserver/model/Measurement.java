package in.co.futech.fabbricaserver.model;

import java.util.List;

public class Measurement {
    private String name;
    private int frequency;
    private List<MeasurementField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public List<MeasurementField> getFields() {
        return fields;
    }

    public void setFields(List<MeasurementField> fields) {
        this.fields = fields;
    }
}
