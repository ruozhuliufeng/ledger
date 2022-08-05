package cn.aixuxi.ledger.service.impl;

import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.mapper.LedgerRecordMapper;
import cn.aixuxi.ledger.service.LedgerRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LedgerRecordServiceImpl extends ServiceImpl<LedgerRecordMapper, LedgerRecord>
        implements LedgerRecordService {
    @Override
    public void importRecordByThird(MultipartFile[] files, String password) {

    }


    private LedgerRecord transformByWeChat(){
        LedgerRecord record = new LedgerRecord();
        return record;
    }
}
