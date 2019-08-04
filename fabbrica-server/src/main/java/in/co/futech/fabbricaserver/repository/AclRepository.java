package in.co.futech.fabbricaserver.repository;

import in.co.futech.fabbricaserver.model.Acl;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AclRepository extends PagingAndSortingRepository<Acl, String>  {
    List<Acl> findByUsernameAndClientid(String username, String clientid);
    boolean existsByUsernameAndClientid(String username, String clientid);
}
