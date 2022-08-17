package cn.aixuxi.ledger.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * 第三方系统用户信息
 *
 * @author ruozhuliufeng
 */
@TableName("ledger_user_oauth")
@Data
public class LedgerUserOauth implements Serializable {
    private final static long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 第三方系统用户ID
     */
    private String uuid;
    /**
     * 系统用户ID
     */
    private Long userId;
    /**
     * 账号
     */
    private String username;
    /**
     * 用户名
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 应用主页
     */
    private String blog;
    /**
     * 公司名
     */
    private String company;
    /**
     * 地址
     */
    private String location;
    /**
     * 邮件
     */
    private String email;
    /**
     * 备注
     */
    private String remark;
    /**
     * 性别
     */
    private String gender;
    /**
     * 来源
     */
    private String source;
}
