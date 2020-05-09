package io.choerodon.migration.domian.service;

import io.choerodon.core.exception.CommonException;
import io.choerodon.migration.domian.hzero.Role;
import io.choerodon.migration.domian.hzero.Tenant;
import io.choerodon.migration.mapper.hzero.platform.RoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 租户事件——角色初始化
 *
 * @author bojiangzhou 2020/04/28
 */
@Component
public class TenantRoleObserver implements TenantObserver<List<Role>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(io.choerodon.migration.domian.service.TenantRoleObserver.class);

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private TemplateRoleCreateService templateRoleCreateService;

    @Override
    public int order() {
        return 50;
    }

    /**
     * 租户初始化时，需先根据模板创建租户管理员，再创建其它模板角色
     *
     * @param tenant 新创建的租户信息
     * @return 初始化的角色
     */
    @Override
    public List<Role> tenantCreate(@Nonnull Tenant tenant) {
        LOGGER.info("Start create tenant tpl role, tenantNum is {}", tenant.getTenantNum());

        // 查询全局的模板角色
        List<Role> tplRoles = roleMapper.selectGlobalTemplateRole();

        Map<Boolean, List<Role>> map = tplRoles.stream().collect(Collectors.partitioningBy(role -> isTenantAdminTemplateRole(role.getCode())));

        Role tenantAdminRoleTpl = map.get(Boolean.TRUE).stream().findFirst().orElseThrow(() -> new CommonException("hiam.error.role.adminRoleTplNotFound"));
        Role record = new Role();
        record.setCode(Role.SUPER_TENANT_ROLE);
        Role superTenantAdmin = roleMapper.selectOne(record);

        // 创建租户管理员角色
        Role tenantAdminRole = templateRoleCreateService.createRoleByTpl(superTenantAdmin, tenantAdminRoleTpl, tenant);

        // 批量创建其它管理员角色
        LinkedList<Role> roles = map.get(Boolean.FALSE).stream()
                .map(tplRole -> templateRoleCreateService.createRoleByTpl(tenantAdminRole, tplRole, tenant))
                .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);

        roles.addFirst(tenantAdminRole);

        LOGGER.info("[TenantInit - Num={}] -> {}", tenant.getTenantNum(), roles);

        return roles;
    }

    /**
     * 判断是否是租户管理员模板角色
     *
     * @param roleCode 角色编码
     * @return true/false
     */
    protected boolean isTenantAdminTemplateRole(String roleCode) {
        return Role.TENANT_ROLE_TPL.equals(roleCode);
    }

}
