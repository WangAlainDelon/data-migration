package io.choerodon.migration.domian.hzero;

import io.choerodon.migration.domian.AuditDomain;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Mr.Wang
 * Date: 2020/4/30
 */
@Table(name = "fd_project_relationship")
public class ProjectRelationship extends AuditDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private Long parentId;

    private Long ResponsibilityUserId;

    private Long programId;

    private Date startDate;

    private Date endDate;

    @Column(name = "is_enabled")
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getResponsibilityUserId() {
        return ResponsibilityUserId;
    }

    public void setResponsibilityUserId(Long responsibilityUserId) {
        ResponsibilityUserId = responsibilityUserId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}