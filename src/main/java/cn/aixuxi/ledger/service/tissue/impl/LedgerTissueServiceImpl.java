package cn.aixuxi.ledger.service.tissue.impl;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.entity.system.LedgerMessage;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.entity.tissue.LedgerTissue;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueQuery;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueUser;
import cn.aixuxi.ledger.enums.MessageStatusEnum;
import cn.aixuxi.ledger.enums.MessageTemplateEnum;
import cn.aixuxi.ledger.enums.MessageTypeEnum;
import cn.aixuxi.ledger.mapper.LedgerTissueMapper;
import cn.aixuxi.ledger.service.LedgerRecordService;
import cn.aixuxi.ledger.service.system.LedgerMessageService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.aixuxi.ledger.service.tissue.LedgerTissueService;
import cn.aixuxi.ledger.service.tissue.LedgerTissueUserService;
import cn.aixuxi.ledger.utils.SecureUtil;
import cn.aixuxi.ledger.utils.UUIDUtil;
import cn.aixuxi.ledger.vo.LedgerTotalVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
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
    private final LedgerMessageService messageService;

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
            // 创建通知消息，并保存至数据库
            List<LedgerMessage> list = new ArrayList<>();
            tissueUserList.forEach(tissueUser -> {
                // 创建通知消息，并保存至数据库
                LedgerMessage message = new LedgerMessage();
                message.setReceiveUserId(tissueUser.getUserId());
                message.setSendUserId(user.getId());
                message.setSendTime(new Date());
                message.setMessageStatus(MessageStatusEnum.WAIT.getCode());
                message.setMessageType(MessageTypeEnum.NOTICE.getCode());
                message.setMessageTitle(MessageTemplateEnum.DISSOLVE_FAMILY.getTitle());
                message.setMessageContent(String.format(MessageTemplateEnum.DISSOLVE_FAMILY.getContent(), tissue.getTissueName(), user.getRealName(), user.getRealName(), user.getPhone()));
                message.setBusinessId(tissueUser.getTissueId());
                list.add(message);
            });
            messageService.saveBatch(list);
        }
        return Result.succeed();
    }

    @Override
    public void deleteUser(Long tissueUserId) {
        LedgerUser user = queryUser();
        // 获取待删除的组织人员
        LedgerTissueUser tissueUser = tissueUserService.getById(tissueUserId);
        // 创建通知消息，并保存至数据库
        LedgerMessage message = new LedgerMessage();
        message.setReceiveUserId(tissueUser.getUserId());
        message.setSendUserId(user.getId());
        message.setSendTime(new Date());
        message.setMessageStatus(MessageStatusEnum.WAIT.getCode());
        message.setMessageType(MessageTypeEnum.NOTICE.getCode());
        message.setMessageTitle(MessageTemplateEnum.REMOVE_FAMILY.getTitle());
        message.setMessageContent(String.format(MessageTemplateEnum.REMOVE_FAMILY.getContent(), user.getRealName(), user.getPhone()));
        message.setBusinessId(tissueUser.getTissueId());
        messageService.save(message);
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
        if (!CollectionUtils.isEmpty(tissueIdList)) {
            list = this.listByIds(tissueIdList);
            list = list.stream().filter(item -> item.getTissueType().equals(tissueType)).collect(Collectors.toList());
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
        IPage<LedgerRecord> page = new Page<>(query.getCurrent(), query.getSize());
        List<LedgerRecord> records = recordService.queryRecordListByTissue(page, query);
        page.setRecords(records);
        return Result.succeed(page);
    }

    @Override
    public Result applyJoinFamily(Long tissueId) {
        LedgerTissue tissue = this.getById(tissueId);
        LedgerUser user = queryUser();
        if (ObjectUtils.isEmpty(user.getRealName()) || ObjectUtils.isEmpty(user.getPhone())){
            return Result.failed("您的真实姓名或手机号码不能为空");
        }
        // 创建通知消息，并保存至数据库
        LedgerMessage message = new LedgerMessage();
        message.setReceiveUserId(tissue.getTissueLeader());
        message.setSendUserId(user.getId());
        message.setSendTime(new Date());
        message.setMessageStatus(MessageStatusEnum.WAIT.getCode());
        message.setMessageType(MessageTypeEnum.TISSUE.getCode());
        message.setMessageTitle(MessageTemplateEnum.APPLY_JOIN_FAMILY.getTitle());
        message.setMessageContent(String.format(MessageTemplateEnum.APPLY_JOIN_FAMILY.getContent(), user.getRealName(), user.getPhone()));
        message.setBusinessId(tissueId);
        messageService.save(message);
        return Result.succeed("您的申请已提交，请等待负责人审核处理");
    }

    /**
     * 邀请加入组织
     *
     * @param tissue 组织
     * @param userId 邀请用户ID
     */
    @Override
    public void inviteJoinFamily(LedgerTissue tissue, Long userId) {
        LedgerUser user = queryUser();
        // 创建通知消息，并保存至数据库
        LedgerMessage message = new LedgerMessage();
        message.setReceiveUserId(userId);
        message.setSendUserId(user.getId());
        message.setSendTime(new Date());
        message.setMessageStatus(MessageStatusEnum.WAIT.getCode());
        message.setMessageType(MessageTypeEnum.TISSUE.getCode());
        message.setMessageTitle(MessageTemplateEnum.INVITE_FAMILY.getTitle());
        message.setMessageContent(String.format(MessageTemplateEnum.INVITE_FAMILY.getContent(), user.getRealName(), user.getPhone(),tissue.getTissueName(),tissue.getTissueCode()));
        message.setBusinessId(tissue.getId());
        messageService.save(message);
    }

    /**
     * 获取组织的累计金额
     *
     * @param tissueId 组织ID
     * @return 累计金额信息
     */
    @Override
    public LedgerTotalVO queryFamilyTotal(Long tissueId) {
        return this.baseMapper.queryFamilyTotal(tissueId);
    }

    private LedgerUser queryUser() {
        Long userId = secureUtil.getUserId();
        return userService.getById(userId);
    }
}
