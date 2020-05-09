package io.choerodon.migration.infra.util;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.hzero.core.exception.NotLoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

/**
 * 模拟用户登录工具
 *
 * @author bojiangzhou 2018/07/12
 */
public class UserUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserUtils.class);

    /**
     * 获取当前登录用户 UserDetails. 未登录则抛出异常.
     * 
     * @return CustomUserDetails
     */
    public static CustomUserDetails getUserDetails() {
        return Optional.ofNullable(DetailsHelper.getUserDetails()).orElseThrow(NotLoginException::new);
    }

    /**
     * 模拟登录，使得可以通过DetailsHelper获取用户ID、租户ID等信息
     * 
     * @param userRepository 用户资源库
     * @param loginName 登录名
     */
//    public static void login(UserHzeroMapper userRepository, String loginName) {
//        CustomUserDetails details = new CustomUserDetails(loginName, "", Collections.emptyList());
//        User sampleUser = new User();
//        sampleUser.setLoginName(loginName);
//        User user = userRepository.selectOne(sampleUser);
//
//        details.setUserId(user.getId());
//        details.setLanguage(user.getLanguage());
//        details.setTimeZone(user.getTimeZone());
//        details.setEmail(user.getEmail());
//        details.setOrganizationId(user.getOrganizationId());
//
//        AbstractAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(details, "", Collections.emptyList());
//        authentication.setDetails(details);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }

    /**
     * 模拟登陆
     *
     * @param loginName 登录名
     * @param userId 用户ID
     * @param organizationId 租户ID
     */
    public static void login(String loginName, Long userId, Long organizationId) {
        CustomUserDetails details = new CustomUserDetails(loginName, "", Collections.emptyList());
        details.setUserId(userId);
        details.setLanguage("zh_CN");
        details.setTimeZone("CTT");
        details.setEmail("hand@hand-china.com");
        details.setOrganizationId(organizationId);

        AbstractAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(details, "", Collections.emptyList());
        authentication.setDetails(details);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

//    public static void checkSuperAdmin() {
//        CustomUserDetails self = UserUtils.getUserDetails();
//        if (BaseConstants.ANONYMOUS_USER_ID.equals(self.getUserId()) || BooleanUtils.isTrue(self.getAdmin())) {
//            return;
//        }
//
//        LOGGER.debug("checkSuperAdmin -> CustomUserDetails: {}", self);
//
//        RoleMapper roleRepository = ApplicationContextHelper.getContext().getBean(RoleMapper.class);
//        Role role = roleRepository.selectRoleSimpleById(self.getRoleId());
//        if (!StringUtils.equalsAny(role.getCode(), HZeroConstant.RoleCode.SITE, HZeroConstant.RoleCode.TENANT)) {
//            throw new CommonException("hiam.warn.operation.onlySupperAdminAllowed");
//        }
//    }

}
