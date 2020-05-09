package io.choerodon.migration.domian.hzero;

import io.choerodon.migration.domian.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hzero.core.user.UserType;
import org.hzero.core.util.Regexs;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 与UserE实体基本不变，扩展了{@link UserInfo}，与其一对一关系.
 *
 * @author bojiangzhou 2018/07/04
 */
@Table(name = "iam_user")
@Component
public class User extends AuditDomain {


    //
    // getter/setter
    // ------------------------------------------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 128)
    @Pattern(regexp = Regexs.CODE)
    @ApiModelProperty("登录账号，未传则生成默认账号")
    private String loginName;

    /**
     * 子账户导入-账户导入所需字段
     */
    @Length(max = 128)
    @Email
    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty(value = "所属租户ID", required = true)
    private Long organizationId;

    /**
     * 子账户导入-账户导入所需字段
     */
    @Column(name = "hash_password")
    @ApiModelProperty(value = "密码", required = true)
    @Length(max = 128)
    private String password;

    /**
     * 子账户导入-账户导入所需字段
     */
    @ApiModelProperty(value = "真实姓名", required = true)
    @Length(max = 128)
    private String realName;

    /**
     * 子账户导入-账户导入所需字段
     */
    @ApiModelProperty(value = "手机号")
    @Length(max = 32)
    private String phone;

    @ApiModelProperty(value = "国际冠码，默认+86")
    private String internationalTelCode;

    @ApiModelProperty(value = "用户头像地址")
    @Length(max = 480)
    private String imageUrl;

    @ApiModelProperty(value = "用户二进制头像")
    private String profilePhoto;

    @ApiModelProperty(value = "语言，默认 zh_CN")
    private String language;

    @ApiModelProperty(value = "时区，默认 GMT+8")
    private String timeZone;

    @ApiModelProperty(value = "密码最后一次修改时间")
    private Date lastPasswordUpdatedAt;

    @ApiModelProperty(value = "最近登录时间")
    private Date lastLoginAt;

    @Column(name = "is_enabled")
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @Column(name = "is_locked")
    @ApiModelProperty(value = "是否锁定")
    private Boolean locked;


    @Column(name = "is_ldap")
    @ApiModelProperty(value = "是否LDAP用户")
    private Boolean ldap;


    @ApiModelProperty(value = "锁定时间")
    private Date lockedUntilAt;

    @ApiModelProperty(value = "密码尝试次数")
    private Integer passwordAttempt;

    @Column(name = "is_admin")
    @ApiModelProperty(value = "是否超级管理员")
    private Boolean admin;



    /**
     * 用户类型，默认平台用户
     * @see UserType
     */
    @ApiModelProperty(value = "用户类型，中台用户-P，C端用户-C")
    private String userType;

    @Transient
    private String tenantNum;

    public String getTenantNum() {
        return tenantNum;
    }

    public void setTenantNum(String tenantNum) {
        this.tenantNum = tenantNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInternationalTelCode() {
        return internationalTelCode;
    }

    public void setInternationalTelCode(String internationalTelCode) {
        this.internationalTelCode = internationalTelCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Date getLastPasswordUpdatedAt() {
        return lastPasswordUpdatedAt;
    }

    public void setLastPasswordUpdatedAt(Date lastPasswordUpdatedAt) {
        this.lastPasswordUpdatedAt = lastPasswordUpdatedAt;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }



    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getLdap() {
        return ldap;
    }

    public void setLdap(Boolean ldap) {
        this.ldap = ldap;
    }

    public Date getLockedUntilAt() {
        return lockedUntilAt;
    }

    public void setLockedUntilAt(Date lockedUntilAt) {
        this.lockedUntilAt = lockedUntilAt;
    }

    public Integer getPasswordAttempt() {
        return passwordAttempt;
    }

    public void setPasswordAttempt(Integer passwordAttempt) {
        this.passwordAttempt = passwordAttempt;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
