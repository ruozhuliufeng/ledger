package cn.aixuxi.ledger.service.system.impl;

import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.mapper.LedgerUserMapper;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ruozhuliufeng
 */
@Service
public class LedgerUserServiceImpl extends ServiceImpl<LedgerUserMapper, LedgerUser>
        implements LedgerUserService {

}
