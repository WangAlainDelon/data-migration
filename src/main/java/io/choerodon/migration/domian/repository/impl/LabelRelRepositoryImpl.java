package io.choerodon.migration.domian.repository.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.migration.domian.hzero.Label;
import io.choerodon.migration.domian.hzero.LabelRel;
import io.choerodon.migration.domian.repository.LabelRelRepository;
import io.choerodon.migration.infra.util.LabelAssignType;
import io.choerodon.migration.mapper.hzero.platform.LabelHzeroMapper;
import io.choerodon.migration.mapper.hzero.platform.LabelRelMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Pair;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

import static io.choerodon.mybatis.domain.AuditDomain.*;
import static io.choerodon.mybatis.domain.AuditDomain.FIELD_CREATION_DATE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * User: Mr.Wang
 * Date: 2020/5/7
 */
@Component
public class LabelRelRepositoryImpl implements LabelRelRepository {

    @Autowired
    private LabelRelMapper labelRelMapper;
    @Autowired
    private LabelHzeroMapper labelMapper;


    @Override
    public void assignLabels(String type, LabelAssignType assignType, Set<Long> dataIds, Set<Long> labelIds) {
        if (StringUtils.isBlank(type) || Objects.isNull(assignType) || CollectionUtils.isEmpty(dataIds) || CollectionUtils.isEmpty(labelIds)) {
            return;
        }

        // 查询所有原有标签关系
        Example example = new Example(LabelRel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(LabelRel.FIELD_DATA_TYPE, type);
        criteria.andIn(LabelRel.FIELD_DATA_ID, dataIds);
        criteria.andIn(LabelRel.FIELD_LABEL_ID, labelIds);
        List<LabelRel> labelRels = labelRelMapper.selectByExample(criteria);
        List<Long> labelRelIds = new ArrayList<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(labelRels)) {
            labelRelIds = labelRels.stream().map(LabelRel::getLabelRelId).collect(toList());
        }

        // 删除原有的标签关系
        this.batchDeleteByIds(labelRelIds);

        // 构建新的标签关系
        List<LabelRel> labelRelations = dataIds.stream().flatMap(dataId ->
                labelIds.stream().map(labelId -> LabelRel.built(type, dataId, labelId, assignType.getCode())))
                .collect(toList());

        // 添加标签关系
        labelRelMapper.batchInsert(labelRelations);
    }

    @Override
    public void batchDeleteByIds(List<Long> labelRelIds) {
        if (CollectionUtils.isEmpty(labelRelIds)) {
            return;
        }

        // 分片处理，每个分片最多处理1000条数据(sql in 语句的限制)
        ListUtils.partition(labelRelIds, 1000).forEach(subList -> {
            // 批量删除数据
            this.labelRelMapper.batchDeleteByIds(subList);
        });
    }

    @Override
    public Pair<List<Label>, List<Label>> updateLabelRelationsByLabelView(String type, Long dataId,
                                                                          LabelAssignType assignType, Set<Long> viewLabelIds) {
        if (StringUtils.isBlank(type) || Objects.isNull(dataId) || Objects.isNull(assignType)) {
            return new Pair<>(Collections.emptyList(), Collections.emptyList());
        }
        List<Label> viewLabels = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(viewLabelIds)) {
            // 查询视图标签
            Example example = new Example(Label.class);
            Example.Criteria criteria = example.createCriteria();
            String join = StringUtils.join(viewLabelIds, BaseConstants.Symbol.COMMA);
            criteria.andIn("id", viewLabelIds);
            viewLabels = labelMapper.selectByExample(example);
        }

        // 处理数据为空的情况
        viewLabels = Optional.ofNullable(viewLabels).orElse(Collections.emptyList());
        // 菜单数据库的标签
        List<Label> dbLabels = Optional.ofNullable(this.selectLabelsByDataTypeAndDataId(type, dataId))
                .orElse(Collections.emptyList()).stream().map(LabelRel::getLabel).collect(Collectors.toList());

        // 菜单当前视图的标签除去菜单数据库的标签,即为需要增加的标签
        List<Label> needAddedLabels = ListUtils.removeAll(viewLabels, dbLabels);
        // 菜单数据库的标签出去菜单当前视图的标签,即为需要移除的标签
        List<Label> needRemovedLabels = ListUtils.removeAll(dbLabels, viewLabels);

        // 添加标签
        if (CollectionUtils.isNotEmpty(needAddedLabels)) {
            this.addLabels(type, dataId, assignType, needAddedLabels);
        }
        // 删除标签
        if (CollectionUtils.isNotEmpty(needRemovedLabels)) {
            this.removeLabels(type, dataId, needRemovedLabels);
        }

        // 返回结果
        return new Pair<>(needAddedLabels, needRemovedLabels);
    }

    @Override
    public List<LabelRel> selectLabelsByDataTypeAndDataId(String type, Long dataId) {
        // 查询结果
        return Optional.ofNullable(this.selectLabelsByDataTypeAndDataIds(type, Collections.singleton(dataId)))
                .orElse(Collections.emptyMap())
                .get(dataId);
    }

    @Override
    public Map<Long, List<LabelRel>> selectLabelsByDataTypeAndDataIds(String type, Set<Long> dataIds) {
        if (StringUtils.isBlank(type) || CollectionUtils.isEmpty(dataIds)) {
            return Collections.emptyMap();
        }

        // 根据数据类型和数据ID查询标签关系数据
        Example example = new Example(LabelRel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(LabelRel.FIELD_DATA_TYPE, type);
        criteria.andIn(LabelRel.FIELD_DATA_ID, dataIds);
        List<LabelRel> labelRelations = labelRelMapper.selectByExample(example);
//        List<LabelRel> labelRelations = this.selectByCondition(Condition.builder(LabelRel.class)
//                .andWhere(Sqls.custom()
//                        // 数据类型
//                        .andEqualTo(LabelRel.FIELD_DATA_TYPE, type)
//                        // 数据ID
//                        .andIn(LabelRel.FIELD_DATA_ID, dataIds)
//                ).build()
//                // 排除who字段
//                .excludeProperties(FIELD_LAST_UPDATE_DATE, FIELD_LAST_UPDATED_BY, FIELD_CREATED_BY, FIELD_CREATION_DATE));
        if (CollectionUtils.isEmpty(labelRelations)) {
            return Collections.emptyMap();
        }

        // 查询所有标签
        Example example1 = new Example(Label.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo(Label.FIELD_ENABLED_FLAG, BaseConstants.Flag.YES);
        criteria1.andEqualTo(Label.FIELD_VISIBLE_FLAG, BaseConstants.Flag.YES);
        criteria1.andIn(Label.FIELD_ID, labelRelations.stream().map(LabelRel::getLabelId).collect(toSet()));
        List<Label> labels = labelMapper.selectByExample(example1);
//        List<Label> labels = this.labelRepository.selectByCondition(Condition.builder(Label.class)
//                .andWhere(Sqls.custom()
//                        // 启用
//                        .andEqualTo(Label.FIELD_ENABLED_FLAG, BaseConstants.Flag.YES)
//                        // 可见
//                        .andEqualTo(Label.FIELD_VISIBLE_FLAG, BaseConstants.Flag.YES)
//                        // 标签IDs
//                        .andIn(Label.FIELD_ID, labelRelations.stream().map(LabelRel::getLabelId).collect(toSet()))
//                ).build()
//                // 排除who字段
//                .excludeProperties(FIELD_LAST_UPDATE_DATE, FIELD_LAST_UPDATED_BY, FIELD_CREATED_BY, FIELD_CREATION_DATE));
        if (CollectionUtils.isEmpty(labels)) {
            return Collections.emptyMap();
        }

        // 将查询到的标签转换成map    key ---> value === labelId ---> label
        Map<Long, Label> labelMap = labels.stream().collect(toMap(Label::getId, t -> t));

        // 设置Label,并按数据ID分组返回数据
        return labelRelations.stream()
                .peek(labelRel -> labelRel.setLabel(labelMap.get(labelRel.getLabelId())))
                .collect(Collectors.groupingBy(LabelRel::getDataId));
    }

    @Override
    public List<LabelRel> addLabels(String type, Long dataId, LabelAssignType assignType, Set<Long> labelIds) {
        if (StringUtils.isBlank(type) || Objects.isNull(dataId) || Objects.isNull(assignType) || CollectionUtils.isEmpty(labelIds)) {
            return Collections.emptyList();
        }

        // 查询已经分配的标签关系
//        List<LabelRel> dbLabelRelations = this.selectByCondition(Condition.builder(LabelRel.class)
//                .select(LabelRel.FIELD_LABEL_ID)
//                .andWhere(Sqls.custom()
//                        .andEqualTo(LabelRel.FIELD_DATA_TYPE, type)
//                        .andEqualTo(LabelRel.FIELD_DATA_ID, dataId)
//                        .andIn(LabelRel.FIELD_LABEL_ID, labelIds)
//                ).build());
        Example example = new Example(LabelRel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(LabelRel.FIELD_DATA_TYPE, type);
        criteria.andEqualTo(LabelRel.FIELD_DATA_ID, dataId);
        criteria.andIn(LabelRel.FIELD_LABEL_ID, labelIds);
        List<LabelRel> dbLabelRelations = labelRelMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(dbLabelRelations)) {
            // 查询标签数据
//            List<String> dbLabelNames = this.labelRepository.selectByCondition(Condition.builder(Label.class)
//                    .select(Label.FIELD_NAME)
//                    .andWhere(Sqls.custom()
//                            .andIn(Label.FIELD_ID, dbLabelRelations.stream().map(LabelRel::getLabelId).collect(toSet()))
//                    ).build()).stream().map(Label::getName).collect(toList());
            Example example1 = new Example(Label.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andIn(Label.FIELD_ID, dbLabelRelations.stream().map(LabelRel::getLabelId).collect(toSet()));
            List<String> dbLabelNames = labelMapper.selectByExample(example1).stream().map(Label::getName).collect(toList());
            // 分配标签失败, 数据类型: [{0}] - 数据ID: [{1}] 存在已经分配的标签: [{2}]
            throw new CommonException("hiam.error.label_rel.add_label.exists_rels",
                    type, dataId, StringUtils.join(dbLabelNames, BaseConstants.Symbol.COMMA));
        }

        // 构建关系数据
        return labelIds.stream()
                // 转换数据
                .map(labelId -> LabelRel.built(type, dataId, labelId, assignType.getCode()))
                // 插入数据
                .peek(labelRel -> labelRelMapper.insertSelective(labelRel))
                .collect(toList());
    }

    @Override
    public List<LabelRel> addLabels(String type, Long dataId, LabelAssignType assignType, List<Label> labels) {
        if (Objects.isNull(type) || Objects.isNull(dataId) || Objects.isNull(assignType) || CollectionUtils.isEmpty(labels)) {
            return Collections.emptyList();
        }

        // 添加标签,并返回结果
        return this.addLabels(type, dataId, assignType, labels.stream().map(Label::getId).collect(toSet()));
    }

    @Override
    public void removeLabels(String type, Long dataId, Set<Long> labelIds) {
        if (StringUtils.isBlank(type) || Objects.isNull(dataId) || CollectionUtils.isEmpty(labelIds)) {
            return;
        }

        // 查询自动分配的标签关系对象
//        List<LabelRel> dbAutoLabelRelations = this.selectByCondition(Condition.builder(LabelRel.class)
//                .select(LabelRel.FIELD_LABEL_REL_ID, LabelRel.FIELD_ASSIGN_TYPE, LabelRel.FIELD_LABEL_ID)
//                .andWhere(Sqls.custom()
//                        .andEqualTo(LabelRel.FIELD_DATA_TYPE, type)
//                        .andEqualTo(LabelRel.FIELD_DATA_ID, dataId)
//                        .andEqualTo(LabelRel.FIELD_ASSIGN_TYPE, LabelAssignType.AUTO.getCode())
//                        .andIn(LabelRel.FIELD_LABEL_ID, labelIds)
//                ).build());
        Example example = new Example(LabelRel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(LabelRel.FIELD_DATA_TYPE, type);
        criteria.andEqualTo(LabelRel.FIELD_DATA_ID, dataId);
        criteria.andEqualTo(LabelRel.FIELD_ASSIGN_TYPE, LabelAssignType.AUTO.getCode());
        criteria.andIn(LabelRel.FIELD_LABEL_ID, labelIds);
        List<LabelRel> dbAutoLabelRelations = labelRelMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(dbAutoLabelRelations)) {
            // 查询标签数据
//            List<String> dbLabelNames = this.labelRepository.selectByCondition(Condition.builder(Label.class)
//                    .select(Label.FIELD_NAME)
//                    .andWhere(Sqls.custom()
//                            .andIn(Label.FIELD_ID, dbAutoLabelRelations.stream().map(LabelRel::getLabelId).collect(toSet()))
//                    ).build()).stream().map(Label::getName).collect(toList());

            Example example1 = new Example(Label.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andIn(Label.FIELD_ID, dbAutoLabelRelations.stream().map(LabelRel::getLabelId).collect(toSet()));
            List<String> dbLabelNames = labelMapper.selectByExample(example1).stream().map(Label::getName).collect(toList());
            // 移除标签失败, 数据类型: [{0}] - 数据ID: [{1}] 存在自动分配的标签: [{2}]
            throw new CommonException("hiam.error.label_rel.remove_label.exists_auto_rels",
                    type, dataId, StringUtils.join(dbLabelNames, BaseConstants.Symbol.COMMA));
        }

        // 查询手动分配的标签
//        List<LabelRel> dbManualLabelRelations = this.selectByCondition(Condition.builder(LabelRel.class)
//                .select(LabelRel.FIELD_LABEL_REL_ID, LabelRel.FIELD_ASSIGN_TYPE, LabelRel.FIELD_LABEL_ID)
//                .andWhere(Sqls.custom()
//                        .andEqualTo(LabelRel.FIELD_DATA_TYPE, type)
//                        .andEqualTo(LabelRel.FIELD_DATA_ID, dataId)
//                        .andEqualTo(LabelRel.FIELD_ASSIGN_TYPE, LabelAssignType.MANUAL.getCode())
//                        .andIn(LabelRel.FIELD_LABEL_ID, labelIds)
//                ).build());
        Example example3 = new Example(LabelRel.class);
        Example.Criteria criteria3 = example.createCriteria();
        criteria3.andEqualTo(LabelRel.FIELD_DATA_TYPE, type);
        criteria3.andEqualTo(LabelRel.FIELD_DATA_ID, dataId);
        criteria3.andEqualTo(LabelRel.FIELD_ASSIGN_TYPE, LabelAssignType.MANUAL.getCode());
        criteria3.andIn(LabelRel.FIELD_LABEL_ID, labelIds);
        List<LabelRel> dbManualLabelRelations = labelRelMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(dbManualLabelRelations)) {
            return;
        }

        // 删除数据
        dbManualLabelRelations.forEach(labelRel -> labelRelMapper.deleteByPrimaryKey(labelRel));
    }

    @Override
    public void removeLabels(String type, Long dataId, List<Label> labels) {
        if (Objects.isNull(type) || Objects.isNull(dataId) || CollectionUtils.isEmpty(labels)) {
            return;
        }
        // 删除标签
        this.removeLabels(type, dataId, labels.stream().map(Label::getId).collect(toSet()));
    }

    @Override
    public void recycleLabels(String type, Set<Long> dataIds, Set<Long> labelIds) {
        if (StringUtils.isBlank(type) || CollectionUtils.isEmpty(dataIds) || CollectionUtils.isEmpty(labelIds)) {
            return;
        }

        // 查询所有原有标签关系
//        List<Long> labelRelIds = Optional.ofNullable(this.selectByCondition(Condition.builder(LabelRel.class)
//                .select(LabelRel.FIELD_LABEL_REL_ID)
//                .andWhere(Sqls.custom()
//                        .andEqualTo(LabelRel.FIELD_DATA_TYPE, type)
//                        .andIn(LabelRel.FIELD_DATA_ID, dataIds)
//                        .andIn(LabelRel.FIELD_LABEL_ID, labelIds)
//                ).build())).orElse(Collections.emptyList()).stream().map(LabelRel::getLabelRelId).collect(toList());
        Example example = new Example(LabelRel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(LabelRel.FIELD_DATA_TYPE, type);
        criteria.andIn(LabelRel.FIELD_DATA_ID, dataIds);
        criteria.andIn(LabelRel.FIELD_LABEL_ID, labelIds);
        List<Long> labelRelIds = labelRelMapper.selectByExample(example).stream().map(LabelRel::getLabelRelId).collect(toList());
        // 删除数据
        this.batchDeleteByIds(labelRelIds);
    }

    @Override
    public Set<Long> selectInheritLabelIdsByDataTypeAndDataIds(String type, Set<Long> dataIds) {
        if (StringUtils.isBlank(type) || CollectionUtils.isEmpty(dataIds)) {
            return Collections.emptySet();
        }

        // 查询父级角色或继承角色的标签
//        Set<Long> labelIds = Optional.ofNullable(this.selectByCondition(Condition.builder(LabelRel.class)
//                .selectDistinct(LabelRel.FIELD_LABEL_ID)
//                .andWhere(Sqls.custom()
//                        .andEqualTo(LabelRel.FIELD_DATA_TYPE, type)
//                        .andIn(LabelRel.FIELD_DATA_ID, dataIds)
//                ).build())).orElse(Collections.emptyList())
//                .stream().map(LabelRel::getLabelId).collect(Collectors.toSet());

        Example example = new Example(LabelRel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(LabelRel.FIELD_DATA_TYPE, type);
        Example.Criteria criteria1 = criteria.andIn(LabelRel.FIELD_DATA_ID, dataIds);
        Set<Long> labelIds = labelRelMapper.selectByExample(example).stream().map(LabelRel::getLabelId).collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(labelIds)) {
            return Collections.emptySet();
        }

        // 查询可继承的标签
        Example example1 = new Example(Label.class);
        Example.Criteria criteria2 = example1.createCriteria();
        criteria2.andEqualTo(Label.FIELD_INHERIT_FLAG, BaseConstants.Flag.YES);
        criteria2.andIn(Label.FIELD_ID, labelIds);
        List<Label> labels = labelMapper.selectByExample(example1);
        if (org.springframework.util.CollectionUtils.isEmpty(labelIds)) {
            return Collections.emptySet();
        }
        return labels.stream().map(Label::getId).collect(Collectors.toSet());
    }
}
