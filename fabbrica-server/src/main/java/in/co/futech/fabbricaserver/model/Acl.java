package in.co.futech.fabbricaserver.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "acl")
public class Acl extends AbstractDocument {

    private String username;
    private String cliendid;
    private List<String> publish;
    private List<String> subscribe;
    private List<String> pubsub;
    
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCliendid() {
        return this.cliendid;
    }

    public void setCliendid(String cliendid) {
        this.cliendid = cliendid;
    }

    public List<String> getPublish() {
        return this.publish;
    }

    public void setPublish(List<String> publish) {
        this.publish = publish;
    }

    public List<String> getSubscribe() {
        return this.subscribe;
    }

    public void setSubscribe(List<String> subscribe) {
        this.subscribe = subscribe;
    }

    public List<String> getPubsub() {
        return this.pubsub;
    }

    public void setPubsub(List<String> pubsub) {
        this.pubsub = pubsub;
    }

}