package cn.aixuxi.ledger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费记录
 *
 * @author ruozhuliufeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ledger_record")
public class LedgerRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 消费记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 交易记录用户
     */
    private Long userId;
    /**
     * 交易时间
     */
    private Date transactionTime;
    /**
     * 交易分类
     */
    private String transactionCategory;

    /**
     * 交易对方
     */
    private String counterpartyName;

    /**
     * 商品名称
     */
    private String productName;
    /**
     * 交易类型 0：收入 1：支出 2：其他
     */
    private Integer transactionType;
    /**
     * 支付方式
     */
    private String paymentMethod;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 当前状态
     */
    private String currentStatus;
    /**
     * 交易描述
     */
    private String remark;

    /**
     * 交易单号
     */
    private String transactionSn;

    /**
     * 商品单号
     */
    private String merchatOrderSn;

    /**
     * 交易对方账户
     */
    private String counterpartyAccount;

}
