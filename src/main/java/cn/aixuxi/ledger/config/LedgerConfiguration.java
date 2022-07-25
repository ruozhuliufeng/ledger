package cn.aixuxi.ledger.config;

import cn.aixuxi.ledger.properties.LedgerJwtProperties;
import cn.aixuxi.ledger.properties.LedgerWxProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({LedgerJwtProperties.class, LedgerWxProperties.class })
public class LedgerConfiguration {

}
