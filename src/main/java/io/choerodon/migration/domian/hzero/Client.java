package io.choerodon.migration.domian.hzero;

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.core.util.Regexs;

import io.choerodon.migration.domian.AuditDomain;


/**
 * 实体：客户端
 *
 * @author bojiangzhou 2019/08/02
 */
@ApiModel("客户端")
@Table(name = "oauth_client")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client extends AuditDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(value = "客户端名称/必填")
    @Size(min = 1, max = 30)
    @NotNull
    @Pattern(regexp = Regexs.CODE)
    private String name;
    @ApiModelProperty(value = "组织ID/必填")
    private Long organizationId;
    @ApiModelProperty(value = "客户端资源/非必填/默认：default")
    private String resourceIds;
    @ApiModelProperty(value = "客户端秘钥/必填")
    @Size(min = 6, max = 240)
    @NotNull
    private String secret;
    @ApiModelProperty(value = "作用域/非必填")
    private String scope;
    @ApiModelProperty(value = "授权类型/必填")
    @NotNull
    private String authorizedGrantTypes;
    @ApiModelProperty(value = "重定向地址/非必填")
    private String webServerRedirectUri;
    @ApiModelProperty(value = "访问授权超时时间/必填")
    private Long accessTokenValidity;
    @ApiModelProperty(value = "授权超时时间/必填")
    private Long refreshTokenValidity;
    @ApiModelProperty(value = "附加信息/非必填")
    private String additionalInformation;
    @ApiModelProperty(value = "自动授权域/非必填")
    private String autoApprove;
    @ApiModelProperty(value = "启用标识")
    private Integer enabledFlag;


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

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public Long getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Long accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Long getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Long refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(String autoApprove) {
        this.autoApprove = autoApprove;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }


}
