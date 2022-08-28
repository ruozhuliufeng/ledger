package cn.aixuxi.ledger.service.system;

import cn.aixuxi.ledger.entity.system.LedgerMessage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 消息表 Service 接口
 *
 * @author ruozhuliufeng
 */
public interface LedgerMessageService extends IService<LedgerMessage> {
    /**
     * 获取已读消息列表
     *
     * @param loginUserId 当前登录用户ID
     * @return 消息列表
     */
    List<LedgerMessage> queryReadMessage(Long loginUserId);

    /**
     * 获取未读消息列表
     *
     * @param loginUserId 当前登录用户ID
     * @return 消息列表
     */
    List<LedgerMessage> queryNoReadMessage(Long loginUserId);

    /**
     * 确认消息
     *
     * @param messageId 消息ID
     */
    void confirmMessage(Long messageId);
}
