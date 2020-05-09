package io.choerodon.migration.domian.hzero;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 租户配置扩展表
 *
 * @author xiaoyu.zhao@hand-china.com 2020-04-21 20:46:05
 */
@Table(name = "hpfm_tenant_config")
public class TenantConfig {

    public static final String FIELD_TENANT_CONFIG_ID = "tenantConfigId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CONFIG_KEY = "configKey";
    public static final String FIELD_CONFIG_VALUE = "configValue";
    public static final String[] SELECT_FIELDS = {"tenantConfigId", "tenantId", "configKey", "configValue"};

    public TenantConfig() {
    }

    public TenantConfig(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tenantConfigId;

    @NotNull
    private Long tenantId;

    @NotBlank
    private String configKey;

    @NotBlank
    private String configValue;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getTenantConfigId() {
        return tenantConfigId;
    }

    public TenantConfig setTenantConfigId(Long tenantConfigId) {
        this.tenantConfigId = tenantConfigId;
        return this;
    }

    /**
     * @return 租户Id, hpfm_tenant.tenant_id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public TenantConfig setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 配置Key
     */
    public String getConfigKey() {
        return configKey;
    }

    public TenantConfig setConfigKey(String configKey) {
        this.configKey = configKey;
        return this;
    }

    /**
     * @return 配置值
     */
    public String getConfigValue() {
        return configValue;
    }

    public TenantConfig setConfigValue(String configValue) {
        this.configValue = configValue;
        return this;
    }

    private Date creationDate;

    private Long createdBy;

    private Date lastUpdateDate;

    private Long lastUpdatedBy;

    private Long objectVersionNumber;


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
