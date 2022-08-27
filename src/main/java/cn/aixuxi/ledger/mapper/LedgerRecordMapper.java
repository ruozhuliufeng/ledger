package cn.aixuxi.ledger.mapper;

import cn.aixuxi.ledger.dto.LedgerReportDTO;
import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueQuery;
import cn.aixuxi.ledger.vo.LedgerQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 记录操作接口
 *
 * @author ruozhuliufeng
 */
public interface LedgerRecordMapper extends BaseMapper<LedgerRecord> {
    /**
     * 批量新增或更新数据
     *
     * @param list 数据列表
     */
    int saveOrUpdateRecordList(@Param("list") List<LedgerRecord> list);

    /**
     * 查询收入信息
     *
     * @param userId 操作用户ID
     * @return 支出信息
     */
    List<LedgerReportDTO> queryIncomeRatioReport(@Param("userId") Long userId);

    /**
     * 查询支出信息
     *
     * @param userId 操作用户ID
     * @return 支出信息
     */
    List<LedgerReportDTO> queryExpenseRatioReport(@Param("userId") Long userId);

    /**
     * 查询其他信息
     *
     * @param userId 操作用户ID
     * @return 支出信息
     */
    List<LedgerReportDTO> queryOtherRatioReport(@Param("userId") Long userId);

    /**
     * 查询最近收入信息
     *
     * @param userId 操作用户ID
     * @return 支出信息
     */
    List<LedgerReportDTO> queryRecentIncomeReport(@Param("userId") Long userId);

    /**
     * 查询最近支出信息
     *
     * @param userId 操作用户ID
     * @return 支出信息
     */
    List<LedgerReportDTO> queryRecentExpenseReport(@Param("userId") Long userId);

    /**
     * 查询最近其他信息
     *
     * @param userId 操作用户ID
     * @return 支出信息
     */
    List<LedgerReportDTO> queryRecentOtherReport(@Param("userId") Long userId);

    /**
     * 查询当前组织内成员的收支记录
     * @param page 分页信息
     * @param query 查询参数
     * @return 收支记录
     */
    List<LedgerRecord> queryRecordListByTissue(IPage<LedgerRecord> page,@Param("query") LedgerTissueQuery query);
}
