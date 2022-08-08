package cn.aixuxi.ledger.mapper;

import cn.aixuxi.ledger.entity.LedgerRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 记录操作接口
 * @author ruozhuliufeng
 */
public interface LedgerRecordMapper extends BaseMapper<LedgerRecord> {
    /**
     * 批量新增或更新数据
     * @param list 数据列表
     */
    int saveOrUpdateRecordList(@Param("list") List<LedgerRecord> list);
}
