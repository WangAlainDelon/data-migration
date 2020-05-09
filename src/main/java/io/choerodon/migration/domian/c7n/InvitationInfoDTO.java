package io.choerodon.migration.domian.c7n;

import io.choerodon.migration.domian.AuditDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jiameng.cao
 * @date 2019/10/14
 */
@Table(name = "fd_invitation_info")
public class InvitationInfoDTO extends AuditDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "邀请用户邮箱")
    private String email;

    @ApiModelProperty(value = "邀请用户进入的组织")
    private Long orgId;

    @ApiModelProperty(value = "邀请用户被分配了角色的项目id")
    private Long projectId;

    @ApiModelProperty(value = "邀请用户被分配的角色id集合")
    private String roleId;

    @ApiModelProperty(value = "链接token")
    private String urlToken;

    @ApiModelProperty(value = "链接到期时间")
    private Date urlEndDate;

    @ApiModelProperty(value = "是否有效")
    @Column(name = "is_enabled")
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUrlToken() {
        return urlToken;
    }

    public void setUrlToken(String urlToken) {
        this.urlToken = urlToken;
    }

    public Date getUrlEndDate() {
        return urlEndDate;
    }

    public void setUrlEndDate(Date urlEndDate) {
        this.urlEndDate = urlEndDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
