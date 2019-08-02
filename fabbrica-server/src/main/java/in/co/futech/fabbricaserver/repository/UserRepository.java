package in.co.futech.fabbricaserver.repository;

import in.co.futech.fabbricaserver.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {
    Optional<User> findByUsername(String username);
}
