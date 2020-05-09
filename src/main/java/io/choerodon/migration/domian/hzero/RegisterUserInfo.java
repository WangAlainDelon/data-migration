package io.choerodon.migration.domian.hzero;

import io.choerodon.migration.domian.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: Mr.Wang
 * Date: 2020/5/8
 */
@Table(name = "iam_register_user_info")
public class RegisterUserInfo extends AuditDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String userName;
    private String organizationName;
    private String organizationHomePage;
    private String organizationPosition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationHomePage() {
        return organizationHomePage;
    }

    public void setOrganizationHomePage(String organizationHomePage) {
        this.organizationHomePage = organizationHomePage;
    }

    public String getOrganizationPosition() {
        return organizationPosition;
    }

    public void setOrganizationPosition(String organizationPosition) {
        this.organizationPosition = organizationPosition;
    }
}
