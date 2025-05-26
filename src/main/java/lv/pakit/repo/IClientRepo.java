package lv.pakit.repo;

import lv.pakit.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClientRepo extends JpaRepository<Client, Long> {

    Optional<Client> findByUsername(String username);
}
