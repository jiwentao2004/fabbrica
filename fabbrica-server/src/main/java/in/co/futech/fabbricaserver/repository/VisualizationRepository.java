package in.co.futech.fabbricaserver.repository;

import in.co.futech.fabbricaserver.model.Visualization;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisualizationRepository extends PagingAndSortingRepository<Visualization, String> {
    Optional<Visualization> findByCode(String code);
}