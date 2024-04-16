package study.batch.springbatchtutorial.job.parameters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
public class DataSourceJobParams {
    @Value("#{jobParameters[jdbc-url]}")
    private String jdbcUrl;
}