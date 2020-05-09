package io.choerodon.migration.domian.hzero;

import io.choerodon.migration.infra.util.RolePermissionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Set;

/**
 * 角色权限集关系，permission_id 用来表示权限集，Choerodon表示的是权限，注意区分。
 */
@ApiModel("角色权限关系")
@Table(name = "iam_role_permission")
public class RolePermission {

    public static final String FIELD_ROLE_ID = "roleId";
    public static final String FIELD_CREATE_FLAG = "createFlag";
    public static final String FIELD_INHERIT_FLAG = "inheritFlag";

    /**
     * 默认为权限集类型
     */
    public static final RolePermissionType DEFAULT_TYPE = RolePermissionType.PS;

    public RolePermission() {}

    /**
     *
     * @param roleId 角色ID
     * @param permissionSetId 权限集ID
     * @param inheritFlag 继承标识
     * @param createFlag 创建标识
     */
    public RolePermission(Long roleId, Long permissionSetId, String inheritFlag, String createFlag, String type) {
        this.roleId = roleId;
        this.permissionSetId = permissionSetId;
        this.inheritFlag = inheritFlag;
        this.createFlag = createFlag;
        this.type = type;
    }

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty("角色ID")
    private Long roleId;
    /**
     * 该权限ID表示权限集ID
     */
    @Column(name = "permission_id")
    @ApiModelProperty("权限ID")
    private Long permissionSetId;
    @Column(name = "type")
    @ApiModelProperty("权限类型")
    private String type;
    @Column(name = "h_create_flag")
    @ApiModelProperty("创建标识")
    private String createFlag;
    @Column(name = "h_inherit_flag")
    @ApiModelProperty("继承标识")
    private String inheritFlag;

    @Transient
    @ApiModelProperty("角色编码")
    private String roleCode;
    @Transient
    @ApiModelProperty("角色唯一路径")
    private String levelPath;
    @Transient
    @ApiModelProperty("权限集合")
    private Set<Long> permissionSetIds;
    @Transient
    @ApiModelProperty("权限层级")
    private String level;
    @Transient
    private boolean bothCreateAndInheritFlag;


    public Long getId() {
        return id;
    }

    public RolePermission setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getRoleId() {
        return roleId;
    }

    public RolePermission setRoleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public Long getPermissionSetId() {
        return permissionSetId;
    }

    public RolePermission setPermissionSetId(Long permissionSetId) {
        this.permissionSetId = permissionSetId;
        return this;
    }

    public String getType() {
        return type;
    }

    public RolePermission setType(String type) {
        this.type = type;
        return this;
    }

    public String getCreateFlag() {
        return createFlag;
    }

    public RolePermission setCreateFlag(String createFlag) {
        this.createFlag = createFlag;
        return this;
    }

    public String getInheritFlag() {
        return inheritFlag;
    }

    public RolePermission setInheritFlag(String inheritFlag) {
        this.inheritFlag = inheritFlag;
        return this;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public RolePermission setRoleCode(String roleCode) {
        this.roleCode = roleCode;
        return this;
    }

    public String getLevelPath() {
        return levelPath;
    }

    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath;
    }

    public Set<Long> getPermissionSetIds() {
        return permissionSetIds;
    }

    public RolePermission setPermissionSetIds(Set<Long> permissionSetIds) {
        this.permissionSetIds = permissionSetIds;
        return this;
    }

    public String getLevel() {
        return level;
    }

    public RolePermission setLevel(String level) {
        this.level = level;
        return this;
    }

    public boolean isBothCreateAndInheritFlag() {
        return bothCreateAndInheritFlag;
    }

    public RolePermission setBothCreateAndInheritFlag(boolean bothCreateAndInheritFlag) {
        this.bothCreateAndInheritFlag = bothCreateAndInheritFlag;
        return this;
    }
}
