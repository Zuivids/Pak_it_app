package lv.pakit.repo;

import lv.pakit.model.PackageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPackageItemRepo extends JpaRepository<PackageItem, Long> {

    List<PackageItem> findByDeclarationDeclarationId(long declarationId);
}
