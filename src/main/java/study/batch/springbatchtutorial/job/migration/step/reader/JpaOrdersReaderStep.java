package study.batch.springbatchtutorial.job.migration.step.reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import study.batch.springbatchtutorial.core.domain.orders.Orders;
import study.batch.springbatchtutorial.core.domain.orders.OrdersRepository;

import java.util.Arrays;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JpaOrdersReaderStep {
    private final OrdersRepository ordersRepository;
    public static final int PAGE_SIZE = 100;

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> jpaOrdersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrdersReader")
                .repository(ordersRepository)
                .methodName("findAll")
//                .pageSize(PAGE_SIZE)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();

    }
}
