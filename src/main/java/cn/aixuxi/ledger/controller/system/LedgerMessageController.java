package cn.aixuxi.ledger.controller.system;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.system.LedgerMessage;
import cn.aixuxi.ledger.enums.MessageStatusEnum;
import cn.aixuxi.ledger.service.system.LedgerMessageService;
import cn.aixuxi.ledger.utils.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消息控制器
 *
 * @author ruozhuliufeng
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/message")
public class LedgerMessageController {
    private final LedgerMessageService messageService;
    private final SecureUtil secureUtil;

    /**
     * 获取当前用户的已读消息列表
     * @return 消息列表
     */
    @GetMapping("/query/read/list")
    public Result<List<LedgerMessage>> queryReadMessage() {
        Long loginUserId = secureUtil.getUserId();
        List<LedgerMessage> list = messageService.queryReadMessage(loginUserId);
        return Result.succeed(list);
    }
    /**
     * 获取当前用户的未读消息列表
     * @return 消息列表
     */
    @GetMapping("/query/no/read/list")
    public Result<List<LedgerMessage>> queryNoReadMessage() {
        Long loginUserId = secureUtil.getUserId();
        List<LedgerMessage> list = messageService.queryNoReadMessage(loginUserId);
        return Result.succeed(list);
    }

    /**
     * 获取未处理消息数量
     * @return 消息数量
     */
    @GetMapping("/query/wait/message/num")
    public Result<Integer> queryWaitMessageNum(){
        Long loginUserId = secureUtil.getUserId();
        List<LedgerMessage> list = messageService.list(
                new QueryWrapper<LedgerMessage>().eq("receive_user_id",loginUserId)
                        .eq("message_status", MessageStatusEnum.WAIT.getCode())
        );
        int waitMessageNum;
        if (CollectionUtils.isEmpty(list)){
            waitMessageNum = 0;
        }else {
            waitMessageNum = list.size();
        }
        return Result.succeed(waitMessageNum);
    }

    /**
     * 确认消息
     * @param id 消息ID
     * @return Result
     */
    @GetMapping("/confirm/{id}")
    public Result confirmMessage(@PathVariable("id") Long id){
        messageService.confirmMessage(id);
        return Result.succeed();
    }
    /**
     * 已读消息
     * @param id 消息ID
     * @return Result
     */
    @GetMapping("/read/{id}")
    public Result readMessage(@PathVariable("id") Long id){
        LedgerMessage message = new LedgerMessage();
        message.setId(id);
        message.setMessageStatus(MessageStatusEnum.HANDLE.getCode());
        messageService.updateById(message);
        return Result.succeed();
    }
}
