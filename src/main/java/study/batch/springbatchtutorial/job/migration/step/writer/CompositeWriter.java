package study.batch.springbatchtutorial.job.migration.step.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import study.batch.springbatchtutorial.core.domain.orders.Orders;

import java.util.Arrays;
import java.util.List;
@Component
@RequiredArgsConstructor
public class CompositeWriter {
    private final JdbcBatchItemWriter<Orders> deleteOrdersWriter;
    private final JdbcBatchItemWriter<Orders> insertOrdersWriter;
    private final JdbcBatchItemWriter<Orders> deleteAccountsWriter;
    @StepScope
    @Bean
    public CompositeItemWriter<Orders> compositeOrdersItemWriter() {
        List<ItemWriter<? super Orders>> itemWriters = Arrays.asList(deleteOrdersWriter, deleteAccountsWriter, insertOrdersWriter);
        return new CompositeItemWriterBuilder<Orders>()
                .delegates(itemWriters)
                .build();
    }
}
