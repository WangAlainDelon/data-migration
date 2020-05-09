package io.choerodon.migration.domian.service;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.migration.domian.hzero.Label;
import io.choerodon.migration.domian.hzero.Role;
import io.choerodon.migration.domian.hzero.Tenant;
import io.choerodon.migration.domian.hzero.User;
import io.choerodon.migration.infra.util.HiamError;
import io.choerodon.migration.infra.util.LabelAssignType;
import io.choerodon.migration.infra.util.LabelConstants;
import io.choerodon.migration.mapper.hzero.platform.RoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模板角色创建服务
 *
 * @author bojiangzhou 2020/04/29
 */
@Component
public class TemplateRoleCreateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateRoleCreateService.class);

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleCreateInternalService roleCreateInternalService;

    /**
     * 继承模板角色创建一个新的角色，创建用户默认为系统匿名用户
     *
     * @param parentRole 父级角色
     * @param tplRole    模板角色
     * @param tenant     角色所属租户
     * @return 创建的角色
     */
    public Role createRoleByTpl(@Nonnull Role parentRole, @Nonnull Role tplRole, @Nonnull Tenant tenant) {
        return createRoleByTpl(parentRole, tplRole, tenant, null);
    }

    /**
     * 继承模板角色创建一个新的角色
     *
     * @param parentRole 父级角色
     * @param tplRole    模板角色
     * @param tenant     角色所属租户
     * @param adminUser  创建角色的用户；为空时，如果存在当前登录用户则用登录用户，否则默认使用系统匿名用户
     * @return 创建的角色
     */
    public Role createRoleByTpl(@Nonnull Role parentRole, @Nonnull Role tplRole, @Nonnull Tenant tenant, @Nullable User adminUser) {
        adminUser = getAdminUser(adminUser);

        Role role = new Role();
        role.setTenantId(tenant.getTenantId());
        role.setTenantNum(tenant.getTenantNum());
        role.setInheritRoleId(tplRole.getId());
        role.setParentRoleId(parentRole.getId());
        role.setLevel(tplRole.getLevel());

        setupRoleCode(role, tplRole, adminUser);
        setupRoleName(role, tplRole, adminUser);
        setupRoleLabel(role, tplRole, adminUser);

        try {
            return roleCreateInternalService.createRole(role, adminUser, true, false);
        } catch (CommonException e) {
            if (HiamError.ROLE_CODE_EXISTS.equalsIgnoreCase(e.getCode())) {
                LOGGER.warn("createRoleByRoleTpl: role code exists, role={}", role);

                Role param = new Role();
                param.setTenantId(tenant.getTenantId());
                param.setCode(role.getCode());
                param.setLevel(role.getLevel());
                return roleMapper.selectOne(param);
            }
            throw e;
        }
    }

    /**
     * 返回管理用户，如果传入的用户为 null，则返回系统匿名用户
     *
     * @param adminUser 创建角色的用户
     * @return 创建角色的用户
     */
    @Nonnull
    protected User getAdminUser(@Nullable User adminUser) {
        if (null != adminUser) {
            return adminUser;
        }

        CustomUserDetails self = DetailsHelper.getUserDetails();
        if (self == null) {
            self = DetailsHelper.getAnonymousDetails();
            LOGGER.info("use anonymous user to create role from tpl.");
        }

        adminUser = new User();
        adminUser.setId(self.getUserId());
        adminUser.setOrganizationId(self.getOrganizationId());
        adminUser.setLoginName(self.getUsername());
        adminUser.setUserType(self.getUserType());
        adminUser.setLanguage(self.getLanguage());

        return adminUser;
    }

    /**
     * 设置角色名称，默认使用模板角色的子角色名称
     *
     * @param role    新角色
     * @param tplRole 模板角色
     */
    protected void setupRoleName(Role role, Role tplRole, User adminUser) {
        List<Map<String, String>> mapList = roleMapper.selectTplRoleNameById(tplRole.getId());
        Map<String, String> tls = mapList.stream().collect(Collectors.toMap(m -> m.get("lang"), m -> m.get("name")));
        Map<String, Map<String, String>> _tls = new HashMap<>();
        _tls.put(Role.FIELD_NAME, tls);
        role.set_tls(_tls);
        role.setName(tls.get(adminUser.getLanguage()));
    }

    /**
     * 默认取模板角色编码的最后一段
     *
     * @param role    新角色
     * @param tplRole 模板角色
     */
    protected void setupRoleCode(Role role, Role tplRole, User adminUser) {
        String[] arr = StringUtils.split(tplRole.getCode(), BaseConstants.Symbol.SLASH);
        role.setCode(arr[arr.length - 1]);
    }

    /**
     * 设置角色的标签，默认继承模板角色的标签
     *
     * @param role    新角色
     * @param tplRole 模板角色
     */
    protected void setupRoleLabel(Role role, Role tplRole, User adminUser) {
        List<Label> roleLabels = Optional.ofNullable(tplRole.getRoleLabels())
                .orElse(Collections.emptyList())
                .stream().filter(label -> !LabelConstants.TENANT_ROLE_TPL.equals(label.getName()))
                .collect(Collectors.toList());

        role.setRoleLabels(roleLabels);
        role.setLabelAssignType(LabelAssignType.AUTO);
    }


}
