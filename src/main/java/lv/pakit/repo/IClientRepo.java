package lv.pakit.repo;

import lv.pakit.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClientRepo extends JpaRepository<Client, Long> {
}
