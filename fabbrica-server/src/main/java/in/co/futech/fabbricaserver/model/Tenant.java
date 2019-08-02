package in.co.futech.fabbricaserver.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document(collection = "tenants")
public class Tenant extends AbstractDocument {

    @Indexed(unique = true)
    @NotNull
    @NotEmpty
    @NotBlank
    private String code;
    @NotNull
    @NotEmpty
    @NotBlank
    @Indexed
    private String name;
    private List<Address> addresses;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

}