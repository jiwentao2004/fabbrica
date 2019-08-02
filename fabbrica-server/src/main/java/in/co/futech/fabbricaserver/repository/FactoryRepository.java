package in.co.futech.fabbricaserver.repository;

import in.co.futech.fabbricaserver.model.Factory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FactoryRepository extends PagingAndSortingRepository<Factory, String> {
    Optional<Factory> findByCode(String code);
}