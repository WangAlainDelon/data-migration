package io.choerodon.migration.service;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/4/28 17:36
 */
public interface OauthService {
    /**
     * oauth_client表数据迁移
     */
    void oauthClientMigration();

    void oauthLdapMigration();

    void oauthLdapAutoMigration();

    void oauthLdapErrorUserMigration();

    void oauthPasswordPolicyMigration();

    void oauthPasswordHistoryMigration();
}
