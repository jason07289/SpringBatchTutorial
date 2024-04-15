package study.batch.springbatchtutorial.job.dbdataread;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import study.batch.springbatchtutorial.core.domain.accounts.Accounts;
import study.batch.springbatchtutorial.core.domain.orders.Orders;
import study.batch.springbatchtutorial.core.domain.orders.OrdersRepository;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class JpaCommonConfig {
    private final OrdersRepository ordersRepository;
    public static final int PAGE_SIZE = 5;


    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> jpaOrdersProcessor(){
        //읽어온 Orders를 Accounts로 매핑
        return Accounts::create;
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> jpaOrdersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrdersReader")
                .repository(ordersRepository)
                .methodName("findAll")
                .pageSize(PAGE_SIZE)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();

    }
}
