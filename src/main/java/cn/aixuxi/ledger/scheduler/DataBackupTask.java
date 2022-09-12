package cn.aixuxi.ledger.scheduler;

import cn.aixuxi.ledger.constant.LedgerConstant;
import cn.aixuxi.ledger.utils.WebDavFileUtil;
import cn.hutool.core.io.FileUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

/**
 * 数据库备份至webdav
 */
@Slf4j
@Configuration
@EnableScheduling
@AllArgsConstructor
public class DataBackupTask {

    /**
     * 数据库配置信息
     */
    private final DataSourceProperties properties;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 每天 0点备份数据至webdav
     */
    @SneakyThrows
//    @Scheduled(cron = "* * 0 * * ?")
//    @Scheduled(cron = "0/10 * * * * ? ")
    public void backupTask() {
        // 备份数据库，获取备份数据库文件地址
        String format = dateFormat.format(System.currentTimeMillis());
        String filePath = exportSql(format);
        if (ObjectUtils.isEmpty(filePath)){
            log.error("数据库文件生成失败！请核实");
            return;
        }
        // 文件上传至webdav
        String fileUploadPath = WebDavFileUtil.upload(filePath, Files.newInputStream(new File(filePath).toPath()));
        log.info("webDav上传文件地址：{}",fileUploadPath);
    }

    /**
     * 获取数据库名
     */
    public String getDataBaseName() {
        return properties.getUrl().substring(properties.getUrl().indexOf("3306"), properties.getUrl().indexOf("?")).replaceAll("/", "").replaceAll("3306", "");
    }

    /**
     * 获取主机地址
     */
    private String getHost() {
        return properties.getUrl().substring(properties.getUrl().indexOf("mysql"), properties.getUrl().indexOf("3306")).replace(":", "").replace("//", "").replace("mysql", "");
    }

    /**
     * 导出 sql 并返回相关信息
     */
    @SneakyThrows
    private String exportSql(String time) {
        String path = new File((ResourceUtils.getURL("classpath:").getPath())).getAbsolutePath();
        String filePath = path + File.separator + LedgerConstant.UPLOAD_PATH_PREFIX_STATIC + File.separator + LedgerConstant.UPLOAD_PATH_PREFIX_UPLOAD_FILE + File.separator;
        String host = getHost();
        String dataBaseName = getDataBaseName();
        String fileName = time + "-"+ dataBaseName + ".sql";
        // 指定导出的 sql 存放的文件夹
        File saveFile = new File(filePath );
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        // 拼接备份命令
        sb.append("mysqldump").append(" --opt")
                .append(" -h ").append(host).append(" --user=")
                .append(properties.getUsername())
                .append(" --password=")
                .append(properties.getPassword())
                .append(" --result-file=")
                .append(filePath + fileName)
                .append(" --default-character-set=utf8mb4 ")
                .append(dataBaseName);
        try {
            Process exec = Runtime.getRuntime().exec(sb.toString());
            if (exec.waitFor() == 0) {
                log.info("数据库备份成功，保存路径：" + filePath);
            } else {
                log.info("process.waitFor:{}",exec.waitFor());
                return null;
            }
        } catch (IOException e) {
            log.error("备份 数据库 出现 IO异常 ", e);
            return null;
        } catch (InterruptedException e) {
            log.error("备份 数据库 出现 线程中断异常 ", e);
            return null;
        } catch (Exception e) {
            log.error("备份 数据库 出现 其他异常 ", e);
            return null;
        }
        return filePath + fileName;
    }

}
