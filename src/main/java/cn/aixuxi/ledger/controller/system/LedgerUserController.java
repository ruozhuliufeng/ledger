package cn.aixuxi.ledger.controller.system;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.constant.LedgerConstant;
import cn.aixuxi.ledger.entity.system.LedgerRole;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.entity.system.LedgerUserRole;
import cn.aixuxi.ledger.service.system.LedgerRoleService;
import cn.aixuxi.ledger.service.system.LedgerUserRoleService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.aixuxi.ledger.vo.LedgerQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 系统管理-用户管理
 *
 * @author ruozhuliufeng
 */
@RestController
@RequestMapping("/system/user")
@AllArgsConstructor
public class LedgerUserController {
    private final LedgerUserService userService;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;
    private final LedgerRoleService roleService;
    private final LedgerUserRoleService userRoleService;

    /**
     * 查看用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/info/{id}")
    public Result<LedgerUser> info(@PathVariable("id") Long id) {
        LedgerUser user = userService.getById(id);
        Assert.notNull(user, "找不到该用户");
        List<LedgerRole> roles = roleService.listRolesByUserId(id);
        user.setRoles(roles);
        return Result.succeed(user);
    }

    /**
     * 用户列表分页查询
     *
     * @param username 用户名
     * @return 分页用户数据
     */
    @GetMapping("/list")
    public Result<Page<LedgerUser>> list(String username) {
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int size = ServletRequestUtils.getIntParameter(request, "size", 10);

        Page<LedgerUser> pageData = userService.page(new Page<>(current, size), new QueryWrapper<LedgerUser>()
                .like(StrUtil.isNotBlank(username), "account", username));
        pageData.getRecords().forEach(item -> item.setRoles(roleService.listRolesByUserId(item.getId())));
        return Result.succeed(pageData);
    }

    /**
     * 系统管理-新增用户
     *
     * @param user 用户信息
     * @return 新增后用户信息
     */
    @PostMapping("/save")
    public Result<LedgerUser> save(@RequestBody LedgerUser user) {
        user.setCreateTime(new Date());
        user.setStatus(LedgerConstant.STATUS_ON);
        // 默认密码
        String password = passwordEncoder.encode(LedgerConstant.DEFULT_PASSWORD);
        user.setPassword(password);

        // 默认头像
        user.setAvatar(LedgerConstant.DEFULT_AVATAR);
        userService.save(user);
        return Result.succeed(user);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    @PostMapping("/update")
    public Result<LedgerUser> update(@RequestBody LedgerUser user) {
        user.setUpdateTime(new Date());
        userService.updateById(user);
        return Result.succeed(user);
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户ID
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody List<Integer> ids) {
        userService.removeByIds(ids);
        userRoleService.remove(new QueryWrapper<LedgerUserRole>().in("user_id", ids));
        return Result.succeed();
    }

    /**
     * 为用户分配角色
     *
     * @param userId  用户ID
     * @param query 角色ID列表
     */
    @PostMapping("/role/{userId}")
    public Result rolePerm(@PathVariable("userId") Long userId, @RequestBody LedgerQuery query) {
        List<LedgerUserRole> userRoles = new ArrayList<>();
        query.getRoleIds().forEach(roleId -> {
            LedgerUserRole userRole = new LedgerUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        });

        userRoleService.remove(new QueryWrapper<LedgerUserRole>().eq("user_id", userId));
        userRoleService.saveBatch(userRoles);

        // 删除缓存
        LedgerUser user = userService.getById(userId);
        userService.clearUserAuthorityInfo(user.getAccount());

        return Result.succeed();
    }

    /**
     * 重置密码
     *
     * @param userId 用户ID
     */
    @PostMapping("/repass")
    public Result repass(@RequestBody Integer userId) {
        LedgerUser user = userService.getById(userId);
        user.setPassword(passwordEncoder.encode(LedgerConstant.DEFULT_PASSWORD));
        user.setUpdateTime(new Date());
        userService.updateById(user);
        return Result.succeed();
    }

    /**
     * 上传头像
     *
     * @param avatarFile 头像文件
     * @return 头像地址
     */
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(@RequestParam("avatarFile") MultipartFile avatarFile) {
        String avatarUrl = userService.uploadAvatar(avatarFile);
        return Result.succeed(avatarUrl);
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @GetMapping("/query/user/phone")
    public Result<LedgerUser> queryUserByPhone(@RequestParam("phone") String phone) {
        LedgerUser user = userService.getOne(new QueryWrapper<LedgerUser>()
                .like("phone", phone));
        if (ObjectUtils.isEmpty(user)) {
            return Result.failed("该用户未注册或未填写手机号码，请核实");
        } else {
            return Result.succeed(user);
        }
    }
}
