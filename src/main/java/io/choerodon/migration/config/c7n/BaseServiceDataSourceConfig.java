package io.choerodon.migration.config.c7n;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/4/28 11:14
 */
@Configuration
@MapperScan(basePackages = "io.choerodon.migration.mapper.c7n.base", sqlSessionTemplateRef = "baseServiceSqlSessionTemplate")
public class BaseServiceDataSourceConfig {
    @Primary
    @Bean(name = "baseServiceDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.base-service")
    public DataSourceProperties baseServiceDateSourceProperties() {
        return new DataSourceProperties();
    }


    @Primary
    @Bean(name = "baseServiceDataSource")
    public DataSource baseServiceDataSource(@Qualifier("baseServiceDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "baseServiceSqlSessionTemplate")
    public SqlSessionTemplate baseServiceSqlSessionTemplate(@Qualifier("baseServiceSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Primary
    @Bean("baseServiceSqlSessionFactory")
    public SqlSessionFactory baseServiceSqlSessionFactory(@Qualifier("baseServiceDataSource") DataSource baseServiceDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(baseServiceDataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:/mapper/c7n/*.xml"));
        return sqlSessionFactory.getObject();
    }

    @Primary
    @Bean(name = "baseServiceTransactionManager")
    public DataSourceTransactionManager baseServiceTransactionManager(@Qualifier("baseServiceDataSource") DataSource baseServiceDataSource) {
        return new DataSourceTransactionManager(baseServiceDataSource);
    }
}
