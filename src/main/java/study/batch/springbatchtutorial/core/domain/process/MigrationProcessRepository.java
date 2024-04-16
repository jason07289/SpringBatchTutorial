package study.batch.springbatchtutorial.core.domain.process;

import org.springframework.data.jpa.repository.JpaRepository;
import study.batch.springbatchtutorial.core.domain.orders.Orders;

public interface MigrationProcessRepository extends JpaRepository<MigrationProcess, Long> {
}
