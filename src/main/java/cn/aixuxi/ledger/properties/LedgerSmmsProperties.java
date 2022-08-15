package cn.aixuxi.ledger.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SM.MS 图床相关配置
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties("ledger.smms")
public class LedgerSmmsProperties {
    /**
     * SM.MS 用户名
     */
    private String username;
    /**
     * SM.MS 密码
     */
    private String password;
}
