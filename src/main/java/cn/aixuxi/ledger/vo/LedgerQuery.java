package cn.aixuxi.ledger.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LedgerQuery {
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 角色ID列表
     */
    private List<Long> roleIds;
    /**
     * 菜单ID列表
     */
    private List<Long> menuIds;
}
