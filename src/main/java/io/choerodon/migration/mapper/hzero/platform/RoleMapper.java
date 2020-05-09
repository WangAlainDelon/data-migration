package io.choerodon.migration.mapper.hzero.platform;

import io.choerodon.migration.domian.hzero.Role;
import io.choerodon.migration.domian.vo.RoleVO;
import io.choerodon.migration.mapper.hzero.HzeroBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * User: Mr.Wang
 * Date: 2020/5/6
 */
public interface RoleMapper extends HzeroBaseMapper<Role> {

    /**
     * 查询全局模板角色
     *
     * @return 内置的租户层模板角色
     */
    List<Role> selectGlobalTemplateRole();
    List<Map<String, String>> selectTplRoleNameById(@Param("roleId")Long roleId);

    List<Role> selectAllSubRoles(Long id);

    List<RoleVO> selectUserAdminRoles(RoleVO params);
}
