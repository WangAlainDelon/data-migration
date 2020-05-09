package io.choerodon.migration.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.choerodon.core.exception.CommonException;
import io.choerodon.migration.domian.c7n.MemberRoleDTO;
import io.choerodon.migration.domian.c7n.OrganizationDTO;
import io.choerodon.migration.domian.c7n.ProjectDTO;
import io.choerodon.migration.domian.c7n.RoleDTO;
import io.choerodon.migration.domian.hzero.*;
import io.choerodon.migration.domian.service.TenantConfigObserver;
import io.choerodon.migration.domian.service.TenantGroupObserver;
import io.choerodon.migration.domian.service.TenantRoleObserver;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.MemberRoleC7NMapper;
import io.choerodon.migration.mapper.c7n.base.OrganizationC7NMapper;
import io.choerodon.migration.mapper.c7n.base.ProjectC7NMapper;
import io.choerodon.migration.mapper.c7n.base.RoleC7NMapper;
import io.choerodon.migration.mapper.hzero.platform.*;
import io.choerodon.migration.service.OraganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: Mr.Wang
 * Date: 2020/4/28
 */
@Service
public class OraganizationServiceImpl implements OraganizationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OraganizationServiceImpl.class);

    @Autowired
    private OrganizationC7NMapper organizationC7NMapper;

    @Autowired
    private TenantMapper tenantMapper;


    @Autowired
    private TenantConfigMapper tenantConfigMapper;

    @Autowired
    private MemberRoleC7NMapper memberRoleC7NMapper;

    @Autowired
    private MemberRoleHzeroMapper memberRoleHzeroMapper;

    @Autowired
    private RoleC7NMapper roleC7NMapper;

    @Autowired
    private RoleHzeroMapper roleHzeroMapper;

    @Autowired
    private ProjectC7NMapper projectC7NMapper;

    @Autowired
    private ProjectUserMapper projectUserMapper;

    @Autowired
    private TenantConfigObserver tenantConfigObserver;

    @Autowired
    private TenantGroupObserver tenantGroupObserver;

    @Autowired
    private TenantRoleObserver tenantRoleObserver;


    @Override
    public void organizationDataMigration() {
        //1.创建租户 hpfm_tenant
        MigrationUtils.migration(OrganizationDTO.class, organizationC7NMapper, tenantMapper, t ->
                t.stream().map(organizationDTO -> {
                            Tenant insertDTO = new Tenant();
                            insertDTO.setTenantId(organizationDTO.getId());
                            insertDTO.setEnabledFlag(organizationDTO.getEnabled() == true ? 1 : 0);
                            insertDTO.setLimitUserQty(null);
                            insertDTO.setTenantNum(organizationDTO.getCode());
                            insertDTO.setTenantName(organizationDTO.getName());
                            insertDTO.setCreatedBy(organizationDTO.getCreatedBy());
                            insertDTO.setCreationDate(organizationDTO.getCreationDate());
                            insertDTO.setLastUpdateDate(organizationDTO.getLastUpdateDate());
                            insertDTO.setLastUpdatedBy(organizationDTO.getLastUpdatedBy());
                            insertDTO.setObjectVersionNumber(organizationDTO.getObjectVersionNumber());
                            return insertDTO;
                        }
                ).collect(Collectors.toList())
        );
        //2.租户配置 hpfm_tenant_config
        List<Tenant> tenants = tenantMapper.selectAll();
        for (Tenant tenant : tenants) {
            OrganizationDTO organizationDTO = organizationC7NMapper.selectByPrimaryKey(tenant.getTenantId());
            if (Objects.isNull(organizationDTO)) {
                continue;
            }
            List<TenantConfig> tenantConfigs = new ArrayList<>();

            if (!Objects.isNull(organizationDTO.getAddress()) && !"".equals(organizationDTO.getAddress().trim())) {
                TenantConfig tenantConfig = new TenantConfig();
                tenantConfig.setTenantId(tenant.getTenantId());
                tenantConfig.setConfigKey("address");
                tenantConfig.setConfigValue(organizationDTO.getAddress());
                tenantConfigs.add(tenantConfig);
            }


            if (!Objects.isNull(organizationDTO.getUserId()) && !"".equals(organizationDTO.getUserId().toString().trim())) {
                TenantConfig tenantConfig1 = new TenantConfig();
                tenantConfig1.setTenantId(tenant.getTenantId());
                tenantConfig1.setConfigKey("userId");
                tenantConfig1.setConfigValue(String.valueOf(organizationDTO.getUserId()));
                tenantConfigs.add(tenantConfig1);
            }


            if (!Objects.isNull(organizationDTO.getCategory()) && !"".equals(organizationDTO.getCategory().trim())) {
                TenantConfig tenantConfig2 = new TenantConfig();
                tenantConfig2.setTenantId(tenant.getTenantId());
                tenantConfig2.setConfigKey("category");
                tenantConfig2.setConfigValue(organizationDTO.getCategory());
                tenantConfigs.add(tenantConfig2);
            }


            if (!Objects.isNull(organizationDTO.getImageUrl()) && !"".equals(organizationDTO.getImageUrl().trim())) {
                TenantConfig tenantConfig3 = new TenantConfig();
                tenantConfig3.setTenantId(tenant.getTenantId());
                tenantConfig3.setConfigKey("imageUrl");
                tenantConfig3.setConfigValue(organizationDTO.getImageUrl());
                tenantConfigs.add(tenantConfig3);
            }


            if (!Objects.isNull(organizationDTO.getHomePage()) && !"".equals(organizationDTO.getHomePage().trim())) {
                TenantConfig tenantConfig4 = new TenantConfig();
                tenantConfig4.setTenantId(tenant.getTenantId());
                tenantConfig4.setConfigKey("homePage");
                tenantConfig4.setConfigValue(organizationDTO.getHomePage());
                tenantConfigs.add(tenantConfig4);
            }


            if (!Objects.isNull(organizationDTO.getScale()) && !"".equals(organizationDTO.getScale().toString().trim())) {
                TenantConfig tenantConfig5 = new TenantConfig();
                tenantConfig5.setTenantId(tenant.getTenantId());
                tenantConfig5.setConfigKey("scale");
                tenantConfig5.setConfigValue(String.valueOf(organizationDTO.getScale()));
                tenantConfigs.add(tenantConfig5);
            }
            if (!Objects.isNull(organizationDTO.getBusinessType()) && !"".equals(organizationDTO.getBusinessType().trim())) {
                TenantConfig tenantConfig6 = new TenantConfig();
                tenantConfig6.setTenantId(tenant.getTenantId());
                tenantConfig6.setConfigKey("businessType");
                tenantConfig6.setConfigValue(String.valueOf(organizationDTO.getBusinessType()));
                tenantConfigs.add(tenantConfig6);
            }
            if (!Objects.isNull(organizationDTO.getEmailSuffix()) && "".equals(organizationDTO.getEmailSuffix().trim())) {
                TenantConfig tenantConfig7 = new TenantConfig();
                tenantConfig7.setTenantId(tenant.getTenantId());
                tenantConfig7.setConfigKey("emailSuffix");
                tenantConfig7.setConfigValue(String.valueOf(organizationDTO.getEmailSuffix()));
                tenantConfigs.add(tenantConfig7);
            }

            if (!Objects.isNull(organizationDTO.getRegister() && !"".equals(organizationDTO.getRegister().toString().trim()))) {
                TenantConfig tenantConfig8 = new TenantConfig();
                tenantConfig8.setTenantId(tenant.getTenantId());
                tenantConfig8.setConfigKey("isRegister");
                tenantConfig8.setConfigValue(String.valueOf(organizationDTO.getRegister()));
                tenantConfigs.add(tenantConfig8);
            }

            if (!Objects.isNull(organizationDTO.getRemoteTokenEnabled()) && !"".equals(organizationDTO.getRemoteTokenEnabled().toString().trim())) {
                TenantConfig tenantConfig9 = new TenantConfig();
                tenantConfig9.setTenantId(tenant.getTenantId());
                tenantConfig9.setConfigKey("remoteTokenEnabled");
                tenantConfig9.setConfigValue(String.valueOf(organizationDTO.getRemoteTokenEnabled()));
                tenantConfigs.add(tenantConfig9);
                tenant.setTenantConfigs(tenantConfigs);
            }
            tenantConfigObserver.tenantCreate(tenant);
        }

        //3.创建默认的集团
        for (Tenant tenant : tenants) {
            tenantGroupObserver.tenantCreate(tenant);
        }
    }
}
