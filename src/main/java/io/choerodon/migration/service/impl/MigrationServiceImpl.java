package io.choerodon.migration.service.impl;

import io.choerodon.migration.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * User: Mr.Wang
 * Date: 2020/4/27
 */
@Service
public class MigrationServiceImpl implements MigrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationServiceImpl.class);

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private OraganizationService oraganizationService;

    @Autowired
    private UserServcie userServcie;

    @Autowired
    private OauthService oauthService;

    @Autowired
    private LabelAndRoleService labelAndRoleService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private InvitationInfoService invitationInfoService;

    @Autowired
    private RegisterInfoService registerInfoService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private GitLabActiveUserService gitLabActiveUserService;

    @Autowired
    private SysSettingService sysSettingService;

    @Autowired
    private UploadHistoryService uploadHistoryService;


    @Override
    public void migrationData(String moduleName) {
        threadPoolExecutor.execute(new ThreadTask(moduleName));
    }

    public class ThreadTask implements Runnable {
        private String moduleName;

        public ThreadTask(String moduleName) {
            this.moduleName = moduleName;
        }

        public void run() {

            if ("oauth-ldap".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION OAUTH-LDAP DATA======================================");
                oauthService.oauthLdapMigration();
                oauthService.oauthLdapAutoMigration();
                LOGGER.info("======================================END MIGRATION OAUTH-LDAP DATA======================================");
            }

            if ("oauth-password".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION OAUTH-LDAP-ERROR-USER DATA======================================");
                oauthService.oauthPasswordPolicyMigration();
                oauthService.oauthPasswordHistoryMigration();
                LOGGER.info("======================================END MIGRATION OAUTH-LDAP-ERROR-USER DATA======================================");
            }
            if ("organization".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION ORGANIZATION DATA======================================");
                oraganizationService.organizationDataMigration();
                LOGGER.info("======================================END MIGRATION ORGANIZATION DATA======================================");
            }
            if ("role".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION  ROLE DATA======================================");
                labelAndRoleService.labelAndRoleDataMigration();
                LOGGER.info("======================================END MIGRATION  ROLE DATA======================================");
            }

            if ("user".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION USER DATA======================================");
                userServcie.userDataMigration();
                LOGGER.info("======================================END MIGRATION USER DATA======================================");
            }

            if ("memberAndRole".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION MEMBER AND ROLE DATA======================================");
                labelAndRoleService.memberAndRoleDataMigration();
                LOGGER.info("======================================END MIGRATION MEMBER AND ROLE DATA======================================");
            }
            if ("project".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION PROJECT DATA======================================");
                projectService.projectDataMigration();
                LOGGER.info("======================================END MIGRATION PROJECT DATA======================================");
            }
            if ("invitationInfo".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION INVITATIONINFO DATA======================================");
                invitationInfoService.invitationInfoDataMigration();
                LOGGER.info("======================================END MIGRATION INVITATIONINFO DATA======================================");
            }
            if ("registerInfo".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION REGISTERINFO DATA======================================");
                registerInfoService.registerInfoDataMigration();
                LOGGER.info("======================================END MIGRATION REGISTERINFO DATA======================================");
            }
            if ("report".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION REPORT DATA======================================");
                reportService.reportDataMigration();
                LOGGER.info("======================================END MIGRATION REPORT DATA======================================");
            }
            if ("gitlabActiveUser".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION GITLABACTIVEUSER DATA======================================");
                gitLabActiveUserService.gitlabActiveUserDataMigration();
                LOGGER.info("======================================END MIGRATION GITLABACTIVEUSER DATA======================================");
            }
            if ("sysSetting".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION SYSSETTING DATA======================================");
                sysSettingService.sysSettingDataMigration();
                LOGGER.info("======================================END MIGRATION SYSSETTING DATA======================================");
            }
            if ("uploadHistory".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION UPLOADHISTORY DATA======================================");
                uploadHistoryService.uploadHistoryDataMigration();
                LOGGER.info("======================================END MIGRATION UPLOADHISTORY DATA======================================");
            }
            if ("oauth-client".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION OAUTH-CLIENT DATA======================================");
                oauthService.oauthClientMigration();
                LOGGER.info("======================================END MIGRATION OAUTH-CLIENT DATA======================================");
            }

            if ("oauth-ldap-error-user".equals(moduleName)) {
                LOGGER.info("======================================START MIGRATION OAUTH-LDAP-ERROR-USER DATA======================================");
                oauthService.oauthLdapErrorUserMigration();
                LOGGER.info("======================================END MIGRATION OAUTH-LDAP-ERROR-USER DATA======================================");
            }


        }
    }
}
