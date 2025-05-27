package lv.pakit.repo;

import lv.pakit.model.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeclarationRepo extends JpaRepository<Declaration, Long>, JpaSpecificationExecutor<Declaration> {

}