package study.batch.springbatchtutorial.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class DataSourceConfig {
    private final DataSourceProperties dataSourceProperties;

    /**
     * dataSource 구분을 위해서 기본 dataSource도 설정해줘야한다.
     * @return
     */
    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
    private DataSource createDataSource(String url, String userName, String password, String driverClassName) {
        HikariDataSource dataSource =
                new HikariDataSource();

        dataSource.setJdbcUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }
    @Bean(name = "routingDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.hikari.target")
    public DataSource routingDataSource() {
        Map<Object, Object> targetDataSourceMap = new HashMap<>();
        String[] targets = dataSourceProperties.getTargets();
        String[] urls = dataSourceProperties.getUrls();
        String[] userNames = dataSourceProperties.getUsernames();
        String[] passwords = dataSourceProperties.getPasswords();
        String[] driverClassNames = dataSourceProperties.getDriverClassNames();
        for(int i =0; i < targets.length; i++){
            String target = targets[i];
            DataSource targetDataSource;
            try {
                String password = "";
                if(passwords.length !=0) {
                    password = passwords[i];
                }
                targetDataSource = this.createDataSource(urls[i], userNames[i], password, driverClassNames[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("jae.batch.target.datasource 설정값 하위 리스트들의 길이는 같아야 합니다.");
            }
            targetDataSourceMap.put(target, targetDataSource);
        }

        AbstractRoutingDataSource routingDataSource = new RoutingExternalDataSource();
        routingDataSource.setDefaultTargetDataSource(dataSource());
        routingDataSource.setTargetDataSources(targetDataSourceMap);
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
        return routingDataSource;
    }


}
