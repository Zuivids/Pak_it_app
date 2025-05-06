package lv.pakit.repo;

import lv.pakit.model.PackageItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPackageItemRepo extends JpaRepository<PackageItem, Long> {

}
