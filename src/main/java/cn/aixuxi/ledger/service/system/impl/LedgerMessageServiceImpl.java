package cn.aixuxi.ledger.service.system.impl;

import cn.aixuxi.ledger.entity.system.LedgerMessage;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueUser;
import cn.aixuxi.ledger.enums.MessageStatusEnum;
import cn.aixuxi.ledger.enums.MessageTypeEnum;
import cn.aixuxi.ledger.mapper.LedgerMessageMapper;
import cn.aixuxi.ledger.service.system.LedgerMessageService;
import cn.aixuxi.ledger.service.tissue.LedgerTissueUserService;
import cn.aixuxi.ledger.utils.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 服务实现类
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
@Service
public class LedgerMessageServiceImpl extends ServiceImpl<LedgerMessageMapper, LedgerMessage>
        implements LedgerMessageService {
    private final LedgerTissueUserService tissueUserService;
    private final SecureUtil secureUtil;
    /**
     * 获取已读消息列表
     *
     * @param loginUserId 当前登录用户ID
     * @return 消息列表
     */
    @Override
    public List<LedgerMessage> queryReadMessage(Long loginUserId) {
        return this.baseMapper.queryMessageList(loginUserId, MessageStatusEnum.HANDLE.getCode());
    }

    /**
     * 获取未读消息列表
     *
     * @param loginUserId 当前登录用户ID
     * @return 消息列表
     */
    @Override
    public List<LedgerMessage> queryNoReadMessage(Long loginUserId) {
        return this.baseMapper.queryMessageList(loginUserId, MessageStatusEnum.WAIT.getCode());
    }

    /**
     * 确认消息
     *
     * @param messageId 消息ID
     */
    @Override
    public void confirmMessage(Long messageId) {
        // 获取消息
        LedgerMessage message = this.getById(messageId);
        if (MessageTypeEnum.TISSUE.getCode().equals(message.getMessageType())){
            // 组织确认，将用户保存至组织内
            LedgerTissueUser tissueUser = new LedgerTissueUser();
            // 业务ID即为组织ID
            tissueUser.setTissueId(message.getBusinessId());
            // 加入时间
            tissueUser.setJoinTime(new Date());
            // 当前处理用户
            LedgerUser user = secureUtil.getUser();
            tissueUser.setUserId(user.getId());
            tissueUser.setNickName(user.getNickName());
            tissueUserService.save(tissueUser);
        }
        // 消息状态为已读
        message.setMessageStatus(MessageStatusEnum.HANDLE.getCode());
        this.updateById(message);
    }

}
