package io.choerodon.migration.mapper.hzero.platform;

import io.choerodon.migration.domian.hzero.RolePermission;
import io.choerodon.migration.mapper.hzero.HzeroBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限(集)
 *
 * @author jiangzhou.bo@hand-china.com 2018/06/21 20:39
 */
public interface RolePermissionMapper extends HzeroBaseMapper<RolePermission> {
    List<RolePermission> selectRolePermissionSets(RolePermission params);
    void batchInsertBySql(@Param("permissionSets") List<RolePermission> permissionSets);
}
