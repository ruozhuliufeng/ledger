package cn.aixuxi.ledger.config;

import cn.aixuxi.ledger.properties.LedgerJwtProperties;
import cn.aixuxi.ledger.properties.LedgerSmmsProperties;
import cn.aixuxi.ledger.properties.LedgerWebDavProperties;
import cn.aixuxi.ledger.properties.LedgerWxProperties;
import com.fundebug.Fundebug;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({LedgerJwtProperties.class,
        LedgerWxProperties.class,
        LedgerSmmsProperties.class,
        LedgerWebDavProperties.class})
public class LedgerConfiguration {

    /**
     * 使用 fundebug 监控项目报警信息并提醒
     */
    @Bean
    public Fundebug getBean(){
        // 使用项目的apiKey
        Fundebug fundebug = new Fundebug("your fundebug api key");
        // 默认关闭
        fundebug.setSilent(true);
        return fundebug;
    }
}
