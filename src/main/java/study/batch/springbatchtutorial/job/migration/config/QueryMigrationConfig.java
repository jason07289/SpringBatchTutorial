package study.batch.springbatchtutorial.job.migration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.batch.springbatchtutorial.core.domain.accounts.Accounts;
import study.batch.springbatchtutorial.core.domain.orders.Orders;

import javax.sql.DataSource;


/**
 * desc: 주문 테이블 -> 정산 테이블 데이터 이관 query write
 * program args: --spring.batch.job.names=queryMigrationJob
 */
@Configuration
@RequiredArgsConstructor
public class QueryMigrationConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    public static final int CHUNK_SIZE = 5;

    @Bean
    public Job queryMigrationJob(Step queryMigrationStep) {
        return jobBuilderFactory.get("queryMigrationJob")
                //RunIdIncrementer에서 job에 대한 id가 나온다.
                .incrementer(new RunIdIncrementer())
                .start(queryMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step queryMigrationStep(ItemReader jpaOrdersReader, ItemProcessor jpaOrdersProcessor, ItemWriter queryAccountsWriter) {
        return stepBuilderFactory.get("queryMigrationStep")
                .<Orders, Accounts>chunk(CHUNK_SIZE)
                .reader(jpaOrdersReader)
                .processor(jpaOrdersProcessor)
                .writer(queryAccountsWriter)
                .build();
    }


}
