package io.choerodon.migration.mapper.hzero.platform;

import io.choerodon.migration.domian.hzero.MemberRole;
import io.choerodon.migration.mapper.c7n.C7nBaseMapper;
import io.choerodon.migration.mapper.hzero.HzeroBaseMapper;


/**
 * @author carllhw
 */
public interface MemberRoleHzeroMapper extends HzeroBaseMapper<MemberRole> {
    MemberRole selectMemberRole(MemberRole params);
}
