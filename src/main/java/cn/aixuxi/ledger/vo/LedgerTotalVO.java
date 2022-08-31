package cn.aixuxi.ledger.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 汇总信息VO
 */
@Data
public class LedgerTotalVO implements Serializable{
    /**
     * 初始金额
     */
    private BigDecimal initialAmount;
    /**
     * 累计收入金额
     */
    private BigDecimal totalIncomePrice;

    /**
     * 累计支出金额
     */
    private BigDecimal totalExpendPrice;

    /**
     * 累计总金额
     */
    private BigDecimal totalPrice;
}
