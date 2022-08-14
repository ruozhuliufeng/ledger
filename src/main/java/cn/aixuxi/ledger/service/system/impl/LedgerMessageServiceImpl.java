package cn.aixuxi.ledger.service.system.impl;

import cn.aixuxi.ledger.entity.system.LedgerMessage;
import cn.aixuxi.ledger.mapper.LedgerMessageMapper;
import cn.aixuxi.ledger.service.system.LedgerMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ruozhuliufeng
 */
@Service
public class LedgerMessageServiceImpl extends ServiceImpl<LedgerMessageMapper, LedgerMessage>
        implements LedgerMessageService {

}
