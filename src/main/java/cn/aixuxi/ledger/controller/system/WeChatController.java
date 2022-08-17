package cn.aixuxi.ledger.controller.system;

import cn.aixuxi.ledger.service.system.LedgerUserOauthService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.aixuxi.ledger.utils.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序接口
 *
 * @author ruozhuliufeng
 */
@RequestMapping("/wx")
@RestController
@AllArgsConstructor
public class WeChatController {
    private final LedgerUserOauthService userOauthService;
    private final LedgerUserService userService;
    private final RedisUtil redisUtil;
}
