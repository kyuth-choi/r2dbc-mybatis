package com.kuyth.mybatis.r2dbc.demo.config;

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.r2dbc.ReactiveSqlSessionFactory;
import org.apache.ibatis.r2dbc.impl.DefaultReactiveSqlSessionFactory;
import org.apache.ibatis.r2dbc.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class R2dbcDbConfig {

    R2dbcDbProperties r2dbcDbProperties;

    public R2dbcDbConfig(R2dbcDbProperties r2dbcDbProperties) {
        this.r2dbcDbProperties = r2dbcDbProperties;
    }

    @Bean("smartstoreReactiveSqlSessionFactory")
    public ReactiveSqlSessionFactory getReactiveSqlSession() throws IOException {

        Configuration configuration = new Configuration();

        Properties properties = new Properties();
        // spring admin metrics 사용 설정
        properties.setProperty("metrics.enabled", "true");
        configuration.setVariables(properties);
        configuration.setDefaultEnumTypeHandler(EnumOrdinalTypeHandler.class);

        // Springboot VFS 설정 -> 안하면 getTypeAliasRegistry 여기서 패키지 경로 못찾음.
        configuration.setVfsImpl(SpringBootVFS.class);
        // 패키치 경로 내 class alias 설정
        configuration.getTypeAliasRegistry().registerAliases("com.kuyth.mybatis.r2dbc.demo.vo");

        // resource 경로에서 xml파일 찾아서 mybatis에 등록
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/*.xml");
        for (Resource resource : resources) {
            new XMLMapperBuilder(resource.getInputStream(), configuration, resource.toString(), configuration.getSqlFragments()).parse();
        }

        // connection pool 설정
        ConnectionPoolConfiguration connectionPoolConfiguration = ConnectionPoolConfiguration.builder(connectionFactory())
                .maxSize(r2dbcDbProperties.getMaximumPoolSize())
                .maxLifeTime(Duration.ofMillis(r2dbcDbProperties.getMaxLifetime()))
                .build();

        return new DefaultReactiveSqlSessionFactory(configuration, new ConnectionPool(connectionPoolConfiguration));
    }

    @Bean("smartstoreConnectionFactory")
    public ConnectionFactory connectionFactory() {
        return MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                .host(r2dbcDbProperties.getHost())
                .port(r2dbcDbProperties.getPort())
                .username(r2dbcDbProperties.getUsername())
                .password(r2dbcDbProperties.getPassword())
                .connectTimeout(Duration.ofMillis(r2dbcDbProperties.getConnectionTimeout()))
                .build());
    }

}
