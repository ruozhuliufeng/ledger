package cn.aixuxi.ledger.service.system;

import cn.aixuxi.ledger.entity.system.LedgerRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色信息表 Service 接口
 *
 * @author ruozhuliufeng
 */
public interface LedgerRoleService extends IService<LedgerRole> {
    /**
     * 根据用户ID获取用户角色信息
     *
     * @param userId 用户ID
     * @return 用户角色信息
     */
    List<LedgerRole> listRolesByUserId(Integer userId);
}
