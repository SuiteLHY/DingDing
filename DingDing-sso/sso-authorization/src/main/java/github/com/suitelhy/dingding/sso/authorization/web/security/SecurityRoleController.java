package github.com.suitelhy.dingding.sso.authorization.web.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import github.com.suitelhy.dingding.core.infrastructure.application.model.TaskResult;
import github.com.suitelhy.dingding.core.infrastructure.domain.Page;
import github.com.suitelhy.dingding.core.infrastructure.web.model.WebResult;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.OAuth2AuthenticationInfo;
import github.com.suitelhy.dingding.security.service.api.infrastructure.web.util.OAuth2AuthenticationUtil;
import github.com.suitelhy.dingding.sso.authorization.application.task.RoleTask;
import github.com.suitelhy.dingding.sso.authorization.infrastructure.application.dto.RoleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * (安全) 权限业务
 *
 * @Description (安全) 权限控制相关业务.
 */
@RestController
@RequestMapping("/security/role")
@Slf4j
public class SecurityRoleController {

    @Autowired
    private RoleTask roleTask;

    /**
     * 获取当前用户角色列表
     *
     * @param authentication
     * @param pageIndex
     * @param pageSize
     *
     * @return API响应的主要数据
     */
    @GetMapping("/roleList")
    public String getRoleList(final @AuthenticationPrincipal OAuth2Authentication authentication
            , @RequestParam(defaultValue = "0") int pageIndex
            , @RequestParam(defaultValue = "10") int pageSize)
    {
        @NotNull WebResult<Page<RoleDto>> result;
        try {
//            final Map<String, Object> oAuth2Details = (Map<String, Object>) authentication.getUserAuthentication().getDetails();
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

            @NotNull TaskResult<Page<RoleDto>> taskResult_RoleDto = roleTask.selectRoleByUsername(userAuthenticationDetails.getUserName(), pageIndex, pageSize
                    , userAuthenticationDetails);

            result = WebResult.Factory.DEFAULT.create(taskResult_RoleDto);
            /*return toJSONString.writeValueAsString(result);*/
        } catch (Exception e) {
            log.error("SecurityRoleController#getRoleList", e);
            e.printStackTrace();

            result = WebResult.Factory.DEFAULT.createUnknown("接口异常!"
                    , Page.empty()
                    , ""
                    , "");
            /*result.extra.put(TaskResult.Vo.ExtraPropertyVo.EXCEPTION.key, e.getMessage());*/
        }
        /*return "{}";*/
        return result.toString();
    }

    /**
     * 获取全部用户角色列表
     *
     * @param authentication
     * @param pageIndex
     * @param pageSize
     *
     * @return API响应的主要数据
     */
    @GetMapping("/allRoleList")
    public String getAllRoleList(final @AuthenticationPrincipal OAuth2Authentication authentication
            , @RequestParam(defaultValue = "0") String pageIndex
            , @RequestParam(defaultValue = "10") String pageSize)
    {
        @NotNull WebResult<Page<RoleDto>> result;
        try {
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

            final TaskResult<Page<RoleDto>> TaskResult_RoleDtoList = roleTask.selectAllRole(Integer.parseInt(pageIndex)
                    , Integer.parseInt(pageSize)
                    , userAuthenticationDetails);

            result = WebResult.Factory.DEFAULT.create(TaskResult_RoleDtoList);
            /*return toJSONString.writeValueAsString(result);*/
        } catch (NumberFormatException e) {
            result = WebResult.Factory.DEFAULT.createFailure(WebResult.Vo.StatusVo.FAILURE
                    , "请求参数非法!"
                    , Page.empty()
                    , ""
                    , WebResult.Vo.ErrorVo.BAD_REQUEST
                    , "");
        } catch (Exception e) {
            log.error("SecurityRoleController#getRoleList", e);
            e.printStackTrace();

            result = WebResult.Factory.DEFAULT.createUnknown("接口异常!"
                    , Page.empty()
                    , ""
                    , "");
        }
        return result.toString();
    }

    /**
     * 添加角色
     *
     * @Description 添加单个角色.
     *
     * @param authentication
     * @param roleCode
     * @param roleName
     * @param roleDescription
     *
     * @return API响应的主要数据
     */
    @PutMapping("/assignedRole")
    public String addRole(@AuthenticationPrincipal final OAuth2Authentication authentication
            , @RequestParam(name = "code") String roleCode
            , @RequestParam(name = "name") String roleName
            , @RequestParam(name = "description") String roleDescription)
            throws JsonProcessingException
    {
        /*final Map<String, Object> result = new LinkedHashMap<>(3);*/
        try {
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

//            final Map<String, Object> new_role_data = new HashMap<>(2);
//            if (!StringUtils.isEmpty(roleName)) {
//                new_role_data.put("role_name", roleName);
//            }
//            if (!StringUtils.isEmpty(roleDescription)) {
//                new_role_data.put("role_description", roleDescription);
//            }

            @NotNull TaskResult<RoleDto> taskResult_addRole = roleTask.addRole(roleCode
                    , roleName
                    , roleDescription
                    , userAuthenticationDetails);

            return WebResult.Factory.DEFAULT.create(taskResult_addRole).toString();
            /*if (taskResult_AddRole.isSuccess()) {
                result.put("status", true);
                result.put("message", "操作成功");
                result.put("description", "角色 {".concat(roleCode).concat("} 添加成功"));

                return toJSONString.writeValueAsString(result);
            } else if (taskResult_AddRole.isFailure()) {
                result.put("status", false);
                result.put("message", "操作失败");
                result.put("description", "没有足够的操作权限");

                return toJSONString.writeValueAsString(result);
            } else {
                result.put("status", false);
                result.put("message", "操作失败");
                result.put("description", "指定角色已存在或非法参数");

                return toJSONString.writeValueAsString(result);
            }*/
        } catch (Exception e) {
            log.error("SecurityRoleController#deleteRole", e);
            e.printStackTrace();
        }

        /*result.put("status", false);
        result.put("message", "操作失败");
        result.put("description", "接口异常");
        return toJSONString.writeValueAsString(result);*/
        return WebResult.Factory.DEFAULT.createDefault().toString();
    }

    /**
     * 更新指定角色
     *
     * @Description 局部更新.
     *
     * @param authentication
     * @param old_roleCode
     * @param old_roleName
     * @param old_roleDescription
     * @param roleName
     * @param roleDescription
     *
     * @return API响应的主要数据
     */
    @PatchMapping("/assignedRole")
    public String updateRole(@AuthenticationPrincipal final OAuth2Authentication authentication
            , @RequestParam(name = "old_code") String old_roleCode
            , @RequestParam(name = "old_name") String old_roleName
            , @RequestParam(name = "old_description") String old_roleDescription
            , @RequestParam(name = "new_name", required = false) String roleName
            , @RequestParam(name = "new_description", required = false) String roleDescription)
    {
        try {
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

            Map<String, Object> new_role_data = new HashMap<>(1);
            if (! StringUtils.isEmpty(roleName)) {
                new_role_data.put("role_name", roleName);
            }
            if (! StringUtils.isEmpty(roleDescription)) {
                new_role_data.put("role_description", roleDescription);
            }

            @NotNull TaskResult<RoleDto> taskResult_updateRole = roleTask.updateRole(old_roleCode
                    , old_roleName
                    , old_roleDescription
                    , new_role_data
                    , userAuthenticationDetails);

            return WebResult.Factory.DEFAULT
                    .create(taskResult_updateRole)
                    .toString();
        } catch (Exception e) {
            log.error("SecurityRoleController#deleteRole", e);
            e.printStackTrace();
        }
        return WebResult.Factory.DEFAULT.createDefault().toString();
    }

    /**
     * 删除指定角色
     *
     * @param authentication
     * @param roleCode
     *
     * @return API响应的主要数据
     */
    @DeleteMapping("/assignedRole")
    public String deleteRole(@AuthenticationPrincipal final OAuth2Authentication authentication
            , @RequestParam(name = "role_code") String roleCode)
    {
        try {
            final OAuth2AuthenticationInfo.AbstractUserAuthentication.@NotNull AbstractDetails userAuthenticationDetails = OAuth2AuthenticationUtil.getInstance().getDetails(authentication);

            @NotNull TaskResult<RoleDto> taskResult_deleteRole = roleTask.deleteRole(roleCode, userAuthenticationDetails);

            return WebResult.Factory.DEFAULT.create(taskResult_deleteRole).toString();
        } catch (Exception e) {
            log.error("SecurityRoleController#deleteRole", e);
            e.printStackTrace();
        }
        return WebResult.Factory.DEFAULT.createDefault().toString();
    }

}
