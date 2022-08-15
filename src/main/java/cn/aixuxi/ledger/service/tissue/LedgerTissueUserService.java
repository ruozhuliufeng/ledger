package cn.aixuxi.ledger.service.tissue;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueUser;
import cn.aixuxi.ledger.vo.LedgerTissueUserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 组织表 Service 接口
 *
 * @author ruozhuliufeng
 */
public interface LedgerTissueUserService extends IService<LedgerTissueUser> {
    /**
     * 查询组织人员列表
     * @param tissueId 组织ID
     * @return 组织人员列表
     */
    Result<List<LedgerTissueUserVO>> queryUserList(Long tissueId);
}
