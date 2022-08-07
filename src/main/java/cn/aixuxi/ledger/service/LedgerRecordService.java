package cn.aixuxi.ledger.service;

import cn.aixuxi.ledger.entity.LedgerRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

public interface LedgerRecordService extends IService<LedgerRecord> {
    void importRecordByThird(MultipartFile files, String password);
}
