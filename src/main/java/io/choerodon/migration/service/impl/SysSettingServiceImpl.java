package io.choerodon.migration.service.impl;

import io.choerodon.migration.domian.c7n.SysSettingDTO;
import io.choerodon.migration.domian.hzero.SysSetting;
import io.choerodon.migration.infra.util.ConvertUtils;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.SysSettingC7NMapper;
import io.choerodon.migration.mapper.hzero.platform.SysSettingHzeroMapper;
import io.choerodon.migration.service.SysSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Mr.Wang
 * Date: 2020/4/30
 */
@Service
public class SysSettingServiceImpl implements SysSettingService {

    @Autowired
    private SysSettingC7NMapper sysSettingC7NMapper;

    @Autowired
    private SysSettingHzeroMapper sysSettingHzeroMapper;

    @Override
    public void sysSettingDataMigration() {
        //iam_sys_setting
        MigrationUtils.migration(SysSettingDTO.class, sysSettingC7NMapper, sysSettingHzeroMapper, objects -> ConvertUtils.convertList(objects, SysSetting.class));
    }
}
