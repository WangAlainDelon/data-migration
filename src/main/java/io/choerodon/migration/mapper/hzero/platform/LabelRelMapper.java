package io.choerodon.migration.mapper.hzero.platform;

import io.choerodon.migration.domian.hzero.LabelRel;
import io.choerodon.migration.mapper.hzero.HzeroBaseMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 标签关系表Mapper
 *
 * @author bo.he02@hand-china.com 2020-04-26 10:04:19
 */
public interface LabelRelMapper extends Mapper<LabelRel>,HzeroBaseMapper<LabelRel> {
    void batchDeleteByIds(@Param("ids") List<Long> ids);
}
