package study.batch.springbatchtutorial.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.JobParameters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
public class DatabaseConfig {

    /**
     * dataSource 구분을 위해서 기본 dataSource도 설정해줘야한다.
     * @return
     */
    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.main")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "externalDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.target")
    public DataSource externalDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
//        HikariDataSource hikariDataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
//
//        return new LazyConnectionDataSourceProxy(hikariDataSource);
    }

}
