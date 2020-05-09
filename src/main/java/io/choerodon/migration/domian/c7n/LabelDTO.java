package io.choerodon.migration.domian.c7n;

import io.choerodon.migration.domian.AuditDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @author superlee
 * @since 2019-04-23
 */
@Table(name = "iam_label")
public class LabelDTO extends AuditDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "层级")
    @Column(name = "fd_level")
    private String level;

    @ApiModelProperty(value = "描述")
    private String description;



    public LabelDTO() {
    }

    public LabelDTO(String name) {
        this.name = name;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
