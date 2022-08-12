package cn.aixuxi.ledger.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 报表DTO
 */
@Data
public class LedgerReportDTO {
    /**
     * 名称
     */
    private String name;
    /**
     * 金额
     */
    private BigDecimal value;
}
