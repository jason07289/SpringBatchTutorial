package study.batch.springbatchtutorial.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingExternalDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {

        return DataSourceContextHolder.getDataSourceKey();
    }
}
