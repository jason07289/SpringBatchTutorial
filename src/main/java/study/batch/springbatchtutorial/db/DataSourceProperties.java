package study.batch.springbatchtutorial.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Configuration
@ConfigurationProperties(prefix = "jae.batch.datasource")
@Getter
@Setter
@Validated
public class DataSourceProperties {
    @Size(min=1)
    private String[] targets;
    @Size(min=1)
    private String[] urls;
    @Size(min=1)
    private String[] usernames;

    private String[] passwords;
    @Size(min=1)
    private String[] driverClassNames;
}
