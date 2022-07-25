package cn.aixuxi.ledger.controller.system;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.constant.LedgerConstant;
import cn.aixuxi.ledger.utils.RedisUtil;
import com.google.code.kaptcha.Producer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class AuthController {
    private final RedisUtil redisUtil;
    private final Producer producer;

    @GetMapping("/captcha")
    public Result<Map<String,Object>> captcha() throws IOException{
        String key = UUID.randomUUID().toString();
        String code = producer.createText();
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        redisUtil.hset(LedgerConstant.CAPTCHA_KEY,key,code,120);
        Map<String,Object> result = new HashMap<>();
        result.put("token",key);
        result.put("captchaImg",base64Img);
        return Result.succeed(result);
    }

}
