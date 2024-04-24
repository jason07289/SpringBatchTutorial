package study.batch.springbatchtutorial.job.migration.step.writer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import study.batch.springbatchtutorial.core.domain.accounts.Accounts;
import study.batch.springbatchtutorial.core.domain.orders.Orders;
import study.batch.springbatchtutorial.db.DataSourceContextHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Component
public class QuerWriterStep {

    private final DataSource routingDataSource; // DataSource DI
    public static final String SCHEMA_NAME = "spring_batch";

    public QuerWriterStep(@Qualifier("routingDataSource") DataSource externalDataSource) {
        this.routingDataSource = externalDataSource;
    }

    /**
     * 실제 데이터 insert Orders to Accounts
     * id 유지를 위해 native query 사용
     * @return
     */
    @StepScope
    @Bean
    public JdbcBatchItemWriter<Accounts> queryAccountsWriter(@Value("#{jobParameters['external.target']}") String target) throws RuntimeException{
        //AbstractRoutingDataSource에서 datasource 선택
        DataSourceContextHolder.setDataSourceKey(target);

        return new JdbcBatchItemWriterBuilder<Accounts>()
                .dataSource(routingDataSource)
                //postgresSQL의 경우 스키마 명시가 필요했음.
                //accounts에는 column_name values는 java의 fieldName
                .sql("insert into " + SCHEMA_NAME +
                        ".accounts(id, order_item, price, order_date, account_date) " +
                        "values (:id, :orderItem, :price, :orderDate, :accountDate)")
                //적어도 하나의 항목이 행을 업데이트하거나 삭제하지 않을 경우 예외를 throw 할지 여부를 설정. 기본값은 true긴함.
                .assertUpdates(true)
                //Pojo 기반으로 Insert SQL의 Values를 매핑 .columnMapped() 와 대조 -> key, value 기반으로 value 매핑
                .beanMapped()
                .build();
    }

    public ItemWriter<Orders> myItemWriter() {
        JdbcBatchItemWriter<Orders> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(routingDataSource);
        writer.setSql("INSERT INTO my_table (col1, col2) VALUES (?, ?)");
        return writer;
    }

    /**
     * 실제 데이터 insert Orders to Orders
     * id 유지를 위해 native query 사용
     * @return
     */
    @Bean
    @StepScope
    public JdbcBatchItemWriter<Orders> deleteOrdersWriter(@Value("#{jobParameters['external.target']}") String target) throws RuntimeException{
        //AbstractRoutingDataSource에서 datasource 선택
        DataSourceContextHolder.setDataSourceKey(target);
        String deleteSql = "DELETE FROM spring_batch.orders WHERE id = :id; \n";


        return new JdbcBatchItemWriterBuilder<Orders>()
                .dataSource(routingDataSource)
                //postgresSQL의 경우 스키마 명시가 필요했음.
                //accounts에는 column_name values는 java의 fieldName
                .sql(deleteSql)
                //적어도 하나의 항목이 행을 업데이트하거나 삭제하지 않을 경우 예외를 throw 할지 여부를 설정. 기본값은 true긴함.
                //삭제는 일어나지 않을 수 있기에 false
                .assertUpdates(false)
                //Pojo 기반으로 Insert SQL의 Values를 매핑 .columnMapped() 와 대조 -> key, value 기반으로 value 매핑
                .beanMapped()
                .build();
    }

    /**
     * 실제 데이터 insert Orders to Orders
     * id 유지를 위해 native query 사용
     * @return
     */
    @Bean
    @StepScope
    public JdbcBatchItemWriter<Orders> deleteAccountsWriter(@Value("#{jobParameters['external.target']}") String target) throws RuntimeException{
        //AbstractRoutingDataSource에서 datasource 선택
        DataSourceContextHolder.setDataSourceKey(target);
        String deleteSql = "DELETE FROM spring_batch.accounts WHERE id = :id; \n";


        return new JdbcBatchItemWriterBuilder<Orders>()
                .dataSource(routingDataSource)
                //postgresSQL의 경우 스키마 명시가 필요했음.
                //accounts에는 column_name values는 java의 fieldName
                .sql(deleteSql)
                //적어도 하나의 항목이 행을 업데이트하거나 삭제하지 않을 경우 예외를 throw 할지 여부를 설정. 기본값은 true긴함.
                //삭제는 일어나지 않을 수 있기에 false
                .assertUpdates(false)
                //Pojo 기반으로 Insert SQL의 Values를 매핑 .columnMapped() 와 대조 -> key, value 기반으로 value 매핑
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Orders> insertOrdersWriter(@Value("#{jobParameters['external.target']}") String target) throws RuntimeException{
        //AbstractRoutingDataSource에서 datasource 선택
        DataSourceContextHolder.setDataSourceKey(target);
        String insertSql = "INSERT INTO spring_batch.orders (id, order_item, price, order_date) \n" +
                "VALUES (:id, :orderItem, :price, :orderDate);";

        return new JdbcBatchItemWriterBuilder<Orders>()
                .dataSource(routingDataSource)
                //postgresSQL의 경우 스키마 명시가 필요했음.
                //accounts에는 column_name values는 java의 fieldName
                .sql(insertSql)
                //적어도 하나의 항목이 행을 업데이트하거나 삭제하지 않을 경우 예외를 throw 할지 여부를 설정. 기본값은 true긴함.
                .assertUpdates(true)
                //Pojo 기반으로 Insert SQL의 Values를 매핑 .columnMapped() 와 대조 -> key, value 기반으로 value 매핑
                .beanMapped()
                .build();
    }

}
