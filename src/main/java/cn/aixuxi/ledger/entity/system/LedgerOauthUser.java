package cn.aixuxi.ledger.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 第三方系统用户信息
 */
@Data
@TableName("ledger_oauth_user")
public class LedgerOauthUser implements Serializable{

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 应用主页
     */
    private String blog;
    /**
     * 公司
     */
    private String company;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private String gender;
    /**
     * 地址
     */
    private String location;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 备注
     */
    private String remark;
    /**
     * 来源
     */
    private String source;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 账号
     */
    private String username;
    /**
     * 第三方系统用户ID
     */
    private String uuid;
}
