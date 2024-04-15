package study.batch.springbatchtutorial.job.validatedparam.dbdataread;

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
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import study.batch.springbatchtutorial.core.domain.accounts.Accounts;
import study.batch.springbatchtutorial.core.domain.accounts.AccountsRepository;
import study.batch.springbatchtutorial.core.domain.orders.Orders;
import study.batch.springbatchtutorial.core.domain.orders.OrdersRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * desc: 주문 테이블 -> 정산 테이블 데이터 이관
 * program args: --spring.batch.job.names=trMigrationJob
 */
@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {
    private final OrdersRepository ordersRepository;
    private final AccountsRepository accountsRepository;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step trMigrationStep(ItemReader trOrdersReader, ItemProcessor trOrdersProcessor, ItemWriter trAccountsWriter) {
        return stepBuilderFactory.get("trMigrationStep")
                //<I: input, O:output> input으로 읽어와서 output으로 job내에서 사용된다.
                .<Orders, Accounts>chunk(5)
                .reader(trOrdersReader)
//                .writer(items -> {
//                    items.forEach(System.out::println);
//                })
                .processor(trOrdersProcessor)
                .writer(trAccountsWriter)
                .build();
    }

//    /**
//     * 실제 데이터 입력하는 부분
//     * id 채번을 막기 위해 native 쿼리로 작성 필요.
//     * @return
//     */
//    @StepScope
//    @Bean
//    public ItemWriter<Accounts> trAccountsNativeQueryWriter(){
//        return new RepositoryItemWriterBuilder<Accounts>()
//                .repository(accountsRepository)
//                .methodName("save")
//                .build();
//    }


    /**
     * 실제 데이터 입력하는 부분
     * id autoIncrement 문제 고민좀 해봐야함.
     * @return
     */
    @StepScope
    @Bean
    public RepositoryItemWriter<Accounts> trAccountsRepositoryWriter(){
        return new RepositoryItemWriterBuilder<Accounts>()
                .repository(accountsRepository)
                //메소드 명 string으로 접근하기에 안좋아 보임.
                .methodName("save")
                .build();
    }

    /**
     * 실제 데이터 입력하는 부분
     * id autoIncrement 문제 고민좀 해봐야함.
     * @return
     */
    @StepScope
    @Bean
    public ItemWriter<Accounts> trAccountsWriter(){
        return items -> accountsRepository.saveAll(items);
    }

    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> trOrdersProcessor(){
        //읽어온 Orders를 Accounts로 매핑
        return Accounts::create;
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrdersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrdersReader")
                .repository(ordersRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();

    }
}
