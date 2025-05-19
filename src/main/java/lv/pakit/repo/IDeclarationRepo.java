package lv.pakit.repo;

import lv.pakit.model.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDeclarationRepo extends JpaRepository<Declaration, Long> {

    List<Declaration> findByIdentifierCodeContainingIgnoreCase(String identifierCode);
    List<Declaration> findByClientFullNameContainingIgnoreCase(String clientName);
    List<Declaration> findBySenderNameContainingIgnoreCase(String senderName);
    List<Declaration> findBySenderAddressContainingIgnoreCase(String senderAddress);
    List<Declaration> findBySenderCountryCodeContainingIgnoreCase(String senderCountryCode);
    List<Declaration> findBySenderPhoneNumberContainingIgnoreCase(String senderPhoneNumber);
    List<Declaration> findByReceiverNameContainingIgnoreCase(String receiverName);
    List<Declaration> findByReceiverAddressContainingIgnoreCase(String receiverAddress);
    List<Declaration> findByReceiverCountryCodeContainingIgnoreCase(String receiverCountryCode);
    List<Declaration> findByReceiverPhoneNumberContainingIgnoreCase(String receiverPhoneNumber);
    List<Declaration> findByTotalWeight(double totalWeight);
    List<Declaration> findByTotalValue(double totalValue);
    List<Declaration> findByDateContainingIgnoreCase(String date);

    //TODO add JPA index
}

