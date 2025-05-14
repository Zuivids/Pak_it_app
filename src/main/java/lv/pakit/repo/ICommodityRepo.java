package lv.pakit.repo;

import lv.pakit.model.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommodityRepo extends JpaRepository<Commodity, Long> {
    List<Commodity> findByCommodityCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String commodityCode, String description);
}
