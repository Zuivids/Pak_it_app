package lv.pakit.repo;

import lv.pakit.model.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommodityRepo extends JpaRepository<Commodity, Integer> {
}
