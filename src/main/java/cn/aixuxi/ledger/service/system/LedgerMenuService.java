package cn.aixuxi.ledger.service.system;

import cn.aixuxi.ledger.dto.LedgerMenuDTO;
import cn.aixuxi.ledger.entity.system.LedgerMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜单信息表 Service 接口
 *
 * @author ruozhuliufeng
 */
public interface LedgerMenuService extends IService<LedgerMenu> {
    /**
     * 获取当前用户导航菜单
     *
     * @return 当前用户导航菜单
     */
    List<LedgerMenuDTO> getCurrentUserNav();

    /**
     * 获取所有菜单树
     *
     * @return 所有菜单树
     */
    List<LedgerMenu> tree();
}
