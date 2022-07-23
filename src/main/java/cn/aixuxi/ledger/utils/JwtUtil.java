package cn.aixuxi.ledger.utils;

import cn.aixuxi.ledger.properties.LedgerJwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    private final LedgerJwtProperties jwtProperties;

    /**
     * 生成JWT
     *
     * @param username 用户名
     * @return JWT
     */
    public String generateToken(String username) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * jwtProperties.getExpire());
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 解析JWT
     *
     * @param token JWT
     * @return 解析结果
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * JWT是否过期
     *
     * @param claims 解析结果
     * @return 是否过期
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
