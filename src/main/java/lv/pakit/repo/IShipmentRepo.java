package lv.pakit.repo;

import lv.pakit.model.shipment.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IShipmentRepo extends JpaRepository<Shipment, Long>, JpaSpecificationExecutor<Shipment> {
}
