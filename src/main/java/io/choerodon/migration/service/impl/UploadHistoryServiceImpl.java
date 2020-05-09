package io.choerodon.migration.service.impl;

import io.choerodon.migration.domian.c7n.UploadHistoryDTO;
import io.choerodon.migration.domian.hzero.UploadHistory;
import io.choerodon.migration.infra.util.ConvertUtils;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.UploadHistoryC7NMapper;
import io.choerodon.migration.mapper.hzero.platform.UploadHistoryHzeroMapper;
import io.choerodon.migration.service.UploadHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Mr.Wang
 * Date: 2020/4/30
 */
@Service
public class UploadHistoryServiceImpl implements UploadHistoryService {

    @Autowired
    private UploadHistoryC7NMapper uploadHistoryC7NMapper;
    @Autowired
    private UploadHistoryHzeroMapper uploadHistoryHzeroMapper;

    @Override
    public void uploadHistoryDataMigration() {
        //iam_upload_history
        MigrationUtils.migration(UploadHistoryDTO.class, uploadHistoryC7NMapper, uploadHistoryHzeroMapper, objects -> ConvertUtils.convertList(objects, UploadHistory.class));
    }
}
