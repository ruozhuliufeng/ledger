package cn.aixuxi.ledger.controller.system;

import cn.aixuxi.ledger.service.system.LedgerMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
