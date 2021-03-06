package io.choerodon.migration.domian.c7n;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

import io.choerodon.migration.domian.AuditDomain;

/**
 * @author superlee
 * @since 2019-04-23
 */
@Table(name = "oauth_ldap")
public class LdapDTO extends AuditDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键/非必填")
    private Long id;

    @ApiModelProperty(value = "名称/必填")
    @NotEmpty(message = "error.ldap.name.empty")
    private String name;

    @ApiModelProperty(value = "组织ID/必填")
    @NotNull(message = "error.ldap.organizationId.null")
    private Long organizationId;

    @ApiModelProperty(value = "主机名/必填")
    @NotEmpty(message = "error.ldap.serverAddress.empty")
    private String serverAddress;

    @ApiModelProperty(value = "LDAP端口/必填")
    @NotEmpty(message = "error.ldap.port.empty")
    private String port;

    @ApiModelProperty(value = "管理员登录名/必填")
    @NotEmpty(message = "error.ldap.account.empty")
    private String account;

    @ApiModelProperty(value = "管理员密码/必填")
    @NotEmpty(message = "error.ldap.password.empty")
    @Column(name = "ldap_password")
    private String password;


    @Column(name = "use_ssl")
    @ApiModelProperty(value = "是否使用SSL/必填/默认：false")
    private Boolean useSSL;

    @Column(name = "is_enabled")
    @ApiModelProperty(value = "是否启用/非必填")
    private Boolean enabled;

    @ApiModelProperty(value = "基准DN/非必填")
    private String baseDn;

    @ApiModelProperty(value = "目录类型/非必填")
    private String directoryType;

    @ApiModelProperty(value = "用户对象类,多个objectClass以逗号分割/非必填")
    @NotEmpty(message = "error.ldap.objectClass.empty")
    private String objectClass;

    @ApiModelProperty(value = "登录名属性/非必填")
    private String loginNameField;

    @ApiModelProperty(value = "用户名属性/非必填")
    private String realNameField;

    @ApiModelProperty(value = "邮箱属性/非必填")
    private String emailField;

    @ApiModelProperty(value = "手机号属性/非必填")
    private String phoneField;

    @ApiModelProperty(value = "同步用户自定义过滤条件，格式以'('开始以')'结束")
    private String customFilter;

    @ApiModelProperty(value = "saga每次发送用户的数量，默认值为500，必填")
    @NotNull(message = "error.ldap.sagaBatchSize.null")
    private Integer sagaBatchSize;

    @ApiModelProperty(value = "ldap服务器连接超时时间，单位是秒，默认值10秒，必填")
    @NotNull(message = "error.ldap.connectionTimeout.null")
    private Integer connectionTimeout;

    @ApiModelProperty(value = "ldap服务端对象的唯一标识，用于记录同步用户失败信息")
    @NotEmpty(message = "error.ldap.uuidField.empty")
    private String uuidField;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getUseSSL() {
        return useSSL;
    }

    public void setUseSSL(Boolean useSSL) {
        this.useSSL = useSSL;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getBaseDn() {
        return baseDn;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public String getDirectoryType() {
        return directoryType;
    }

    public void setDirectoryType(String directoryType) {
        this.directoryType = directoryType;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public String getLoginNameField() {
        return loginNameField;
    }

    public void setLoginNameField(String loginNameField) {
        this.loginNameField = loginNameField;
    }

    public String getRealNameField() {
        return realNameField;
    }

    public void setRealNameField(String realNameField) {
        this.realNameField = realNameField;
    }

    public String getEmailField() {
        return emailField;
    }

    public void setEmailField(String emailField) {
        this.emailField = emailField;
    }

    public String getPhoneField() {
        return phoneField;
    }

    public void setPhoneField(String phoneField) {
        this.phoneField = phoneField;
    }

    public String getCustomFilter() {
        return customFilter;
    }

    public void setCustomFilter(String customFilter) {
        this.customFilter = customFilter;
    }

    public Integer getSagaBatchSize() {
        return sagaBatchSize;
    }

    public void setSagaBatchSize(Integer sagaBatchSize) {
        this.sagaBatchSize = sagaBatchSize;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getUuidField() {
        return uuidField;
    }

    public void setUuidField(String uuidField) {
        this.uuidField = uuidField;
    }
}
