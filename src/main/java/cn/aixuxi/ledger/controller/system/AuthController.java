package cn.aixuxi.ledger.controller.system;

import cn.aixuxi.ledger.utils.RedisUtil;
import com.google.code.kaptcha.Producer;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    private final RedisUtil redisUtil;
    private final Producer producer;


}
