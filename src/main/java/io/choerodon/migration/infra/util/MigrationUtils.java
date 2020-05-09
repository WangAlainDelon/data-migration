package io.choerodon.migration.infra.util;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.choerodon.core.exception.CommonException;
import io.choerodon.migration.mapper.c7n.C7nBaseMapper;
import io.choerodon.migration.mapper.hzero.HzeroBaseMapper;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/4/29 16:26
 */
public class MigrationUtils {

    private static final int SIZE = 200;

    public static <T, R> void migration(Class<T> c7nClass, C7nBaseMapper<T> c7nBaseMapper, HzeroBaseMapper<R> hzeroC7nBaseMapper, DataConverter<T,R> converter) {
        T t = null;
        try {
            t = c7nClass.newInstance();
        } catch (Exception e) {
            throw new CommonException("error.class.init.failed", e);
        }
        int count = c7nBaseMapper.selectCount(t);

        if (count <= SIZE) {
            List<R> result = converter.convert(c7nBaseMapper.selectAll());
            hzeroC7nBaseMapper.batchInsert(result);
        } else {
            int pageNum = (count + SIZE - 1)/SIZE;

            for (int page = 1; page <= pageNum; page++) {
                PageInfo<T> objectPageInfo = PageHelper.startPage(page, SIZE).doSelectPageInfo(c7nBaseMapper::selectAll);
                List<R> result = converter.convert(objectPageInfo.getList());
                hzeroC7nBaseMapper.batchInsert(result);
            }
        }
    }
}
