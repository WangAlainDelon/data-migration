package io.choerodon.migration.domian.hzero;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

import io.choerodon.migration.domian.AuditDomain;


@Table(name = "oauth_ldap_error_user")
public class LdapErrorUser extends AuditDomain {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "外键，ldap history id")

    private Long ldapHistoryId;
    @ApiModelProperty(value = "ldap server 端对象的唯一标识，可以根据该字段在ldap server中查询该对象")

    private String uuid;
    @ApiModelProperty(value = "登录名")

    private String loginName;
    @ApiModelProperty(value = "邮箱")

    private String email;
    @ApiModelProperty(value = "真实姓名")

    private String realName;
    @ApiModelProperty(value = "手机号")

    private String phone;
    @ApiModelProperty(value = "同步失败原因")
    private String cause;

    public Long getId() {
        return id;
    }

    public LdapErrorUser setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getLdapHistoryId() {
        return ldapHistoryId;
    }

    public LdapErrorUser setLdapHistoryId(Long ldapHistoryId) {
        this.ldapHistoryId = ldapHistoryId;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public LdapErrorUser setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getLoginName() {
        return loginName;
    }

    public LdapErrorUser setLoginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public LdapErrorUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getRealName() {
        return realName;
    }

    public LdapErrorUser setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public LdapErrorUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getCause() {
        return cause;
    }

    public LdapErrorUser setCause(String cause) {
        this.cause = cause;
        return this;
    }
}
