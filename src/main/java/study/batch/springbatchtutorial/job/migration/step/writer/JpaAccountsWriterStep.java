package study.batch.springbatchtutorial.job.migration.step.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import study.batch.springbatchtutorial.core.domain.accounts.Accounts;
import study.batch.springbatchtutorial.core.domain.accounts.AccountsRepository;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class JpaAccountsWriterStep {
    private final AccountsRepository accountsRepository;
    private final EntityManager entityManager;

    /**
     * 실제 데이터 입력하는 부분
     * id autoIncrement 문제 고민좀 해봐야함.
     * @return
     */
    @StepScope
    @Bean
    public ItemWriter<Accounts> jpaAccountsCustomWriter(){
        return accounts ->
            accounts.forEach(accountsEach -> {
                entityManager.persist(accountsEach);
            });
    }

    /**
     * 실제 데이터 입력하는 부분
     * id autoIncrement 문제 고민좀 해봐야함.
     * @return
     */
    @StepScope
    @Bean
    public RepositoryItemWriter<Accounts> jpaAccountsRepositoryWriter(){
        return new RepositoryItemWriterBuilder<Accounts>()
                .repository(accountsRepository)
                //메소드 명 string으로 접근하기에 안좋아 보임.
                .methodName("save")
                .build();
    }
}
