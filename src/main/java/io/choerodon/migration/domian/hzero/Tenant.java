package io.choerodon.migration.domian.hzero;

import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.choerodon.migration.domian.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;


import org.hzero.core.util.Regexs;

@Table(name = "hpfm_tenant")
public class Tenant extends AuditDomain {

    public static final String TENANT_ID = "tenantId";
    public static final String TENANT_NAME = "tenantName";
    public static final String TENANT_NUM = "tenantNum";
    public static final String ENABLED_FLAG = "enabledFlag";

    public static final String NULL_VALUE = "";
    public static final String LIMIT_USER_QTY = "limitUserQty";


    /**
     * 校验传递的查询参数是否为""，若为""则转换为null
     */
    public void validateQueryCondition() {
        if (!Objects.isNull(tenantName) && Objects.equals(NULL_VALUE, tenantName)) {
            this.tenantName = null;
        }
        if (!Objects.isNull(tenantNum) && Objects.equals(NULL_VALUE, tenantNum)) {
            this.tenantNum = null;
        }
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @NotBlank
    @Length(max = 120)
    @ApiModelProperty("租户名称")
    private String tenantName;
    @NotBlank
    @Length(max = 15)
    @ApiModelProperty("租户编号")
    @Pattern(regexp = Regexs.CODE)
    private String tenantNum;
    @NotNull
    @Range(max = 1, min = 0)
    @ApiModelProperty("是否启用")
    private Integer enabledFlag;
    @ApiModelProperty("限制用户数")
    private Integer limitUserQty;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    @ApiModelProperty("默认集团")
    private Group group;
    @Transient
    @ApiModelProperty("数据来源key")
    private String sourceKey;
    @Transient
    @ApiModelProperty("数据来源代码")
    private String sourceCode;

    @Transient
    @ApiModelProperty("租户配置")
    private List<TenantConfig> tenantConfigs;

    //
    // 子类
    // ------------------------------------------------------------------------------


    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public Tenant setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 租户名称
     */
    public String getTenantName() {
        return tenantName;
    }

    public Tenant setTenantName(String tenantName) {
        this.tenantName = tenantName;
        return this;
    }

    /**
     * @return 租户编号
     */
    public String getTenantNum() {
        return tenantNum;
    }

    public Tenant setTenantNum(String tenantNum) {
        this.tenantNum = tenantNum;
        return this;
    }

    /**
     * @return 是否启用
     */
    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    /**
     * @return 传输字段: 默认集团
     */
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * @return 传输字段: 数据来源Key
     */
    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    /**
     * @return 传输字段: 数据来源代码
     */
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    /**
     * @return 限制用户数，null表示不限制用户数
     */
    public Integer getLimitUserQty() {
        return limitUserQty;
    }

    public void setLimitUserQty(Integer limitUserQty) {
        this.limitUserQty = limitUserQty;
    }

    public List<TenantConfig> getTenantConfigs() {
        return tenantConfigs;
    }

    public void setTenantConfigs(List<TenantConfig> tenantConfigs) {
        this.tenantConfigs = tenantConfigs;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "tenantId=" + tenantId +
                ", tenantName='" + tenantName + '\'' +
                ", tenantNum='" + tenantNum + '\'' +
                ", enabledFlag=" + enabledFlag +
                ", limitUserQty=" + limitUserQty +
                ", group=" + group +
                ", sourceKey='" + sourceKey + '\'' +
                ", sourceCode='" + sourceCode + '\'' +
                ", tenantConfigs=" + tenantConfigs +
                '}';
    }
}
