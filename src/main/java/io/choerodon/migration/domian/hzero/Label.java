package io.choerodon.migration.domian.hzero;

import io.choerodon.migration.domian.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * 标签管理实体类
 *
 * @author xiaoyu.zhao@hand-china.com 2020-02-25 14:22:16
 */
@Component
@Table(name = "iam_label")
public class Label extends AuditDomain {
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_FD_LEVEL = "fdLevel";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_TAG = "tag";
    public static final String FIELD_INHERIT_FLAG = "inheritFlag";
    public static final String FIELD_PRESET_FLAG = "presetFlag";
    public static final String FIELD_VISIBLE_FLAG = "visibleFlag";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(value = "名称")

    private String name;
    @ApiModelProperty(value = "类型")

    private String type;
    @ApiModelProperty(value = "层级")
    private String fdLevel;
    @ApiModelProperty(value = "描述")

    private String description;
    @ApiModelProperty(value = "是否启用")

    private Integer enabledFlag;
    @ApiModelProperty(value = "标记")

    private String tag;

    private Integer inheritFlag;

    public Integer getInheritFlag() {
        return inheritFlag;
    }

    public void setInheritFlag(Integer inheritFlag) {
        this.inheritFlag = inheritFlag;
    }
    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


    //
    // getter/setter
    // ------------------------------------------------------------------------------


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    /**
     * @return
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return 名称
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 类型
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return 层级
     */
    public String getFdLevel() {
        return fdLevel;
    }

    public void setFdLevel(String fdLevel) {
        this.fdLevel = fdLevel;
    }

    /**
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
