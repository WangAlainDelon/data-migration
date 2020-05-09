package io.choerodon.migration.domian.hzero;

import io.choerodon.migration.domian.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;



@Table(name = "iam_member_role")
@ApiModel("成员角色")
public class MemberRole extends AuditDomain {


    //
    // getter/setter
    // ------------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty("角色ID，传入角色ID或角色编码")
    private Long roleId;
    @ApiModelProperty("成员ID")
    private Long memberId;
    @ApiModelProperty("成员类型，用户-user/客户端-client")
    private String memberType;
    private Long sourceId;
    private String sourceType;
    /**
     * 子账户导入-角色导入所需字段
     * 废弃 2019-11-26
     */
    @Column(name = "h_assign_level")
    @ApiModelProperty("分配层级，租户层-organization/组织层-org")
    private String assignLevel;
    @ApiModelProperty("分配层级值，租户层-角色所属租户ID/组织层-组织ID")
    @Column(name = "h_assign_level_value")
    private Long assignLevelValue;

    @Transient
    private String assignLevelCode;

    public String getAssignLevelCode() {
        return assignLevelCode;
    }

    public void setAssignLevelCode(String assignLevelCode) {
        this.assignLevelCode = assignLevelCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    /**
     * @return 角色分配层级
     */
    public String getAssignLevel() {
        return assignLevel;
    }

    public void setAssignLevel(String assignLevel) {
        this.assignLevel = assignLevel;
    }

    /**
     * @return 角色分配层级值
     */
    public Long getAssignLevelValue() {
        return assignLevelValue;
    }

    public void setAssignLevelValue(Long assignLevelValue) {
        this.assignLevelValue = assignLevelValue;
    }

}
