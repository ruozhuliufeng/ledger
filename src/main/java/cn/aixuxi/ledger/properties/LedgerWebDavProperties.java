package cn.aixuxi.ledger.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebDav 服务器配置
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties(prefix = "ledger.webdav")
public class LedgerWebDavProperties {
    /**
     * webdav url
     */
    private String url;
    /**
     * 登录用户名
     */
    private String client;
    /**
     * 应用密码
     */
    private String secret;
    /**
     * 上传文件根目录
     */
    private String rootPath;
}
