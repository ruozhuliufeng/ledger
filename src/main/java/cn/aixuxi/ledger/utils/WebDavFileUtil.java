package cn.aixuxi.ledger.utils;

import cn.aixuxi.ledger.properties.LedgerWebDavProperties;
import cn.hutool.extra.spring.SpringUtil;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * WebDav 文件处理
 *
 * @author ruozhuliufeng
 */
@Slf4j
public class WebDavFileUtil {
    private static LedgerWebDavProperties properties;

    private static LedgerWebDavProperties getProperties() {
        if (properties == null) {
            properties = SpringUtil.getBean(LedgerWebDavProperties.class);
        }
        return properties;
    }

    /**
     * 上传文件 <br/>
     * 当对应路径对应文件名已经存在，再次上传会覆盖掉原来的文件
     *
     * @param filePath    文件的相对路径或者绝对路径均可
     * @param inputStream 输入流
     * @return 保存后的绝对路径
     * @throws IOException io异常
     */
    public static String upload(String filePath, InputStream inputStream) throws IOException {
        // 如果路径中没有加上webDav的url，则添加上去，返回绝对路径
        filePath = getAbsolutePath(filePath);

        // 根据用户名、密码初始化Sardine对象
        Sardine sardine = SardineFactory.begin(getProperties().getClient(), getProperties().getSecret());
        // 判断目录是否存在
        String dirPath = filePath.substring(0, filePath.lastIndexOf("/")+1);
        if (!exist(dirPath, sardine)) {
            mkdir(dirPath, sardine);
        }
        // 执行文件上传操作
        sardine.put(filePath, inputStream);

        return filePath;
    }

    /**
     * 如果路径中没有加上webDav的url，则添加上去，返回绝对路径
     *
     * @param filePath 文件路径
     * @return 路径
     */
    private static String getAbsolutePath(String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
        return getProperties().getUrl() + "/" + getProperties().getRootPath() + "/" + fileName;
    }


//    /**
//     * 下载文件 <br/>
//     * 输出流输出
//     * @param filePath 文件路径
//     * @param outputStream 输入流
//     * @throws IOException io 异常
//     */
//    public static void download2Stream(String filePath, OutputStream outputStream) throws IOException {
//        // 获取绝对路径
//        filePath = CommonUtil.getAbsolutePath(config.getBaseUrl(), filePath);
//        // 初始化对象
//        Sardine sardine = SardineFactory.begin(getProperties().getClient(), getProperties().getSecret());
//        if (exist(filePath, sardine)) {
//            // get操作，读取文件
//            InputStream inputStream = sardine.get(filePath);
//            StreamUtil.transStream(inputStream, outputStream);
//            sardine.shutdown();
//            return;
//        }
//        log.info("地址：{}，文件不存在!", filePath);
//    }
//
    /**
     * 删除文件或者目录 <br/>
     * 删除目录必须以 / 结尾，会将指定目录的子目录以及文件全部删除
     * @param filePath 文件路径
     * @throws IOException 文件不存在报异常，404
     */
    public static void delete(String filePath) throws IOException {
        filePath = getAbsolutePath(filePath);
        // 初始化对象
        Sardine sardine = SardineFactory.begin(getProperties().getClient(), getProperties().getSecret());
        // 根据指定路径（url）执行删除操作
        sardine.delete(filePath);
        sardine.shutdown();
    }

    /**
     * 更改文件/目录名称 <br/>
     * 移动文件/目录
     * e.g.:
     * 1. 修改目录名称（20200619 改成 20200621）
     * http://ip:port/webdav/sardline/20200619/ > http://ip:port/webdav/sardline/20200621/
     * 2. 修改文件名称（9586.jpg 改成 3562.jpg）
     * http://ip:port/webdav/sardline/20200619/9586.jpg > http://ip:port/webdav/sardline/20200619/3562.jpg
     * 3. 移动文件夹 （将20200619目录移动到 date 下。注意必须以原来目录结尾，不然会将目录下的文件直接移动过去，20200619被删除）
     * http://ip:port/webdav/sardline/20200619/ > http://ip:port/webdav/sardline/date/20200619/
     * 4. 移动文件 （最后文件名如果指定不一样，会在移动后直接重命名）
     * http://ip:port/webdav/sardline/20200619/9980.jpg > http://ip:port/webdav/sardline/20200621/9980.jpg
     *
     * @param source 源目录/文件名
     * @param dest 目标目录、文件
     * @throws IOException io异常
     */
    public static void move(String source, String dest) throws IOException {

        Sardine sardine = SardineFactory.begin(getProperties().getClient(), getProperties().getSecret());

        if (exist(source, sardine)) {
            // 源文件存在
            String destDir = dest.substring(0, dest.lastIndexOf("/"));
            if (!exist(destDir, sardine)) {
                // 目标不存在，则创建
                mkdir(destDir, sardine);
            }
            // 执行重命名/移动操作
            sardine.move(source, dest);
            sardine.shutdown();
        } else {
            log.info("源文件：{} 不存在", source);
        }
    }


    /**
     * 创建目录
     *
     * @param dirPath 目录文件
     * @param sardine sardine
     */
    private static void mkdir(String dirPath, Sardine sardine) throws IOException {
        sardine.createDirectory(dirPath);
    }

    /**
     * 判断绝对路径（目录或者文件均可）是否已经存在
     *
     * @param filePath 绝对路径
     * @return true: 已存在，false:不存在
     * @throws IOException io异常
     */
    public static Boolean exist(String filePath, Sardine sardine) throws IOException {
        return sardine.exists(filePath);
    }

}
