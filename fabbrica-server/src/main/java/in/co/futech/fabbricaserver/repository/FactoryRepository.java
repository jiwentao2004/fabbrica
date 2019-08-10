package in.co.futech.fabbricaserver.repository;

import in.co.futech.fabbricaserver.model.Factory;
import in.co.futech.fabbricaserver.model.Tenant;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FactoryRepository extends PagingAndSortingRepository<Factory, String> {
    Optional<Factory> findByCode(String code);
    List<Factory> findByTenantIn(List<Tenant> tenants);
}