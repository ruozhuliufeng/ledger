package cn.aixuxi.ledger.service.tissue.impl;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.entity.tissue.LedgerTissue;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueQuery;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueUser;
import cn.aixuxi.ledger.mapper.LedgerTissueMapper;
import cn.aixuxi.ledger.service.LedgerRecordService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.aixuxi.ledger.service.tissue.LedgerTissueService;
import cn.aixuxi.ledger.service.tissue.LedgerTissueUserService;
import cn.aixuxi.ledger.utils.SecureUtil;
import cn.aixuxi.ledger.utils.UUIDUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Service
@AllArgsConstructor
public class LedgerTissueServiceImpl extends ServiceImpl<LedgerTissueMapper, LedgerTissue>
        implements LedgerTissueService {
    private final SecureUtil secureUtil;
    private final LedgerTissueUserService tissueUserService;
    private final LedgerUserService userService;
    private final LedgerRecordService recordService;

    @Override
    public Result<LedgerTissue> saveTissue(LedgerTissue tissue) {
        LedgerUser user = queryUser();
        // 真实姓名及手机号不能为空
        if (ObjectUtils.isEmpty(user.getPhone()) || ObjectUtils.isEmpty(user.getRealName())) {
            return Result.failed("真实姓名及手机号不能为空！");
        }
        tissue.setTissueCode(UUIDUtil.generateShortUuid());
        tissue.setTissueLeader(user.getId());
        this.save(tissue);
        LedgerTissueUser tissueUser = new LedgerTissueUser();
        tissueUser.setTissueId(tissue.getId());
        tissueUser.setUserId(user.getId());
        tissueUser.setNickName(user.getNickName());
        tissueUserService.save(tissueUser);
        return Result.succeed(tissue);
    }

    /**
     * 解散组织
     *
     * @param id 组织ID
     */
    @Override
    public Result delete(Long id) {
        LedgerTissue tissue = this.getById(id);
        LedgerUser user = queryUser();
        if (!user.getId().equals(tissue.getTissueLeader())) {
            return Result.failed("只有负责人可以处理该操作！");
        }
        this.baseMapper.deleteById(id);
        // 根据组织ID，获取组织ID内所有人
        List<LedgerTissueUser> tissueUserList = tissueUserService.list(
                new QueryWrapper<LedgerTissueUser>().eq("tissue_id", id)
        );
        if (!CollectionUtils.isEmpty(tissueUserList)) {
            // TODO 创建通知消息，并保存至数据库
            // 家庭（xxx家庭）已被负责人解散，如有疑问请联系负责人（xxx,10086123456）
            StringBuilder content = new StringBuilder("家庭(");
            content.append(tissue.getTissueName())
                    .append(")已被负责人解散,如有疑问,请及时联系负责人(")
                    .append(user.getRealName())
                    .append(",")
                    .append(user.getPhone())
                    .append(",)");
        }
        return Result.succeed();
    }

    @Override
    public void deleteUser(Long tissueUserId) {
        LedgerUser user = queryUser();
        // 获取待删除的组织人员
        LedgerTissueUser tissueUser = tissueUserService.getById(tissueUserId);
        // TODO 创建通知消息，并保存至数据库
        // 您已被负责人从家庭中移出，如有疑问，请及时联系负责人
        String content = "您已被负责人从家庭中移出，如有疑问，请及时联系负责人(" + user.getRealName() + "," + user.getPhone() + ")";
        // 删除组织人员
        tissueUserService.removeById(tissueUserId);
    }

    @Override
    public List<LedgerTissue> queryFamily(Integer tissueType) {
        LedgerUser user = queryUser();
        List<LedgerTissueUser> tissueUserList = tissueUserService.list(
                new QueryWrapper<LedgerTissueUser>().eq("user_id", user.getId())
        );
        List<Long> tissueIdList = tissueUserList.stream().map(LedgerTissueUser::getTissueId).distinct().collect(Collectors.toList());
        List<LedgerTissue> list = new ArrayList<>();
        // 存在组织
        if (!CollectionUtils.isEmpty(tissueIdList)){
            list = this.listByIds(tissueIdList);
            list = list.stream().filter(item->item.getTissueType().equals(tissueType)).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 查询组织内成员的收支记录
     *
     * @param query 查询条件
     * @return 收支记录
     */
    @Override
    public Result<IPage<LedgerRecord>> queryRecordList(LedgerTissueQuery query) {
        IPage<LedgerRecord> page = new Page<>(query.getCurrent(),query.getSize());
        List<LedgerRecord> records = recordService.queryRecordListByTissue(page,query);
        page.setRecords(records);
        return Result.succeed(page);
    }

    private LedgerUser queryUser() {
        Long userId = secureUtil.getUserId();
        return userService.getById(userId);
    }
}
