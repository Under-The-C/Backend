package LikeLion.UnderTheCBackend.repository;

import LikeLion.UnderTheCBackend.entity.B_Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<B_Payment, Long> {
    public Optional<B_Payment> findByMerchantUid(String merchantUid);
}
