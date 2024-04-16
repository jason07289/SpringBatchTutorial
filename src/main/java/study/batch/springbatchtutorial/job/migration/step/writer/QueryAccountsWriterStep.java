package study.batch.springbatchtutorial.job.migration.step.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import study.batch.springbatchtutorial.core.domain.accounts.Accounts;

import javax.sql.DataSource;
@RequiredArgsConstructor
@Component
public class QueryAccountsWriterStep {
    private final DataSource dataSource; // DataSource DI
    public static final String SCHEMA_NAME = "spring_batch";

    /**
     * 실제 데이터 insert
     * id 유지를 위해 native query 사용
     * @return
     */
    @StepScope
    @Bean
    public JdbcBatchItemWriter<Accounts> queryAccountsWriter() throws RuntimeException{
        return new JdbcBatchItemWriterBuilder<Accounts>()
                .dataSource(dataSource)
                //postgresSQL의 경우 스키마 명시가 필요했음.
                //accounts에는 column_name values는 java의 fieldName
                .sql("insert into " + SCHEMA_NAME +
                        ".accounts(id, order_item, price, order_date, account_date) " +
                        "values (:id, :orderItem, :price, :orderDate, :accountDate)")
                //적어도 하나의 항목이 행을 업데이트하거나 삭제하지 않을 경우 예외를 throw할지 여부를 설정. 기본값은 true긴함.
                .assertUpdates(true)
                //Pojo 기반으로 Insert SQL의 Values를 매핑 .columnMapped() 와 대조 -> key, value 기반으로 value 매핑
                .beanMapped()
                .build();
    }
}