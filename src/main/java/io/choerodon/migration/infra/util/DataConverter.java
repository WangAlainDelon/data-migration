package io.choerodon.migration.infra.util;

import java.util.List;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/4/29 15:50
 */
@FunctionalInterface
public interface DataConverter<T, R> {

    List<R> convert(List<T> objects);
}
