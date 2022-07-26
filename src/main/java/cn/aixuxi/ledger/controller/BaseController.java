package cn.aixuxi.ledger.controller;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.constant.LedgerConstant;
import cn.aixuxi.ledger.dto.PassDTO;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.aixuxi.ledger.utils.RedisUtil;
import com.google.code.kaptcha.Producer;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户基础服务
 */
@RestController
@AllArgsConstructor
@RequestMapping("/base")
public class BaseController {
    private final RedisUtil redisUtil;
    private final Producer producer;
    private final LedgerUserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/captcha")
    public Result<Map<String,Object>> captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        redisUtil.hset(LedgerConstant.CAPTCHA_KEY,key,code,180);
        Map<String,Object> result = new HashMap<>();
        result.put("token",key);
        result.put("captchaImg",base64Img);
        return Result.succeed(result);
    }

    /**
     * 获取登录用户信息
     *
     * @param principal 用户凭证
     * @return 用户信息
     */
    @GetMapping("/user/info")
    public Result<LedgerUser> userInfo(Principal principal) {
        LedgerUser user = userService.getByUsername(principal.getName());
        return Result.succeed(user);
    }

    /**
     * 用户更新密码
     *
     * @param passDTO   面膜信息
     * @param principal 用户信息
     */
    @PostMapping("/update/password")
    public Result updatePass(@Validated @RequestBody PassDTO passDTO, Principal principal) {
        LedgerUser user = userService.getByUsername(principal.getName());
        boolean matches = passwordEncoder.matches(passDTO.getCurrentPass(), user.getPassword());
        if (!matches) {
            return Result.failed("旧密码不正确");
        }
        user.setPassword(passwordEncoder.encode(passDTO.getPassword()));
        user.setUpdateTime(new Date());
        userService.updateById(user);
        return Result.succeed();
    }
}
