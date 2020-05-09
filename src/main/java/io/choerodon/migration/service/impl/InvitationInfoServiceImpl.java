package io.choerodon.migration.service.impl;

import io.choerodon.migration.domian.c7n.InvitationInfoDTO;
import io.choerodon.migration.domian.hzero.InvitationInfo;
import io.choerodon.migration.infra.util.ConvertUtils;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.InvitationInfoC7nMapper;
import io.choerodon.migration.mapper.hzero.platform.InvitationInfoHzeroMapper;
import io.choerodon.migration.service.InvitationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Mr.Wang
 * Date: 2020/4/30
 */
@Service
public class InvitationInfoServiceImpl implements InvitationInfoService {

    @Autowired
    private InvitationInfoC7nMapper invitationInfoC7nMapper;
    @Autowired
    private InvitationInfoHzeroMapper invitationInfoHzeroMapper;

    @Override
    public void invitationInfoDataMigration() {
        //fd_invitation_info
        MigrationUtils.migration(InvitationInfoDTO.class, invitationInfoC7nMapper, invitationInfoHzeroMapper, objects -> ConvertUtils.convertList(objects, InvitationInfo.class));
    }
}
