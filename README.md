# r2dbc-mybatis

## 수정 사항

- mybatis-config xml에서 java설정으로 변경
- StringTypeHandler 추가
- mybatis resultType Object로 지정한 경우 parsing 오류 수정


## R2DBC(Reactive Relational Database Connectivity)란

- 관계형 database 연동에 Reactive Stream과 non-Blocking API 기능을 제공해주는 라이브러리

## R2DBC 구현 Library 및 비교 분석

- `mybatis-r2dbc` : [GitHub - linux-china/mybatis-r2dbc: MyBatis R2DBC Adapter](https://github.com/linux-china/mybatis-r2dbc)
- `r2dbc-mysql` : [GitHub - mirromutth/r2dbc-mysql: R2DBC MySQL Implementation](https://github.com/mirromutth/r2dbc-mysql)

### **1. r2dbc-mysql**

- 장점
    1. JPA와 비슷한 구조로 개발되어 query를 작성 필요없이 쉽게 개발 가능
    2. @Id, @Table 등의 Annotation 지원
- 단점
    1. JPA와 유사하나 JPA 지원하는 다량의 Annotation 미지원
    2. 복합키(Composite Key) 미지원 → 해결 불가, mybatis-r2dbc를 사용하게된 이유
    3. save 메소드의 ID객체가 세팅된 경우 무조건 update 하는 이슈 → `Persistable`<T> 상속으로 해결 가능

### **2. mybatis-r2dbc**

- 장점
    1. r2dbc-mysql에서 미지원하여 오류 발생하는 복합키 설정이 필요 없음
    2. Mybatis를 사용하여 JPA에서 사용하기 불편한 복잡한 쿼리 개발에 용이
- 단점
    1. Mybatis 설정이 xml로 구현되어 있음 → mybatis configuraion 수동 설정 추가
    2. Mapper.class Autowired 불가 → `reactiveSqlSessionFactory` 사용하여 세팅
    3. Mybatis를 사용하여 xml에 query를 명시해야함

## mybatis-**r2dbc 적용하기**

r2dbc-mysql의 복합키(Composite Key) 미지원 이슈로 인하여 mybatis-r2dbc 채택

`mybatis-r2dbc`는 mybatis-config.xml 설정 기반 → 현재 프로젝트(쇼아2) 구성에 맞춰 JAVA Config로 설정 진행

1. Confuguration 설정
    
    ```java
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
    ```

## 참조

* R2DBC: https://r2dbc.io/
* R2DBC Spec: https://r2dbc.io/spec/0.8.2.RELEASE/spec/html/
* R2DBC Pool: https://github.com/r2dbc/r2dbc-pool
* Spring Data R2DBC: https://github.com/spring-projects/spring-data-r2dbc https://docs.spring.io/spring-data/r2dbc/docs/1.0.0.M2/reference/html/
* linux-china/mybatis-r2dbc : https://github.com/linux-china/mybatis-r2dbc
* mirromutth/r2dbc-mysql : https://github.com/mirromutth/r2dbc-mysql
