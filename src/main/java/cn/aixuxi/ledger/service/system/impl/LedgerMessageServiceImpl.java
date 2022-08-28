package cn.aixuxi.ledger.service.system.impl;

import cn.aixuxi.ledger.entity.system.LedgerMessage;
import cn.aixuxi.ledger.enums.MessageStatusEnum;
import cn.aixuxi.ledger.mapper.LedgerMessageMapper;
import cn.aixuxi.ledger.service.system.LedgerMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author ruozhuliufeng
 */
@Service
public class LedgerMessageServiceImpl extends ServiceImpl<LedgerMessageMapper, LedgerMessage>
        implements LedgerMessageService {

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
        // TODO 根据不同的消息，处理不同的结果

        // 消息状态为已读
        message.setMessageStatus(MessageStatusEnum.HANDLE.getCode());
        this.updateById(message);
    }

}
