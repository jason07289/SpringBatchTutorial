package study.batch.springbatchtutorial.job.migration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Configuration;

/**
 * desc: 주문 테이블 -> 정산 테이블 데이터 이관 query write, 이후 성공, 실패에 따른 분기
 * program args: --spring.batch.job.names=queryMigrationMultiStepConfig
 */
@Configuration
@RequiredArgsConstructor
public class QueryMigrationMultiStepConfig {

    private final JobBuilderFactory jobBuilderFactory;

//    @Bean
    public Job queryMigrationJob(Step queryMigrationStep) {
        return jobBuilderFactory.get("queryMigrationJob")
                //RunIdIncrementer에서 job에 대한 id가 나온다.
                .incrementer(new RunIdIncrementer())
                .start(queryMigrationStep)
                .build();
    }
}
