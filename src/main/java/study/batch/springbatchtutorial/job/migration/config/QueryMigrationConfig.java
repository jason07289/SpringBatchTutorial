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
import study.batch.springbatchtutorial.job.migration.listener.AccountsWriterListener;
import study.batch.springbatchtutorial.job.migration.listener.MigrationChunkListener;


/**
 * desc: 주문 테이블 -> 정산 테이블 데이터 이관 query write
 * program args: --spring.batch.job.names=queryMigrationJob for 1, --spring.batch.job.names=queryOrdersMigrationJob for 2
 */
@Configuration
@RequiredArgsConstructor
public class QueryMigrationConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    //Job parameter를 통해 동적으로 변경 할 수 있으면 좋다.
    public static int CHUNK_SIZE = 1;
    public static int SKIP_LIMIT = 100;

    @Bean
    /**
     * 1
     */
    public Job queryMigrationJob(Step queryMigrationStep) {
        return jobBuilderFactory.get("queryMigrationJob")
                //RunIdIncrementer에서 job에 대한 id가 나온다.
                .incrementer(new RunIdIncrementer())
                .start(queryMigrationStep)
                .build();
    }

    @Bean
    /**
     * 2
     */
    public Job queryOrdersMigrationJob(Step queryOrdersMigrationStep) {
        return jobBuilderFactory.get("queryOrdersMigrationJob")
                //RunIdIncrementer에서 job에 대한 id가 나온다.
                .incrementer(new RunIdIncrementer())
                .start(queryOrdersMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step queryMigrationStep(ItemReader jpaOrdersReader,
                                   ItemProcessor jpaOrdersProcessor,
                                   ItemWriter queryAccountsWriter,
                                   MigrationChunkListener chunkListener,
                                   AccountsWriterListener writerListener
                                   ) {
        return stepBuilderFactory.get("queryMigrationStep")
                .<Orders, Accounts>chunk(CHUNK_SIZE)
                .reader(jpaOrdersReader)
                .processor(jpaOrdersProcessor)
                .writer(queryAccountsWriter)
                .faultTolerant()
                .skipLimit(SKIP_LIMIT) // 최대 허용 스킵 횟수
                .skip(Exception.class) // 스킵할 예외 유형
                .listener(writerListener)
                .listener(chunkListener)
                .build();
    }

    @JobScope
    @Bean
    public Step queryOrdersMigrationStep(ItemReader jpaOrdersReader,
                                   ItemProcessor jpaOrdersProcessor,
                                   ItemWriter compositeOrdersItemWriter,
                                   MigrationChunkListener chunkListener,
                                   AccountsWriterListener writerListener
    ) {
        return stepBuilderFactory.get("queryOrdersMigrationStep")
                .<Orders, Orders>chunk(CHUNK_SIZE)
                .reader(jpaOrdersReader)
//                .processor()
                .writer(compositeOrdersItemWriter)
//                .faultTolerant()
//                .skipLimit(SKIP_LIMIT) // 최대 허용 스킵 횟수
//                .skip(Exception.class) // 스킵할 예외 유형
//                .listener(writerListener)
                .listener(chunkListener)
                .build();
    }


}
