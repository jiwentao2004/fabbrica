package in.co.futech.fabbricaserver.repository;

import in.co.futech.fabbricaserver.model.Tenant;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends PagingAndSortingRepository<Tenant, String> {
    Optional<Tenant> findByCode(String coode);
}