package cn.aixuxi.ledger.controller.system;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.system.LedgerRole;
import cn.aixuxi.ledger.entity.system.LedgerRoleMenu;
import cn.aixuxi.ledger.entity.system.LedgerUserRole;
import cn.aixuxi.ledger.service.system.LedgerRoleMenuService;
import cn.aixuxi.ledger.service.system.LedgerRoleService;
import cn.aixuxi.ledger.service.system.LedgerUserRoleService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统管理-角色管理
 *
 * @author ruozhuliufeng
 */
@RestController
@RequestMapping("/system/role")
@AllArgsConstructor
public class LedgerRoleController {

    private final LedgerRoleService roleService;
    private final LedgerRoleMenuService roleMenuService;
    private final LedgerUserService userService;
    private final HttpServletRequest request;
    private final LedgerUserRoleService userRoleService;

    /**
     * 角色信息
     *
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/info/{id}")
    public Result<LedgerRole> info(@PathVariable("id") Integer id) {
        LedgerRole role = roleService.getById(id);
        // 获取角色相关联的菜单ID
        List<LedgerRoleMenu> roleMenuList = roleMenuService.list(new QueryWrapper<LedgerRoleMenu>().eq("role_id", id));
        List<Integer> menuIds = roleMenuList.stream().map(LedgerRoleMenu::getMenuId).collect(Collectors.toList());

        role.setMenuIds(menuIds);
        return Result.succeed(role);
    }

    /**
     * 角色列表
     *
     * @param name 角色名称
     * @return 角色列表
     */
    @PostMapping("list")
    public Result<Page<LedgerRole>> list(String name) {
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int size = ServletRequestUtils.getIntParameter(request, "size", 10);
        Page<LedgerRole> pageData = roleService.page(new Page<>(current, size),
                new QueryWrapper<LedgerRole>()
                        .like(StrUtil.isNotBlank(name), "name", name));
        return Result.succeed(pageData);
    }

    /**
     * 新建角色
     *
     * @param role 角色信息
     */
    @PostMapping("/save")
    public Result<LedgerRole> save(@Validated @RequestBody LedgerRole role) {
        roleService.save(role);
        return Result.succeed(role);
    }

    /**
     * 更新角色
     *
     * @param role 角色信息
     */
    @PostMapping("/update")
    public Result<LedgerRole> update(@Validated @RequestBody LedgerRole role) {
        roleService.updateById(role);
        // 更新缓存
        userService.clearUserAuthorityInfoByRoleId(role.getId());
        return Result.succeed(role);
    }

    /**
     * 批量删除角色信息
     *
     * @param ids 角色ID列表
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        // 删除中间表
        userRoleService.remove(new QueryWrapper<LedgerUserRole>().in("role_id", ids));
        roleMenuService.remove(new QueryWrapper<LedgerRoleMenu>().in("role_id", ids));

        // 同步删除缓存
        ids.forEach(userService::clearUserAuthorityInfoByRoleId);

        return Result.succeed();
    }


    /**
     * 为角色分配权限
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    @PostMapping("/perms/{roleId}")
    public Result<List<Integer>> perms(@PathVariable("roleId") Integer roleId, @RequestBody List<Integer> menuIds) {
        List<LedgerRoleMenu> roleMenuList = new ArrayList<>();
        menuIds.forEach(menuId -> {
            LedgerRoleMenu roleMenu = new LedgerRoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenuList.add(roleMenu);
        });

        // 先删除原来的记录，再保存新的
        roleMenuService.remove(new QueryWrapper<LedgerRoleMenu>().eq("role_id", roleId));
        roleMenuService.saveBatch(roleMenuList);

        // 删除缓存
        userService.clearUserAuthorityInfoByRoleId(roleId);
        return Result.succeed(menuIds);
    }

}
