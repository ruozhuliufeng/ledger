package cn.aixuxi.ledger.vo;

import cn.aixuxi.ledger.entity.system.LedgerRole;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 页面展示用户数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LedgerUserVO extends LedgerUser {
    /**
     * 用户角色信息
     */
    private List<LedgerRole> roles;
}
