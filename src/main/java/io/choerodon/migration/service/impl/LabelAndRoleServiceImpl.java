package io.choerodon.migration.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.choerodon.migration.domian.c7n.*;
import io.choerodon.migration.domian.hzero.*;
import io.choerodon.migration.domian.service.TenantRoleObserver;
import io.choerodon.migration.infra.util.ConvertUtils;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.*;
import io.choerodon.migration.mapper.hzero.platform.*;
import io.choerodon.migration.service.LabelAndRoleService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: Mr.Wang
 * Date: 2020/4/29
 */
@Service
public class LabelAndRoleServiceImpl implements LabelAndRoleService {
    @Autowired
    private LabelC7NMapper labelC7NMapper;

    @Autowired
    private LabelHzeroMapper labelHzeroMapper;

    @Autowired
    private RoleC7NMapper roleC7NMapper;

    @Autowired
    private RoleHzeroMapper roleHzeroMapper;

    @Autowired
    private RoleLabelC7NMapper roleLabelC7NMapper;

    @Autowired
    private RoleLabelHzeroMapper roleLabelHzeroMapper;

    @Autowired
    private MemberRoleC7NMapper memberRoleC7NMapper;

    @Autowired
    private MemberRoleHzeroMapper memberRoleHzeroMapper;

    @Autowired
    private ProjectC7NMapper projectC7NMapper;

    @Autowired
    private ProjectUserMapper projectUserMapper;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private TenantRoleObserver tenantRoleObserver;

    @Override
    public void memberAndRoleDataMigration() {

        //5.数据修复member_role
        int selectCount = memberRoleC7NMapper.selectCount(null);
        int size = 100;
        int totalPage = (selectCount + size - 1) / size;
        int pageNum = 0;
        do {
            PageHelper.startPage(pageNum, size, " id ASC");
            PageInfo<MemberRoleDTO> pageInfo = new PageInfo<>(this.memberRoleC7NMapper.selectAll());
            if (!CollectionUtils.isEmpty(pageInfo.getList())) {
                List<MemberRoleDTO> list = pageInfo.getList();
                List<MemberRoleDTO> memberRoleDTOS = list.stream().filter(memberRoleDTO -> "project".equals(memberRoleDTO.getSourceType())).collect(Collectors.toList());
                List<MemberRoleDTO> memberRoleDTOS1 = list.stream().filter(memberRoleDTO -> !"project".equals(memberRoleDTO.getSourceType())).collect(Collectors.toList());
                List<ProjectUser> projectUsers = new ArrayList<>();
                List<MemberRole> memberRoles1 = new ArrayList<>();
                for (MemberRoleDTO memberRoleDTO : memberRoleDTOS) {
                    //projectUser
                    RoleDTO roleDTO = roleC7NMapper.selectByPrimaryKey(memberRoleDTO.getRoleId());
                    if (Objects.isNull(roleDTO)) {
                        continue;
                    }
                    Role record = new Role();
                    if ("role/project/default/project-member".equals(roleDTO.getCode().trim())) {
                        record.setCode("project-member");
                    }
                    if ("role/project/default/project-owner".equals(roleDTO.getCode().trim())) {
                        record.setCode("project-admin");
                    }
                    ProjectDTO projectDTO = projectC7NMapper.selectByPrimaryKey(memberRoleDTO.getSourceId());
                    record.setTenantId(projectDTO.getOrganizationId());
                    record.setLevel("organization");
                    Role role = roleHzeroMapper.selectOne(record);


                    ProjectUser projectUser = new ProjectUser();
                    projectUser.setId(memberRoleDTO.getId());
                    projectUser.setMemberId(memberRoleDTO.getMemberId());
                    projectUser.setProjectId(memberRoleDTO.getSourceId());
                    projectUser.setRoleId(role.getId());

                    projectUser.setCreatedBy(memberRoleDTO.getCreatedBy());
                    projectUser.setCreationDate(memberRoleDTO.getCreationDate());
                    projectUser.setLastUpdateDate(memberRoleDTO.getLastUpdateDate());
                    projectUser.setLastUpdatedBy(memberRoleDTO.getLastUpdatedBy());
                    projectUser.setObjectVersionNumber(memberRoleDTO.getObjectVersionNumber());
                    projectUsers.add(projectUser);
                }
                for (MemberRoleDTO memberRoleDTO : memberRoleDTOS1) {
                    RoleDTO roleDTO = roleC7NMapper.selectByPrimaryKey(memberRoleDTO.getRoleId());
                    if (Objects.isNull(roleDTO)) {
                        continue;
                    }
                    Role record = new Role();
                    //平台管理员
                    if ("role/site/default/administrator".equals(roleDTO.getCode())) {
                        record.setCode("role/site/default/administrator");
                    }
                    //组织管理员
                    if ("role/organization/default/administrator".equals(roleDTO.getCode())) {
                        record.setCode("organization-administrator");
                    }
                    //组织成员
                    if ("role/organization/default/organization-member".equals(roleDTO.getCode())) {
                        record.setCode("organization-member");
                    }
                    record.setTenantId(memberRoleDTO.getSourceId());
                    record.setLevel(memberRoleDTO.getSourceType());
                    Role role = roleHzeroMapper.selectOne(record);
                    MemberRole memberRole = new MemberRole();
                    memberRole.setId(memberRoleDTO.getId());
                    memberRole.setMemberId(memberRoleDTO.getMemberId());
                    memberRole.setRoleId(role.getId());
                    memberRole.setSourceId(memberRoleDTO.getSourceId());
                    memberRole.setAssignLevel(memberRoleDTO.getSourceType());
                    memberRole.setMemberType(memberRoleDTO.getMemberType());
                    memberRole.setSourceType(memberRoleDTO.getSourceType());
                    memberRole.setAssignLevelValue(memberRoleDTO.getSourceId());

                    memberRole.setCreatedBy(memberRoleDTO.getCreatedBy());
                    memberRole.setCreationDate(memberRoleDTO.getCreationDate());
                    memberRole.setLastUpdateDate(memberRoleDTO.getLastUpdateDate());
                    memberRole.setLastUpdatedBy(memberRoleDTO.getLastUpdatedBy());
                    memberRole.setObjectVersionNumber(memberRoleDTO.getObjectVersionNumber());
                    memberRoles1.add(memberRole);
                }

                //批量插入
                projectUserMapper.batchInsert(projectUsers);
                memberRoleHzeroMapper.batchInsert(memberRoles1);
            }
            pageNum++;
        } while (pageNum <= totalPage);

    }

    @Override
    public void labelAndRoleDataMigration() {

        List<Tenant> tenants = tenantMapper.selectAll();
        //4.初始化role
        for (Tenant tenant : tenants) {
            tenantRoleObserver.tenantCreate(tenant);
        }

        //iam_label
//        MigrationUtils.migration(LabelDTO.class, labelC7NMapper, labelHzeroMapper, t -> t.stream().map(
//                labelDTO -> {
//                    Label label = new Label();
//                    label.setId(labelDTO.getId());
//                    label.setDescription(labelDTO.getDescription());
//                    //默认标签可用
//                    label.setEnabledFlag(Integer.valueOf(1));
//                    label.setFdLevel(labelDTO.getLevel());
//                    label.setName(labelDTO.getName());
//                    label.setType(labelDTO.getType());
//
//                    label.setCreatedBy(labelDTO.getCreatedBy());
//                    label.setCreationDate(labelDTO.getCreationDate());
//                    label.setLastUpdateDate(labelDTO.getLastUpdateDate());
//                    label.setLastUpdatedBy(labelDTO.getLastUpdatedBy());
//                    label.setObjectVersionNumber(labelDTO.getObjectVersionNumber());
//                    return label;
//                }
//        ).collect(Collectors.toList()));


        //iam_role
//        MigrationUtils.migration(RoleDTO.class, roleC7NMapper, roleHzeroMapper, t -> t.stream().map(roleDTO -> {
//            Role role = new Role();
//            role.setId(roleDTO.getId());
//            role.setAssignable(Boolean.FALSE);
//            role.setBuiltIn(roleDTO.getBuiltIn());
//            role.setCode(roleDTO.getCode());
//            role.setDescription(roleDTO.getDescription());
//            role.setCreatedByTenantId(roleDTO.getOrganizationId());
//            role.setEnableForbidden(roleDTO.getEnableForbidden());
//            role.setLevel(roleDTO.getResourceLevel());
//            role.setName(roleDTO.getName());
//            role.setTenantId(roleDTO.getOrganizationId());
//            role.setModified(roleDTO.getModified());
//
//            role.setCreatedBy(roleDTO.getCreatedBy());
//            role.setCreationDate(roleDTO.getCreationDate());
//            role.setLastUpdateDate(roleDTO.getLastUpdateDate());
//            role.setLastUpdatedBy(roleDTO.getLastUpdatedBy());
//            role.setObjectVersionNumber(roleDTO.getObjectVersionNumber());
//            return role;
//        }).collect(Collectors.toList()));


        //role_label
//        MigrationUtils.migration(RoleLabelDTO.class, roleLabelC7NMapper, roleLabelHzeroMapper, objects -> ConvertUtils.convertList(objects, RoleLabel.class));

    }
}
