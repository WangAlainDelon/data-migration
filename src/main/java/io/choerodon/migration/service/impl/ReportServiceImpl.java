package io.choerodon.migration.service.impl;

import io.choerodon.migration.domian.c7n.ReportDTO;
import io.choerodon.migration.domian.hzero.Report;
import io.choerodon.migration.infra.util.ConvertUtils;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.ReportC7NMapper;
import io.choerodon.migration.mapper.hzero.platform.ReportHzeroMapper;
import io.choerodon.migration.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Mr.Wang
 * Date: 2020/4/30
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportC7NMapper reportC7NMapper;

    @Autowired
    private ReportHzeroMapper reportHzeroMapper;

    @Override
    public void reportDataMigration() {
        //fd_report
        MigrationUtils.migration(ReportDTO.class, reportC7NMapper, reportHzeroMapper, objects -> ConvertUtils.convertList(objects, Report.class));
    }
}
