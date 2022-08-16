package cn.aixuxi.ledger.entity.tissue;

import lombok.Data;

import java.util.List;

/**
 * 组织相关条件
 *
 * @author ruozhuliufeng
 */
@Data
public class LedgerTissueQuery {
    /**
     * 组织用户ID列表
     */
    private List<Long> tissueUserIds;
    /**
     * 组织名称
     */
    private String name;
    /**
     * 组织编码
     */
    private String code;
}
