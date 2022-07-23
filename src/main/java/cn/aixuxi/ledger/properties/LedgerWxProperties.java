package cn.aixuxi.ledger.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信相关配置
 */
@Data
@ConfigurationProperties(prefix = "ledger.wx")
public class LedgerWxProperties {
}
