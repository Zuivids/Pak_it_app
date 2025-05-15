package lv.pakit.repo;

import lv.pakit.model.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDeclarationRepo extends JpaRepository<Declaration, Long> {

    List<Declaration> findByIdentifierCodeContainingIgnoreCase(String identifierCode);
    //TODO add filter to all fields except declarationId
    //TODO add JPA index
}

