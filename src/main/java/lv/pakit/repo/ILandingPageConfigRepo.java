package lv.pakit.repo;

import lv.pakit.model.LandingPageConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILandingPageConfigRepo extends JpaRepository<LandingPageConfig, String> {

    List<LandingPageConfig> findAllByOrderBySortOrderAsc();
}
