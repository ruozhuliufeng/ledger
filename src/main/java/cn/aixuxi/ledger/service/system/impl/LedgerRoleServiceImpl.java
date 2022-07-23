package cn.aixuxi.ledger.service.system.impl;

import cn.aixuxi.ledger.entity.system.LedgerRole;
import cn.aixuxi.ledger.mapper.LedgerRoleMapper;
import cn.aixuxi.ledger.service.system.LedgerRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author ruozhuliufeng
 */
@Service
public class LedgerRoleServiceImpl extends ServiceImpl<LedgerRoleMapper, LedgerRole>
        implements LedgerRoleService {

    /**
     * 根据用户ID获取用户角色信息
     *
     * @param userId 用户ID
     * @return 用户角色信息
     */
    @Override
    public List<LedgerRole> listRolesByUserId(Integer userId) {
        List<LedgerRole> roleList = this.list(
                new QueryWrapper<LedgerRole>()
                        .inSql("id","select role_id from ledger_user_role where user_id = " + userId)
        );
        return roleList;
     }
}
