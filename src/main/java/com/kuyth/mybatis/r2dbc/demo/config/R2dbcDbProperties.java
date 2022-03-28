package com.kuyth.mybatis.r2dbc.demo.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "spring.r2dbc.datasource")
public class R2dbcDbProperties {

    private String host;
    private int port;
    private String database;
    private String jdbcUrl;
    private String username;
    private String password;
    private Integer maximumPoolSize;
    private Long maxLifetime;
    private Long connectionTimeout;
    private Long validationTimeout;

    private DataSourceProperties dataSourceProperties;

    @Setter
    @Getter
    @ToString
    public static class DataSourceProperties {
        private String cachePrepStmts;
        private String prepStmtCacheSize;
        private String prepStmtCacheSqlLimit;
        private String useServerPrepStmts;
    }
}
