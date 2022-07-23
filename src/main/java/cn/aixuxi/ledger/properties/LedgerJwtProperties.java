package cn.aixuxi.ledger.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt相关配置
 */
@Data
@ConfigurationProperties(prefix = "ledger.jwt")
public class LedgerJwtProperties {
    /**
     * Jwt签名密钥
     */
    private String secret;
    /**
     * Jwt过期时间
     */
    private Long expire;
    /**
     * Header
     */
    private String header;
}
