package cn.aixuxi.ledger.mapper;

import cn.aixuxi.ledger.entity.system.LedgerMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息表
 *
 * @author ruozhuliufeng
 */
public interface LedgerMessageMapper extends BaseMapper<LedgerMessage> {
    /**
     * 获取消息列表
     * @param loginUserId 当前登录用户ID
     * @param code 消息状态
     * @return 消息列表
     */
    List<LedgerMessage> queryMessageList(@Param("loginUserId") Long loginUserId,@Param("code") Integer code);
}
