package cn.aixuxi.ledger.constant;

/**
 * 项目常量
 */
public interface LedgerConstant {
    /**
     * redis 验证码 key
     */
    String CAPTCHA_KEY = "captcha";
    /**
     * 状态 - 启用
     */
    Integer STATUS_ON = 0;
    /**
     * 状态 - 禁用
     */
    Integer STATUS_OFF = 1;
    /**
     * 默认密码
     */
    String DEFULT_PASSWORD = "123654";
    /**
     * 默认头像地址
     */
    String DEFULT_AVATAR = "https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg";

    String UPLOAD_PATH_PREFIX_STATIC = "static";

    String UPLOAD_PATH_PREFIX_UPLOAD_FILE = "uploadFile";
    /**
     * 压缩文件类型
     */
    String FILE_TYPE_ZIP = "ZIP";
    /**
     * 微信账单
     */
    String LEDGER_WECHAT = "微信";
    /**
     * 支付宝账单
     */
    String LEDGER_ALIPAY = "alipay";
    /**
     * SM.MS API地址
     */
    String SMMS_BASIC_API = "https://sm.ms/api/v2/";
    /**
     * SM.MS 获取Token地址
     */
    String SMMS_GET_API_TOKEN = SMMS_BASIC_API + "/token";
    /**
     * SM.MS 上传图片API地址
     */
    String SM_MS_UPLOAD_IMAGE = SMMS_BASIC_API + "/upload";
}
