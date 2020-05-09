package io.choerodon.migration.domian.hzero;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.choerodon.migration.infra.util.HiamResourceLevel;
import io.choerodon.migration.infra.util.LabelAssignType;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import org.apache.commons.lang3.StringUtils;
import org.hzero.common.HZeroConstant;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Regexs;
import org.hzero.mybatis.common.query.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * 增加了继承角色ID、父级三维角色定位字段。
 *
 * @author bojiangzhou 2018/07/04
 */
@ModifyAudit
@VersionAudit
@MultiLanguage
@Table(name = "iam_role")
public class Role extends AuditDomain {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_LEVEL = "level";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_LEVEL_PATH = "levelPath";
    public static final String FIELD_INHERIT_LEVEL_PATH = "inheritLevelPath";
    public static final String FIELD_IS_ENABLED = "isEnabled";
    public static final String FIELD_ROLE_ID = "roleId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PARENT_ROLE_ID = "parentRoleId";
    public static final String FIELD_INHERIT_ROLE_ID = "inheritRoleId";
    public static final String FIELD_PARENT_ROLE_ASSIGN_LEVEL = "parentRoleAssignLevel";
    public static final String FIELD_PARENT_ROLE_ASSIGN_LEVEL_VALUE = "parentRoleAssignLevelValue";
    public static final String FIELD_BUILD_IN = "isBuiltIn";
    public static final String FIELD_CREATED_BY_TENANT_ID = "createdByTenantId";
    public static final String FIELD_TPL_ROLE_NAME = "tpl_role_name";

    public static final Long ROOT_ID = 0L;
    public static final String DEFAULT_SUPPER_ROLE_LEVEL_PATH_PREFIX = "0.organization.0";

    public static final Long SITE_ADMIN_ID = 1L;
    public static final Long TENANT_ADMIN_ID = 2L;

    public static final String ZH_CN_DEFAULT_ROLE_NAME = "管理员角色";
    public static final String EN_DEFAULT_ROLE_NAME = "Admin Role";
    public static final String ZH_CN_TPL_ROLE_NAME_SUFFIX = "模板";
    public static final String EN_TPL_ROLE_NAME_SUFFIX = "template";
    /**
     * 标签数据类型
     */
    public static final String LABEL_DATA_TYPE = "ROLE";

    public static final String SUPER_SITE_ROLE = HZeroConstant.RoleCode.SITE;
    public static final String SUPER_TENANT_ROLE = HZeroConstant.RoleCode.TENANT;
    public static final String TENANT_ROLE_TPL = HZeroConstant.RoleCode.TENANT_TEMPLATE;

    @Id
    @Where
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @MultiLanguageField
    @NotEmpty
    private String name;
    @NotEmpty
    @Pattern(regexp = Regexs.CODE_LOWER)
    @Where
    private String code;
    private String description;
    @Column(name = "fd_level")
    private String level;
    private Boolean isEnabled;

    //
    // getter/setter
    // ------------------------------------------------------------------------------
    private Boolean isModified;
    private Boolean isEnableForbidden;
    private Boolean isBuiltIn;
    private Boolean isAssignable;
    private Long objectVersionNumber;
    @Column(name = "h_tenant_id")
    private Long tenantId;
    @Column(name = "h_inherit_role_id")
    @Where
    private Long inheritRoleId;
    @Column(name = "h_parent_role_id")
    @Where
    private Long parentRoleId;
    @Column(name = "h_parent_role_assign_level")
    private String parentRoleAssignLevel;
    @Column(name = "h_parent_role_assign_level_val")
    private Long parentRoleAssignLevelValue;
    /**
     * 父子层级关系
     */
    @Column(name = "h_level_path")
    @Where
    private String levelPath;
    /**
     * 继承层级关系
     */
    @Column(name = "h_inherit_level_path")
    private String inheritLevelPath;
    /**
     * 创建者租户ID
     */
    private Long createdByTenantId;
    /**
     * 模板角色的子角色名称
     */
    @MultiLanguageField
    private String tplRoleName;

    /**
     * 角色权限集关系
     */
    @Transient
    private Integer userCount;
    @Transient
    private Long copyFromRoleId;
    @Transient
    private String tenantNum;
    @Transient
    private String parentRoleAssignLevelCode;
    @Transient
    private String createdByTenantNum;
    @Transient
    @JsonIgnore
    private Role parentRole;
    @Transient
    @JsonIgnore
    private List<Role> createdSubRoles;
    @Transient
    @JsonIgnore
    private Role inheritRole;
    @Transient
    @JsonIgnore
    private Role copyRole;
    @Transient
    @JsonIgnore
    private List<Role> inheritSubRoles;
    @Transient
    private List<Label> roleLabels;
    @Transient
    private List<RolePermission> permissionSets = new ArrayList<>();

    public List<RolePermission> getPermissionSets() {
        return permissionSets;
    }

    public void setPermissionSets(List<RolePermission> permissionSets) {
        this.permissionSets = permissionSets;
    }

    @Transient
    @JsonIgnore
    private LabelAssignType labelAssignType;

    public LabelAssignType getLabelAssignType() {
        return labelAssignType;
    }

    public void setLabelAssignType(LabelAssignType labelAssignType) {
        this.labelAssignType = labelAssignType;
    }

    /**
     * 设置父级分配层级
     *
     * @param parentMemberRole 父级用户角色
     */
    public void setupParentAssignLevel(MemberRole parentMemberRole) {
        this.parentRoleAssignLevel = parentMemberRole.getAssignLevel();
        this.parentRoleAssignLevelValue = parentMemberRole.getAssignLevelValue();

    }

    public void buildCreatedRoleLevelPath(Role parentRole) {
        String path = generatePath(parentRole);
        this.levelPath = String.format("%s%s%s", parentRole.getLevelPath(), BaseConstants.PATH_SEPARATOR, path);
    }

    /**
     * 构建levelPath
     *
     * @param parentRole  父级角色
     * @param inheritRole 继承角色
     */
    public void buildInheritRoleLevelPath(Role parentRole, Role inheritRole) {
        String path = generatePath(parentRole);

        if (inheritRole != null) {
            this.inheritLevelPath = String.format("%s%s%s", inheritRole.getInheritLevelPath(), BaseConstants.PATH_SEPARATOR, path);
        } else {
            this.inheritLevelPath = path;
        }
    }

    private String generatePath(Role parentRole) {
        List<String> options = new ArrayList<>(8);

        // 父级角色 为平台级 或者 0 租户的租户级角色时，加角色租户编码
        if (parentRole == null
                || HiamResourceLevel.SITE.value().equals(parentRole.getLevel())
                || (Objects.equals(parentRole.getTenantId(), BaseConstants.DEFAULT_TENANT_ID) && HiamResourceLevel.ORGANIZATION.value().equals(parentRole.getLevel()))) {
            options.add(this.tenantNum);
        }
        // 父级分配层级简码
        options.add(HiamResourceLevel.levelOf(this.parentRoleAssignLevel).simpleCode());
        // 父级分配层级非租户级时，加分配层级值
        if (!HiamResourceLevel.ORGANIZATION.value().equals(this.parentRoleAssignLevel)) {
            options.add(this.parentRoleAssignLevelCode);
        }
        // 创建者租户与角色租户不一致时，加创建者租户
        if (!Objects.equals(this.createdByTenantId, this.tenantId)) {
            options.add(this.createdByTenantNum);
        }
        // 角色编码
        options.add(this.code);

        return StringUtils.join(options, ".");
    }
    /**
     * path 规则：角色租户编码.父级分配层级.父级分配层级编码.创建者租户编码.当前角色代码
     */
//    private String generatePath(Role parentRole) {
//        List<String> options = new ArrayList<>(8);
//
//        // 父级角色 为平台级 或者 0 租户的租户级角色时，加角色租户编码
//        if (parentRole == null
//                || HiamResourceLevel.SITE.value().equals(parentRole.getLevel())
//                || (Objects.equals(parentRole.getTenantId(), BaseConstants.DEFAULT_TENANT_ID) && HiamResourceLevel.ORGANIZATION.value().equals(parentRole.getLevel()))) {
//            options.add(this.tenantNum);
//        }
//        // 父级分配层级简码
//        options.add(HiamResourceLevel.levelOf(this.parentRoleAssignLevel).simpleCode());
//        // 父级分配层级非租户级时，加分配层级值
//        if (!HiamResourceLevel.ORGANIZATION.value().equals(this.parentRoleAssignLevel)) {
//            options.add(this.parentRoleAssignLevelCode);
//        }
//        // 创建者租户与角色租户不一致时，加创建者租户
//        if (!Objects.equals(this.createdByTenantId, this.tenantId)) {
//            options.add(this.createdByTenantNum);
//        }
//        // 角色编码
//        options.add(this.code);
//
//        return StringUtils.join(options, ".");
//    }

    /**
     * 初始化超级管理员角色的路径
     */
    public void initSupperRoleLevelPath() {
        this.levelPath = this.code;
        this.inheritLevelPath = this.code;
    }

    /**
     * 通过角色模板初始化角色名称
     *
     * @param language 当前上下文语言环境
     * @param roleTpl  角色模板
     */
    public void initRoleNameByRoleTpl(String language, Role roleTpl) {
        if (Locale.SIMPLIFIED_CHINESE.toString().equals(language)) {
            this.name = StringUtils.isBlank(roleTpl.getName()) ? ZH_CN_DEFAULT_ROLE_NAME
                    : roleTpl.getName().replace(ZH_CN_TPL_ROLE_NAME_SUFFIX, "");
        } else {
            this.name = StringUtils.isBlank(roleTpl.getName()) ? EN_DEFAULT_ROLE_NAME
                    : roleTpl.getName().replace(EN_TPL_ROLE_NAME_SUFFIX, "");
        }
    }

    public List<Label> getRoleLabels() {
        return roleLabels;
    }

    public void setRoleLabels(List<Label> roleLabels) {
        this.roleLabels = roleLabels;
    }


    public Long getId() {
        return id;
    }

    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public Role setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getModified() {
        return isModified;
    }

    public void setModified(Boolean modified) {
        isModified = modified;
    }

    public Boolean getEnableForbidden() {
        return isEnableForbidden;
    }

    public void setEnableForbidden(Boolean enableForbidden) {
        isEnableForbidden = enableForbidden;
    }

    public Boolean getBuiltIn() {
        return isBuiltIn;
    }

    public void setBuiltIn(Boolean builtIn) {
        isBuiltIn = builtIn;
    }

    public Boolean getAssignable() {
        return isAssignable;
    }

    public void setAssignable(Boolean assignable) {
        isAssignable = assignable;
    }

    @Override
    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    @Override
    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    /**
     * @return 租户ID. 0 则为全局
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 继承角色ID
     */
    public Long getInheritRoleId() {
        return inheritRoleId;
    }

    public void setInheritRoleId(Long inheritRoleId) {
        this.inheritRoleId = inheritRoleId;
    }

    /**
     * @return 父级角色标识
     */
    public Long getParentRoleId() {
        return parentRoleId;
    }

    public void setParentRoleId(Long parentRoleId) {
        this.parentRoleId = parentRoleId;
    }

    /**
     * @return 父级角色分配层级
     * @see MemberRole#getAssignLevel()
     */
    public String getParentRoleAssignLevel() {
        return parentRoleAssignLevel;
    }

    public void setParentRoleAssignLevel(String parentRoleAssignLevel) {
        this.parentRoleAssignLevel = parentRoleAssignLevel;
    }

    /**
     * @return 父级角色分配层级值
     * @see MemberRole#getAssignLevelValue()
     */
    public Long getParentRoleAssignLevelValue() {
        return parentRoleAssignLevelValue;
    }

    public void setParentRoleAssignLevelValue(Long parentRoleAssignLevelValue) {
        this.parentRoleAssignLevelValue = parentRoleAssignLevelValue;
    }


    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Long getCopyFromRoleId() {
        return copyFromRoleId;
    }

    public void setCopyFromRoleId(Long copyFromRoleId) {
        this.copyFromRoleId = copyFromRoleId;
    }

    public String getLevelPath() {
        return levelPath;
    }

    public Role setLevelPath(String levelPath) {
        this.levelPath = levelPath;
        return this;
    }

    public String getInheritLevelPath() {
        return inheritLevelPath;
    }

    public void setInheritLevelPath(String inheritLevelPath) {
        this.inheritLevelPath = inheritLevelPath;
    }

    public Long getCreatedByTenantId() {
        return createdByTenantId;
    }

    public void setCreatedByTenantId(Long createdByTenantId) {
        this.createdByTenantId = createdByTenantId;
    }

    public String getTplRoleName() {
        return tplRoleName;
    }

    public void setTplRoleName(String tplRoleName) {
        this.tplRoleName = tplRoleName;
    }

    public Role getParentRole() {
        return parentRole;
    }

    public void setParentRole(Role parentRole) {
        this.parentRole = parentRole;
    }

    public List<Role> getCreatedSubRoles() {
        return createdSubRoles;
    }

    public void setCreatedSubRoles(List<Role> createdSubRoles) {
        this.createdSubRoles = createdSubRoles;
    }

    public Role getInheritRole() {
        return inheritRole;
    }

    public void setInheritRole(Role inheritRole) {
        this.inheritRole = inheritRole;
    }

    public Role getCopyRole() {
        return copyRole;
    }

    public void setCopyRole(Role copyRole) {
        this.copyRole = copyRole;
    }

    public List<Role> getInheritSubRoles() {
        return inheritSubRoles;
    }

    public void setInheritSubRoles(List<Role> inheritSubRoles) {
        this.inheritSubRoles = inheritSubRoles;
    }

    public String getTenantNum() {
        return tenantNum;
    }

    public void setTenantNum(String tenantNum) {
        this.tenantNum = tenantNum;
    }

    public String getParentRoleAssignLevelCode() {
        return parentRoleAssignLevelCode;
    }

    public void setParentRoleAssignLevelCode(String parentRoleAssignLevelCode) {
        this.parentRoleAssignLevelCode = parentRoleAssignLevelCode;
    }

    public String getCreatedByTenantNum() {
        return createdByTenantNum;
    }

    public void setCreatedByTenantNum(String createdByTenantNum) {
        this.createdByTenantNum = createdByTenantNum;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", level='" + level + '\'' +
                ", isEnabled=" + isEnabled +
                ", isModified=" + isModified +
                ", isEnableForbidden=" + isEnableForbidden +
                ", isBuiltIn=" + isBuiltIn +
                ", isAssignable=" + isAssignable +
                ", objectVersionNumber=" + objectVersionNumber +
                ", tenantId=" + tenantId +
                ", inheritRoleId=" + inheritRoleId +
                ", parentRoleId=" + parentRoleId +
                ", parentRoleAssignLevel='" + parentRoleAssignLevel + '\'' +
                ", parentRoleAssignLevelValue=" + parentRoleAssignLevelValue +
                ", levelPath='" + levelPath + '\'' +
                ", inheritLevelPath='" + inheritLevelPath + '\'' +
                ", copyFromRoleId=" + copyFromRoleId +
                ", parentRole=" + parentRole +
                '}';
    }
}
