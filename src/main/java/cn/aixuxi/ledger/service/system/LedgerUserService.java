package cn.aixuxi.ledger.service.system;

import cn.aixuxi.ledger.entity.system.LedgerUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户信息表 Service 接口
 *
 * @author ruozhuliufeng
 */
public interface LedgerUserService extends IService<LedgerUser> {
    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    LedgerUser getByUsername(String username);

    /**
     * 获取用户权限信息
     *
     * @param userId 用户id
     * @return 用户权限信息
     */
    String getUserAuthorityInfo(Integer userId);

    /**
     * 清除用户权限信息
     *
     * @param username 用户名
     */
    void clearUserAuthorityInfo(String username);

    /**
     * 根据角色id清除用户权限信息
     *
     * @param roleId 角色id
     */
    void clearUserAuthorityInfoByRoleId(Integer roleId);

    /**
     * 根据菜单id清除用户权限信息
     *
     * @param menuId 菜单id
     */
    void clearUserAuthorityInfoByMenuId(Integer menuId);
}
