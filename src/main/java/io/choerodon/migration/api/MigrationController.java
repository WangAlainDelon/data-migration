package io.choerodon.migration.api;

import io.choerodon.migration.service.MigrationService;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: Mr.Wang
 * Date: 2020/4/27
 */
@RestController
@RequestMapping("/v1/migration")
public class MigrationController {


    @Autowired
    private MigrationService migrationService;


    @ApiModelProperty("迁移接口，传入功能模块的名字")
    @GetMapping
    @Permission(permissionPublic = true)
    public void migration(@RequestParam(value = "moduleName") String moduleName) {
        migrationService.migrationData(moduleName);
    }
}
