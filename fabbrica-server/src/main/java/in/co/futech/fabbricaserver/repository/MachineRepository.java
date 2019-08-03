package in.co.futech.fabbricaserver.repository;

import in.co.futech.fabbricaserver.model.Machine;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MachineRepository extends PagingAndSortingRepository<Machine, String> {
    Optional<Machine> findByCode(String code);
}