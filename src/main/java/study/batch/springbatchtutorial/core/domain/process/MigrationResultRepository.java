package study.batch.springbatchtutorial.core.domain.process;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MigrationResultRepository extends JpaRepository<MigrationResult, Long> {
    Optional<MigrationResult> findByResourceId(Long resourceId);
}
