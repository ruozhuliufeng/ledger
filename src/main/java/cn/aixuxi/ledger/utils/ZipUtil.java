package cn.aixuxi.ledger.utils;

import cn.aixuxi.ledger.exception.LedgerException;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;

/**
 * Zip 解压工具类
 *
 * @author ruozhuliufeng
 */
@Slf4j
public class ZipUtil {

    /**
     * 解压压缩包
     *
     * @param file     压缩包文件
     * @param filePath 解压位置
     * @param password 解压密码
     * @return 解压后的数据
     */
    public static List<File> unzipFile(File file, String filePath, String password) {
        // 获取解压后的附件
        List<File> extractedFileList = new ArrayList<>();
        ZipFile zipFile = new ZipFile(file);
        // 判断是否需要解压密码
        try {
            if (zipFile.isEncrypted()){
                if (StrUtil.isBlank(password)){
                    throw new LedgerException("zip 解压密码为空");
                }else {
                    // 设置解压密码
                    zipFile.setPassword(password.toCharArray());
                }
                try{
                    zipFile.extractAll(filePath);
                }catch (ZipException e){
                    log.error("zip 解压失败！失败原因：{}",e.getMessage());
                    throw new LedgerException("解压密码错误");
                }
                List<FileHeader> headerList = zipFile.getFileHeaders();
                for (FileHeader fileHeader:headerList){
                    if (!fileHeader.isDirectory()){
                        extractedFileList.add(new File(filePath,fileHeader.getFileName()));
                    }
                }
                File[] extractedFiles = new File[extractedFileList.size()];
                extractedFileList.toArray(extractedFiles);
            }
        } catch (ZipException e) {
            log.error("ZIP解压有误！{}",e.getMessage());
            throw new LedgerException("解压失败");
        }
        return extractedFileList;
    }
}
