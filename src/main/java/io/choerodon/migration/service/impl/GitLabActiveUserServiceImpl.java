package io.choerodon.migration.service.impl;

import io.choerodon.migration.domian.c7n.GitlabAcitveUserDTO;
import io.choerodon.migration.domian.hzero.GitlabAcitveUser;
import io.choerodon.migration.infra.util.ConvertUtils;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.GitlabAcitveUserC7NMapper;
import io.choerodon.migration.mapper.hzero.platform.GitlabAcitveUserHzeroMapper;
import io.choerodon.migration.service.GitLabActiveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Mr.Wang
 * Date: 2020/4/30
 */
@Service
public class GitLabActiveUserServiceImpl implements GitLabActiveUserService {

    @Autowired
    private GitlabAcitveUserC7NMapper gitlabAcitveUserC7NMapper;

    @Autowired
    private GitlabAcitveUserHzeroMapper gitlabAcitveUserHzeroMapper;

    @Override
    public void gitlabActiveUserDataMigration() {
        //gitlab_acitve_user
        MigrationUtils.migration(GitlabAcitveUserDTO.class, gitlabAcitveUserC7NMapper, gitlabAcitveUserHzeroMapper, objects -> ConvertUtils.convertList(objects, GitlabAcitveUser.class));
    }
}
