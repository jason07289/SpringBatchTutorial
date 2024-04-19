package study.batch.springbatchtutorial.db;

public class DataSourceContextHolder {
    private static final ThreadLocal<String> dataSourceKey = new ThreadLocal<>();

    public static void setDataSourceKey(String key) {
        dataSourceKey.set(key);
    }

    public static String getDataSourceKey() {
        return dataSourceKey.get();
    }

    public static void clearDataSourceKey() {
        dataSourceKey.remove();
    }
}
