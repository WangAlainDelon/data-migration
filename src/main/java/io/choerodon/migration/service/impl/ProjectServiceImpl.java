package io.choerodon.migration.service.impl;

import io.choerodon.migration.domian.c7n.*;
import io.choerodon.migration.domian.hzero.*;
import io.choerodon.migration.infra.util.ConvertUtils;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.*;
import io.choerodon.migration.mapper.hzero.platform.*;
import io.choerodon.migration.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Mr.Wang
 * Date: 2020/4/30
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectC7NMapper projectC7NMapper;

    @Autowired
    private ProjectHzeroMapper projectHzeroMapper;

    @Autowired
    private ProjectCategoryC7NMapper projectCategoryC7NMapper;

    @Autowired
    private ProjectCategoryHzeroMapper projectCategoryHzeroMapper;

    @Autowired
    private ProjectMapCategoryC7NMapper projectMapCategoryC7NMapper;

    @Autowired
    private ProjectMapCategoryHzeroMapper projectMapCategoryHzeroMapper;

    @Autowired
    private ProjectTypeC7NMapper projectTypeC7NMapper;

    @Autowired
    private ProjectTypeHzeroMapper projectTypeHzeroMapper;

    @Autowired
    private ProjectRelationshipC7NMapper projectRelationshipC7NMapper;

    @Autowired
    private ProjectRelationshipHzeroMapper projectRelationshipHzeroMapper;

    @Override
    public void projectDataMigration() {
        //fd_project
        MigrationUtils.migration(ProjectDTO.class, projectC7NMapper, projectHzeroMapper, objects -> ConvertUtils.convertList(objects, Project.class));

        //fd_project_category
        MigrationUtils.migration(ProjectCategoryDTO.class, projectCategoryC7NMapper, projectCategoryHzeroMapper, objects -> ConvertUtils.convertList(objects, ProjectCategory.class));

        //fd_project_map_category
        MigrationUtils.migration(ProjectMapCategoryDTO.class, projectMapCategoryC7NMapper, projectMapCategoryHzeroMapper, objects -> ConvertUtils.convertList(objects, ProjectMapCategory.class));

        //da_project_type
        MigrationUtils.migration(ProjectTypeDTO.class, projectTypeC7NMapper, projectTypeHzeroMapper, objects -> ConvertUtils.convertList(objects, ProjectType.class));

        //fd_project_relationship
        MigrationUtils.migration(ProjectRelationshipDTO.class, projectRelationshipC7NMapper, projectRelationshipHzeroMapper, objects -> ConvertUtils.convertList(objects, ProjectRelationship.class));
    }
}
