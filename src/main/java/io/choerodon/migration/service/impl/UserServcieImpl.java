package io.choerodon.migration.service.impl;

import io.choerodon.migration.domian.c7n.UserDTO;
import io.choerodon.migration.domian.c7n.UserInfoProDTO;
import io.choerodon.migration.domian.hzero.RegisterUserInfo;
import io.choerodon.migration.infra.util.ConvertUtils;
import io.choerodon.migration.infra.util.MigrationUtils;
import io.choerodon.migration.mapper.c7n.base.UserC7NMapper;
import io.choerodon.migration.mapper.c7n.base.UserInfoProMapper;
import io.choerodon.migration.mapper.hzero.platform.RegisterUserInfoMapper;
import io.choerodon.migration.mapper.hzero.platform.UserHzeroMapper;
import io.choerodon.migration.service.UserServcie;

import io.choerodon.migration.domian.hzero.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * User: Mr.Wang
 * Date: 2020/4/28
 */
@Service
public class UserServcieImpl implements UserServcie {

    @Autowired
    private UserC7NMapper userC7NMapper;
    @Autowired
    private UserInfoProMapper userInfoProMapper;
    @Autowired
    private UserHzeroMapper userHzeroMapper;

    @Autowired
    private RegisterUserInfoMapper registerUserInfoMapper;

    @Override
    public void userDataMigration() {
        //user表
        MigrationUtils.migration(UserDTO.class, userC7NMapper, userHzeroMapper,
                t -> t.stream().map(
                        userDTO -> {
                            User user = new User();
                            user.setId(userDTO.getId());
                            user.setAdmin(userDTO.getAdmin());
                            user.setEmail(userDTO.getEmail());
                            user.setEnabled(userDTO.getEnabled());
                            user.setImageUrl(userDTO.getImageUrl());
                            user.setPhone(userDTO.getPhone());
                            user.setInternationalTelCode(userDTO.getInternationalTelCode());
                            user.setLanguage(userDTO.getLanguage());
                            user.setLastLoginAt(userDTO.getLastLoginAt());
                            user.setLdap(userDTO.getLdap());
                            user.setLocked(userDTO.getLocked());
                            user.setLoginName(userDTO.getLoginName());
                            user.setLockedUntilAt(userDTO.getLockedUntilAt());
                            user.setOrganizationId(userDTO.getOrganizationId());
                            user.setPassword(userDTO.getPassword());
                            user.setRealName(userDTO.getRealName());
                            user.setLastPasswordUpdatedAt(userDTO.getLastPasswordUpdatedAt());
                            user.setPasswordAttempt(userDTO.getPasswordAttempt());
                            user.setProfilePhoto(userDTO.getProfilePhoto());
                            user.setTimeZone(userDTO.getTimeZone());
                            user.setUserType("P");

                            user.setCreatedBy(userDTO.getCreatedBy());
                            user.setCreationDate(userDTO.getCreationDate());
                            user.setLastUpdateDate(userDTO.getLastUpdateDate());
                            user.setLastUpdatedBy(userDTO.getLastUpdatedBy());
                            user.setObjectVersionNumber(userDTO.getObjectVersionNumber());
                            return user;
                        }
                ).collect(Collectors.toList())
        );
        //register_user表
        MigrationUtils.migration(UserInfoProDTO.class, userInfoProMapper, registerUserInfoMapper, objects -> ConvertUtils.convertList(objects,RegisterUserInfo.class));

    }
}
