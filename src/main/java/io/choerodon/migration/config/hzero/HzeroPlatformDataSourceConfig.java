package io.choerodon.migration.config.hzero;

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
@MapperScan(basePackages = "io.choerodon.migration.mapper.hzero.platform", sqlSessionTemplateRef = "hzeroPlatformSqlSessionTemplate")
public class HzeroPlatformDataSourceConfig {
    @Primary
    @Bean(name = "hzeroPlatformDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.hzero-platform")
    public DataSourceProperties hzeroPlatformDateSourceProperties() {
        return new DataSourceProperties();
    }


    @Primary
    @Bean(name = "hzeroPlatformDataSource")
    public DataSource hzeroPlatformDataSource(@Qualifier("hzeroPlatformDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "hzeroPlatformSqlSessionTemplate")
    public SqlSessionTemplate hzeroPlatformSqlSessionTemplate(@Qualifier("hzeroPlatformSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Primary
    @Bean("hzeroPlatformSqlSessionFactory")
    public SqlSessionFactory hzeroPlatformSqlSessionFactory(@Qualifier("hzeroPlatformDataSource") DataSource hzeroPlatformDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(hzeroPlatformDataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:/mapper/hzero/*.xml"));
        return sqlSessionFactory.getObject();
    }

    @Primary
    @Bean(name = "hzeroPlatformTransactionManager")
    public DataSourceTransactionManager hzeroPlatformTransactionManager(@Qualifier("hzeroPlatformDataSource") DataSource hzeroPlatformDataSource) {
        return new DataSourceTransactionManager(hzeroPlatformDataSource);
    }
}
