package io.choerodon.migration.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.migration.domian.hzero.TenantConfig;
import io.choerodon.migration.mapper.hzero.platform.HzeroLdapMapper;
import io.choerodon.migration.mapper.hzero.platform.TenantConfigMapper;
import io.choerodon.migration.mapper.hzero.platform.TenantMapper;
import io.choerodon.migration.service.TenantConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseAppService;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 租户配置扩展表应用服务默认实现
 *
 * @author xiaoyu.zhao@hand-china.com 2020-04-21 20:46:05
 */
@Service
public class TenantConfigServiceImpl extends BaseAppService implements TenantConfigService {

    @Autowired
    private TenantConfigMapper tenantConfigMapper;

    @Autowired
    private HzeroLdapMapper hzeroLdapMapper;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    @Qualifier(value = "hzeroPlatformDataSource")
    private DataSource dataSource;


    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = "hzeroPlatformTransactionManager")
    public List<TenantConfig> createOrUpdateTenantConfigs(Long tenantId, List<TenantConfig> tenantConfigs) {
        if (CollectionUtils.isEmpty(tenantConfigs) || tenantId == null) {
            return tenantConfigs;
        }
        tenantConfigs.forEach(tenantConfig -> tenantConfig.setTenantId(tenantId));
        validList(tenantConfigs);
//        SecurityTokenHelper.validTokenIgnoreInsert(tenantConfigs);
        Map<Boolean, List<TenantConfig>> insertOrUpdate = tenantConfigs.stream().collect(
                Collectors.partitioningBy(tenantConfig -> tenantConfig.getObjectVersionNumber() != null));
        List<TenantConfig> updateConfigs = insertOrUpdate.get(true);
        List<TenantConfig> insertConfigs = insertOrUpdate.get(false);
//        if (CollectionUtils.isNotEmpty(updateConfigs)) {
//            // 更新租户扩展配置信息
//            tenantConfigMapper.batchUpdateOptional(updateConfigs, TenantConfig.FIELD_CONFIG_VALUE);
//        }
        if (CollectionUtils.isNotEmpty(insertConfigs)) {
            // 新增租户扩展配置
            for (TenantConfig tenantConfig : insertConfigs) {
                int count = tenantConfigMapper.selectCount(new TenantConfig().setTenantId(tenantConfig.getTenantId())
                        .setConfigKey(tenantConfig.getConfigKey()));
                if (count > 0) {
                    continue;
                }
                tenantConfigMapper.insertSelective(tenantConfig);
            }

        }
        return tenantConfigs;
    }

    /**
     * 验证参数合法性
     *
     * @param tenantConfig 租户扩展配置
     */
    private void validateParams(TenantConfig tenantConfig) {
        hzeroLdapMapper.selectAll();
        int count = tenantConfigMapper.selectCount(new TenantConfig().setTenantId(tenantConfig.getTenantId())
                .setConfigKey(tenantConfig.getConfigKey()));
        if (count > 0) {
//            throw new CommonException(BaseConstants.ErrorCode.DATA_EXISTS);

        }
    }
}
