package lv.pakit.repo;

import lv.pakit.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClientRepo extends JpaRepository<Client, Long> {
}
