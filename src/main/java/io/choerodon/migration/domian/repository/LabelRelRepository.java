package io.choerodon.migration.domian.repository;

import io.choerodon.migration.domian.hzero.Label;
import io.choerodon.migration.domian.hzero.LabelRel;
import io.choerodon.migration.infra.util.LabelAssignType;
import org.hzero.core.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Mr.Wang
 * Date: 2020/5/7
 */
public interface LabelRelRepository {
    void assignLabels(String type, LabelAssignType assignType, Set<Long> dataIds, Set<Long> labelIds);

    Pair<List<Label>, List<Label>> updateLabelRelationsByLabelView(String type, Long dataId, LabelAssignType assignType, Set<Long> viewLabelIds);

    /**
     * 通过ID批量删除
     *
     * @param labelRelIds 待删除的ID
     */
    void batchDeleteByIds(List<Long> labelRelIds);

    List<LabelRel> selectLabelsByDataTypeAndDataId(String type, Long dataId);

    Map<Long, List<LabelRel>> selectLabelsByDataTypeAndDataIds(String type, Set<Long> dataIds);

    List<LabelRel> addLabels(String type, Long dataId, LabelAssignType assignType, Set<Long> labelIds);

    List<LabelRel> addLabels(String type, Long dataId, LabelAssignType assignType, List<Label> labels);

    void removeLabels(String type, Long dataId, Set<Long> labelIds);

    void removeLabels(String type, Long dataId, List<Label> labels);

    void recycleLabels(String type, Set<Long> dataIds, Set<Long> labelIds);

    Set<Long> selectInheritLabelIdsByDataTypeAndDataIds(String type, Set<Long> dataIds);
}
