package io.choerodon.migration.domian.service;

import io.choerodon.core.exception.CommonException;
import io.choerodon.migration.domian.hzero.MemberRole;
import io.choerodon.migration.domian.hzero.Role;
import io.choerodon.migration.domian.hzero.Tenant;
import io.choerodon.migration.domian.hzero.User;
import io.choerodon.migration.infra.util.HiamResourceLevel;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author jianbo.li
 * @date 2019/6/27 20:14
 */
@Component
public class RoleCreateInternalService extends RoleCreateService {

    /**
     * 内部调用去掉这块的角色校验
     */
    @Override
    public Role checkAdminRole(Role role, User adminUser) {
        // 父级角色
        Role parentRole = roleMapper.selectByPrimaryKey(role.getParentRoleId());

        if (parentRole == null) {
            throw new CommonException("hiam.warn.role.parentRoleNotFound");
        }

        // 父级分配层级默认为 organization-0
        MemberRole memberRole = new MemberRole();
        memberRole.setAssignLevel(HiamResourceLevel.ORGANIZATION.value());
        memberRole.setAssignLevelValue(BaseConstants.DEFAULT_TENANT_ID);

        Tenant tenant = tenantMapper.selectByPrimaryKey(BaseConstants.DEFAULT_TENANT_ID);
        Assert.notNull(tenant, "default tenant not found with tenantId=" + BaseConstants.DEFAULT_TENANT_ID);
        memberRole.setAssignLevelCode(tenant.getTenantNum());

        // 设置父级分配层级值
        role.setupParentAssignLevel(memberRole);

        role.setParentRole(parentRole);

        return parentRole;
    }
}
