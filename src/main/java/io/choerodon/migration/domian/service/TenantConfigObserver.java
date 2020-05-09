package io.choerodon.migration.domian.service;

import io.choerodon.migration.domian.hzero.Tenant;
import io.choerodon.migration.domian.hzero.TenantConfig;
import io.choerodon.migration.service.TenantConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 租户事件-租户配置
 *
 * @author bojiangzhou 2020/04/28
 */
@Component
public class TenantConfigObserver implements TenantObserver<List<TenantConfig>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantConfigObserver .class);

    @Autowired
    private TenantConfigService tenantConfigService;

    @Override
    public int order() {
        return 10;
    }

    @Override
    public List<TenantConfig> tenantCreate(@Nonnull Tenant tenant) {
        List<TenantConfig> configs = tenant.getTenantConfigs();
        if (CollectionUtils.isNotEmpty(configs)) {
            // 租户创建后初始化租户配置信息
            configs = tenantConfigService.createOrUpdateTenantConfigs(tenant.getTenantId(), configs);
        }

        LOGGER.info("[TenantInit - Num={}] -> {}", tenant.getTenantNum(), configs);

        return configs;
    }

}
