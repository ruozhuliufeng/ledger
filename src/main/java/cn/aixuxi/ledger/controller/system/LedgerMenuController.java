package cn.aixuxi.ledger.controller.system;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.dto.LedgerMenuDTO;
import cn.aixuxi.ledger.entity.system.LedgerMenu;
import cn.aixuxi.ledger.entity.system.LedgerRoleMenu;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.service.system.LedgerMenuService;
import cn.aixuxi.ledger.service.system.LedgerRoleMenuService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/menu")
@AllArgsConstructor
public class LedgerMenuController {
    private final LedgerUserService userService;
    private final LedgerMenuService menuService;
    private final LedgerRoleMenuService roleMenuService;

    /**
     * 当前用户的菜单及权限信息
     *
     * @param principal 登录信息
     * @return 菜单及权限信息
     */
    @GetMapping("/nav")
    public Result<Map<String, Object>> nav(Principal principal) {
        LedgerUser user = userService.getByUsername(principal.getName());

        // 获取权限信息
        String authorityInfo = userService.getUserAuthorityInfo(user.getId());
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");

        // 获取导航栏信息
        List<LedgerMenuDTO> navList = menuService.getCurrentUserNav();
        Map<String, Object> result = new HashMap<>(4);
        result.put("authoritys", authorityInfoArray);
        result.put("nav", navList);
        return Result.succeed(result);
    }

    /**
     * 获取菜单信息
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    @GetMapping("/info/{id}")
    public Result<LedgerMenu> info(@PathVariable("id") Integer id) {
        return Result.succeed(menuService.getById(id));
    }

    /**
     * 菜单列表查询
     *
     * @return 菜单列表
     */
    @GetMapping("/list")
    public Result<List<LedgerMenu>> list() {
        List<LedgerMenu> menus = menuService.tree();
        return Result.succeed(menus);
    }


    /**
     * 新增菜单
     *
     * @param menu 菜单
     */
    @PostMapping("/save")
    public Result<LedgerMenu> save(@Validated @RequestBody LedgerMenu menu) {
        menuService.save(menu);
        return Result.succeed(menu);
    }


    /**
     * 编辑菜单
     *
     * @param menu 菜单
     */
    @PostMapping("/update")
    public Result<LedgerMenu> update(@Validated @RequestBody LedgerMenu menu) {
        menuService.updateById(menu);
        // 清除权限
        userService.clearUserAuthorityInfoByMenuId(menu.getId());
        return Result.succeed(menu);
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     */
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        int count = menuService.count(new QueryWrapper<LedgerMenu>().eq("parent_id", id));
        if (count > 0) {
            return Result.failed("请先删除子菜单");
        }
        // 清除该菜单相关的权限缓存
        userService.clearUserAuthorityInfoByMenuId(id);

        menuService.removeById(id);

        // 同步删除中间关联表
        roleMenuService.remove(new QueryWrapper<LedgerRoleMenu>().eq("menu_id", id));
        return Result.succeed();
    }


}
