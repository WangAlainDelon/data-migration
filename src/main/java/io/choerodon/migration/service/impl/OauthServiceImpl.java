package io.choerodon.migration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.choerodon.migration.domian.c7n.*;
import io.choerodon.migration.domian.hzero.*;
import io.choerodon.migration.infra.util.ConvertUtils;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.*;
import io.choerodon.migration.mapper.hzero.platform.*;
import io.choerodon.migration.service.OauthService;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/4/28 17:36
 */
@Service
public class OauthServiceImpl implements OauthService {


    @Autowired
    private C7nClientMapper c7nClientMapper;
    @Autowired
    private HzeroClientMapper hzeroClientMapper;
    @Autowired
    private C7NLdapMapperC7n c7nLdapMapper;
    @Autowired
    private HzeroLdapMapper hzeroLdapMapper;
    @Autowired
    private C7nLdapErrorUserMapper c7nLdapErrorUserMapper;
    @Autowired
    private HzeroLdapErrorUserMapper hzeroLdapErrorUserMapper;
    @Autowired
    private C7nPasswordPolicyMapper c7nPasswordPolicyMapper;
    @Autowired
    private HzeroPasswordPolicyMapper hzeroPasswordPolicyMapper;
    @Autowired
    private C7nPasswordHistoryMapper c7nPasswordHistoryMapper;
    @Autowired
    private HzeroPasswordHistoryMapper hzeroPasswordHistoryMapper;

    @Override
    public void oauthClientMigration() {
        MigrationUtils.migration(ClientDTO.class, c7nClientMapper, hzeroClientMapper, t -> ConvertUtils.convertList(t, Client.class));
    }

    @Override
    public void oauthLdapMigration() {
        MigrationUtils.migration(LdapDTO.class, c7nLdapMapper, hzeroLdapMapper, t -> ConvertUtils.convertList(t, Ldap.class));
    }

    @Override
    public void oauthLdapAutoMigration() {

    }

    @Override
    public void oauthLdapErrorUserMigration() {
        MigrationUtils.migration(LdapErrorUserDTO.class, c7nLdapErrorUserMapper, hzeroLdapErrorUserMapper, t -> ConvertUtils.convertList(t, LdapErrorUser.class));
    }

    @Override
    public void oauthPasswordPolicyMigration() {
        MigrationUtils.migration(PasswordPolicyDTO.class, c7nPasswordPolicyMapper, hzeroPasswordPolicyMapper, t -> ConvertUtils.convertList(t, PasswordPolicy.class));
    }

    @Override
    public void oauthPasswordHistoryMigration() {
        MigrationUtils.migration(PasswordHistoryDTO.class, c7nPasswordHistoryMapper, hzeroPasswordHistoryMapper, t -> ConvertUtils.convertList(t, PasswordHistory.class));
    }

}
