package cn.aixuxi.ledger.service.tissue;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.entity.tissue.LedgerTissue;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueQuery;
import cn.aixuxi.ledger.vo.LedgerTotalVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 组织表 Service 接口
 *
 * @author ruozhuliufeng
 */
public interface LedgerTissueService extends IService<LedgerTissue> {
    Result<LedgerTissue> saveTissue(LedgerTissue tissue);

    /**
     * 解散组织
     *
     * @param id 组织ID
     */
    Result delete(Long id);

    /**
     * 批量删除组织人员
     *
     * @param tissueUserId 组织ID列表
     */
    void deleteUser(Long tissueUserId);

    /**
     * 查询当前登录用户的组织
     *
     * @param tissueType 组织类型
     * @return 组织列表
     */
    List<LedgerTissue> queryFamily(Integer tissueType);

    /**
     * 查询组织内成员的收支记录
     *
     * @param query 查询条件
     * @return 收支记录
     */
    Result<IPage<LedgerRecord>> queryRecordList(LedgerTissueQuery query);

    /**
     * 申请加入组织
     *
     * @param tissueId 组织ID
     */
    Result applyJoinFamily(Long tissueId);

    /**
     * 邀请加入组织
     *
     * @param tissue 组织
     * @param userId 邀请用户ID
     */
    void inviteJoinFamily(LedgerTissue tissue, Long userId);

    /**
     * 获取组织的累计金额
     *
     * @param tissueId 组织ID
     * @return 累计金额信息
     */
    LedgerTotalVO queryFamilyTotal(Long tissueId);
}
