package io.choerodon.migration.domian.service;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.migration.domian.hzero.*;
import io.choerodon.migration.domian.vo.RoleVO;
import io.choerodon.migration.infra.util.*;
import io.choerodon.migration.mapper.hzero.platform.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色创建相关核心业务抽象类
 *
 * @author bojiangzhou 2019/01/23
 */
public abstract class RoleCreateService extends AbstractRoleCommonService {
    @Autowired
    protected TenantMapper tenantMapper;

    @Autowired
    private UserHzeroMapper userHzeroMapper;

    @Autowired
    private MemberRoleHzeroMapper memberRoleHzeroMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleCreateService.class);

    protected void checkAdminUser(Role role, User adminUser) {
        if (adminUser == null || adminUser.getId() == null) {
            throw new CommonException("hiam.warn.role.adminUserIsNull");
        }

        User tmp = userHzeroMapper.selectUserTenant(adminUser.getId());
        if (tmp == null) {
            throw new CommonException("hiam.warn.role.userWithTenantNotFound");
        }
        adminUser.setTenantNum(tmp.getTenantNum());
        adminUser.setOrganizationId(tmp.getOrganizationId());

        role.setCreatedByTenantId(adminUser.getOrganizationId());
        role.setCreatedByTenantNum(adminUser.getTenantNum());
    }


    public Role createRole(Role role, User adminUser, boolean inherited, boolean duplicate) {
        LOGGER.info(">> create role before: role={}, adminUser={}, inherited={}, duplicate={}",
                role, adminUser, inherited, duplicate);

        // 检查管理用户
        checkAdminUser(role, adminUser);

        // 检查创建类型
        checkCreateType(role, inherited, duplicate);

        // 检查管理角色
        checkAdminRole(role, adminUser);

        // 检查角色所属租户
        checkRoleTenant(role);

        // 检查角色层级
        checkRoleLevel(role);

        // 检查角色名称
        checkRoleName(role);

        // 检查继承的角色
        if (inherited) {
            checkInheritRoleRole(role);
        }
        // 检查复制的角色
        else if (duplicate) {
            checkDuplicateRole(role);
        }

        // 生成角色编码
        setupRoleCode(role, adminUser);

        // 检查角色是否存在
        checkRoleExists(role, adminUser);

        // 处理角色路径
        handleRoleLevelPath(role);

        // 创建前处理
        handleBeforeCreate(role, adminUser);

        // 创建角色
        persistRole(role);

        // 处理角色权限
        handleRolePermission(role);

        // 20200226 处理角色标签
        handleRoleLabels(role);

        // 角色处理完成，最后处理的一些事情
        postHandle(role);

        LOGGER.info(">> create role success: {}", role);

        return role;
    }

    protected void checkCreateType(Role role, boolean inherited, boolean duplicate) {
        // 继承角色创建
        if (inherited) {
            if (role.getInheritRoleId() == null) {
                throw new CommonException("hiam.warn.role.inheritedRoleIdMustNotBeNull");
            }
            role.setCopyFromRoleId(null);
        }
        // 复制角色创建
        else if (duplicate) {
            if (role.getCopyFromRoleId() == null) {
                throw new CommonException("hiam.warn.role.duplicateRoleIdMustNotBeNull");
            }
            role.setInheritRoleId(null);
        }
        // 直接创建角色
        else {
            role.setCopyFromRoleId(null);
            role.setInheritRoleId(null);
        }
    }

    protected Role checkAdminRole(Role role, User adminUser) {
        // 父级角色
        Role parentRole = roleMapper.selectByPrimaryKey(role.getParentRoleId());

        if (parentRole == null) {
            throw new CommonException("hiam.warn.role.parentRoleNotFound");
        }

        // 当前用户的管理角色
        RoleVO params = new RoleVO();
        params.setId(parentRole.getId());

        // 校验是否是管理角色
        CustomUserDetails self = UserUtils.getUserDetails();

        params = Optional.ofNullable(params).orElse(new RoleVO());
        params.setUserId(self.getUserId());
        params.setUserTenantId(self.getTenantId());
        params.setUserOrganizationId(self.getOrganizationId());
        List<RoleVO> selfAdminRoles = roleMapper.selectUserAdminRoles(params);

        if (CollectionUtils.isEmpty(selfAdminRoles)) {
            throw new CommonException("hiam.warn.role.parentRoleIsNotAdminRole");
        }

        // 查询父级角色的用户角色分配关系
        MemberRole memberRole = new MemberRole();
        memberRole.setRoleId(role.getParentRoleId());
        memberRole.setMemberId(adminUser.getId());
        memberRole.setMemberType(HiamMemberType.USER.value());
        MemberRole memberRole1 = memberRoleHzeroMapper.selectMemberRole(memberRole);

        if (memberRole1 == null) {
            throw new CommonException("hiam.warn.role.memberRoleNotFound");
        }
        // 设置父级分配层级值
        role.setupParentAssignLevel(memberRole1);

        role.setParentRole(parentRole);

        return parentRole;
    }

    protected void checkRoleTenant(Role role) {
        Role adminRole = role.getParentRole();
        // 0 租户的租户级角色可以选择其它租户
        if (Objects.equals(adminRole.getTenantId(), BaseConstants.DEFAULT_TENANT_ID) &&
                StringUtils.equals(adminRole.getLevel(), HiamResourceLevel.ORGANIZATION.value())) {
            // 继承创建，租户ID=继承角色租户ID
            if (role.getInheritRole() != null) {
                if (!Objects.equals(role.getTenantId(), role.getInheritRole().getTenantId())) {
                    throw new CommonException("hiam.warn.role.roleTenantIdEqualsInheritRoleTenantId");
                }
            }
        } else {
            // 非继承，租户ID=父级角色ID
            if (!Objects.equals(role.getTenantId(), adminRole.getTenantId())) {
                throw new CommonException("hiam.warn.role.roleTenantIdEqualsParentRoleTenantId");
            }
        }
        // 默认平台租户
        if (role.getTenantId() == null) {
            role.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        }
        // 租户
        if (StringUtils.isBlank(role.getTenantNum())) {
            Tenant tenant = tenantMapper.selectByPrimaryKey(role.getTenantId());
            tenant = Optional.ofNullable(tenant).orElseThrow(() -> new CommonException("hiam.warn.role.tenantNotFound"));
            role.setTenantNum(tenant.getTenantNum());
        }
    }


    protected void checkRoleLevel(Role role) {
        Role adminRole = role.getParentRole();

        if (StringUtils.isNotBlank(role.getLevel()) &&
                !StringUtils.equalsIgnoreCase(role.getLevel(), adminRole.getLevel())) {
            throw new CommonException("hiam.warn.role.roleLevelEqualsAdminRoleLevel");
        } else {
            role.setLevel(adminRole.getLevel());
        }
    }

    protected void checkRoleName(Role role) {
        // 角色名称不能重复
        //roleRepository.checkRoleNameUnique(role.getParentRoleId(), role.getTenantId(), role.getLevel(), role.getName());
    }


    protected void checkInheritRoleRole(Role role) {
        Role inheritRole = roleMapper.selectByPrimaryKey(role.getInheritRoleId());
        if (inheritRole == null) {
            throw new CommonException("hiam.warn.role.inheritedRoleNotFound");
        }
        role.setCopyFromRoleId(null);
        role.setInheritRole(inheritRole);

        // 检查本次创建的角色 是否通过 禁用继承角色 的角色创建而来
//        String nonInheritableLevelPath = profileClient.getProfileValueByOptions(inheritRole.getTenantId(),null,
//                role.getInheritRoleId(),Constants.Config.CONFIG_CODE_ROLE_DISABLE_INHERIT);
//        if(nonInheritableLevelPath != null && nonInheritableLevelPath.equals(inheritRole.getLevelPath())) {
//            throw new CommonException("hiam.warn.role.inheritedRoleDisabled");
//        }
    }

    protected void checkDuplicateRole(Role role) {
        Role duplicateRole = roleMapper.selectByPrimaryKey(role.getCopyFromRoleId());
        if (duplicateRole == null) {
            throw new CommonException("hiam.warn.role.duplicateRoleNotFound");
        }
        role.setInheritRoleId(null);
        role.setCopyRole(duplicateRole);
    }


    protected void setupRoleCode(Role role, User adminUser) {
        // 根据层级取出前缀
        //String prefix = HiamResourceLevel.levelOf(role.getLevel()).code();
        //role.setCode(prefix + BaseConstants.Symbol.SLASH + role.getCode());
    }

    protected void checkRoleExists(Role role, User adminUser) {
        Role params = new Role();

        params.setTenantId(role.getTenantId());
        params.setCode(role.getCode());
        params.setParentRoleId(role.getParentRoleId());
        params.setParentRoleAssignLevel(role.getParentRoleAssignLevel());
        params.setParentRoleAssignLevelValue(role.getParentRoleAssignLevelValue());
        params.setCreatedByTenantId(adminUser.getOrganizationId());

        // 判断编码是否重复
        if (roleMapper.selectCount(params) > 0) {
            throw new CommonException(HiamError.ROLE_CODE_EXISTS);
        }
    }

    protected void handleRoleLevelPath(Role role) {
        role.buildCreatedRoleLevelPath(role.getParentRole());
        role.buildInheritRoleLevelPath(role.getParentRole(), role.getInheritRole());
    }

    protected void handleBeforeCreate(Role role, User adminUser) {
        role.setBuiltIn(false);
        role.setEnabled(true);
        role.setAssignable(false);
        role.setEnableForbidden(true);
        role.setModified(true);
        role.setCreatedBy(adminUser.getId());
        role.setCreatedByTenantId(adminUser.getOrganizationId());
    }
    protected void persistRole(Role role) {
        roleMapper.insertSelective(role);
    }

    protected void handleRolePermission(Role role) {
        // 处理 roleId
        role.setPermissionSets(new ArrayList<>(1024));
        // 如果是继承角色，复制继承的权限集，并打上继承标识
        if (role.getInheritRole() != null) {
            initInheritRolePermissionSets(role);
        }
        // 如果是复制角色，复制角色的权限集，并打上创建标识
        else if (role.getCopyRole() != null) {
            initDuplicateRolePermissionSets(role);
        }

        // 维护role_permission表
        LOGGER.info("Assign role permission, roleId is {}, permissionSet size is {}", role.getId(), role.getPermissionSets().size());
        if (CollectionUtils.isNotEmpty(role.getPermissionSets())) {
            rolePermissionMapper.batchInsertBySql(role.getPermissionSets());
        }
        //role.getPermissionSets().parallelStream().forEach(rps -> {
        //    rolePermissionRepository.insertSelective(rps);
        //});
    }

    /**
     * 初始化继承权限集
     */
    protected void initInheritRolePermissionSets(Role role) {
        RolePermission params = new RolePermission();
        params
                .setRoleId(role.getInheritRoleId())
                .setLevel(role.getLevel())
        ;

        // 查询出继承的权限集
        // 设置默认类型为权限集类型，必输项
        if (StringUtils.isBlank(params.getType())) {
            params.setType(RolePermission.DEFAULT_TYPE.name());
        }

        List<RolePermission> inheritRolePermissionSets = rolePermissionMapper.selectRolePermissionSets(params);

        // 当前角色继承的权限
        List<RolePermission> rolePermissionSets = new ArrayList<>(inheritRolePermissionSets.size());
        Long roleId = role.getId();

        // 处理继承的权限
        inheritRolePermissionSets.forEach(rps -> {
            // 被继承的角色存在此权限集, 则继承=Y, 否则,说明被逻辑删除了, 则继承=X
            String inheritFlag;
            if (StringUtils.equalsAny(Constants.YesNoFlag.YES, rps.getCreateFlag(), rps.getInheritFlag())) {
                inheritFlag = Constants.YesNoFlag.YES;
            } else {
                inheritFlag = Constants.YesNoFlag.DELETE;
            }

            // 添加权限集
            rolePermissionSets.add(new RolePermission(roleId, rps.getPermissionSetId(), inheritFlag,
                    Constants.YesNoFlag.NO, RolePermissionType.PS.name()));
        });

        LOGGER.info("inherit {} permissions from role.", rolePermissionSets.size());

        role.getPermissionSets().addAll(rolePermissionSets);
    }

    protected void initDuplicateRolePermissionSets(Role role) {
        RolePermission params = new RolePermission();
        params
                .setRoleId(role.getCopyFromRoleId())
                .setLevel(role.getLevel())
        ;

        // 查询出复制的权限
        List<RolePermission> duplicateRolePermissionSets = rolePermissionMapper.selectRolePermissionSets(params);

        List<RolePermission> rolePermissionSets = new ArrayList<>(duplicateRolePermissionSets.size());
        Long roleId = role.getId();

        // 处理复制的权限
        duplicateRolePermissionSets.forEach(item -> {
            // 只有被复制角色中权限正常, 才可以复制过来
            if (StringUtils.equalsAny(Constants.YesNoFlag.YES, item.getCreateFlag(), item.getInheritFlag())) {
                rolePermissionSets.add(new RolePermission(roleId, item.getPermissionSetId(), Constants.YesNoFlag.NO,
                        Constants.YesNoFlag.YES, RolePermissionType.PS.name()));
            }
        });

        LOGGER.info("copy {} permissions from role.", rolePermissionSets.size());

        role.getPermissionSets().addAll(rolePermissionSets);
    }

    @Override
    protected void handleRoleLabels(Role role) {
        Set<Long> roleIds = new HashSet<>(4);
        // 父级角色
        Role parentRole = role.getParentRole();
        // 继承角色
        Role inheritRole = role.getInheritRole();
        if (Objects.nonNull(parentRole)) {
            roleIds.add(parentRole.getId());
        }
        if (Objects.nonNull(inheritRole)) {
            roleIds.add(inheritRole.getId());
        }

        if (CollectionUtils.isEmpty(roleIds)) {
            super.handleRoleLabels(role);
            return;
        }

        // 查询可继承的标签IDs
        Set<Long> inheritLabelIds = this.labelRelRepository.selectInheritLabelIdsByDataTypeAndDataIds(Role.LABEL_DATA_TYPE, roleIds);
        if (CollectionUtils.isNotEmpty(inheritLabelIds)) {
            // 分配标签
            this.labelRelRepository.addLabels(Role.LABEL_DATA_TYPE, role.getId(), LabelAssignType.AUTO, inheritLabelIds);

            // 获取角色标签视图
            List<Label> roleLabels = Optional.ofNullable(role.getRoleLabels()).orElse(Collections.emptyList());
            // 将可继承的标签加到视图标签中
            roleLabels.addAll(inheritLabelIds.stream().map(labelId -> {
                Label label = new Label();
                label.setId(labelId);
                return label;
            }).collect(Collectors.toSet()));
            // 设置标签
            role.setRoleLabels(roleLabels);
        }

        // 处理角色标签字段的标签
        super.handleRoleLabels(role);
    }

    protected void postHandle(Role role) {
        role.setPermissionSets(null);
    }
}
