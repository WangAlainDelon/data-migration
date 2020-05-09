package io.choerodon.migration.service;

import io.choerodon.migration.domian.hzero.TenantConfig;

import java.util.List;

/**
 * 租户配置扩展表应用服务
 *
 * @author xiaoyu.zhao@hand-china.com 2020-04-21 20:46:05
 */
public interface TenantConfigService {


    /**
     * 批量新增或更新租户配置信息
     *
     * @param tenantId      租户Id
     * @param tenantConfigs 新增或更新的租户配置信息
     * @return 新增或更新的结果
     */
    List<TenantConfig> createOrUpdateTenantConfigs(Long tenantId, List<TenantConfig> tenantConfigs);

}
