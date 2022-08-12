package cn.aixuxi.ledger.vo;

import lombok.Data;

import java.util.Date;

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
}
