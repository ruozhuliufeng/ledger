package cn.aixuxi.ledger.service.system.impl;

import cn.aixuxi.ledger.dto.LedgerMenuDTO;
import cn.aixuxi.ledger.entity.system.LedgerMenu;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.mapper.LedgerMenuMapper;
import cn.aixuxi.ledger.mapper.LedgerUserMapper;
import cn.aixuxi.ledger.service.system.LedgerMenuService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现类
 *
 * @author ruozhuliufeng
 */
@Service
@AllArgsConstructor
public class LedgerMenuServiceImpl extends ServiceImpl<LedgerMenuMapper, LedgerMenu>
        implements LedgerMenuService {

    private final LedgerUserMapper userMapper;
    private final LedgerUserService userService;

    /**
     * 获取当前用户导航菜单
     *
     * @return 当前用户导航菜单
     */
    @Override
    public List<LedgerMenuDTO> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LedgerUser user = userService.getByUsername(username);
        List<Integer> menuIds = userMapper.getNavMenuIds(user.getId());
        List<LedgerMenu> menuList = this.listByIds(menuIds);
        // 转树形结构
        List<LedgerMenu> menuTree = buildTreeMenu(menuList);
        // 转DTO
        return convert(menuTree);
    }

    /**
     * 获取所有菜单树
     *
     * @return 所有菜单树
     */
    @Override
    public List<LedgerMenu> tree() {
        // 获取所有菜单
        List<LedgerMenu> menuList = this.list(new QueryWrapper<LedgerMenu>().orderByAsc("sort"));
        // 转为树形结构
        return buildTreeMenu(menuList);
    }

    private List<LedgerMenuDTO> convert(List<LedgerMenu> menuTree) {
        List<LedgerMenuDTO> menuDTOList = new ArrayList<>();
        menuTree.forEach(menu->{
            LedgerMenuDTO menuDTO = new LedgerMenuDTO();
            menuDTO.setId(menu.getId());
            menuDTO.setTitle(menu.getName());
            menuDTO.setName(menu.getName());
            menuDTO.setPath(menu.getPath());
            menuDTO.setComponent(menu.getComponent());
            if (menu.getChilden().size() > 0){
                // 子节点调用当前方法再次进行转换
                menuDTO.setChildren(convert(menu.getChilden()));
            }
            menuDTOList.add(menuDTO);
        });
        return menuDTOList;
    }

    private List<LedgerMenu> buildTreeMenu(List<LedgerMenu> menuList) {
        List<LedgerMenu> finalMenus = new ArrayList<>();
        // 先各自寻找到各自的孩子
        for (LedgerMenu menu : menuList) {
            for (LedgerMenu child : menuList) {
                if (child.getParentId().equals(menu.getId())) {
                    menu.getChilden().add(child);
                }
            }
            // 提取父节点
            if (menu.getParentId() == 0) {
                finalMenus.add(menu);
            }
        }
        return finalMenus;
    }
}
