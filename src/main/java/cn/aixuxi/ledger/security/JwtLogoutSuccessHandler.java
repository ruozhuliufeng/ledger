package cn.aixuxi.ledger.security;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.properties.LedgerJwtProperties;
import cn.aixuxi.ledger.utils.JwtUtil;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    private final JwtUtil jwtUtil;
    private final LedgerJwtProperties jwtProperties;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        // 清空请求头中中的token
        response.setHeader(jwtProperties.getHeader(), "");
        Result result = Result.succeed();
        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
