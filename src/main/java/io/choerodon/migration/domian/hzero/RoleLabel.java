package io.choerodon.migration.domian.hzero;

import io.choerodon.migration.domian.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 角色标签关系实体类
 *
 * @author xiaoyu.zhao@hand-china.com 2020-02-25 16:10:10
 */
@ApiModel("角色标签关系")
@Table(name = "iam_role_label")
public class RoleLabel extends AuditDomain {


    @ApiModelProperty("")
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(value = "角色的id",required = true)
    @NotNull
    private Long roleId;
    @ApiModelProperty(value = "label的id",required = true)
    @NotNull
    private Long labelId;


    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 
     */
	public Long getId() {
		return id;
	}

	public RoleLabel setId(Long id) {
		this.id = id;
		return this;
	}
    /**
     * @return 角色的id
     */
	public Long getRoleId() {
		return roleId;
	}

	public RoleLabel setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}
    /**
     * @return label的id
     */
	public Long getLabelId() {
		return labelId;
	}

	public RoleLabel setLabelId(Long labelId) {
		this.labelId = labelId;
		return this;
	}

}
