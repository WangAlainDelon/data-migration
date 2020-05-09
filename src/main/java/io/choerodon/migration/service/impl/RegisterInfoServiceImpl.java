package io.choerodon.migration.service.impl;

import io.choerodon.migration.domian.c7n.RegisterInfoDTO;
import io.choerodon.migration.domian.hzero.RegisterInfo;
import io.choerodon.migration.infra.util.ConvertUtils;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.RegisterInfoC7NMapper;
import io.choerodon.migration.mapper.hzero.platform.RegisterInfoHzeroMapper;
import io.choerodon.migration.service.RegisterInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Mr.Wang
 * Date: 2020/4/30
 */
@Service
public class RegisterInfoServiceImpl implements RegisterInfoService {

    @Autowired
    private RegisterInfoC7NMapper registerInfoC7NMapper;
    @Autowired
    private RegisterInfoHzeroMapper registerInfoHzeroMapper;

    @Override
    public void registerInfoDataMigration() {
        //fd_register_info
        MigrationUtils.migration(RegisterInfoDTO.class, registerInfoC7NMapper, registerInfoHzeroMapper, objects -> ConvertUtils.convertList(objects, RegisterInfo.class));
    }
}
