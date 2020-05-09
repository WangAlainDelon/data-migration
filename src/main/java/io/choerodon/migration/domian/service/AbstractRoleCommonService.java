package io.choerodon.migration.domian.service;

import io.choerodon.migration.domian.hzero.Label;
import io.choerodon.migration.domian.hzero.Role;
import io.choerodon.migration.domian.repository.LabelRelRepository;
import io.choerodon.migration.infra.util.LabelAssignType;
import io.choerodon.migration.mapper.hzero.platform.LabelRelMapper;
import io.choerodon.migration.mapper.hzero.platform.RoleMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * 角色通用处理service
 *
 * @author bergturing 2020/04/28
 */
public abstract class AbstractRoleCommonService {
    @Autowired
    protected RoleMapper roleMapper;

    @Autowired
    protected LabelRelRepository labelRelRepository;

    @Autowired
    protected LabelRelMapper labelRelMapper;

    /**
     * 处理角色标签
     *
     * @param role 角色标签
     */
    protected void handleRoleLabels(Role role) {
        // 获取角色标签视图数据
        List<Label> roleLabels = Optional.ofNullable(role.getRoleLabels()).orElse(Collections.emptyList());
        LabelAssignType assignType = Optional.ofNullable(role.getLabelAssignType()).orElse(LabelAssignType.MANUAL);
        // 更新标签数据
        Pair<List<Label>, List<Label>> updateResult = this.labelRelRepository.updateLabelRelationsByLabelView(Role.LABEL_DATA_TYPE,
                role.getId(), assignType, roleLabels.stream().map(Label::getId).filter(Objects::nonNull).collect(toSet()));
        if (CollectionUtils.isEmpty(updateResult.getFirst()) && CollectionUtils.isEmpty(updateResult.getSecond())) {
            return;
        }

        // 查询子角色
        List<Role> subRoles = this.roleMapper.selectAllSubRoles(role.getId());
        if (CollectionUtils.isEmpty(subRoles)) {
            return;
        }
        // 角色ID
        Set<Long> subRoleIds = subRoles.stream().map(Role::getId).collect(toSet());

        // 分配子角色可继承的标签
        this.assignSubRoleLabels(subRoleIds, updateResult.getFirst());
        // 回收子角色可继承的标签
        this.recycleSubRoleLabels(subRoleIds, updateResult.getSecond());
    }

    /**
     * 分配子角色标签(用于给子角色分配可继承的标签，如果标签不是可继承的，就不处理)
     *
     * @param subRoleIds   子角色IDs
     * @param assignLabels 待分配的标签
     */
    private void assignSubRoleLabels(Set<Long> subRoleIds, List<Label> assignLabels) {
        // 增加的标签里属于可继承的标签IDs
        Set<Long> addedInheritLabelIds = Optional.ofNullable(assignLabels).orElse(Collections.emptyList())
                .stream().filter(label -> BaseConstants.Flag.YES.equals(label.getInheritFlag()))
                .map(Label::getId)
                .collect(toSet());

        // 处理添加的可继承的标签
        this.labelRelRepository.assignLabels(Role.LABEL_DATA_TYPE, LabelAssignType.AUTO, subRoleIds, addedInheritLabelIds);
    }

    /**
     * 回收子角色标签(用于回收子角色可继承的标签，如果标签不是可继承的，就不处理)
     *
     * @param subRoleIds    子角色IDs
     * @param recycleLabels 待回收的标签
     */
    private void recycleSubRoleLabels(Set<Long> subRoleIds, List<Label> recycleLabels) {
        // 移除的标签里属于可继承的标签IDs
        Set<Long> removedInheritLabelIds = Optional.ofNullable(recycleLabels).orElse(Collections.emptyList())
                .stream().filter(label -> BaseConstants.Flag.YES.equals(label.getInheritFlag()))
                .map(Label::getId)
                .collect(toSet());

        // 处理移除的可继承的标签
        this.labelRelRepository.recycleLabels(Role.LABEL_DATA_TYPE, subRoleIds, removedInheritLabelIds);
    }
}
