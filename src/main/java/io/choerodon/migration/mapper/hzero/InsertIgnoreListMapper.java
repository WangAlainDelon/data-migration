package io.choerodon.migration.mapper.hzero;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/4/29 17:19
 */
@RegisterMapper
public interface InsertIgnoreListMapper<T> {

    @InsertProvider(type = InsertIgnoreListProvider.class, method = "dynamicSQL")
    void batchInsert(List<? extends T> objects);
}
