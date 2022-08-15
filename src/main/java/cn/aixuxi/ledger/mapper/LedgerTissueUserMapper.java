package cn.aixuxi.ledger.mapper;

import cn.aixuxi.ledger.entity.tissue.LedgerTissueUser;
import cn.aixuxi.ledger.vo.LedgerTissueUserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织表
 *
 * @author ruozhuliufeng
 */
public interface LedgerTissueUserMapper extends BaseMapper<LedgerTissueUser> {
    // 查询组织人员列表
    List<LedgerTissueUserVO> queryUserList(@Param("tissueId") Long tissueId);
}
