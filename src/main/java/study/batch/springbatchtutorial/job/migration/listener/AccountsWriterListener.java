package study.batch.springbatchtutorial.job.migration.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;
import study.batch.springbatchtutorial.core.domain.accounts.Accounts;
import study.batch.springbatchtutorial.core.domain.process.MigrationProcess;
import study.batch.springbatchtutorial.core.domain.process.MigrationProcessRepository;

import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountsWriterListener implements ItemWriteListener<Accounts> {
    private final MigrationProcessRepository repository;
    @Override
    public void beforeWrite(List<? extends Accounts> list) {
        list.forEach(item -> System.out.println("beforeWrite:  " + item.getOrderItem()));
    }

    @Override
    public void afterWrite(List<? extends Accounts> list) {

    }

    @Override
    public void onWriteError(Exception e, List<? extends Accounts> list) {

    }
}
