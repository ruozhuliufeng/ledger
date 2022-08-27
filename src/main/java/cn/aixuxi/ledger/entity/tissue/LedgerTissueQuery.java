package cn.aixuxi.ledger.entity.tissue;

import cn.aixuxi.ledger.entity.BasePageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 组织相关条件
 *
 * @author ruozhuliufeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LedgerTissueQuery extends BasePageQuery {
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
    /**
     * 组织编码
     */
    private Long tissueId;
    /**
     * 交易类型 0：收入 1：支出 2：其他
     */
    private Integer transactionType;
    /**
     * 交易分类列表
     */
    private List<String> transactionCategoryList;
    /**
     * 交易开始时间
     */
    private Date transactionStartTime;
    /**
     * 交易结束时间
     */
    private Date transactionEndTime;
}
