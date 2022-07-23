package cn.aixuxi.ledger.mapper;

import cn.aixuxi.ledger.entity.system.LedgerUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 用户信息表 Mapper 接口
 *
 * @author ruozhuliufeng
 */
public interface LedgerUserMapper extends BaseMapper<LedgerUser> {
    /**
     * 根据用户ID查询菜单ID列表
     *
     * @param userId 用户ID
     * @return 菜单ID列表
     */
    List<Integer> getNavMenuIds(Integer userId);

    /**
     * 根据菜单ID查询用户列表
     *
     * @param menuId 菜单ID
     * @return 用户列表
     */
    List<LedgerUser> listByMenuId(Integer menuId);
}
