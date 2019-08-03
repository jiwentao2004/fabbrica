package in.co.futech.fabbricaserver.repository;

import in.co.futech.fabbricaserver.model.MachineModel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MachineModelRepository extends PagingAndSortingRepository<MachineModel, String> {
    Optional<MachineModel> findByCode(String code);
}