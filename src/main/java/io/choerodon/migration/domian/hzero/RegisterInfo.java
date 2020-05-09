package io.choerodon.migration.domian.hzero;


import io.choerodon.migration.domian.AuditDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "fd_register_info")
public class RegisterInfo extends AuditDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String userName;

    private String userPhone;

    private String userEmail;

    private String userOrgName;

    private String userOrgHomePage;

    private String userOrgPosition;

    private String userToken;

    private String orgName;

    private String orgHomePage;

    private String orgEmailSuffix;

    private Integer orgScale;

    private String orgBusinessType;

    private Date registerDate;

    private String approvalStatus;

    private String approvalMessage;

    private Long approvalUserId;

    private Date approvalDate;

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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserOrgName() {
        return userOrgName;
    }

    public void setUserOrgName(String userOrgName) {
        this.userOrgName = userOrgName;
    }

    public String getUserOrgHomePage() {
        return userOrgHomePage;
    }

    public void setUserOrgHomePage(String userOrgHomePage) {
        this.userOrgHomePage = userOrgHomePage;
    }

    public String getUserOrgPosition() {
        return userOrgPosition;
    }

    public void setUserOrgPosition(String userOrgPosition) {
        this.userOrgPosition = userOrgPosition;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgHomePage() {
        return orgHomePage;
    }

    public void setOrgHomePage(String orgHomePage) {
        this.orgHomePage = orgHomePage;
    }

    public String getOrgEmailSuffix() {
        return orgEmailSuffix;
    }

    public void setOrgEmailSuffix(String orgEmailSuffix) {
        this.orgEmailSuffix = orgEmailSuffix;
    }

    public Integer getOrgScale() {
        return orgScale;
    }

    public void setOrgScale(Integer orgScale) {
        this.orgScale = orgScale;
    }

    public String getOrgBusinessType() {
        return orgBusinessType;
    }

    public void setOrgBusinessType(String orgBusinessType) {
        this.orgBusinessType = orgBusinessType;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalMessage() {
        return approvalMessage;
    }

    public void setApprovalMessage(String approvalMessage) {
        this.approvalMessage = approvalMessage;
    }

    public Long getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(Long approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }
}
