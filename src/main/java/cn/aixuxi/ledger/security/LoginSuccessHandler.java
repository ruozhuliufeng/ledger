package cn.aixuxi.ledger.security;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.properties.LedgerJwtProperties;
import cn.aixuxi.ledger.utils.JwtUtil;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final LedgerJwtProperties jwtProperties;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        // 生成jwt,并放置到请求头中
        String jwt = jwtUtil.generateToken(authentication.getName());
        response.setHeader(jwtProperties.getHeader(), jwt);
        Result result = Result.succeed();
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
