package io.choerodon.migration.infra.util;

import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.helper.feign.LanguageRemoteService;
import org.apache.commons.lang3.LocaleUtils;
import org.hzero.core.util.ResponseUtils;
import org.hzero.mybatis.domian.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * User: Mr.Wang
 * Date: 2020/5/7
 */
public class LanguageHelper {
    private static final Logger logger = LoggerFactory.getLogger(io.choerodon.mybatis.helper.LanguageHelper.class);
    private static final List<Language> LANGUAGES = new LinkedList();
    private static ThreadLocal<String> language = new ThreadLocal();
    private static List<Language> languages;
    private static volatile LanguageRemoteService languageRemoteService;

    public LanguageHelper() {
    }

    public static List<Language> languages() {
        if (languages == null) {
            loadLanguages();
        }

        return CollectionUtils.isEmpty(languages) ? LANGUAGES : languages;
    }

    public static String language() {
        CustomUserDetails details = DetailsHelper.getUserDetails();
        if (details != null) {
            language(details.getLanguage());
        } else if (language.get() == null) {
            language("zh_CN");
            logger.warn("principal not instanceof CustomUserDetails language is zh_CN");
        }

        return (String)language.get();
    }

    public static void language(String lang) {
        language.set(lang);
    }

    public static Locale locale() {
        return LocaleUtils.toLocale(language());
    }

    private static void loadLanguages() {
        logger.info("Loading languages...");
        languages = (List)ResponseUtils.getResponse(languageRemoteService().listLanguage(), new TypeReference<List<Language>>() {
        });
        if (CollectionUtils.isEmpty(languages)) {
            logger.error("Language is empty!");
        }

        logger.info("Loaded languages, {}", languages);
    }

    private static LanguageRemoteService languageRemoteService() {
        if (languageRemoteService == null) {
            Class var0 = io.choerodon.mybatis.helper.LanguageHelper.class;
            synchronized(io.choerodon.mybatis.helper.LanguageHelper.class) {
                if (languageRemoteService == null) {
                    languageRemoteService = (LanguageRemoteService)ApplicationContextHelper.getContext().getBean(LanguageRemoteService.class);
                }
            }
        }

        return languageRemoteService;
    }

    static {
        LANGUAGES.add((new Language()).setCode("zh_CN").setName("简体中文").setDescription("中文(简体)"));
        LANGUAGES.add((new Language()).setCode("en_US").setName("English").setDescription("英文(美式)"));
    }
}
