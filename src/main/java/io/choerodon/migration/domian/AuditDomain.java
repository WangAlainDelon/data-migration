package io.choerodon.migration.domian;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * User: Mr.Wang
 * Date: 2020/4/27
 */
public class AuditDomain {
    @ApiModelProperty(
            hidden = true
    )
    private Date creationDate;
    @ApiModelProperty(
            hidden = true
    )
    private Long createdBy;
    @ApiModelProperty(
            hidden = true
    )
    private Date lastUpdateDate;
    @ApiModelProperty(
            hidden = true
    )
    private Long lastUpdatedBy;
    @ApiModelProperty(
            hidden = true
    )
    private Long objectVersionNumber;

    public AuditDomain() {
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Long getObjectVersionNumber() {
        return this.objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }
}
