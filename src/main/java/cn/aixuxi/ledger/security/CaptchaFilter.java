package cn.aixuxi.ledger.security;

import cn.aixuxi.ledger.constant.LedgerConstant;
import cn.aixuxi.ledger.exception.CaptchaException;
import cn.aixuxi.ledger.utils.RedisUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter{
    private final RedisUtil redisUtil;
    private final LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if ("/api/login".equals(url) && request.getMethod().equals("POST")) {
            try {
                // 校验验证码
                validate(request);
            } catch (CaptchaException e) {
                // 交给认证失败处理器
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }
        filterChain.doFilter(request,response);
    }

    private void validate(HttpServletRequest httpServletRequest) {
        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("token");
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)){
            throw new CaptchaException("验证码不能为空");
        }
        if (!code.equals(redisUtil.hget(LedgerConstant.CAPTCHA_KEY, key))){
            throw new CaptchaException("验证码错误");
        }

        // 一次性使用
        redisUtil.hdel(LedgerConstant.CAPTCHA_KEY, key);
    }
}
