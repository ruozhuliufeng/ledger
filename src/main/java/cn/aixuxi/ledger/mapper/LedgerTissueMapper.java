package cn.aixuxi.ledger.mapper;

import cn.aixuxi.ledger.entity.tissue.LedgerTissue;
import cn.aixuxi.ledger.vo.LedgerTotalVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 组织表
 *
 * @author ruozhuliufeng
 */
public interface LedgerTissueMapper extends BaseMapper<LedgerTissue> {
    /**
     * 获取组织的累计金额
     *
     * @param tissueId 组织ID
     * @return 累计金额信息
     */
    LedgerTotalVO queryFamilyTotal(@Param("tissueId") Long tissueId);
}
