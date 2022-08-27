package cn.aixuxi.ledger.service;

import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueQuery;
import cn.aixuxi.ledger.vo.LedgerQuery;
import cn.aixuxi.ledger.vo.LedgerReportVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LedgerRecordService extends IService<LedgerRecord> {
    void importRecordByThird(MultipartFile file, String password);

    /**
     * 新建交易记录
     * @param record 记录信息
     */
    boolean saveRecord(LedgerRecord record);

    /**
     * 查询报表信息
     * @return 报表信息
     */
    LedgerReportVO queryReport();

    /**
     * 查询组织内成员收支列表
     * @param query 查询条件
     * @return 收拾记录
     */
    List<LedgerRecord> queryRecordListByTissue(LedgerTissueQuery query);
}
