package cn.aixuxi.ledger.vo;

import cn.aixuxi.ledger.dto.LedgerReportDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 报表VO
 */
@Data
public class LedgerReportVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 收入占比
     */
    private List<LedgerReportDTO> incomeRatioList;
    /**
     * 支出占比
     */
    private List<LedgerReportDTO> expenseRatioList;
    /**
     * 其他占比
     */
    private List<LedgerReportDTO> otherRatioList;
    /**
     * 最近收入
     */
    private List<LedgerReportDTO> recentIncomeList;
    /**
     * 最近支出
     */
    private List<LedgerReportDTO> recentExpenseList;
    /**
     * 最近支出
     */
    private List<LedgerReportDTO> recentOtherList;
}
