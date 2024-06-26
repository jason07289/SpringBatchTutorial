package study.batch.springbatchtutorial.job.migration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.batch.springbatchtutorial.core.domain.accounts.Accounts;
import study.batch.springbatchtutorial.core.domain.orders.Orders;

/**
 * desc: 주문 테이블 -> 정산 테이블 데이터 이관
 * program args: --spring.batch.job.names=jpaMigrationJob
 */
@Configuration
@RequiredArgsConstructor
public class JpaMigrationConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static final int CHUNK_SIZE = 5;
    @Bean
    public Job jpaMigrationJob(Step jpaMigrationStep) {
        return jobBuilderFactory.get("jpaMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(jpaMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step jpaMigrationStep(ItemReader jpaOrdersReader, ItemProcessor jpaOrdersProcessor, ItemWriter jpaAccountsCustomWriter) {
        return stepBuilderFactory.get("jpaMigrationStep")
                //<I: input, O:output> input으로 읽어와서 output으로 job내에서 사용된다.
                .<Orders, Accounts>chunk(CHUNK_SIZE)
                .reader(jpaOrdersReader)
                .processor(jpaOrdersProcessor)
                //id 그대로 유지할 수 있도록 entityManager 직접 접근
                .writer(jpaAccountsCustomWriter)
                .build();
    }

}
