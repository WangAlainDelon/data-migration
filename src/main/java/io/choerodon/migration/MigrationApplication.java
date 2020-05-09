package io.choerodon.migration;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.choerodon.migration.service.TenantConfigService;
import io.choerodon.mybatis.MybatisMapperAutoConfiguration;
import org.hzero.boot.platform.code.autoconfigure.CodeAutoConfigure;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.code.builder.DefaultCodeRuleBuilder;
import org.hzero.boot.platform.data.autoconfigure.PermissionDataAutoConfiguration;
import org.hzero.boot.platform.ds.autoconfigure.DsAutoConfigure;
import org.hzero.boot.platform.entity.autoconfigure.EntityRegistAutoConfiguration;
import org.hzero.boot.platform.event.autoconfigure.EventAutoConfiguration;
import org.hzero.boot.platform.lov.autoconfigure.LovAutoConfiguration;
import org.hzero.boot.platform.profile.autoconfigure.ProfileAutoConfigure;
import org.hzero.boot.platform.rule.autoconfigure.RuleScriptAutoConfigure;
import org.hzero.mybatis.config.BatchInsertHelperConfig;
import org.hzero.mybatis.config.HzeroMybatisMapperConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import io.choerodon.resource.annoation.EnableChoerodonResourceServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * User: Mr.Wang
 * Date: 2020/4/27
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        MybatisMapperAutoConfiguration.class,
        DsAutoConfigure.class,
        EntityRegistAutoConfiguration.class,
        ProfileAutoConfigure.class,
//        CodeAutoConfigure.class,
        EventAutoConfiguration.class,
        LovAutoConfiguration.class,
        RuleScriptAutoConfigure.class,
        BatchInsertHelperConfig.class,
        HzeroMybatisMapperConfig.class
})
@EnableChoerodonResourceServer
public class MigrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(MigrationApplication.class, args);
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                1,
                2,
                1000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
