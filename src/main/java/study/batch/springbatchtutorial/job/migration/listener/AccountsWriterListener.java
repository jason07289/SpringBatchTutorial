package study.batch.springbatchtutorial.job.migration.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import study.batch.springbatchtutorial.core.domain.accounts.Accounts;
import study.batch.springbatchtutorial.core.domain.process.MigrationResult;
import study.batch.springbatchtutorial.core.domain.process.MigrationResultRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountsWriterListener implements ItemWriteListener<Accounts> {
    private final MigrationResultRepository repository;
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void beforeWrite(List<? extends Accounts> accountsList) {
        accountsList.forEach(accounts -> {
            MigrationResult migrationResult = repository.findByResourceId(accounts.getId())
                    .orElse(MigrationResult.create(accounts.getId()));
            migrationResult.resetTxTime();
            repository.save(migrationResult);
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void afterWrite(List<? extends Accounts> accountsList) {
        accountsList.forEach(accounts -> {
            MigrationResult migrationResult = repository.findByResourceId(accounts.getId())
                    .orElseThrow(NoSuchElementException::new);
            migrationResult.isSuccess();
            repository.save(migrationResult);
        });

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onWriteError(Exception exception, List<? extends Accounts> accountsList) {
        accountsList.forEach(accounts -> {
            MigrationResult migrationResult = repository.findByResourceId(accounts.getId())
                    .orElseThrow(NoSuchElementException::new);
            migrationResult.isFail(exception.getMessage());
            repository.save(migrationResult);
        });
    }
}
