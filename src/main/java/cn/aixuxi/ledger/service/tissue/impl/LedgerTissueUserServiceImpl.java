package cn.aixuxi.ledger.service.tissue.impl;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueUser;
import cn.aixuxi.ledger.mapper.LedgerTissueUserMapper;
import cn.aixuxi.ledger.service.tissue.LedgerTissueUserService;
import cn.aixuxi.ledger.vo.LedgerTissueUserVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Service
public class LedgerTissueUserServiceImpl extends ServiceImpl<LedgerTissueUserMapper, LedgerTissueUser>
        implements LedgerTissueUserService {

    /**
     * 查询组织人员列表
     *
     * @param tissueId 组织ID
     * @return 组织人员列表
     */
    @Override
    public Result<List<LedgerTissueUserVO>> queryUserList(Long tissueId) {
        List<LedgerTissueUserVO> list = this.baseMapper.queryUserList(tissueId);
        return Result.succeed(list);
    }
}
