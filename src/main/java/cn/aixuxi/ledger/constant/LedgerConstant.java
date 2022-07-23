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
    String DEFAULT_PASSWORD = "123654";
    /**
     * 默认头像地址
     */
    String DEFULT_AVATAR = "https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg";
}
