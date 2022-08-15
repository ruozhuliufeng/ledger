package cn.aixuxi.ledger.service.tissue;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.tissue.LedgerTissue;
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
     * @param tissueUserIds 组织ID列表
     */
    void deleteUser(List<Long> tissueUserIds);

    /**
     * 查询当前登录用户的组织
     *
     * @param tissueType 组织类型
     * @return 组织列表
     */
    List<LedgerTissue> queryFamily(Integer tissueType);
}
