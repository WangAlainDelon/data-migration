package io.choerodon.migration.domian.service;

import io.choerodon.migration.domian.hzero.Group;
import io.choerodon.migration.domian.hzero.Tenant;
import io.choerodon.migration.infra.util.Constants;
import io.choerodon.migration.mapper.hzero.platform.GroupMapper;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.code.builder.DefaultCodeRuleBuilder;
import org.hzero.core.base.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 租户事件——集团
 *
 * @author bojiangzhou 2020/04/28
 */
@Component
public class TenantGroupObserver implements TenantObserver<Group> {

    private static final Logger LOGGER = LoggerFactory.getLogger(io.choerodon.migration.domian.service.TenantGroupObserver.class);


    @Autowired
    private CodeRuleBuilder codeRuleBuilder;

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public int order() {
        return 20;
    }

    @Override
    public Group tenantCreate(@Nonnull Tenant tenant) {
        String groupNum = codeRuleBuilder.generateCode(
                Constants.Level.TENANT,
                BaseConstants.DEFAULT_TENANT_ID,
                Constants.RuleCodes.HPFM_GROUP,
                Constants.CodeRuleLevelCode.GLOBAL,
                Constants.CodeRuleLevelCode.GLOBAL,
                null
        );

        Group group = new Group();
        group.setGroupNum(groupNum);
        group.setGroupName(tenant.getTenantName());
        group.setTenantId(tenant.getTenantId());
        group.setSourceKey("1");
        group.setSourceCode(StringUtils.defaultIfBlank(tenant.getSourceCode(), Constants.HZERO_GROUP));
        group.setEnabledFlag(tenant.getEnabledFlag());

        Group record = new Group();
        record.setTenantId(tenant.getTenantId());
        if (!Objects.isNull(groupMapper.selectOne(record))) {
            return null;
        }
        groupMapper.insertSelective(group);

        LOGGER.info("[TenantInit - Num={}] -> {}", tenant.getTenantNum(), group);

        return group;
    }

}
