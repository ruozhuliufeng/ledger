package cn.aixuxi.ledger.controller;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.constant.LedgerConstant;
import cn.aixuxi.ledger.dto.LedgerReportDTO;
import cn.aixuxi.ledger.dto.PassDTO;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.service.LedgerRecordService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.aixuxi.ledger.utils.RedisUtil;
import cn.aixuxi.ledger.vo.LedgerQuery;
import cn.aixuxi.ledger.vo.LedgerReportVO;
import cn.aixuxi.ledger.vo.LedgerTotalVO;
import com.wf.captcha.SpecCaptcha;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

/**
 * 用户基础服务
 *
 * @author ruozhuliufeng
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/base")
public class BaseController {
    private final RedisUtil redisUtil;
    private final LedgerUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final LedgerRecordService recordService;

    @GetMapping("/captcha")
    public Result<Map<String, Object>> captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        SpecCaptcha specCaptcha = new SpecCaptcha(120, 40, 5);
        String code = specCaptcha.text().toLowerCase();
        redisUtil.hset(LedgerConstant.CAPTCHA_KEY, key, code, 180);
        Map<String, Object> result = new HashMap<>();
        result.put("token", key);
        result.put("captchaImg", specCaptcha.toBase64());
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

    /**
     * 报表信息查询
     *
     * @return 报表信息
     */
    @GetMapping("/query/report")
    public Result<LedgerReportVO> queryReport() {
        LedgerReportVO reportVO = recordService.queryReport();
        return Result.succeed(reportVO);
    }

    /**
     * 获取登录用户累计数据
     *
     * @return 累计数据
     */
    @GetMapping("/query/total")
    public Result<LedgerTotalVO> queryUserTotal() {
        LedgerTotalVO totalVO = recordService.queryUserTotal();
        return Result.succeed(totalVO);
    }

    /**
     * 获取年度报表
     *
     * @return 报表
     */
    @GetMapping("/query/trade/report")
    public Result<List<LedgerReportDTO>> queryTradeReport() {
        List<LedgerReportDTO> reportDTOList = recordService.queryTradeReport();
        return Result.succeed(reportDTOList);
    }
}
