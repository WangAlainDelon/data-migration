package io.choerodon.migration.mapper.hzero.platform;


import io.choerodon.migration.domian.hzero.User;
import io.choerodon.migration.mapper.hzero.HzeroBaseMapper;
import tk.mybatis.mapper.common.BaseMapper;


/**
 * @author bojiangzhou 2019/04/17 优化代码、SQL
 * @author allen 2018/6/26
 */
public interface UserHzeroMapper extends HzeroBaseMapper<User> {
    User selectUserTenant(Long id);
}
